package com.group.trylang;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignUp extends AppCompatActivity {

    TextView already;
    private FirebaseAuth auth;
    EditText signup_email, signup_password;

    private Button signup;
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        auth =  FirebaseAuth.getInstance();

        signup_email = findViewById(R.id.signup_email);
        signup_password = findViewById(R.id.signin_password);
        already = findViewById(R.id.already);
        signup =  findViewById(R.id.btn_signin);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

        already.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SignIn.class);
                startActivity(intent);
            }
        });

       signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = signup_email.getText().toString();
                String pass = signup_password.getText().toString();

                if(!email.isEmpty() && !pass.isEmpty()){
                    showPD();
                    auth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                progressDialog.dismiss();
                                Toast.makeText(SignUp.this, "Sign Up Successful!", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(), User.class));
                                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                                finish();
                            }else{
                                Toast.makeText(SignUp.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }else{
                    Toast.makeText(SignUp.this, "All fields required!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void showPD(){
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Signing Up");
        progressDialog.setMessage("Please wait...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMax(100);
        progressDialog.show();
    }
}
