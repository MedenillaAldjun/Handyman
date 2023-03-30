package com.group.trylang;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class AddResume extends AppCompatActivity {

    private EditText name, age, gender, address_handy, contact;
    private Button publish;

    private CircleImageView preview;
    private static final int req = 1;
    private Uri mImgUrl = null;

    private StorageReference storage;
    private FirebaseFirestore firestore;
    private FirebaseAuth auth;
    private String currentUserId,address;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_resume);

        preview = findViewById(R.id.preview);
        name = findViewById(R.id.fullname);
        age = findViewById(R.id.age);
        publish = findViewById(R.id.publish);
        gender = findViewById(R.id.gender);
        address_handy = findViewById(R.id.address);
        contact = findViewById(R.id.contact);

        storage = FirebaseStorage.getInstance().getReference();
        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        currentUserId = auth.getCurrentUser().getUid();

        preview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, req);
            }
        });

        publish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String client_name = name.getText().toString();
                String client_age = age.getText().toString();
                String client_gender = gender.getText().toString();
                String client_address = address_handy.getText().toString();
                String client_contact = contact.getText().toString();

                if(!client_name.isEmpty() && !client_age.isEmpty() && !client_gender.isEmpty() &&  !client_address.isEmpty() && !client_contact.isEmpty() && mImgUrl!= null){
                    showPD();
                    StorageReference postRef = storage.child("Handy Plumbing").child(FieldValue.serverTimestamp().toString() + ".jpg");
                    postRef.putFile(mImgUrl).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            if(task.isSuccessful()){
                                postRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        HashMap<String, Object> postMap = new HashMap<>();

                                        postMap.put("image", uri.toString());
                                        postMap.put("user", currentUserId);
                                        postMap.put("fullname", client_name);
                                        postMap.put("age", client_age);
                                        postMap.put("gender", client_gender);
                                        postMap.put("address", client_address);
                                        postMap.put("contact", client_contact);
                                        postMap.put("time", FieldValue.serverTimestamp());

                                        firestore.collection("Plumbing").add(postMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentReference> task) {
                                                if(task.isSuccessful()){
                                                    progressDialog.dismiss();
                                                    LayoutInflater inflater = getLayoutInflater();
                                                    View layout = inflater.inflate(R.layout.custom_toast, (ViewGroup)findViewById(R.id.custom_toast));
                                                    final Toast toast = new Toast(getApplicationContext());
                                                    toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                                                    toast.setDuration(Toast.LENGTH_SHORT);
                                                    toast.setView(layout);

                                                    toast.show();
                                                    startActivity(new Intent(AddResume.this, HandyDashboard.class));
                                                    finish();
                                                }else{
                                                    Toast.makeText(AddResume.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                    }
                                });

                            }else{
                                Toast.makeText(AddResume.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
                }else{
                    Toast.makeText(AddResume.this, "Please check the fields required!", Toast.LENGTH_SHORT).show();
                }


            }
        });



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == req && resultCode == RESULT_OK) {
            mImgUrl = data.getData();
            preview.setImageURI(mImgUrl);
        }


    }

    private void showPD(){
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Adding Post");
        progressDialog.setMessage("Please wait...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMax(100);
        progressDialog.show();
    }


}