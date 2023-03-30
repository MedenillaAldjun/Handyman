package com.group.trylang;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.Button;

public class User extends AppCompatActivity {

    Button handy, client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);


        handy = findViewById(R.id.btn_handyman);
        client = findViewById(R.id.btn_client);

        handy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), HandySetUp.class);
                startActivity(intent);
            }
        });

        client.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ClientSetUp.class);
                startActivity(intent);
            }
        });
    }


}