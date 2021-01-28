/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servlets;

import entities.Deal;
import entities.Product;
import entities.User;
import exceptions.IncorrectValueException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
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
@WebServlet(name = "MyServlet", urlPatterns = {"/addProduct","/createProduct",
                                               "/productList",
                                               "/buyProduct", "/createDeal"})
public class MyServlet extends HttpServlet {
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
            switch (path) {
                
//====================================================================================================================                
            case "/createProduct":
                if((User)request.getSession().getAttribute("user") == null){
                    request.getSession().setAttribute("redirectURL", "/WebLibrary/createProduct");
                    response.sendRedirect(paths.getString("login"));
                }
                else{
                    request.setAttribute("info", "Создание продукта");
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
                            request.getRequestDispatcher(paths.getString("createProduct")).forward(request, response);
                        }catch(IncorrectValueException e){
                            request.setAttribute("info", e.toString());
                            request.getRequestDispatcher(paths.getString("addProductForm")).forward(request, response);
                        }
                    }
                }
                break;
                
//====================================================================================================================                
            case "/addProduct":
                if((User)request.getSession().getAttribute("user") == null){
                    request.getSession().setAttribute("redirectURL", "/WebLibrary/addProduct");
                    response.sendRedirect(paths.getString("login"));
                }
                else{
                    request.setAttribute("info", "Добавить продукт");
                    request.getRequestDispatcher(paths.getString("addProductForm")).forward(request, response);
                }
                break;
                
//====================================================================================================================                
            case "/productList":
                request.setAttribute("productList", productFacade.findAll());
                request.getRequestDispatcher(paths.getString("productList")).forward(request, response);
                break;
                
//====================================================================================================================                
            case "/buyProduct":
                if((User)request.getSession().getAttribute("user") == null){
                    request.getSession().setAttribute("redirectURL", "/WebLibrary/buyProduct");
                    response.sendRedirect(paths.getString("login"));
                }
                else{
                    request.setAttribute("productList", productFacade.findAll());
                    request.setAttribute("userList", userFacade.findAll());
                    request.getRequestDispatcher(paths.getString("buyProduct")).forward(request, response);
                }
                break;
                
//====================================================================================================================                
            case "/createDeal":
                if((User)request.getSession().getAttribute("user") == null){
                    request.getSession().setAttribute("redirectURL", "/WebLibrary/buyProduct");
                    response.sendRedirect(paths.getString("login"));
                }
                else{
                    request.setAttribute("productList", productFacade.findAll());
                    request.setAttribute("userList", userFacade.findAll());

                    request.setAttribute("info", "Созание сделки");
                    Integer deal_product_id = Integer.parseInt(request.getParameter("productId"));
                    User deal_user = (User) request.getSession().getAttribute("user");
                    Integer deal_quantity = Integer.parseInt(request.getParameter("quantity"));

                    User user = userFacade.find(deal_user.getId());
                    Product product = productFacade.find(deal_product_id);

                    if(deal_product_id == null || user == null || deal_quantity == null || deal_quantity <= 0){
                        request.setAttribute("info", "Заполните все поля!");
                        request.getRequestDispatcher(paths.getString("buyProduct")).forward(request, response);
                    }
                    else if(product.getQuantity() < deal_quantity){
                        request.setAttribute("info", "На скаладе недостаточно товара!");
                        request.getRequestDispatcher(paths.getString("buyProduct")).forward(request, response);
                    }
                    else if(user.getMoney() < product.getPrice()*deal_quantity){
                        request.setAttribute("info", "Недостаточно средств!");
                        request.getRequestDispatcher(paths.getString("buyProduct")).forward(request, response);
                    }
                    else{

                        Deal deal = new Deal(user, product, deal_quantity);
                        dealFacade.create(deal);
                        try {
                            user.setMoney(user.getMoney()-product.getPrice()*deal_quantity);
                            product.setQuantity(product.getQuantity()-deal_quantity);
                            userFacade.edit(user);
                            productFacade.edit(product);
                        } catch (IncorrectValueException ex) {
                            request.setAttribute("info", ex);
                            request.getRequestDispatcher("/WEB-INF/sellProduct.jsp").forward(request, response);
                        }
                        response.sendRedirect(paths.getString("buyProduct"));
                    }
                }
                break;
            default:
                throw new AssertionError();
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
