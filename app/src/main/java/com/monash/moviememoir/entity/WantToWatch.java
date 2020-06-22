package com.monash.moviememoir.entity;

public class WantToWatch {
    private String moviename;
    private String releasetdate;
    private String addtime;

    public WantToWatch(String moviename, String releasetdate, String addtime){
        this.moviename = moviename;
        this.releasetdate = releasetdate;
        this.addtime = addtime;
    }

    public String getMoviename() {
        return moviename;
    }

    public void setMoviename(String moviename) {
        this.moviename = moviename;
    }

    public String getReleasetdate() {
        return releasetdate;
    }

    public void setReleasetdate(String releasetdate) {
        this.releasetdate = releasetdate;
    }

    public String getAddtime() {
        return addtime;
    }

    public void setAddtime(String addtime) {
        this.addtime = addtime;
    }
}
