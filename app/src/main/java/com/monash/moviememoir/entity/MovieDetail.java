package com.monash.moviememoir.entity;

public class MovieDetail {

    private String id;
    private String imageUrl;
    private String name;
    private String genres;
    private String cast;
    private String releaseDate;
    private String countries;
    private String directors;
    private String plot;
    private String ratingScores;

    public MovieDetail() {

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGenres() {
        return genres;
    }

    public void setGenres(String genres) {
        this.genres = genres;
    }

    public String getCast() {
        return cast;
    }

    public void setCast(String cast) {
        this.cast = cast;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getCountries() {
        return countries;
    }

    public void setCountries(String countries) {
        this.countries = countries;
    }

    public String getDirectors() {
        return directors;
    }

    public void setDirectors(String directors) {
        this.directors = directors;
    }

    public String getPlot() {
        return plot;
    }

    public void setPlot(String plot) {
        this.plot = plot;
    }

    public String getRatingScores() {
        return ratingScores;
    }

    public void setRatingScores(String ratingScores) {
        this.ratingScores = ratingScores;
    }

    @Override
    public String toString() {
        return "MovieDetail{" +
                "id='" + id + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", name='" + name + '\'' +
                ", genres='" + genres + '\'' +
                ", cast='" + cast + '\'' +
                ", releaseDate='" + releaseDate + '\'' +
                ", countries='" + countries + '\'' +
                ", directors='" + directors + '\'' +
                ", plot='" + plot + '\'' +
                ", ratingScores='" + ratingScores + '\'' +
                '}';
    }
}
