package com.group.trylang;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Pair;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Splash extends AppCompatActivity {

    private View logo, logo_text, logo_tag, logo_bot;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        logo = findViewById(R.id.logo);
        logo_text = findViewById(R.id.logo_text);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                FirebaseUser currentUser = firebaseAuth.getCurrentUser();
                if (currentUser == null) {
                   Intent intent = new Intent(Splash.this, SignIn.class);
                    ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(Splash.this,
                            Pair.create(logo,"logo"),
                            Pair.create(logo_text, "logo_text"));
                    startActivity(intent, options.toBundle());
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                }else {
                    Intent intent = new Intent(Splash.this, User.class);
                    ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(Splash.this,
                            Pair.create(logo,"logo"),
                            Pair.create(logo_text, "logo_text"));
                    startActivity(intent, options.toBundle());
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                }
           }
        }, 1500);

    //}
    }
}