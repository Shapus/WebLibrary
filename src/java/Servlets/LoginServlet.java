/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servlets;

import com.google.gson.Gson;
import entities.User;
import exceptions.IncorrectValueException;
import java.io.IOException;
import java.util.HashMap;
import java.util.ResourceBundle;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Response;
import session.UserFacade;

/**
 *
 * @author pupil
 */
@WebServlet(name = "LoginServlet", urlPatterns = {"/login",
                                                  "/registration"
                                                 })
public class LoginServlet extends HttpServlet {
    @EJB
    private UserFacade userFacade;
    
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
            
            Object outValue = new Object();
            int code = 200;
            String error = "";
            Gson gson = new Gson();
            switch (path) {
                
//====================================================================================================================                
                case "/login":
                    String login = request.getParameter("login");
                    String password = request.getParameter("password");
                    if("".equals(login) || login == null || "".equals(password) || password == null){
                        error = "Bad request";
                        code = 400;
                    }
                    else{
                        User user = userFacade.check(login, password);
                        if(user == null){
                            error = "Bad request";
                            code = 400;
                        }
                        else{
                            outValue = userFacade.login(user.getId());
                        }
                        
                    }
                    break;
                   
                    
//====================================================================================================================                    
                case "/registration": 
                    String registration_login = request.getParameter("login");
                    String registration_password = request.getParameter("password");
                    if("".equals(registration_login) || registration_login == null || "".equals(registration_password) || registration_password == null){
                        error = "Bad request";
                        code = 400;
                    }
                    else if(userFacade.loginExist(registration_login)){
                        error = "Login already exist";
                        code = 409;
                    }
                    else{
                        try{
                            User user = new User(registration_login, registration_password, User.Role.USER);
                            userFacade.create(user);
                            outValue = "User registered";
                        }catch(IncorrectValueException e){
                            error = "Bad request";
                        code = 400;
                        }
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
