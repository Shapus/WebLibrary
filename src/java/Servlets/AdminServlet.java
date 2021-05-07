/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servlets;

import com.google.gson.Gson;
import entities.Deal;
import entities.Product;
import entities.User;
import entities.User.Role;
import exceptions.IncorrectValueException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.ejb.EJB;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import javax.ws.rs.core.Context;
import session.DealFacade;
import session.ProductFacade;
import session.UserFacade;
import tools.UploadImage;

/**
 *
 * @author pupil
 */
@WebServlet(name = "AdminServlet", urlPatterns = {"/create-product",
                                                "/change-product",
                                               "/delete-product", 
                                               "/restore-product",
                                               "/user-list",
                                               "/block-user", 
                                               "/restore-user"})
@MultipartConfig()
public class AdminServlet extends HttpServlet {
    @EJB
    private ProductFacade productFacade;
    @EJB
    private UserFacade userFacade;
    @EJB
    private DealFacade dealFacade;

    @Context
    ServletContext servletContext;
    
    public static final ResourceBundle paths = ResourceBundle.getBundle("properties.JspPaths");
    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
            response.setContentType("application/json;charset=UTF-8");
            String path = request.getServletPath();
            request.setCharacterEncoding("UTF-8");
            
            HashMap outValue = new HashMap();
            ServletOutputStream outStream = response.getOutputStream();
            Gson gson = new Gson();
            User user = (User)request.getSession().getAttribute("user");
            if(user == null || user.getRole() != Role.ADMIN){
                
            }
            switch (path) {
                
//====================================================================================================================                
            case "/create-product":
                List<Part> fileParts = null;
                try{
                    fileParts = request
                                .getParts()
                                .stream()
                                .filter(part -> "file".equals(part.getName()))
                                .collect(Collectors.toList());
                }catch(Exception e){
                    
                }
                
                try{
                    String product_name = request.getParameter("name");
                    Float price = Float.parseFloat(request.getParameter("price"));
                    Integer quantity = Integer.parseInt(request.getParameter("quantity"));
                    
                    String image = UploadImage.upload(fileParts);
                    try{
                        Product product = new Product(product_name, price, quantity, image);
                        productFacade.create(product);
                        outValue.put("product", product);
                    }catch(IncorrectValueException e){
                        outValue.put("error", "Incorrect values");
                    }
                }
                catch(NullPointerException e){
                    outValue.put("error", "Values not found");
                }
                break;
                
//====================================================================================================================                 
            case "/change-product":
                try{
                    Integer change_product_id = Integer.parseInt(request.getParameter("id"));
                    String change_product_name = request.getParameter("name");
                    int change_product_quantity = (int)Float.parseFloat(request.getParameter("quantity"));
                    Float change_product_price = Float.parseFloat(request.getParameter("price"));
                    
                    try {       
                        Product change_product = productFacade.find(change_product_id);
                        change_product.setName(change_product_name);
                        change_product.setQuantity(change_product_quantity);
                        change_product.setPrice(change_product_price);
                        productFacade.edit(change_product);
                        outValue.put("product", change_product);
                    } catch (IncorrectValueException|NullPointerException ex) {
                        outValue.put("error", "Incorrect values");
                    }
                }catch(NumberFormatException e){
                    outValue.put("error", "Incorrect values");
                }
                break;
                
                
//====================================================================================================================                  
            case "/delete-product":
                String delete_value = (String)request.getParameterMap().get("id")[0];
                int delete_product_id = Integer.parseInt(delete_value);
                Product delete_product = productFacade.find(delete_product_id);
                delete_product.setDeleted(true);
                productFacade.edit(delete_product);
                request.getSession().setAttribute("deal_info", "Продукт id-"+delete_product_id+" удален<br>");
                response.sendRedirect("productList");
                break;
                
                
//====================================================================================================================                  
            case "/restore-product":
                String restore_value = (String)request.getParameterMap().get("id")[0];
                int restore_product_id = Integer.parseInt(restore_value);
                Product restore_product = productFacade.find(restore_product_id);
                restore_product.setDeleted(false);
                productFacade.edit(restore_product);
                request.getSession().setAttribute("deal_info", "Продукт id-"+restore_product_id+" восстановлен<br>");
                response.sendRedirect("productList");
                break;
                
                
//====================================================================================================================                  
            case "/user-list":
                request.setAttribute("userList", userFacade.findAll());
                request.getRequestDispatcher(paths.getString("userList")).forward(request, response);
                break;
                
                
//====================================================================================================================                  
            case "/block-user":
                String block_value = "defaultId";
                try{
                    block_value = (String)request.getParameterMap().get("id")[0];
                    int block_user_id = Integer.parseInt(block_value);
                    User block_user = userFacade.find(block_user_id);
                    block_user.setDeleted(true);
                    userFacade.edit(block_user);
                    request.getSession().setAttribute("deal_info", "Пользователь "+block_user.getLogin()+" заблокирован<br>");
                }catch(NullPointerException | NumberFormatException e1){
                    request.getSession().setAttribute("deal_info", "Пользователь с id \""+block_value+"\" не найден<br>");
                }
                response.sendRedirect("userList");
                break;
                
                
//====================================================================================================================                  
            case "/restore-user":
                String restore_user_value = "defaultId";
                try{
                    restore_user_value = (String)request.getParameterMap().get("id")[0];
                    int restore_user_id = Integer.parseInt(restore_user_value);
                    User restore_user = userFacade.find(restore_user_id);
                    restore_user.setDeleted(false);
                    userFacade.edit(restore_user);
                    request.getSession().setAttribute("deal_info", "Пользователь "+restore_user.getLogin()+" восстановлен<br>");
                }catch(NullPointerException | NumberFormatException e1){
                    request.getSession().setAttribute("deal_info", "Пользователь с id \""+restore_user_value+"\" не найден<br>");
                }
                response.sendRedirect("userList");
                break;
                
                
//====================================================================================================================  
            default:
                request.getRequestDispatcher("error404.jsp").forward(request, response);
        }
        outStream.print(gson.toJson(outValue));
    }
    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}
