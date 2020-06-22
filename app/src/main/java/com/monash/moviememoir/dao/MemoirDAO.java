package com.monash.moviememoir.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.monash.moviememoir.entity.MemoirView;

import java.util.List;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface MemoirDAO {
    @Query("SELECT * FROM memoir")
    List<MemoirView> getAll();

    @Query("SELECT * FROM memoir WHERE recordid = :registerid LIMIT 1")
    MemoirView findByID(int registerid);

    @Insert
    void insertAll(MemoirView... memoirViews);

    @Insert
    long insert(MemoirView memoirView);

    @Delete
    void delete(MemoirView memoirView);

    @Update(onConflict = REPLACE)
    void updateMemoirs(MemoirView... memoirViews);

    @Query("DELETE FROM memoir")
    void deleteAll();

}
