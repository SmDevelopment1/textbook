package com.MrSoftIt.class9_10allbook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

public class WebViewActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {



    ProgressBar progressBar;
    public DrawerLayout drawer1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_support);
        toolbar.setTitle("নবম ও দশম শ্রেণির পাঠ্যপুস্তক");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
      //  getSupportActionBar().setDisplayShowHomeEnabled(true);


        progressBar = findViewById(R.id.progress_circular_wev);

        WebView webView = (WebView) findViewById(R.id.webview);






        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent homeIntnt =new Intent(WebViewActivity.this,MainActivity.class);
                startActivity(homeIntnt);
                finish();
            }
        });



    }

    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_message:

                Intent homeIntnt =new Intent(WebViewActivity.this,MainActivity.class);
                startActivity(homeIntnt);
                break;
            case R.id.nav_chat:
                Intent resultIntnt =new Intent(WebViewActivity.this,WebViewActivity.class);
                startActivity(resultIntnt);

                break;
            case R.id.nav_profile:
                Toast.makeText(this, "text   .....", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_share:
                Toast.makeText(this, "Share", Toast.LENGTH_SHORT).show();
                break;

        }

        drawer1.closeDrawer(GravityCompat.START);
        return true;
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}
