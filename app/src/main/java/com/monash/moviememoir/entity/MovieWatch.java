package com.monash.moviememoir.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity
public class MovieWatch {
    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "movie_name")
    public String movieName;

    @ColumnInfo(name = "release_date")
    public Date releaseDate;

    @ColumnInfo(name = "add_datetime")
    public Date addDatetime;

    @ColumnInfo(name = "imdb_id")
    public String IMDbID;

    @ColumnInfo(name = "image_url")
    public String imageurl;


    @Ignore
    public MovieWatch() {

    }

    public MovieWatch(String movieName, Date releaseDate, Date addDatetime, String IMDbID) {
        this.movieName = movieName;
        this.releaseDate = releaseDate;
        this.addDatetime = addDatetime;
        this.IMDbID = IMDbID;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMovieName() {
        return movieName;
    }

    public void setMovieName(String movieName) {
        this.movieName = movieName;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }

    public Date getAddDatetime() {
        return addDatetime;
    }

    public void setAddDatetime(Date addDatetime) {
        this.addDatetime = addDatetime;
    }

    public String getIMDbID() {
        return IMDbID;
    }

    public void setIMDbID(String IMDbID) {
        this.IMDbID = IMDbID;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }
}
