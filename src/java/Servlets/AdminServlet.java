/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servlets;

import com.google.gson.Gson;
import entities.Product;
import entities.User;
import entities.User.Role;
import exceptions.IncorrectValueException;
import java.io.IOException;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import javax.ejb.EJB;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
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
            response.setHeader("Access-Control-Allow-Origin", "*");
            response.setHeader("Access-Control-Allow-Headers", "origin, content-type, accept, Authorization");
            String path = request.getServletPath();
            request.setCharacterEncoding("UTF-8");
            
            Object outValue = new Object();
            int code = 200;
            String error = "";
            Gson gson = new Gson();
            User user = null;
            try{
                String token = request.getHeader("Authorization").split(" ")[1];
                user = userFacade.getUser(token);
                if(user == null || user.getRole() != Role.ADMIN){
                    response.sendError(404, "Not found");
                    return;
                }
            }catch(NullPointerException e){
                response.sendError(404, "Not found");
                return;
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
                    Product product = new Product(product_name, price, quantity, image);
                    productFacade.create(product);
                    outValue = product;
                }
                catch(IncorrectValueException|NullPointerException e){
                    error = "Incorrect values";
                    code = 400;
                }
                break;
                
//====================================================================================================================                 
            case "/change-product":
                try{
                    Integer change_product_id = Integer.parseInt(request.getParameter("id"));
                    String change_product_name = request.getParameter("name");
                    Integer change_product_quantity = Integer.parseInt(request.getParameter("quantity"));
                    Float change_product_price = Float.parseFloat(request.getParameter("price"));    
                    
                    Product change_product = productFacade.find(change_product_id);
                    change_product.setName(change_product_name);
                    change_product.setQuantity(change_product_quantity);
                    change_product.setPrice(change_product_price);
                    productFacade.edit(change_product);
                    outValue = change_product;
                }catch(IncorrectValueException|NumberFormatException e){
                    error = "Incorrect values";
                    code = 400;
                }
                break;
                
                
//====================================================================================================================                  
            case "/delete-product":
                try{
                    int delete_product_id = Integer.parseInt(request.getParameter("id"));
                    Product delete_product = productFacade.find(delete_product_id);
                    delete_product.setDeleted(true);
                    productFacade.edit(delete_product);
                    outValue = delete_product;
                }catch(NullPointerException e){
                    error = "Incorrect values";
                    code = 400;
                }
                break;
                
                
//====================================================================================================================                  
            case "/restore-product":
                try{
                    int restore_product_id = Integer.parseInt(request.getParameter("id"));
                    Product restore_product = productFacade.find(restore_product_id);
                    restore_product.setDeleted(false);
                    productFacade.edit(restore_product);
                    outValue = restore_product;
                }catch(NullPointerException e){
                    error = "Incorrect values";
                    code = 400;
                }
                break;
                
                
//====================================================================================================================                  
            case "/user-list":
                outValue = userFacade.findAll();
                break;
                
                
//====================================================================================================================                  
            case "/block-user":
                try{
                    int block_user_id = Integer.parseInt(request.getParameter("id"));
                    User block_user = userFacade.find(block_user_id);
                    block_user.setBlocked(true);
                    userFacade.edit(block_user);
                    outValue = true;
                }catch(NullPointerException | NumberFormatException e1){
                    error = "Incorrect values";
                    code = 400;
                }
                break;
                
                
//====================================================================================================================                  
            case "/restore-user":
                String restore_user_value = "defaultId";
                try{
                    restore_user_value = (String)request.getParameterMap().get("id")[0];
                    int restore_user_id = Integer.parseInt(restore_user_value);
                    User restore_user = userFacade.find(restore_user_id);
                    restore_user.setBlocked(false);
                    userFacade.edit(restore_user);
                    outValue = true;
                }catch(NullPointerException | NumberFormatException e1){
                    error = "Incorrect values";
                    code = 400;
                }
                break;
        }
        if(code == 200){
            response.getWriter().print(gson.toJson(outValue)); 
        }else{
            response.sendError(code, error);
        }
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
