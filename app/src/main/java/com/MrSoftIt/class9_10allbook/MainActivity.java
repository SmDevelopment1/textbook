package com.MrSoftIt.class9_10allbook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import android.view.MenuItem;

import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;


import com.flurry.android.FlurryAgent;
import com.flurry.android.FlurryPerformance;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.List;

import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,EasyPermissions.PermissionCallbacks {

    private DrawerLayout drawer;

    private BookAdapter adapter;



    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference collectionReference = db.collection("users");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_support);
        toolbar.setTitle("নবম ও দশম শ্রেণির পাঠ্যপুস্তক");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);



        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        // TODO: Add adView to your view hierarchy.


        new FlurryAgent.Builder()
                .withDataSaleOptOut(false) //CCPA - the default value is false
                .withCaptureUncaughtExceptions(true)
                .withIncludeBackgroundSessionsInMetrics(true)
                .withPerformanceMetrics(FlurryPerformance.ALL)
                .build(this,"MHNDFHMZ75GJ5R2635VP");



        recyclear();

        String[] perms = {android.Manifest.permission.READ_EXTERNAL_STORAGE,android.Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (EasyPermissions.hasPermissions(this, perms)) {
            if (MainActivity.InternetConnection.checkConnection(MainActivity.this)) {
                // Its Available...

            } else {
                // Not Available...
                Toast.makeText(this, " No Internet ", Toast.LENGTH_LONG).show();

            }


        } else {
            EasyPermissions.requestPermissions(this, "We need storage permissions ",
                    123, perms);
        }

    }

    private void recyclear() {


        Query query = collectionReference;

        FirestoreRecyclerOptions<NoteClass> options = new FirestoreRecyclerOptions.Builder<NoteClass>()
                .setQuery(query, NoteClass.class)
                .build();

        adapter = new BookAdapter(options);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,2,GridLayoutManager.VERTICAL,false);
        RecyclerView recyclerView = findViewById(R.id.recyclearView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(gridLayoutManager);
       // recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);


        adapter.setOnItemClickListener(new BookAdapter.OnItemClickListener(){
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                NoteClass noteClass = documentSnapshot.toObject(NoteClass.class);
                String id = documentSnapshot.getId();
                String pdf = noteClass.getPdfUrl();
                String name= noteClass.getName();

                 Intent pdfIntent = new Intent(MainActivity.this,ChaptersActivity.class);

                   pdfIntent.putExtra("id",id);
                   pdfIntent.putExtra("pdf",pdf);
                   pdfIntent.putExtra("BookName",name);

                   startActivity(pdfIntent);
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_message:

                Intent homeIntnt =new Intent(MainActivity.this,MainActivity.class);
                startActivity(homeIntnt);
                break;
            case R.id.nav_chat:
                Intent resultIntnt =new Intent(MainActivity.this,WebViewActivity.class);
                startActivity(resultIntnt);

                break;
            case R.id.nav_profile:
                Intent resultIntnt1 =new Intent(MainActivity.this,SuggestionActivity.class);
                startActivity(resultIntnt1);
                break;
            case R.id.nav_share:
                Intent resultIntnt2 =new Intent(MainActivity.this,AboutActivity.class);
                startActivity(resultIntnt2);

                break;

        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    public static class InternetConnection {

        /**
         * CHECK WHETHER INTERNET CONNECTION IS AVAILABLE OR NOT
         */
        public static   boolean checkConnection(Context context) {
            final ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

            if (connMgr != null) {
                NetworkInfo activeNetworkInfo = connMgr.getActiveNetworkInfo();

                if (activeNetworkInfo != null) { // connected to the internet
                    // connected to the mobile provider's data plan
                    if (activeNetworkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                        // connected to wifi
                        return true;
                    } else return activeNetworkInfo.getType() == ConnectivityManager.TYPE_MOBILE;
                }
            }
            return false;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }


    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {

        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this).build().show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == AppSettingsDialog.DEFAULT_SETTINGS_REQ_CODE) {

        }
    }

}
