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
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.core.Response;
import session.DealFacade;
import session.ProductFacade;
import session.UserFacade;

/**
 *
 * @author pupil
 */
@WebServlet(name = "UserServlet", urlPatterns = {"/createDeal",
                                                 "/logout",
                                                 "/user"})
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
    
    @Override
    protected void doOptions(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
            resp.setHeader("Access-Control-Allow-Origin", "*");
            resp.setHeader("Access-Control-Allow-Headers", "Authorization,Content-Type,Accept,Origin");
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Headers", "Authorization");
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
            if(user == null){
                response.sendError(401, "Unauthorized");
                return;
            }else if(user.isBlocked()){
                response.sendError(400, "User is blocked");
                return;
            }
        }catch(NullPointerException e){
            response.sendError(401, "Unauthorized");
            return;
        }
        
        switch (path) {
            
//====================================================================================================================                    
            case "/logout":
                userFacade.logout(user.getId());
                break;
                
                
//====================================================================================================================                    
            case "/user":
                outValue = user.getId();
                break;
                
                
//====================================================================================================================
            case "/createDeal":               
                try{
                    Integer deal_product_id = Integer.parseInt(request.getParameter("productId"));
                    Integer deal_quantity = Integer.parseInt(request.getParameter("quantity"));
                    Product product = productFacade.find(deal_product_id);

                    if(deal_quantity <= 0 || product.getQuantity() < deal_quantity || user.getMoney() < product.getPrice()*deal_quantity){
                        code = 400;
                        error = "Bad request";
                    }
  
                    else{
                        Deal deal = new Deal(user, product, deal_quantity);
                        Product product_copy = product;
                        dealFacade.create(deal);
                        boolean user_edited = false;
                        try {
                            user.setMoney(user.getMoney()-product.getPrice()*deal_quantity);
                            product.setQuantity(product.getQuantity()-deal_quantity);
                            productFacade.edit(product);
                            userFacade.edit(user);
                            user_edited = true;
                            outValue = deal;
                        } catch (IncorrectValueException ex) {
                            if(!user_edited){
                                productFacade.edit(product_copy);
                            }
                        }
                    }
                }catch(NumberFormatException e){
                    code = 400;
                    error = "Incorrect values";
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
