package com.monash.moviememoir.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;


import com.monash.moviememoir.entity.MovieWatch;

import java.util.List;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface MovieWatchDAO {
    
    @Query("SELECT * FROM moviewatch")
    LiveData<List<MovieWatch>> getAll();

    @Query("SELECT * FROM moviewatch WHERE id = :id LIMIT 1")
    MovieWatch findByID(int id);

    @Query("SELECT * FROM moviewatch WHERE id = :IMDBid LIMIT 1")
    MovieWatch findByIMDBID(String IMDBid);

    @Insert
    void insertAll(MovieWatch... moviewatchs);

    @Insert
    long insert(MovieWatch moviewatch);

    @Delete
    void delete(MovieWatch moviewatch);

    @Update(onConflict = REPLACE)
    void updateMovieWatchs(MovieWatch... moviewatchs);

    @Query("DELETE FROM moviewatch")
    void deleteAll();
}
