package com.monash.moviememoir.entity;

public class Memoir {
    private String moviename;
    private String release;
    private String watchtime;
    private String comment;
    private int score;
    private Users userid;
    private Cinema cinemaid;

    public String getMoviename() {
        return moviename;
    }

    public void setMoviename(String moviename) {
        this.moviename = moviename;
    }

    public String getRelease() {
        return release;
    }

    public void setRelease(String release) {
        this.release = release;
    }

    public String getWatchtime() {
        return watchtime;
    }

    public void setWatchtime(String watchtime) {
        this.watchtime = watchtime;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public Users getUserid() {
        return userid;
    }

    public void setUserid(Users userid) {
        this.userid = userid;
    }

    public Cinema getCinemaid() {
        return cinemaid;
    }

    public void setCinemaid(Cinema cinemaid) {
        this.cinemaid = cinemaid;
    }
}
