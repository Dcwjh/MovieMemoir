/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import java.text.ParseException;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
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
import moviememoir.Memoir;
import moviememoir.Users;

/**
 *
 * @author 50339
 */
@Stateless
@Path("moviememoir.memoir")
public class MemoirFacadeREST extends AbstractFacade<Memoir> {

    @PersistenceContext(unitName = "UserMovieMemoirPU")
    private EntityManager em;

    public MemoirFacadeREST() {
        super(Memoir.class);
    } 
    

    @POST
    @Override
    @Consumes({MediaType.APPLICATION_JSON})
    public void create(Memoir entity) {
        super.create(entity);
    }

    @PUT
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void edit(@PathParam("id") Long id, Memoir entity) {
        super.edit(entity);
    }

    @DELETE
    @Path("{id}")
    public void remove(@PathParam("id") Long id) {
        super.remove(super.find(id));
    }

    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Memoir find(@PathParam("id") Long id) {
        return super.find(id);
    }
    
    @GET
    @Path("findByWatchDate/{watchTime}}")
    @Produces({"application/json"})
    public List<Memoir> findByWatchTime(@PathParam("watchtime") String
   watchtime) {
        Query query = em.createNamedQuery("Memoir.findByWatchTime");
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date newDate;
        try {
            newDate = format.parse(watchtime);
            query.setParameter("watchDate", newDate);
        } catch (ParseException ex) {
            Logger.getLogger(CredentialsFacadeREST.class.getName()).log(Level.SEVERE, null, ex);
        }
        return query.getResultList();
    }
    
    
    
     @GET
    @Path("findByMoviename/{moviename}")
    @Produces({"application/json"})
    public List<Memoir> findByMoviename(@PathParam("moviename") String
    moviename) {
        Query query = em.createNamedQuery("Memoir.findByMoviename");
        query.setParameter("moviename", moviename);
        return query.getResultList();
    }
    
    @GET
    @Path("findByRelease/{release}")
    @Produces({"application/json"})
    public List<Memoir> findByRelease(@PathParam("release") String
    release) {
        Query query = em.createNamedQuery("Memoir.findByMovieReleaseDate");
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date newDate;
        try {
            newDate = format.parse(release);
            query.setParameter("release", newDate);
        } catch (ParseException ex) {
            Logger.getLogger(CredentialsFacadeREST.class.getName()).log(Level.SEVERE, null, ex);
        }
        return query.getResultList();
    }
    
    @GET
    @Path("findByComment/{comment}")
    @Produces({"application/json"})
    public List<Memoir> findByComment(@PathParam("comment") String
    comment) {
        Query query = em.createNamedQuery("Memoir.findByComment");
        query.setParameter("comment", comment);
        return query.getResultList();
    }
    
   
    
    @GET
    @Path("findByScore/{score}")
    @Produces({"application/json"})
    public List<Memoir> findByScore(@PathParam("score") int
    score) {
        Query query = em.createNamedQuery("Memoir.findByScore");
        query.setParameter("score", score);
        return query.getResultList();
    }
    
    @GET
    @Path("findByUserid/{userid}")
    @Produces({"application/json"})
    public List<Memoir> findByUserid(@PathParam("userid") int
    userid) {
        Query query = em.createNamedQuery("Memoir.findByUserid");
        query.setParameter("userid", userid);
        return query.getResultList();
    }
    
    @GET
    @Path("findByCinemaid/{cinemaid}")
    @Produces({"application/json"})
    public List<Memoir> findByCinemaid(@PathParam("cinemaid") int
    cinemaid) {
        Query query = em.createNamedQuery("Memoir.findByCinemaid");
        query.setParameter("cinemaid", cinemaid);
        return query.getResultList();
    }
    
    //    task3 c)
    @GET
    @Path("findByDynamicTwoAttributes/{moviename}/{cinemaname}")
    @Produces({"application/json"})
    public List<Memoir> findByDynamicTwoAttributes(@PathParam("moviename") String moviename, @PathParam("cinemaname") String cinemaname) {
        TypedQuery<Memoir> q = em.createQuery("SELECT m FROM Memoir m WHERE m.moviename= :moviename AND m.cinemaid.cinemaname = :cinemaname", Memoir.class);
        q.setParameter("moviename", moviename);
        q.setParameter("cinemaname", cinemaname);
        return q.getResultList();
    }
    
    //    task3 d)
    @GET
    @Path("findByStaticTwoAttributes/{moviename}/{cinemaname}")
    @Produces({"application/json"})
    public List<Memoir> findByStaticTwoAttributes(@PathParam("moviename") String moviename, @PathParam("cinemaname") String cinemaname) {
        Query query = em.createNamedQuery("Memoir.findByStaticTwoAttributes");
        query.setParameter("moviename", moviename);
        query.setParameter("cinemaname", cinemaname);
        return query.getResultList();
    }
    
     //task4  a)
    @GET
    @Path("findTask4MethodA/{userid}/{begindate}/{enddate}")
    @Produces({MediaType.APPLICATION_JSON})
    public Object method1(@PathParam("userid") Integer userid, @PathParam("begindate") String
            begindate, @PathParam("enddate") String enddate) {
        TypedQuery<Object[]> q = em.createQuery("SELECT m.cinemaid.location,count(m.cinemaid.location) FROM Memoir m WHERE m.userid.userid =:userid AND m.watchtime BETWEEN :begindate AND :enddate GROUP BY m.cinemaid.location", Object[].class);
           
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            java.util.Date date1 = sdf.parse(begindate);
            java.util.Date date2 = sdf.parse(enddate);
            q.setParameter("userid", userid);
            q.setParameter("begindate", date1);
            q.setParameter("enddate", date2);
            List<Object[]> queryList = q.getResultList();
            JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
            queryList.stream().map((row) -> Json.createObjectBuilder().
                    add("postcode", (String) row[0])
                    .add("apperence number", (Long) row[1]).build()).forEachOrdered((personObject) -> {
                        arrayBuilder.add(personObject);
            });
            JsonArray jArray = arrayBuilder.build();
            return jArray;

            
            
        } catch (ParseException e) {
            return null;
        }
        
      
        }

    
    //    task4 b) 
    @GET
    @Path("findTask4MethodB/{userid}/{year}")
    @Produces({MediaType.APPLICATION_JSON})
    public Object findTask4MethodB(@PathParam("userid") int userid,
            @PathParam("year") String year) {
        TypedQuery<Object[]> query = em.createQuery("SELECT FUNCTION('MONTH',m.watchtime),count(m.moviename) FROM Memoir m "
                + "WHERE m.userid.userid =:userid AND FUNCTION('YEAR',m.watchtime) =:year GROUP BY FUNCTION('MONTH', m.watchtime) ", 
                Object[].class);
        query.setParameter("userid", userid);
        query.setParameter("year", year);
        List<Object[]> queryList = query.getResultList();
        
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
        for (Object[] row : queryList) {
        JsonObject personObject = Json.createObjectBuilder()
            .add("month", (String) row[0].toString())
            .add("watchedMovieNumber", (String) row[1].toString()).build();
        arrayBuilder.add(personObject);
        }
        JsonArray jArray = arrayBuilder.build();
        return jArray;
    }
    
     //task4 c)
    @GET
    @Path("findTask4MethodC/{userid}")
    @Produces({MediaType.APPLICATION_JSON})
    public Object findTask4MethodC(@PathParam("userid") int userid) {
        TypedQuery<Object[]> query = em.createQuery("SELECT m1.moviename,m1.score,m1.release FROM Memoir m1 "
                + "WHERE NOT EXISTS (SELECT m2 FROM Memoir m2 WHERE m2.userid = m1.userid AND m1.score < m2.score) AND m1.userid.userid = :userid" , Object[].class);
        query.setParameter("userid", userid);
        List<Object[]> queryList = query.getResultList();
        
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

         
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
        for (Object[] row : queryList) {
            JsonObject personObject = Json.createObjectBuilder()
                .add("movie name", (String) row[0])
                .add("highest score", (short) row[1])
                .add("release date", sdf.format(row[2])).build();
        arrayBuilder.add(personObject);
        }
        JsonArray jArray = arrayBuilder.build();
        return jArray;
    }
    
    //task4 d)
    @GET
    @Path("findTask4MethodD/{userid}")
    @Produces({MediaType.APPLICATION_JSON})
    public Object findTask4MethodD(@PathParam("userid") int userid) {
        TypedQuery<Object[]> query = em.createQuery("SELECT m.moviename, m.release " +
                        "FROM Memoir m WHERE m.userid.userid=:userid AND FUNCTION('year', m.watchtime) = " +
                        "FUNCTION('year', m.release)" , Object[].class);
        query.setParameter("userid", userid);
        List<Object[]> queryList = query.getResultList();
        
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

         
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
        for (Object[] row : queryList) {
        JsonObject personObject = Json.createObjectBuilder()
            .add("movie name", (String) row[0])
            .add("release year", sdf.format(row[1])).build();
        arrayBuilder.add(personObject);
        }
        JsonArray jArray = arrayBuilder.build();
        return jArray;  
    }
    
     //task4 e)
    @GET
    @Path("findTask4MethodE/{userid}")
    @Produces({MediaType.APPLICATION_JSON})
    public Object findTask4MethodE(@PathParam("userid") int userid) {
        TypedQuery<Object[]> query = em.createQuery("SELECT m.moviename," +
                 "FUNCTION('year',m.release) FROM Memoir m WHERE m.userid.userid=:userid AND " +
                   "m.moviename IN (SELECT m.moviename FROM Memoir m WHERE m.userid.userid=:userid GROUP BY " +
                  "m.moviename HAVING count(DISTINCT m.release)>1)" , Object[].class);
        query.setParameter("userid", userid);
        List<Object[]> queryList = query.getResultList();
        
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

         
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
        for (Object[] row : queryList) {
        JsonObject personObject = Json.createObjectBuilder()
            .add("movie name", (String) row[0])
            .add("release year", sdf.format(row[1])).build();
        arrayBuilder.add(personObject);
        }
        JsonArray jArray = arrayBuilder.build();
        return jArray;  
    }
    
    //task4 f)
    @GET
    @Path("findTask4MethodF/{userid}")
    @Produces({MediaType.APPLICATION_JSON})
    public Object findTask4MethodF(@PathParam("userid") int userid) {
        TypedQuery<Object[]> query = em.createQuery("SELECT m.moviename,m.release,m.score FROM Memoir m WHERE m.userid.userid=:userid " +
                    "AND FUNCTION('year',m.release ) BETWEEN FUNCTION('year'," +
                    "current_Date)-3 AND FUNCTION('year', current_Date) ORDER BY m.score desc" , Object[].class);
        query.setParameter("userid", userid);
        List<Object[]> queryList = query.getResultList();
        
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

         
        int i = 0; 
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
        for (Object[] row : queryList) {
            JsonObject personObject = Json.createObjectBuilder()
                .add("movie name", (String) row[0])
                .add("release date", sdf.format(row[1]))
                .add("rating score", (short) row[2]).build();
            arrayBuilder.add(personObject);
            if(i>5)
            break;
            i++;
        }
        JsonArray jArray = arrayBuilder.build();
        return jArray;  
    }

    @GET
    @Override
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Memoir> findAll() {
        return super.findAll();
    }

    @GET
    @Path("{from}/{to}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Memoir> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
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
