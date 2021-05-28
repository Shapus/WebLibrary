/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servlets;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import entities.User;
import exceptions.IncorrectValueException;
import java.io.IOException;
import java.util.ResourceBundle;
import java.util.Scanner;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import session.UserFacade;

/**
 *
 * @author pupil
 */
@WebServlet(name = "LoginServlet", urlPatterns = {"/login",
                                                  "/add-user"
                                                 })
public class LoginServlet extends HttpServlet {
    @EJB
    private UserFacade userFacade;
    
    public static final ResourceBundle paths = ResourceBundle.getBundle("properties.JspPaths");
    
    @Override
    protected void doOptions(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        super.doOptions(req, resp); //To change body of generated methods, choose Tools | Templates.
        resp.setHeader("Access-Control-Allow-Origin", "*");
        resp.setHeader("Access-Control-Allow-Headers", "*");
        resp.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, PATCH, DELETE, HEAD, OPTIONS");
        resp.setHeader("Content-Type", "application/json");
        resp.setHeader("Access-Control-Allow-Credentials", "true");
    }
    
    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getServletPath();
        
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Headers", "*");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, PATCH, DELETE, HEAD, OPTIONS");
        response.setHeader("Content-Type", "application/json");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        
        Object outValue = new Object();
        int code = 200;
        String error = "";
        Gson gson = new Gson();
        Scanner scanner = new Scanner(request.getInputStream());
        String json_string = "";
        while(scanner.hasNext()){
            json_string += scanner.nextLine();
        }
        scanner.close();
        JsonObject json_request = new JsonParser().parse(json_string).getAsJsonObject();
        switch (path) {
            
//====================================================================================================================
            case "/login":
                String login = json_request.get("login").getAsString();
                String password = json_request.get("password").getAsString();
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
                        outValue = userFacade.generateToken(user.getId());
                        error = "Bad request";
                        code = 400;
                    }
                }
                break;
                
                
//====================================================================================================================
            case "/add-user":
                String registration_login = json_request.get("login").getAsString();
                String registration_password = json_request.get("password").getAsString();
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
