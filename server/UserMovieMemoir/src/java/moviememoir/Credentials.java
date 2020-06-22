/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package moviememoir;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author 50339
 */
@Entity
@Table(name = "CREDENTIALS")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Credentials.findAll", query = "SELECT c FROM Credentials c")
    , @NamedQuery(name = "Credentials.findByRegisterid", query = "SELECT c FROM Credentials c WHERE c.registerid = :registerid")
    , @NamedQuery(name = "Credentials.findByUsername", query = "SELECT c FROM Credentials c WHERE c.username = :username")
    , @NamedQuery(name = "Credentials.findByPassword", query = "SELECT c FROM Credentials c WHERE c.password = :password")
    , @NamedQuery(name = "Credentials.findBySignupdate", query = "SELECT c FROM Credentials c WHERE c.signupdate = :signupdate")
    , @NamedQuery(name = "Credentials.findByUserid", query = "SELECT c FROM Credentials c WHERE c.userid = :userid")})
public class Credentials implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "REGISTERID")
    private Long registerid;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "USERNAME")
    private String username;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "PASSWORD")
    private String password;
    @Basic(optional = false)
    @NotNull
    @Column(name = "SIGNUPDATE")
    @Temporal(TemporalType.DATE)
    private Date signupdate;
    @JoinColumn(name = "USERID", referencedColumnName = "USERID")
    @ManyToOne
    private Users userid;

    public Credentials() {
    }
    
    


    public Credentials(Long registerid) {
        this.registerid = registerid;
    }
    
    
        public Credentials(String username, String password, Date signupdate, Users userid){
        this.username = username;
        this.password = password;
        this.signupdate = signupdate;
        this.userid = userid;
        
    }

    public Credentials(Long registerid, String username, String password, Date signupdate) {
        this.registerid = registerid;
        this.username = username;
        this.password = password;
        this.signupdate = signupdate;
    }

 

    public Long getRegisterid() {
        return registerid;
    }

    public void setRegisterid(Long registerid) {
        this.registerid = registerid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getSignupdate() {
        return signupdate;
    }

    public void setSignupdate(Date signupdate) {
        this.signupdate = signupdate;
    }

    public Users getUserid() {
        return userid;
    }

    public void setUserid(Users userid) {
        this.userid = userid;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (registerid != null ? registerid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Credentials)) {
            return false;
        }
        Credentials other = (Credentials) object;
        if ((this.registerid == null && other.registerid != null) || (this.registerid != null && !this.registerid.equals(other.registerid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "moviememoir.Credentials[ registerid=" + registerid + 
                " username = " + username + " password = "+ password + "registerid " + userid.toString() +  " ]";
    }
    
}
