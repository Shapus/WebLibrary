/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servlets;

import entities.Deal;
import entities.Product;
import entities.User;
import entities.User.Role;
import exceptions.IncorrectValueException;
import java.io.IOException;
import java.util.ResourceBundle;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import session.DealFacade;
import session.ProductFacade;
import session.UserFacade;

/**
 *
 * @author pupil
 */
@WebServlet(name = "AdminServlet", urlPatterns = {"/addProduct","/createProduct",
                                               "/changeProductForm", "/changeProductAnswer",
                                               "/deleteProduct", "/restoreProduct"})
public class AdminServlet extends HttpServlet {
    @EJB
    private ProductFacade productFacade;
    @EJB
    private UserFacade userFacade;
    @EJB
    private DealFacade dealFacade;

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
            response.setContentType("text/html;charset=UTF-8");
            String path = request.getServletPath();
            request.setCharacterEncoding("UTF-8");
            User user = (User)request.getSession().getAttribute("user");
            Boolean authenticated = false;
            if(user != null && user.getRole() == Role.ADMIN){
                authenticated = true;
            }else{
                request.getRequestDispatcher("error404.jsp").forward(request, response);
                return;
            }
            switch (path) {
                
//====================================================================================================================                
            case "/createProduct":
                String product_name = request.getParameter("name");
                Float price = null;
                Integer quantity = null;
                try{
                    price = Float.parseFloat(request.getParameter("price"));
                }catch(Exception e){
                    price = null;
                }
                try{
                    quantity = Integer.parseInt(request.getParameter("quantity"));
                }catch(Exception e){
                    quantity = null;
                }
                request.setAttribute("name", product_name);
                request.setAttribute("price", price);
                request.setAttribute("quantity", quantity);
                if("".equals(product_name) || product_name == null || price == null || quantity == null){
                    request.setAttribute("info", "Заполните все поля!");
                    request.getRequestDispatcher(paths.getString("addProductForm")).forward(request, response);
                }
                else{
                    try{
                        Product product = new Product(product_name, price, quantity);
                        productFacade.create(product);
                        request.setAttribute("id", product.getId());
                        request.getRequestDispatcher(paths.getString("createProduct")).forward(request, response);
                    }catch(IncorrectValueException e){
                        request.setAttribute("info", e.toString());
                        request.getRequestDispatcher(paths.getString("addProductForm")).forward(request, response);
                    }
                }
                break;
                
//====================================================================================================================                
            case "/addProduct":
                request.getRequestDispatcher(paths.getString("addProductForm")).forward(request, response);
                break;
                
//====================================================================================================================                
            case "/productList":
                request.setAttribute("productList", productFacade.findAll());
                request.getRequestDispatcher(paths.getString("productList")).forward(request, response);
                break;
                
//==================================================================================================================== 
            case "/changeProductForm":
                String value = (String)request.getParameterMap().get("id")[0];
                int product_id = Integer.parseInt(value);
                Product product = productFacade.find(product_id);
                request.setAttribute("id", product.getId());
                request.setAttribute("name", product.getName());
                request.setAttribute("price", product.getPrice());
                request.setAttribute("quantity", product.getQuantity());
                request.getRequestDispatcher(paths.getString("changeProductForm")).forward(request, response);
                break;
                
//====================================================================================================================                 
            case "/changeProductAnswer":
                try {
                    Integer change_product_id = Integer.parseInt(request.getParameter("id"));
                    String change_product_name = request.getParameter("name");
                    int change_product_quantity = (int)Float.parseFloat(request.getParameter("quantity"));
                    Float change_product_price = Float.parseFloat(request.getParameter("price"));
                    
                    Product change_product = productFacade.find(change_product_id);
                    String old_name = change_product.getName();
                    int old_quantity = change_product.getQuantity();
                    double old_price = change_product.getPrice();
                    change_product.setName(change_product_name);
                    change_product.setQuantity(change_product_quantity);
                    change_product.setPrice(change_product_price);
                    productFacade.edit(change_product);
                    
                    String admin_info = "Вы изменили продукт id-"+change_product.getId()+"<br>";
                    if(!old_name.equals(change_product.getName())){
                       admin_info += "<br>Имя: "+old_name+" -> "+change_product.getName(); 
                    }
                    if(old_quantity != change_product.getQuantity()){
                       admin_info += "<br>Количество: "+old_quantity+" -> "+change_product.getQuantity(); 
                    }
                    if(old_price != change_product.getPrice()){
                       admin_info += "<br>Цена: "+old_price+" -> "+change_product.getPrice(); 
                    }
                    request.getSession().setAttribute("deal_info", admin_info);
                } catch (IncorrectValueException ex) {
                    request.getSession().setAttribute("deal_info", ex.toString());
                } catch (NumberFormatException e){
                    request.getSession().setAttribute("deal_info", "Неверный ввод!");
                }
                response.sendRedirect("productList");
                break;
            case "/deleteProduct":
                String delete_value = (String)request.getParameterMap().get("id")[0];
                int delete_product_id = Integer.parseInt(delete_value);
                Product delete_product = productFacade.find(delete_product_id);
                delete_product.setDeleted(true);
                productFacade.edit(delete_product);
                request.getSession().setAttribute("deal_info", "Продукт id-"+delete_product_id+" удален<br>");
                response.sendRedirect("productList");
                break;
            case "/restoreProduct":
                String restore_value = (String)request.getParameterMap().get("id")[0];
                int restore_product_id = Integer.parseInt(restore_value);
                Product restore_product = productFacade.find(restore_product_id);
                restore_product.setDeleted(false);
                productFacade.edit(restore_product);
                request.getSession().setAttribute("deal_info", "Продукт id-"+restore_product_id+" восстановлен<br>");
                response.sendRedirect("productList");
                break;
            default:
                request.getRequestDispatcher("error404.jsp").forward(request, response);
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
