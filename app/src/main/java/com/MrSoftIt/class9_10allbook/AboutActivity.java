package com.MrSoftIt.class9_10allbook;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class AboutActivity extends AppCompatActivity {


    private TextView con,gmail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);


        con = findViewById(R.id.con);
        gmail = findViewById(R.id.gmail);
        con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                con.setVisibility(View.GONE);
                gmail.setVisibility(View.VISIBLE);
            }
        });

    }
}
