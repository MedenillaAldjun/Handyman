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
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class ClientSetUp extends AppCompatActivity {

    private CircleImageView circleImageView;
    private EditText name, age, gender, address, contact;

    private static final int img_req = 1;
    private Uri imgUrl = null;
    FloatingActionButton nexted;


    private FirebaseAuth auth;
    private FirebaseFirestore firestore;
    private String Uid;

    private ProgressDialog progressDialog;
    private StorageReference storageReference;

    private boolean isPhotoSelected = false;

    private Button save;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_set_up);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

        circleImageView = findViewById(R.id.profile_pic);
        name = findViewById(R.id.client_name);
        age = findViewById(R.id.client_age);
        gender = findViewById(R.id.client_gender);
        address = findViewById(R.id.client_address);
        contact = findViewById(R.id.client_contact);

        nexted =  findViewById(R.id.next);

        nexted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), ClientDashboard.class
                ));
            }
        });

        storageReference = FirebaseStorage.getInstance().getReference();

        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        Uid = auth.getCurrentUser().getUid();

        save = findViewById(R.id.btn_save);

        //retrieve  profile
        firestore.collection("Clients").document(Uid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    if (task.getResult().exists()) {
                        String imageUri = task.getResult().getString("image");
                        String name1 = task.getResult().getString("client_name");
                        String age1 = task.getResult().getString("client_age");
                        String gender1 = task.getResult().getString("client_gender");
                        String address1= task.getResult().getString("client_address");
                        String contact1 = task.getResult().getString("client_contact");
                        name.setText(name1);
                        age.setText(age1);
                        gender.setText(gender1);
                        address.setText(address1);
                        contact.setText(contact1);

                        Glide.with(ClientSetUp.this).load(imageUri).into(circleImageView);
                    }
                }
            }
        });


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String client_name = name.getText().toString();
                String client_age = age.getText().toString();
                String client_gender = gender.getText().toString();
                String client_address = address.getText().toString();
                String client_contact = contact.getText().toString();

                if(!client_name.isEmpty() && !client_address.isEmpty() && !client_contact.isEmpty() && !client_age.isEmpty() && !client_gender.isEmpty() &&  imgUrl!= null){
                    showPD();
                    StorageReference postRef = storageReference.child("Client Profiles").child(FieldValue.serverTimestamp().toString() + ".jpg");
                    postRef.putFile(imgUrl).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            if(task.isSuccessful()){
                                postRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        HashMap<String, Object> postMap = new HashMap<>();
                                        postMap.put("image", uri.toString());
                                        postMap.put("client_name", client_name);
                                        postMap.put("client_age", client_age);
                                        postMap.put("client_gender", client_gender);
                                        postMap.put("client_address", client_address);
                                        postMap.put("client_contact", client_contact);
                                        postMap.put("time", FieldValue.serverTimestamp());

                                        firestore.collection("Clients").document(Uid).set(postMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if(task.isSuccessful()){
                                                    progressDialog.dismiss();
                                                    LayoutInflater inflater = getLayoutInflater();
                                                    View layout = inflater.inflate(R.layout.profilesaved_toast, (ViewGroup)findViewById(R.id.profilesaved_toast));
                                                    final Toast toast = new Toast(getApplicationContext());
                                                    toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                                                    toast.setDuration(Toast.LENGTH_SHORT);
                                                    toast.setView(layout);

                                                    toast.show();
                                                    startActivity(new Intent(ClientSetUp.this, ClientDashboard.class));
                                                    finish();
                                                }else{
                                                    progressDialog.dismiss();
                                                    Toast.makeText(getApplicationContext(), task.getException().toString(), Toast.LENGTH_SHORT).show();
                                                }

                                            }
                                        });
                                    }
                                });

                            }else{
                                progressDialog.dismiss();
                                Toast.makeText(ClientSetUp.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
                }else{
                    Toast.makeText(ClientSetUp.this, "Please check the fields required!", Toast.LENGTH_SHORT).show();
                }


            }
        });


        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, img_req);

            }
        });
    }





    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == img_req && resultCode == RESULT_OK) {
            imgUrl = data.getData();
            circleImageView.setImageURI(imgUrl);

            //isPhotoSelected = true;
        }
    }

    private void showPD(){
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setTitle("Setting Profile");
        progressDialog.setMessage("Please wait...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMax(100);
        progressDialog.show();
    }

}