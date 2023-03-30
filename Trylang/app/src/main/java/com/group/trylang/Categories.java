package com.group.trylang;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class Categories extends AppCompatActivity {

    private ImageButton plumbing,carpentry,masonry,construction;
    private Toolbar category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);

        category = findViewById(R.id.category_toolbar);
        setSupportActionBar(category);
        getSupportActionBar().setTitle("Categories");

        plumbing = findViewById(R.id.btn_plumbing);
        carpentry = findViewById(R.id.btn_carpentry);
        masonry = findViewById(R.id.btn_masonry);
        construction = findViewById(R.id.btn_construction);

        plumbing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), AddPostActivity.class));
            }
        });

        carpentry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), AddPostActivity2.class));
            }
        });

        masonry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), AddPostActivity3.class));
            }
        });

        construction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), AddPostActivity4.class));
            }
        });








    }
}