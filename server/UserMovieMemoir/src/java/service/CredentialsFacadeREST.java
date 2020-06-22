/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import moviememoir.Credentials;
import moviememoir.Users;

/**
 *
 * @author 50339
 */
@Stateless
@Path("moviememoir.credentials")
public class CredentialsFacadeREST extends AbstractFacade<Credentials> {

    @PersistenceContext(unitName = "UserMovieMemoirPU")
    private EntityManager em;

    public CredentialsFacadeREST() {
        super(Credentials.class);
    }

    @POST
    @Override
    @Consumes({ MediaType.APPLICATION_JSON})
    public void create(Credentials entity) {
        System.out.println(entity);
        em.persist(entity.getUserid());
        em.persist(entity);
//        super.create(entity);
    }

    @PUT
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void edit(@PathParam("id") Long id, Credentials entity) {
        super.edit(entity);
    }

    
    
    
    @DELETE
    @Path("{id}")
    public void remove(@PathParam("id") Long id) {
        super.remove(super.find(id));
    }

    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public Credentials find(@PathParam("id") Long id) {
        return super.find(id);
    }
    
//    
//    @POST
//    @Path("myselfLogin/")
//    @Consumes({MediaType.APPLICATION_JSON})
//    public boolean login(@PathParam("username") String username, @PathParam("password") String password) {
//       TypedQuery<Credentials> q = em.createQuery("SELECT c FROM Credentials c WHERE c.username= :username AND c.password = :password", Credentials.class);
//       q.setParameter("username", username);
//       q.setParameter("password",password);
//       
//       if(q.getResultList().size() == 0)
//           return false;
//       if(q.getResultList().size() == 1)
//           return true;
//       return false;
//    }
    
    
    @POST
    @Path("login/")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.TEXT_PLAIN})
    public Object doLogin(Credentials credential){
        Query query = em.createQuery("SELECT c FROM Credentials c WHERE c.username = :username and c.password = :password");
        query.setParameter("username",credential.getUsername());
        query.setParameter("password", credential.getPassword());
        List<Credentials> rs = query.getResultList();
        if(rs.isEmpty()){
            return "0"; // user not exist
        }
        return "1";
    }
    
    
    @GET
    @Path("findByUsername/{username}")
    @Produces({"application/json"})
    public List<Credentials> findByUsername(@PathParam("username") String
    username) {
        Query query = em.createNamedQuery("Credentials.findByUsername");
        query.setParameter("username", username);
        return query.getResultList();
    }
    
    
    @GET
    @Path("findByPassword/{password}")
    @Produces({"application/json"})
    public List<Credentials> findByPassword(@PathParam("password") String
    password) {
        Query query = em.createNamedQuery("Credentials.findByPassword");
        query.setParameter("password", password);
        return query.getResultList();
    }
    
    @POST
    @Consumes({MediaType.APPLICATION_FORM_URLENCODED})
    @Produces(MediaType.TEXT_PLAIN)
    public String create(
            @FormParam("username") String username,  //email
            @FormParam("password") String password,
            @FormParam("signupdate") String signupdate,
            @FormParam("name") String name,   //firstname
            @FormParam("surname") String surname,
            @FormParam("gender") String gender,
            @FormParam("dob") String dob,
            @FormParam("address") String address,
            @FormParam("state") String state,
            @FormParam("postcode") String postcode) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            java.util.Date _signUpDate = sdf.parse(signupdate);
            java.util.Date _dob = sdf.parse(dob);
            Users user;
            user = new Users(name, surname,  gender, _dob, address, state, postcode);
            Credentials credential = new Credentials(username,password,_signUpDate,user);
            em.persist(user);
            em.persist(credential);
            return "success";
        } catch (Exception e) {
            e.printStackTrace();
            return "failed";
        }
    }
    
    
    @GET
    @Path("findBySignupdate/{signupdate}")
    @Produces({"application/json"})
    public List<Credentials> findBySignupdate(@PathParam("signupdate") String
    signupdate) {
        Query query = em.createNamedQuery("Credentials.findBySignupdate");
      
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date newDate;
        try {
            newDate = format.parse(signupdate);
            query.setParameter("signupdate", newDate);
        } catch (ParseException ex) {
            Logger.getLogger(CredentialsFacadeREST.class.getName()).log(Level.SEVERE, null, ex);
        }
        return query.getResultList();
    }
    
    

  
    
    
    @GET
    @Path("findByUserid/{userid}")
    @Produces({"application/json"})
    public List<Credentials> findByUserid(@PathParam("userid") int
    userid) {
        Query query = em.createNamedQuery("Credentials.findByUserid");
        query.setParameter("userid", userid);
        return query.getResultList();
    }
    
    @GET
    @Override
    @Produces({ MediaType.APPLICATION_JSON})
    public List<Credentials> findAll() {
        return super.findAll();
    }

    @GET
    @Path("{from}/{to}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Credentials> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
        return super.findRange(new int[]{from, to});
    }

    @GET
    @Path("count")
    @Produces(MediaType.TEXT_PLAIN)
    public String countREST() {
        return String.valueOf(super.count());
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
}
