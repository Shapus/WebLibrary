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
import javax.persistence.Column;
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
public class User implements Serializable{
public static enum Role{GUEST, USER, ADMIN};
//=============================== VARIABLES
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NotNull
    private Role role;
    @NotNull @Column(unique=true)
    private String login;
    @NotNull
    private String password;
    @NotNull
    private String salt;
    @NotNull @Basic(fetch=FetchType.EAGER)
    private double money;
    @NotNull @Basic(fetch=FetchType.EAGER)
    private boolean blocked;
    @Null @Basic(fetch=FetchType.EAGER)
    private String token;

//=============================== CONSTRUCTORS
    public User(){
        this.blocked = false;
    }
    public User(Role role){
        this.setRole(role);
        this.blocked = false;
    }
    public User(String login, String password, Role role) throws IncorrectValueException{
        this.setLogin(login);
        this.setSalt(tools.EncryptPassword.createSalt());
        this.setPassword(tools.EncryptPassword.createHash(password, getSalt()));
        this.setRole(role);
        this.setMoney(10000);
      
        this.blocked = false;
    }

//=============================== GETTERS
    public Role getRole() {
        return role;
    }
    public String getLogin() {
        return login;
    }
    public String getPassword() {
        return password;
    }
    public double getMoney() {
        return money;
    }
    public int getId(){
        return id;
    }
    public boolean isBlocked() {
        return blocked;
    }
    public String getSalt() {
        return salt;
    }
    public String getToken() {
        return token;
    }
    
    
    
//=============================== SETTERS
    public void setRole(Role role) {
        this.role = role;
    }
    public void setLogin(String login) throws IncorrectValueException {
        if(login.trim().length() == 0){
            throw new IncorrectValueException("Недопустимый логин");
        }
        this.login = login;
    }
    public void setPassword(String password) throws IncorrectValueException {
        if(password.split(" ").equals(" ")){
            throw new IncorrectValueException("Недопустимый пароль");
        }
        if(password.length() < 6){
            throw new IncorrectValueException("Длина пароля должна быть не менее 6 символов");
        }
        this.password = password;
    }  
    public void setMoney(double money) throws IncorrectValueException {
        if(money < 0){
            throw new IncorrectValueException("Нельзя установить количество денег меньше нуля");
        }
        this.money = money;
    }
    public void setBlocked(boolean blocked) {
        this.blocked = blocked;
    }
    public void setSalt(String salt) {
        this.salt = salt;
    }
    public void setToken(String token) {
        this.token = token;
    }
    

    
//=============================== OVERRIDDEN METHODS    
//to string
    @Override    
    public String toString() {
        return "User "+id+" {" + "role=" + role + ", login=" + login + '}';
    }

//hash code
    @Override
    public int hashCode() {
        int hash = 3;
        hash = (int) (97 * hash + this.id);
        hash = 97 * hash + Objects.hashCode(this.role);
        hash = 97 * hash + Objects.hashCode(this.login);
        hash = 97 * hash + Objects.hashCode(this.password);
        hash = 97 * hash + (int) (Double.doubleToLongBits(this.money) ^ (Double.doubleToLongBits(this.money) >>> 32));
        return hash;
    }


//equals
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
        final User other = (User) obj;
        if (this.id != other.id) {
            return false;
        }
        if (Double.doubleToLongBits(this.money) != Double.doubleToLongBits(other.money)) {
            return false;
        }
        if (!Objects.equals(this.login, other.login)) {
            return false;
        }
        if (!Objects.equals(this.password, other.password)) {
            return false;
        }
        if (this.role != other.role) {
            return false;
        }
        return true;
    }

    
    
}
