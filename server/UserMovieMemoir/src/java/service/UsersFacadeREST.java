/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import java.text.SimpleDateFormat;
import java.text.ParseException;
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
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import moviememoir.Users;

/**
 *
 * @author 50339
 */
@Stateless
@Path("moviememoir.users")
public class UsersFacadeREST extends AbstractFacade<Users> {

    @PersistenceContext(unitName = "UserMovieMemoirPU")
    private EntityManager em;

    public UsersFacadeREST() {
        super(Users.class);
    }

    @POST
    @Override
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void create(Users entity) {
        super.create(entity);
    }

    @PUT
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void edit(@PathParam("id") Long id, Users entity) {
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
    public Users find(@PathParam("id") Long id) {
        return super.find(id);
    }

    
    @GET
    @Path("findByUsername/{username}")
    @Produces({"application/json"})
    public List<Users> findByUsername(@PathParam("username") String
    username) {
        Query query = em.createNamedQuery("Users.findByUsername");
        query.setParameter("username", username);
        return query.getResultList();
    }
    
    @GET
    @Path("findBySurname/{surname}")
    @Produces({"application/json"})
    public List<Users> findBySurname(@PathParam("surname") String
    surname) {
        Query query = em.createNamedQuery("Users.findBySurname");
        query.setParameter("surname", surname);
        return query.getResultList();
    }
    
    @GET
    @Path("findByGender/{gender}")
    @Produces({"application/json"})
    public List<Users> findByGender(@PathParam("gender") String
    gender) {
        Query query = em.createNamedQuery("Users.findByGender");
        query.setParameter("gender", gender);
        return query.getResultList();
    }

    @GET
    @Path("findByDob/{dob}")
    @Produces({"application/json"})
    public List<Users> findByDob(@PathParam("dob") String
    dob) {
//        Query query = em.createNamedQuery("Users.findByDob");
//        query.setParameter("dob", dob);
//        return query.getResultList();
        Query query = em.createNamedQuery("Users.findByDob");
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date newDate;
        try {
            newDate = format.parse(dob);
            query.setParameter("dob", newDate);
        } catch (ParseException ex) {
            Logger.getLogger(CredentialsFacadeREST.class.getName()).log(Level.SEVERE, null, ex);
        }
        return query.getResultList();
    }
     
    @GET
    @Path("findByAddress/{address}")
    @Produces({"application/json"})
    public List<Users> findByAddress(@PathParam("address") String
    address) {
        Query query = em.createNamedQuery("Users.findByAddress");
        query.setParameter("address", address);
        return query.getResultList();
    }
    
    @GET
    @Path("findByState/{state}")
    @Produces({"application/json"})
    public List<Users> findByState(@PathParam("state") String
    state) {
        Query query = em.createNamedQuery("Users.findByState");
        query.setParameter("state", state);
        return query.getResultList();
    }
    
    @GET
    @Path("findByPostcode/{postcode}")
    @Produces({"application/json"})
    public List<Users> findByPostcode(@PathParam("postcode") String
    postcode) {
        Query query = em.createNamedQuery("Users.findByPostcode");
        query.setParameter("postcode", postcode);
        return query.getResultList();
    }
    
    //    task3 b)
    @GET
    @Path("findByDynamicThreeAttributes/{address}/{state}/{postcode}")
    @Produces({"application/json"})
    public List<Users> findByThreeDynamicAddressStatePostcode(@PathParam("address") String address, @PathParam("state") String state, @PathParam("postcode") String postcode) {
        TypedQuery<Users> q = em.createQuery("SELECT u FROM Users u WHERE u.address = :address AND u.state=:state AND u.postcode=:postcode", Users.class);
        q.setParameter("address", address);
        q.setParameter("state", state);
        q.setParameter("postcode", postcode);
        return q.getResultList();
    }
    @GET
    @Override
    @Produces({MediaType.APPLICATION_JSON})
    public List<Users> findAll() {
        return super.findAll();
    }

    @GET
    @Path("{from}/{to}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Users> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
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
