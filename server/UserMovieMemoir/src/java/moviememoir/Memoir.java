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
import javax.persistence.Lob;
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
@Table(name = "MEMOIR")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Memoir.findAll", query = "SELECT m FROM Memoir m")
    , @NamedQuery(name = "Memoir.findByRecordid", query = "SELECT m FROM Memoir m WHERE m.recordid = :recordid")
    , @NamedQuery(name = "Memoir.findByMoviename", query = "SELECT m FROM Memoir m WHERE m.moviename = :moviename")
    , @NamedQuery(name = "Memoir.findByRelease", query = "SELECT m FROM Memoir m WHERE m.release = :release")
    , @NamedQuery(name = "Memoir.findByWatchtime", query = "SELECT m FROM Memoir m WHERE m.watchtime = :watchtime")
    , @NamedQuery(name = "Memoir.findByComment", query = "SELECT m FROM Memoir m WHERE m.comment = :comment")
    , @NamedQuery(name = "Memoir.findByScore", query = "SELECT m FROM Memoir m WHERE m.score = :score")
    , @NamedQuery(name = "Memoir.findByUserid", query = "SELECT m FROM Memoir m WHERE m.userid = :userid")
    , @NamedQuery(name = "Memoir.findByCinemaid", query = "SELECT m FROM Memoir m WHERE m.cinemaid = :cinemaid")
    , @NamedQuery(name = "Memoir.findByStaticTwoAttributes", query = "SELECT m FROM Memoir m WHERE m.moviename = :moviename AND m.cinemaid.cinemaname = :cinemaname")
})
public class Memoir implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "RECORDID")
    private Long recordid;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "MOVIENAME")
    private String moviename;
    @Basic(optional = false)
    @NotNull
    @Column(name = "RELEASE")
    @Temporal(TemporalType.DATE)
    private Date release;
    @Basic(optional = false)
    @NotNull
    @Column(name = "WATCHTIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date watchtime;
    @Basic(optional = false)
    @NotNull
    @Lob
    @Size(min = 1, max = 32700)
    @Column(name = "COMMENT")
    private String comment;
    @Basic(optional = false)
    @NotNull
    @Column(name = "SCORE")
    private short score;
    @JoinColumn(name = "CINEMAID", referencedColumnName = "CINEMAID")
    @ManyToOne
    private Cinema cinemaid;
    @JoinColumn(name = "USERID", referencedColumnName = "USERID")
    @ManyToOne
    private Users userid;

    public Memoir() {
    }

    public Memoir(Long recordid) {
        this.recordid = recordid;
    }

    public Memoir(Long recordid, String moviename, Date release, Date watchtime, String comment, short score) {
        this.recordid = recordid;
        this.moviename = moviename;
        this.release = release;
        this.watchtime = watchtime;
        this.comment = comment;
        this.score = score;
    }

    public Long getRecordid() {
        return recordid;
    }

    public void setRecordid(Long recordid) {
        this.recordid = recordid;
    }

    public String getMoviename() {
        return moviename;
    }

    public void setMoviename(String moviename) {
        this.moviename = moviename;
    }

    public Date getRelease() {
        return release;
    }

    public void setRelease(Date release) {
        this.release = release;
    }

    public Date getWatchtime() {
        return watchtime;
    }

    public void setWatchtime(Date watchtime) {
        this.watchtime = watchtime;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public short getScore() {
        return score;
    }

    public void setScore(short score) {
        this.score = score;
    }

    public Cinema getCinemaid() {
        return cinemaid;
    }

    public void setCinemaid(Cinema cinemaid) {
        this.cinemaid = cinemaid;
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
        hash += (recordid != null ? recordid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Memoir)) {
            return false;
        }
        Memoir other = (Memoir) object;
        if ((this.recordid == null && other.recordid != null) || (this.recordid != null && !this.recordid.equals(other.recordid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "moviememoir.Memoir[ recordid=" + recordid + " ]";
    }
    
}
