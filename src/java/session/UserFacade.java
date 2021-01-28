/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package session;

import entities.User;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

/**
 *
 * @author pupil
 */
@Stateless
public class UserFacade extends AbstractFacade<User> {

    @PersistenceContext(unitName = "jktvr19market")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public UserFacade() {
        super(User.class);
    }
    public User check(String login, String password){
        Query q = em.
        createQuery("SELECT u FROM User u WHERE u.login=:login AND u.password=:password").setParameter("login", login).setParameter("password", password);
        User user = (User)q.getSingleResult();
        return user;
    }
    public boolean loginExist(String login){
        Query q = em.
        createQuery("SELECT u FROM User u WHERE u.login=:login").setParameter("login", login);
        try{
            User user = (User)q.getSingleResult();
            return true;
        }catch(NoResultException e){
            return false;
        }
    }
    
}
