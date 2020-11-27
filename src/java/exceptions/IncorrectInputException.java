/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package exceptions;

/**
 *
 * @author pupil
 */
public class IncorrectInputException extends Exception{
    
//=============================== VARIABLES    
    String error;
    
    
//=============================== METHODS    
    public IncorrectInputException(String error) {
        this.error = error;
    }
    
    
//=============================== OVERRIDDEN METHODS    
    @Override
    public String toString(){
        return(this.error);
    }  
}
