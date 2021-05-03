/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servlets;

import static Servlets.SharedServlet.paths;
import entities.User;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

/**
 *
 * @author pupil
 */
@WebServlet(name = "UploadServlet", urlPatterns = {"/upload","/uploadAction"})
@MultipartConfig()
public class UploadServlet {
    
    public static final ResourceBundle paths = ResourceBundle.getBundle("properties.JspPaths");
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
            response.setContentType("text/html;charset=UTF-8");
            String path = request.getServletPath();
            request.setCharacterEncoding("UTF-8");
            User user = (User)request.getSession().getAttribute("user");
            Boolean authenticated = false;
            if(user != null && user.getRole() == User.Role.ADMIN){
                authenticated = true;
            }else{
                request.getRequestDispatcher("error404.jsp").forward(request, response);
                return;
            }
            switch (path) {
                
 //====================================================================================================================                
                case "/upload":
                    request.getRequestDispatcher(paths.getString("upload")).forward(request, response);
//====================================================================================================================                
                case "/uploadAction":
                String uploadFolder = "D:\\UploadFolder";
                List<Part> fileParts = request
                            .getParts()
                            .stream()
                            .filter(part -> "file".equals(part.getName()))
                            .collect(Collectors.toList());
                for(Part filePart :fileParts){
                    String filePath = uploadFolder+File.separator+getFileName(filePart);    
                    try(InputStream fileContent = filePart.getInputStream()){
                        Files.copy(fileContent, new File(filePath).toPath(), StandardCopyOption.REPLACE_EXISTING);
                    } catch (IOException ex) {
                        Logger.getLogger(UploadServlet.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                break;
                default:
                    break;
            }
        }
        private String getFileName(Part part){
            final String partHeader = part.getHeader("content-disposition");
            for(String content : part.getHeader("content-disposition").split(";")){
                if(content.trim().startsWith("filename")){
                    return content
                            .substring(content.indexOf("=")+1)
                            .trim()
                            .replace("\"", "");

                }
            }    
            return null;
        }
}
     
