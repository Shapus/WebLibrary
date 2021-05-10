/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tools;

import static Servlets.AdminServlet.paths;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;
import javax.servlet.http.Part;

/**
 *
 * @author pupil
 */
public class UploadImage {
    private static String uploadFolder = paths.getString("upload_path");
    
    public static String upload(List<Part> fileParts){
        if(fileParts == null){
            return null;
        }
        for(Part filePart :fileParts){
            String filePath = uploadFolder+File.separator+getFileName(filePart);    
            try(InputStream fileContent = filePart.getInputStream()){
                Files.copy(fileContent, new File(filePath).toPath(), StandardCopyOption.REPLACE_EXISTING);
                return uploadFolder+getFileName(filePart);
            } catch (IOException ex) {
                
            }
        }
        return null;
    }
    private static String getFileName(Part part){
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
