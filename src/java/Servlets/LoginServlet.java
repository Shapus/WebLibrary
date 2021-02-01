/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servlets;

import entities.User;
import exceptions.IncorrectValueException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ResourceBundle;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import session.UserFacade;

/**
 *
 * @author pupil
 */
@WebServlet(name = "LoginServlet", urlPatterns = {"/login", "/logout", 
                                                  "/registration", "/createUser"})
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
            response.setContentType("text/html;charset=UTF-8");
            String path = request.getServletPath();
            request.setCharacterEncoding("UTF-8");
            
            switch (path) {
                
//====================================================================================================================                
                case "/login":
                    String login = request.getParameter("login");
                    String password = request.getParameter("password");
                    if("".equals(login) || login == null || "".equals(password) || password == null){
                        request.setAttribute("info", "Заполните все поля!");
                        request.getRequestDispatcher(paths.getString("login")).forward(request, response);
                    }
                    else{
                        User user = userFacade.check(login, password);
                        if(user == null){
                            request.setAttribute("info", "Неверно введены логин и/или пароль!");
                            request.getRequestDispatcher(paths.getString("login")).forward(request, response);
                        }
                        else{
                            request.getSession().setAttribute("user", user);
                            if(user.isDeleted()){
                                request.getSession().setAttribute("user_info", "Пользователь заблокирован!");
                            }
                            String redirectURL = request.getSession().getAttribute("redirectURL")!=null?(String)request.getSession().getAttribute("redirectURL"):"/WebLibrary/";
                            response.sendRedirect(redirectURL);
                        }
                        
                    }
                    break;
                    
//====================================================================================================================                    
                case "/logout":
                    request.getSession().invalidate();
                    response.sendRedirect("/WebLibrary/");
                    break;
                    
//====================================================================================================================                    
                case "/registration":
                    request.getRequestDispatcher(paths.getString("registrationForm")).forward(request, response);
                    break;
                    
//====================================================================================================================                    
                case "/createUser": 
                    String registration_login = request.getParameter("login");
                    String registration_password = request.getParameter("password");
                    if("".equals(registration_login) || registration_login == null || "".equals(registration_password) || registration_password == null){
                        request.setAttribute("info", "Заполните все поля!");
                        request.getRequestDispatcher(paths.getString("registrationForm")).forward(request, response);
                    }
                    else if(userFacade.loginExist(registration_login)){
                        request.setAttribute("info", "Пользователь с таким именем уже существует!");
                        request.getRequestDispatcher(paths.getString("registrationForm")).forward(request, response);
                    }
                    else{
                        try{
                            User user = new User(registration_login, registration_password, User.Role.USER);
                            userFacade.create(user);
                            request.setAttribute("login", user.getLogin());
                            request.getRequestDispatcher(paths.getString("createUser")).forward(request, response);
                        }catch(IncorrectValueException e){
                            request.setAttribute("info", e.toString());
                            request.getRequestDispatcher(paths.getString("registrationForm")).forward(request, response);
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
