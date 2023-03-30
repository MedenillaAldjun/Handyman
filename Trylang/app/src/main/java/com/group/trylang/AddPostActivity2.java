package com.group.trylang;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AddPostActivity2 extends AppCompatActivity {

    private ImageButton imageButton;
    private EditText products, prices, fbnames, gcash_names, gcash_nums;
    private Button publish;

    private ProgressBar post_progressbar;
    private ImageButton preview;
    private static final int req = 1;
    private Uri mImgUrl = null;

    private StorageReference storage;
    private FirebaseFirestore firestore;
    private FirebaseAuth auth;
    private String currentUserId, address;


    private Toolbar addpost_toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);

        imageButton = findViewById(R.id.preview);
        products = findViewById(R.id.product);
        prices = findViewById(R.id.price);
        publish = findViewById(R.id.publish);
        fbnames = findViewById(R.id.fbname);
        gcash_names = findViewById(R.id.gcash_name);
        gcash_nums = findViewById(R.id.gcash_num);

        addpost_toolbar = findViewById(R.id.addpost_toolbar);
        setSupportActionBar(addpost_toolbar);
        getSupportActionBar().setTitle("Add Resume");

        preview = findViewById(R.id.preview);

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

        firestore.collection("Handyman").document(currentUserId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    if (task.getResult().exists()) {
                        String name1 = task.getResult().getString("client_name");
                        String age1 = task.getResult().getString("client_age");
                        String gender1 = task.getResult().getString("client_gender");
                        String address1= task.getResult().getString("client_address");
                        String contact1 = task.getResult().getString("client_contact");
                        products.setText(name1);
                        prices.setText(age1);
                        fbnames.setText(contact1);
                        gcash_names.setText(gender1);
                        gcash_nums.setText(address1);

                    }
                }
            }
        });




        publish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String product = products.getText().toString();
                String price = prices.getText().toString();
                String fbname = fbnames.getText().toString();
                String gcashname = gcash_names.getText().toString();
                String gcashnum = gcash_nums.getText().toString();

                if (!product.isEmpty() && !price.isEmpty()  && mImgUrl != null) {
                    showPD();
                    StorageReference postRef = storage.child("Posted Image").child(FieldValue.serverTimestamp().toString() + ".jpg");
                    postRef.putFile(mImgUrl).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            if (task.isSuccessful()) {
                                postRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        HashMap<String, Object> postMap = new HashMap<>();
                                        postMap.put("image", uri.toString());
                                        postMap.put("user", currentUserId);
                                        postMap.put("product", product);
                                        postMap.put("price", price);
                                        postMap.put("fbname", fbname);
                                        postMap.put("gcashname", gcashname);
                                        postMap.put("gcashnum", gcashnum);
                                        postMap.put("time", FieldValue.serverTimestamp());

                                        firestore.collection("Carpentry").add(postMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentReference> task) {
                                                if (task.isSuccessful()) {
                                                    LayoutInflater inflater = getLayoutInflater();
                                                    View layout = inflater.inflate(R.layout.custom_toast, (ViewGroup) findViewById(R.id.custom_toast));
                                                    final Toast toast = new Toast(getApplicationContext());
                                                    toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                                                    toast.setDuration(Toast.LENGTH_SHORT);
                                                    toast.setView(layout);

                                                    toast.show();
                                                    startActivity(new Intent(AddPostActivity2.this, HandyDashboard.class));
                                                    finish();
                                                } else {
                                                    Toast.makeText(AddPostActivity2.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                    }
                                });

                            } else {
                                Toast.makeText(AddPostActivity2.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
                } else {
                    Toast.makeText(AddPostActivity2.this, "Please check the fields required!", Toast.LENGTH_SHORT).show();
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

    private void showPD() {
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Adding Post");
        progressDialog.setMessage("Please wait...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMax(100);
        progressDialog.show();
    }
}