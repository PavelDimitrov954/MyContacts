package com.example.mycontacts;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactViewHolder> {

    private MainActivity mainActivity;
    private List<Contact> contacts = new ArrayList<>();

    public ContactAdapter(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    @Override
    public ContactViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.contact_item, parent, false);
        return new ContactViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ContactViewHolder holder, int position) {
        Contact currentContact = contacts.get(position);
        holder.textViewName.setText(currentContact.getName());
        holder.textViewPhoneNumber.setText(currentContact.getPhoneNumber());

        holder.buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentPosition = holder.getAdapterPosition();
                if (currentPosition != RecyclerView.NO_POSITION) {
                    deleteContact(contacts.get(currentPosition), currentPosition, v);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }

    public void setContacts(List<Contact> contacts) {
        this.contacts = contacts;
        notifyDataSetChanged();
    }

    private void deleteContact(Contact contact, int position, View v) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                ContactDatabase.getDatabase(mainActivity).contactDao().delete(contact);

                mainActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // Make sure the position is still valid
                        if (position < contacts.size()) {
                            contacts.remove(position);
                            notifyItemRemoved(position);
                            notifyItemRangeChanged(position, contacts.size());
                        }
                    }
                });
            }
        }).start();
    }


    class ContactViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewName;
        private TextView textViewPhoneNumber;
        private Button buttonDelete;

        public ContactViewHolder(View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.textViewName);
            textViewPhoneNumber = itemView.findViewById(R.id.textViewPhoneNumber);
            buttonDelete = itemView.findViewById(R.id.buttonDelete);
        }
    }
}

