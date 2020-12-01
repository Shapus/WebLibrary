/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servlets;

import entities.Product;
import entities.User;
import exceptions.IncorrectValueException;
import java.io.IOException;
import java.io.PrintWriter;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import session.ProductFacade;
import session.UserFacade;

/**
 *
 * @author pupil
 */
@WebServlet(name = "MyServlet", urlPatterns = {"/addProduct","/createProduct",
                                               "/addUser","/createUser",
                                               "/productList"})
public class MyServlet extends HttpServlet {
    @EJB
    private ProductFacade productFacade;
    @EJB
    private UserFacade userFacade;

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
            case "/createProduct":
                request.setAttribute("info", "Создание продукта");
                String product_name = request.getParameter("name");
                Float price = null;
                try{
                price = Float.parseFloat(request.getParameter("price"));
                }catch(Exception e){
                    price = null;
                }
                request.setAttribute("name", product_name);
                request.setAttribute("price", price);
                if("".equals(product_name) || product_name == null || price.equals(null)){
                    request.setAttribute("info", "Заполните все поля!");
                    request.getRequestDispatcher("/WEB-INF/addProductForm.jsp").forward(request, response);
                }
                else{
                    try{
                        Product product = new Product(product_name, price, 1);
                        productFacade.create(product);
                        request.getRequestDispatcher("/WEB-INF/createProduct.jsp").forward(request, response);
                    }catch(IncorrectValueException e){
                        request.setAttribute("info", e.toString());
                        request.getRequestDispatcher("/WEB-INF/addProductForm.jsp").forward(request, response);
                    }
                }
                break;
            case "/addProduct":
                request.setAttribute("info", "Добавить продукт");
                request.getRequestDispatcher("/WEB-INF/addProductForm.jsp").forward(request, response);
                break;
            case "/createUser":
                request.setAttribute("info", "Созание пользователя");
                String login = request.getParameter("login");
                String password = request.getParameter("password");
                request.setAttribute("login", login);
                if("".equals(login) || login == null || "".equals(password) || password == null){
                    request.setAttribute("info", "Заполните все поля!");
                    request.getRequestDispatcher("/WEB-INF/addUserForm.jsp").forward(request, response);
                }
                else{
                    try{
                        User user = new User(login, password, User.Role.USER);
                        userFacade.create(user);
                        request.getRequestDispatcher("/WEB-INF/createUser.jsp").forward(request, response);
                    }catch(IncorrectValueException e){
                        request.setAttribute("info", e.toString());
                        request.getRequestDispatcher("/WEB-INF/addUserForm.jsp").forward(request, response);
                    }
                }
                break;
            case "/addUser":
                request.setAttribute("info", "Добавление пользователя");
                request.getRequestDispatcher("/WEB-INF/addUserForm.jsp").forward(request, response);
                break;
            case "/productList":
                request.setAttribute("productList", productFacade.findAll());
                request.getRequestDispatcher("productList.jsp").forward(request, response);
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
