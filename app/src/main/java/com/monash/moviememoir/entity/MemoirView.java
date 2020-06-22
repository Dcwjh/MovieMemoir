package com.monash.moviememoir.entity;



public class MemoirView {
    private String id;
   private String moviename;
   private String releasedate;
   private String watchdate;
   private String comment;
   private int userscore;
   private int publicscore;
   private String postcode;
   private String imageUrl;
   private String genre;

    public MemoirView(String id,String moviename, String releasedate, String watchdate, String comment, int userscore, int publicscore, String postcode, String imageUrl){
        this.id = id;
        this.moviename = moviename;
        this.releasedate = releasedate;
        this.watchdate = watchdate;
        this.comment = comment;
        this.userscore = userscore;
        this.publicscore = publicscore;
        this.postcode = postcode;
        this.imageUrl = imageUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getMoviename() {
        return moviename;
    }

    public void setMoviename(String moviename) {
        this.moviename = moviename;
    }

    public String getReleasedate() {
        return releasedate;
    }

    public void setReleasedate(String releasedate) {
        this.releasedate = releasedate;
    }

    public String getWatchdate() {
        return watchdate;
    }

    public void setWatchdate(String watchdate) {
        this.watchdate = watchdate;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getUserscore() {
        return userscore;
    }

    public void setUserscore(int userscore) {
        this.userscore = userscore;
    }

    public int getPublicscore() {
        return publicscore;
    }

    public void setPublicscore(int publicscore) {
        this.publicscore = publicscore;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    @Override
    public String toString() {
        return "MemoirView{" +
                "id='" + id + '\'' +
                ", moviename='" + moviename + '\'' +
                ", releasedate='" + releasedate + '\'' +
                ", watchdate='" + watchdate + '\'' +
                ", comment='" + comment + '\'' +
                ", userscore=" + userscore +
                ", publicscore=" + publicscore +
                ", postcode='" + postcode + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", genre='" + genre + '\'' +
                '}';
    }
}
