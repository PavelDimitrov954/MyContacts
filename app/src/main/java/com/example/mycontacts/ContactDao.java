package com.example.mycontacts;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ContactDao {
    @Insert
    void insert(Contact contact);

    @Update
    void update(Contact contact);

    @Delete
    void delete(Contact contact);

    @Query("SELECT * FROM contacts")
    List<Contact> getAllContacts();

    @Query("SELECT * FROM contacts WHERE id = :id")
    Contact getContactById(int id);

    @Query("SELECT * FROM contacts WHERE name LIKE :search OR phoneNumber LIKE :search OR email LIKE :search")
    LiveData<List<Contact>> searchContacts(String search);

}
