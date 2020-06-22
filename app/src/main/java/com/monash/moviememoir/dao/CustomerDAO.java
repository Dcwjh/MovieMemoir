package com.monash.moviememoir.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.monash.moviememoir.entity.Customer;

import java.util.List;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface CustomerDAO {

    @Query("SELECT * FROM customer")
    List<Customer> getAll();
    @Query("SELECT * FROM customer WHERE uid = :customerId LIMIT 1")
    Customer findByID(int customerId);
    @Insert
    void insertAll(Customer... customers);
    @Insert
    long insert(Customer customer);
    @Delete
    void delete(Customer customer);
    @Update(onConflict = REPLACE)

    void updateCustomers(Customer... customers);
    @Query("DELETE FROM customer")
    void deleteAll();
}