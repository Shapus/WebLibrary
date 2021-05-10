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
import javax.ws.rs.core.Response;
import session.DealFacade;
import session.ProductFacade;
import session.UserFacade;

/**
 *
 * @author pupil
 */
@WebServlet(name = "SharedServlet", urlPatterns = {"/product-list"})
public class SharedServlet extends HttpServlet {
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
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
            response.setContentType("application/json;charset=UTF-8");
            response.setHeader("Access-Control-Allow-Origin", "*");
            response.setHeader("Access-Control-Allow-Headers", "origin, content-type, accept, Authorization");
            String path = request.getServletPath();
            request.setCharacterEncoding("UTF-8");
            
            Object outValue = new Object();
            int code = 200;
            String error = "";
            Gson gson = new Gson();
            Role role = Role.GUEST;
            User user = null;
            try{
                String token = request.getHeader("Authorization").split(" ")[1];
                user = userFacade.getUser(token);
                if(user.isBlocked()){
                    response.sendError(400, "User is blocked");
                    return;
                }
            }catch(NullPointerException e){
            }
            switch (path) {
                
//====================================================================================================================                
            case "/product-list":
                if(role == Role.ADMIN){
                    outValue = productFacade.findAll();
                }
                else{
                    outValue = productFacade.findNotDeleted();
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
