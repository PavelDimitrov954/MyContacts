package com.example.mycontacts;


import android.os.AsyncTask;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.concurrent.Executors;

public class EditContactActivity extends AppCompatActivity {

    private EditText editTextName, editTextPhoneNumber, editTextEmail, editTextAddress;
    private Button buttonSave;
    private ContactDatabase database;
    private Contact contact;
    private boolean isEditMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_contact);

        editTextName = findViewById(R.id.editTextName);
        editTextPhoneNumber = findViewById(R.id.editTextPhoneNumber);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextAddress = findViewById(R.id.editTextAddress);
        buttonSave = findViewById(R.id.buttonSave);

        database = ContactDatabase.getDatabase(this);

        // Check if we are in edit mode
        if (getIntent().hasExtra("contact_id")) {
            int contactId = getIntent().getIntExtra("contact_id", -1);
            contact = database.contactDao().getContactById(contactId);
            fillContactData(contact);
            isEditMode = true;
        }

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveContact();
            }
        });
    }

    private void fillContactData(Contact contact) {
        editTextName.setText(contact.getName());
        editTextPhoneNumber.setText(contact.getPhoneNumber());
        editTextEmail.setText(contact.getEmail());
        editTextAddress.setText(contact.getAddress());
    }

    private void saveContact() {
        final String name = editTextName.getText().toString();
        final String phoneNumber = editTextPhoneNumber.getText().toString();
        final String email = editTextEmail.getText().toString();
        final String address = editTextAddress.getText().toString();

        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                if (isEditMode) {
                    // Update existing contact
                    contact.setName(name);
                    contact.setPhoneNumber(phoneNumber);
                    contact.setEmail(email);
                    contact.setAddress(address);
                    database.contactDao().update(contact);
                } else {
                    // Create new contact
                    contact = new Contact(name, phoneNumber, email, address);
                    database.contactDao().insert(contact);
                }

                // Finish activity on UI thread
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        finish(); // Close the activity
                    }
                });
            }
        });
    }
}

//    private void saveContact() {
//        final String name = editTextName.getText().toString();
//        final String phoneNumber = editTextPhoneNumber.getText().toString();
//        final String email = editTextEmail.getText().toString();
//        final String address = editTextAddress.getText().toString();
//
//        new AsyncTask<Void, Void, Void>() {
//            @Override
//            protected Void doInBackground(Void... voids) {
//                if (isEditMode) {
//                    // Update existing contact
//                    contact.setName(name);
//                    contact.setPhoneNumber(phoneNumber);
//                    contact.setEmail(email);
//                    contact.setAddress(address);
//                    database.contactDao().update(contact);
//                } else {
//                    // Create new contact
//                    contact = new Contact(name, phoneNumber, email, address);
//                    database.contactDao().insert(contact);
//                }
//                return null;
//            }
//
//            @Override
//            protected void onPostExecute(Void aVoid) {
//                super.onPostExecute(aVoid);
//                finish(); // Close the activity
//            }
//        }.execute();
//    }
//}
