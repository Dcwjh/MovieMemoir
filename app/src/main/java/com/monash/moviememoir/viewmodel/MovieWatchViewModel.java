package com.monash.moviememoir.viewmodel;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.monash.moviememoir.entity.MovieWatch;
import com.monash.moviememoir.repository.MovieWatchRepository;

import java.util.List;

public class MovieWatchViewModel extends ViewModel {
    private MovieWatchRepository cRepository;
    private MutableLiveData<List<MovieWatch>> allMovieWatchs;

    public MovieWatchViewModel() {
        allMovieWatchs =new MutableLiveData<>();
    }

    public void setmoviewatchs(List<MovieWatch> moviewatchs) {
        allMovieWatchs.setValue(moviewatchs);
    }

    public LiveData<List<MovieWatch>> getAll() {
        return cRepository.getAll();
    }

    public void initalizeVars(Application application){
        cRepository = new MovieWatchRepository(application);
    }

    public void insert(MovieWatch moviewatch) {
        cRepository.insert(moviewatch);
    }

    public void insertAll(MovieWatch... moviewatchs) {
        cRepository.insertAll(moviewatchs);
    }

    public void deleteAll() {
        cRepository.deleteAll();
    }

    public void insertAll(MovieWatch moviewatch) {
        cRepository.delete(moviewatch);
    }

    public void update(MovieWatch... moviewatchs) {
        cRepository.updateMovieWatchs(moviewatchs);
    }

    public MovieWatch insertAll(int id) {
        return cRepository.findByID(id);
    }

    public MovieWatch findByID(int moviewatchid){
        return cRepository.findByID(moviewatchid);
    }

    public MovieWatch findByIMDBID(String IMDBid){
        return cRepository.findByIMDBID(IMDBid);
    }


    public void delete(MovieWatch watchlist) {
        cRepository.delete(watchlist);
    }
}
