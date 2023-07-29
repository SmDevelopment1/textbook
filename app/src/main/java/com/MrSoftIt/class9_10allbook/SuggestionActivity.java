package com.MrSoftIt.class9_10allbook;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import static com.MrSoftIt.class9_10allbook.SuggetionAdapter.*;

public class SuggestionActivity extends AppCompatActivity {


    private DrawerLayout drawer;
    private SuggetionAdapter suggetionAdapter;

    FirebaseFirestore db = FirebaseFirestore.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggestion);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_support);
        toolbar.setTitle("নবম ও দশম শ্রেণির পাঠ্যপুস্তক");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.navigationView);

        RecyclerView recyclerView = findViewById(R.id.recycleaview_suggestion);


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        // TODO: Add adView to your view hierarchy.






        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent homeIntnt =new Intent(SuggestionActivity.this,MainActivity.class);
                startActivity(homeIntnt);
                finish();
            }
        });

        recyclear ();

    }

    private void recyclear () {


        CollectionReference collectionReference = db.collection("suggestion");


        Query query = collectionReference;


        FirestoreRecyclerOptions<Sugg_Note> options2 = new FirestoreRecyclerOptions.Builder<Sugg_Note>()
                .setQuery(query, Sugg_Note.class)
                .build();
        suggetionAdapter = new SuggetionAdapter(options2);

        RecyclerView recyclerView = findViewById(R.id.recycleaview_suggestion);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(suggetionAdapter);




        suggetionAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {

                Toast.makeText(SuggestionActivity.this, "click   ", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    protected void onStart () {
        super.onStart();
        suggetionAdapter.startListening();
    }


    @Override
    protected void onStop () {
        super.onStop();
        suggetionAdapter.stopListening();
    }


}
