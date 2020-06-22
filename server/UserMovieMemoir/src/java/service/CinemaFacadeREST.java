/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import java.util.List;
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
import moviememoir.Cinema;
import moviememoir.Credentials;
import moviememoir.Memoir;

/**
 *
 * @author 50339
 */
@Stateless
@Path("moviememoir.cinema")
public class CinemaFacadeREST extends AbstractFacade<Cinema> {

    @PersistenceContext(unitName = "UserMovieMemoirPU")
    private EntityManager em;

    public CinemaFacadeREST() {
        super(Cinema.class);
    }

    @POST
    @Override
    @Consumes({MediaType.APPLICATION_JSON})
    public void create(Cinema entity) {
        super.create(entity);
    }
    
    
    @POST
    @Consumes({MediaType.APPLICATION_FORM_URLENCODED})
    @Produces(MediaType.TEXT_PLAIN)
    public Long create(
            @FormParam("cinemaname") String cinemaname,  
            @FormParam("location") String location){
        try{
            Cinema cinema = new Cinema(cinemaname, location);
            em.persist(cinema);
            em.flush();
            return cinema.getCinemaid();
        }catch(Exception e){
            e.printStackTrace();
            return 0L;
        }               
    }
    

    @PUT
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void edit(@PathParam("id") Long id, Cinema entity) {
        super.edit(entity);
    }
    
    @GET
    @Path("findByCinemaname/{cinemaname}")
    @Produces({"application/json"})
    public List<Cinema> findByCinmaname(@PathParam("cinemaname") String
    cinemaname) {
        Query query = em.createNamedQuery("Cinema.findByCinemaname");
        query.setParameter("cinemaname", cinemaname);
        return query.getResultList();
    }
    
    @GET
    @Path("findByLocation/{location}")
    @Produces({"application/json"})
    public List<Cinema> findByLocation(@PathParam("location") String
    location) {
        Query query = em.createNamedQuery("Cinema.findByLocation");
        query.setParameter("location", location);
        return query.getResultList();
    }

    @DELETE
    @Path("{id}")
    public void remove(@PathParam("id") Long id) {
        super.remove(super.find(id));
    }

    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Cinema find(@PathParam("id") Long id) {
        return super.find(id);
    }

    @GET
    @Override
    @Produces({ MediaType.APPLICATION_JSON})
    public List<Cinema> findAll() {
        return super.findAll();
    }

    @GET
    @Path("{from}/{to}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Cinema> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
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
