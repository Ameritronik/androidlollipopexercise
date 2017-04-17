package com.codepath.android.lollipopexercise.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.codepath.android.lollipopexercise.R;
import com.codepath.android.lollipopexercise.adapters.ContactsAdapter;
import com.codepath.android.lollipopexercise.models.Contact;

import java.util.List;

import static com.codepath.android.lollipopexercise.R.*;
import static com.codepath.android.lollipopexercise.R.id.*;


public class ContactsActivity extends AppCompatActivity {
    private RecyclerView rvContacts;
    private ContactsAdapter mAdapter;
    private List<Contact> contacts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.activity_contacts);

        // Find RecyclerView and bind to adapter
        rvContacts = (RecyclerView) findViewById(id.rvContacts);

        // allows for optimizations
        rvContacts.setHasFixedSize(true);

        // Define 2 column grid layout
        final GridLayoutManager layout = new GridLayoutManager(ContactsActivity.this, 2);

        // Unlike ListView, you have to explicitly give a LayoutManager to the RecyclerView to position items on the screen.
        // There are three LayoutManager provided at the moment: GridLayoutManager, StaggeredGridLayoutManager and LinearLayoutManager.
        rvContacts.setLayoutManager(layout);

        // get data
        contacts = Contact.getContacts();

        // Create an adapter
        mAdapter = new ContactsAdapter(ContactsActivity.this, contacts);

        // Bind adapter to list
        rvContacts.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new ContactsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                String name = contacts.get(position).getName();
                //Toast.makeText(getBaseContext(), name + " was clicked!", Toast.LENGTH_SHORT).show();
                launchDetailsView(name, position);
            }
        });

    }

    public void launchDetailsView(String name, int position) {
        // first parameter is the context, second is the class of the activity to launch
        Intent i = new Intent(this, DetailsActivity.class);
        // put "extras" into the bundle for access in the second activity
        i.putExtra("EXTRA_CONTACT", contacts.get(position));
        i.putExtra("position", position);
        ActivityOptionsCompat options = ActivityOptionsCompat.
                makeSceneTransitionAnimation(this, rvContacts, "profile");
        // brings up the second activity
        startActivity(i, options.toBundle());
    }


    public void onComposeAction(MenuItem mi) {
        // handle click here
        final View.OnClickListener myOnClickDelListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Do something here
                contacts.remove(0);
                mAdapter.notifyDataSetChanged();
                Snackbar k = Snackbar.make(rvContacts, string.snackbar_removed, Snackbar.LENGTH_LONG);
                View kView = k.getView();
                kView.setBackgroundColor(Color.BLUE);
                TextView textView = (TextView) kView.findViewById(android.support.design.R.id.snackbar_text);
                textView.setMaxLines(5);//this is your max line as your want
                textView.setTextSize(20);
                k.show();

            }
        };
        View.OnClickListener myOnClickAddListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            // Do something here
                Contact c = Contact.getRandomContact(getApplicationContext());
                contacts.add(0,c);
                mAdapter.notifyDataSetChanged();
                Snackbar k = Snackbar.make(rvContacts, string.snackbar_added, Snackbar.LENGTH_INDEFINITE);
                k.setAction(string.snackbar_undo, myOnClickDelListener);
                View kView = k.getView();
                kView.setBackgroundColor(Color.BLUE);
                TextView textView = (TextView) kView.findViewById(android.support.design.R.id.snackbar_text);
                textView.setMaxLines(5);//this is your max line as your want
                textView.setTextSize(20);
                k.show();

            }
        };
        // Pass in the click listener when displaying the Snackbar
        Snackbar s =Snackbar.make(rvContacts, string.snackbar_text, Snackbar.LENGTH_INDEFINITE);
                 s.setAction(string.snackbar_action, myOnClickAddListener);
                 View sView = s.getView();
                 sView.setBackgroundColor(Color.BLUE);
                 TextView textView = (TextView) sView.findViewById(android.support.design.R.id.snackbar_text);
                 textView.setMaxLines(5);//this is your max line as your want
                 textView.setTextSize(20);
                 s.show(); // Don’t forget to show!
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_contacts, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
         switch(id) {
             case miCompose:
                 onComposeAction(item);
                 return true;
             default:
                 return super.onOptionsItemSelected(item);
         }
    }
}
