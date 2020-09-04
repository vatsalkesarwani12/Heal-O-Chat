package com.vatsal.kesarwani.therapy.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.vatsal.kesarwani.therapy.R;

public class AboutUs extends AppCompatActivity {
    private ImageButton linkedin,github,twitter;
    private String llinkedin,lgit,ltweet,lrepo;
    private ImageView star;
    private TextView tv_star;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        init();
        llinkedin="https://www.linkedin.com/in/vatsal-kesarwani-4a3858171/";
        lgit="https://github.com/plazzy99";
        ltweet="https://twitter.com/KesarwaniVatsal";
        lrepo="https://github.com/plazzy99/Therapy";

        linkedin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(llinkedin));
                startActivity(i);
            }
        });

        github.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(lgit));
                startActivity(i);
            }
        });

        twitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(ltweet));
                startActivity(i);
            }
        });

        star.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                starRepo();
            }
        });

        tv_star.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                starRepo();
            }
        });

    }

    private void starRepo() {
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(lrepo));
        startActivity(i);
    }

    private  void init(){
        linkedin=findViewById(R.id.linkedin);
        github=findViewById(R.id.github);
        twitter=findViewById(R.id.twitter);
        star=findViewById(R.id.starImage);
        tv_star=findViewById(R.id.star);
    }
}