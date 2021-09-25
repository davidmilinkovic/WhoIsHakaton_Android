package com.fortablydumb.whoishakatonandroid;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface DomenDao {
    @Query("SELECT * FROM domen")
    List<Domen> getAll();

    @Query("SELECT * FROM domen WHERE naziv=:naziv")
    Domen findByName(String naziv);

    @Insert
    void insert(Domen domen);

    @Update
    void update(Domen domen);

    @Delete
    void delete(Domen domen);
}
