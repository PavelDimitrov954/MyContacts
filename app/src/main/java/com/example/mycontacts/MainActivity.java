package com.example.mycontacts;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ContactDatabase database;
    private ContactAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SearchView searchView = findViewById(R.id.searchView);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ContactAdapter(this);
        recyclerView.setAdapter(adapter);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                newText = "%" + newText + "%";
                ContactDatabase.getDatabase(getApplicationContext()).contactDao().searchContacts(newText).observe(MainActivity.this, new Observer<List<Contact>>() {
                    @Override
                    public void onChanged(List<Contact> contacts) {
                        adapter.setContacts(contacts);
                    }
                });
                return true;
            }
        });

        database = ContactDatabase.getDatabase(this);

        FloatingActionButton fab = findViewById(R.id.fabAddContact);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Navigate to Add/Edit Contact Activity
                startActivity(new Intent(MainActivity.this, EditContactActivity.class));
            }
        });

        // Load contacts from the database and display
        loadContacts();
    }

    private void loadContacts() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                final List<Contact> contacts = database.contactDao().getAllContacts();
                // Update the UI on the main thread
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.setContacts(contacts);
                    }
                });
            }
        }).start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Refresh the list of contacts
        loadContacts();
    }
}
