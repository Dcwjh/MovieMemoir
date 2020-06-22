package com.monash.moviememoir.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;


import com.monash.moviememoir.dao.MovieWatchDAO;
import com.monash.moviememoir.entity.MovieWatch;
import com.monash.moviememoir.utils.Converters;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {MovieWatch.class}, version = 2, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class MovieWatchDatabase extends RoomDatabase {

    public abstract MovieWatchDAO moviewatchDao();
    private static MovieWatchDatabase INSTANCE;

    //we create an ExecutorService with a fixed thread pool so we can later
//    run database operations asynchronously on a background thread.

    private static final int NUMBER_OF_THREADS = 4;

    public static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static synchronized MovieWatchDatabase getInstance(final Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                    MovieWatchDatabase.class, "MovieWatchDatabase")
                    .fallbackToDestructiveMigration().build();

        }
        return INSTANCE;
    }

}
