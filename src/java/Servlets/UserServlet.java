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
@WebServlet(name = "UserServlet", urlPatterns = {"/createDeal"})
public class UserServlet extends HttpServlet {
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
            if(user != null && !user.isDeleted() && (user.getRole() == Role.USER || user.getRole() == Role.ADMIN)){
                authenticated = true;
            }else if(user.isDeleted()){
                response.sendRedirect(".");
                request.getSession().setAttribute("user_info", "Пользователь заблокирован!");
                request.getSession().setAttribute("redirectURL", "/WebLibrary"+path);
                return;
            }else{
                response.sendRedirect("login");
                request.getSession().setAttribute("redirectURL", "/WebLibrary"+path);
                return;
            }
            switch (path) {
                
                
//====================================================================================================================                
            case "/createDeal":
                request.setAttribute("productList", productFacade.findAll());
                request.setAttribute("userList", userFacade.findAll());
                
                try{
                    Integer deal_product_id = Integer.parseInt(request.getParameter("productId"));
                    Integer deal_quantity = Integer.parseInt(request.getParameter("quantity"));

                    Product product = productFacade.find(deal_product_id);

                    if(deal_quantity <= 0){
                        request.getSession().setAttribute("deal_info", "Количество должно быть больше нуля!");
                    }
                    else if(product.getQuantity() < deal_quantity){
                        request.getSession().setAttribute("deal_info", "На скаладе недостаточно товара!");
                    }
                    else if(user.getMoney() < product.getPrice()*deal_quantity){
                        request.getSession().setAttribute("deal_info", "Недостаточно средств!");
                    }
                    else{

                        Deal deal = new Deal(user, product, deal_quantity);
                        dealFacade.create(deal);
                        try {
                            user.setMoney(user.getMoney()-product.getPrice()*deal_quantity);
                            product.setQuantity(product.getQuantity()-deal_quantity);
                            userFacade.edit(user);
                            productFacade.edit(product);
                            request.getSession().setAttribute("deal_info", "Вы купили: "+product.getName()+" в количестве "+deal_quantity);
                        } catch (IncorrectValueException ex) {
                            request.getSession().setAttribute("deal_info", ex);
                            request.getRequestDispatcher("/WEB-INF/sellProduct.jsp").forward(request, response);
                        }
                    }
                }catch(NumberFormatException e){
                    request.getSession().setAttribute("deal_info", "Неверно введены параметры!!");
                }
                response.sendRedirect("productList");
                break;
            default:
                response.sendRedirect("login");
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
