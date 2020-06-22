package com.monash.moviememoir.networkconnect;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.monash.moviememoir.entity.MovieDetail;
import com.monash.moviememoir.entity.MovieInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class IMDbConnect {

    private static final String BASE_URL = "https://imdb-api.com/en/API/";
    private OkHttpClient client;
    private Gson gson;

    public IMDbConnect() {
        client = new OkHttpClient();
        gson = new Gson();
    }


    public List<MovieInfo> searchMovie(String keyword) {
        final String methodPath = "SearchMovie/k_qzQh6KmJ/" + keyword;
        String response = get(BASE_URL + methodPath);
        if (!response.isEmpty()) {
            Map<String, Object> map = gson.fromJson(response, new TypeToken<Map<String, Object>>() {
            }.getType());
            List<Map<String, String>> list = (List<Map<String, String>>) map.get("results");
            Pattern pattern = Pattern.compile("(\\d+)");

            List<MovieInfo> movies = new ArrayList<>();
            if(list!= null) {
                for (Map<String, String> item : list) {
                    MovieInfo movie = new MovieInfo();
                    movie.setId(item.get("id"));
                    movie.setImageURL(item.get("image"));
                    movie.setMovieName(item.get("title"));
                    Matcher matcher = pattern.matcher(item.get("description"));
                    String releaseYear = matcher.find() ? matcher.group(0) : "";
                    movie.setReleaseYear(releaseYear);
                    movies.add(movie);
                }
            }
            return movies;
        }
        return null;
    }


    public MovieDetail getMovieDetail(String id) {
        final String methodPath = "Title/k_qzQh6KmJ/" + id;
        String response = get(BASE_URL + methodPath);
        Map<String, Object> map = gson.fromJson(response, new TypeToken<Map<String, Object>>() {
        }.getType());

        String name = (String) map.get("title");
        String releaseDate = (String) map.get("releaseDate");
        String genres = (String) map.get("genres");
        String countries = (String) map.get("countries");
        String ratingScores = (String) map.get("imDbRating");
        String directors = (String) map.get("directors");
        String plot = (String) map.get("plot");
        List<Map<String, String>> actorList = (List<Map<String, String>>) map.get("actorList");
        List<String> actors = new ArrayList<>();
        for (Map<String, String> item : actorList) {
            actors.add(item.get("name"));
        }
        String cast = TextUtils.join(", ", actors);

        MovieDetail movieDetail = new MovieDetail();
        movieDetail.setId(id);
        movieDetail.setName(name);
        movieDetail.setGenres(genres);
        movieDetail.setCast(cast);
        movieDetail.setReleaseDate(releaseDate);
        movieDetail.setCountries(countries);
        movieDetail.setDirectors(directors);
        movieDetail.setPlot(plot);
        movieDetail.setRatingScores(ratingScores);
        return movieDetail;
    }

    public String getImageUrl(String IMDbID) {
        final String methodPath = "Posters/k_qzQh6KmJ/" + IMDbID;
        String response = get(BASE_URL + methodPath);
        String imageUrl = "";
        try {
            Map<String, Object> data = gson.fromJson(response, new TypeToken<Map<String, Object>>() {
            }.getType());

            List<Map<String, Object>> posters = (List<Map<String, Object>>) data.get("posters");
            if (!posters.isEmpty()) {
                imageUrl = (String) posters.get(0).get("link");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return imageUrl;
    }


    private String get(String url) {
        String rs = "";
        Request request = new Request.Builder().url(url).get().build();
        try {
            Response response = client.newCall(request).execute();
            rs = response.body().string();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rs;
    }
}
