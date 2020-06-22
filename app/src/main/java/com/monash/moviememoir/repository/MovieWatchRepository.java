package com.monash.moviememoir.repository;

import android.app.Application;


import androidx.lifecycle.LiveData;

import com.monash.moviememoir.dao.MovieWatchDAO;
import com.monash.moviememoir.database.MovieWatchDatabase;
import com.monash.moviememoir.entity.MovieWatch;

import java.util.List;

public class MovieWatchRepository {
    private MovieWatchDAO dao;
    private LiveData<List<MovieWatch>> allMovieWatchs;
    private MovieWatch moviewatch;

    public MovieWatchRepository(Application application){
        MovieWatchDatabase db = MovieWatchDatabase.getInstance(application);
        dao = db.moviewatchDao();
    }
//    public LiveData<List<MovieWatch>> getAllMovieWatchs(){
////        allMovieWatchs = (LiveData<List<MovieWatch>>) dao.getAll();
////        return allMovieWatchs;
////    }

    public LiveData<List<MovieWatch>> getAll() {
        allMovieWatchs = dao.getAll();
        return allMovieWatchs;
    }

    public void insert(final  MovieWatch moviewatch){
        MovieWatchDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                dao.insert(moviewatch);
            }
        });
    }

    public void deleteAll(){
        MovieWatchDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                dao.deleteAll();
            }
        });
    }


    public void delete(final  MovieWatch moviewatch){
        MovieWatchDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                dao.delete(moviewatch);
            }
        });
    }

    public void insertAll(final MovieWatch... moviewatchs){
        MovieWatchDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                dao.insertAll(moviewatchs);
            }
        });
    }

    public void updateMovieWatchs(final MovieWatch... moviewatchs){
        MovieWatchDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                dao.updateMovieWatchs(moviewatchs);
            }
        });
    }

    public MovieWatch findByID(final  int id){
        MovieWatchDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                MovieWatch runMovieWatch = dao.findByID(id);
                setMovieWatch(runMovieWatch);
            }
        });
        return moviewatch;
    }

    public MovieWatch findByIMDBID(final  String IMDBid){
        MovieWatchDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                MovieWatch runMovieWatch = dao.findByIMDBID(IMDBid);
                setMovieWatch(runMovieWatch);
            }
        });
        return moviewatch;
    }

    public void setMovieWatch(MovieWatch moviewatch){
        this.moviewatch = moviewatch;
    }
}
