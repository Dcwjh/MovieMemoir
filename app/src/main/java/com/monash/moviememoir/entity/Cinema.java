package com.monash.moviememoir.entity;

public class Cinema {
    private Long cinemaid;
    private String cinemaname;
    private String location;

    public Cinema(){

    }

    public Cinema(String cinemaname, String location){
        this.cinemaname = cinemaname;
        this.location = location;
    }

    public Long getCinemaid() {
        return cinemaid;
    }

    public void setCinemaid(Long cinemaid) {
        this.cinemaid = cinemaid;
    }

    public String getCinemaname() {
        return cinemaname;
    }

    public void setCinemaname(String cinemaname) {
        this.cinemaname = cinemaname;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @Override
    public String toString() {
        return "Cinema{" +
                "cinemaid=" + cinemaid +
                ", cinemaname='" + cinemaname + '\'' +
                ", location='" + location + '\'' +
                '}';
    }
}
