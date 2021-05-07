/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import exceptions.IncorrectValueException;
import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

/**
 *
 * @author pupil
 */
@Entity
public class Product implements Serializable{

//=============================== VARIABLES
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(fetch=FetchType.EAGER)
    private int id;
    @NotNull @Basic(fetch=FetchType.EAGER)
    private String name;
    @NotNull @Basic(fetch=FetchType.EAGER)
    private double price;
    @NotNull @Basic(fetch=FetchType.EAGER)
    private int quantity;
    @NotNull @Basic(fetch=FetchType.EAGER)
    private boolean deleted;
    @Null @Basic(fetch=FetchType.EAGER)
    private String image;
    

//=============================== CONSTRUCTORS 
    public Product(){
        this.deleted = false;
    }
    public Product(String name, double price, int quantity, String image) throws IncorrectValueException{
        this.setName(name);
        this.setPrice(price);
        this.setQuantity(quantity);
        this.deleted = false;
        this.image = image;
    }

    
//=============================== METHODS
    private String getDeletedString(){
        String str = "";
        if(deleted){
            str = "\u001B[41m" + "\u001B[37m" + " DELETED " + "\u001B[0m" + " ";
        }
        return str;
    }
//=============================== GETTERS
    public String getName() {
        return name;
    }
    public double getPrice() {
        return price;
    }
    public int getQuantity() {
        return quantity;
    }
    public int getId() {
        return id;
    }
    public boolean isDeleted(){
        return deleted;
    }
    public String getImage() {
        return image;
    }
    
    
//=============================== SETTERS
    public void setName(String name) throws IncorrectValueException {
        if(name.trim().equals("")){
            throw new IncorrectValueException("Неверно введено название");
        };
        this.name = name;       
    }
    public void setPrice(double price) throws IncorrectValueException {
        if(price <= 0){
            throw new IncorrectValueException("Неверно введена цена");
        }
        this.price = price;
    }
    public void setQuantity(int quantity) throws IncorrectValueException {
        if(quantity < 0){
            throw new IncorrectValueException("Неверно введено количество");
        }
        this.quantity = quantity;
    }
    public void setDeleted(boolean deleted){
        this.deleted = deleted;
    }
    public void setImage(String image) {
        this.image = image;
    }
    
  
    
//=============================== OVERRIDDEN METHODS    
//to string
    @Override
    public String toString() {
        return getDeletedString()+"Product " + id + " {" + "model=" + name + ", cost=" + price + ", quantity=" + quantity + ", image=" + image + "}";
    }
    public String getData() {
        return "Product " + id + " {" + "model=" + name + ", cost=" + price + "}";
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 73 * hash + this.id;
        hash = 73 * hash + Objects.hashCode(this.name);
        hash = 73 * hash + (int) (Double.doubleToLongBits(this.price) ^ (Double.doubleToLongBits(this.price) >>> 32));
        hash = 73 * hash + this.quantity;
        hash = 73 * hash + (this.deleted ? 1 : 0);
        hash = 73 * hash + Objects.hashCode(this.image);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Product other = (Product) obj;
        if (this.id != other.id) {
            return false;
        }
        if (Double.doubleToLongBits(this.price) != Double.doubleToLongBits(other.price)) {
            return false;
        }
        if (this.quantity != other.quantity) {
            return false;
        }
        if (this.deleted != other.deleted) {
            return false;
        }
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        if (!Objects.equals(this.image, other.image)) {
            return false;
        }
        return true;
    }
    
    

}
