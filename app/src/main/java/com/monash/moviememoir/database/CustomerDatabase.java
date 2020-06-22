package com.monash.moviememoir.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;


import com.monash.moviememoir.dao.CustomerDAO;
import com.monash.moviememoir.entity.Customer;

@Database(entities = {Customer.class}, version = 2, exportSchema = false)
public abstract class CustomerDatabase extends RoomDatabase {

    public abstract CustomerDAO customerDao();

    private static CustomerDatabase INSTANCE;

    public static synchronized CustomerDatabase getInstance(final Context context) {

        if (INSTANCE == null) {

            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),

                    CustomerDatabase.class, "CustomerDatabase")

                    .fallbackToDestructiveMigration()
                    .build();

        }

        return INSTANCE;

    }
}