package Adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.text.format.DateFormat;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.group.trylang.R;

import java.util.Date;
import java.util.List;

import Model.Resume;
import de.hdodenhof.circleimageview.CircleImageView;

public class ResumeAdapter extends RecyclerView.Adapter <ResumeAdapter.PostViewHolder> {

    private java.util.List<Resume> List;
    private Context context;
    private FirebaseFirestore firestore;
    private FirebaseAuth auth;
    ProgressDialog progressDialog;

    public void setFilteredList(List<Resume> filteredList){
        this.List =  filteredList;
        notifyDataSetChanged();
    }

    public ResumeAdapter (Context context, List<Resume> List){
        this.List = List;
        this.context = context;

    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.each_resume, parent, false);
        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        Resume post = List.get(position);
        holder.setPost_id(post.getImage());


        holder.setPostname("Name: " +post.getClient_name());
        holder.setPostage("Age: " + post.getClient_age());
        holder.setPostgender("Gender: " + post.getClient_gender());
        holder.setPostaddress("Address: " + post.getClient_address());
        holder.setPostcontact("Contact Number: " + post.getClient_contact());


        long milli = post.getTime().getTime();
        String date = DateFormat.format("MM/dd/yyy", new Date(milli)).toString();
        holder.setPostDate(date);

        String userId = post.getUser();
        firestore.collection("Handyman").document(userId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    String username = task.getResult().getString("client_name");
                    holder.setPostUsernames(username);
                    String image = task.getResult().getString("image");
                    holder.setProfile_pic(image);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return List.size();
    }

    public class PostViewHolder extends RecyclerView.ViewHolder {

        ImageView post_id;
        CircleImageView profile_pic;
        TextView postUsernames, postDate, postname, postage, postgender, postaddress, postcontact,
                dd_product, dd_price, dd_da, dd_gname, dd_gnum;
        ImageButton call;
        View mView;
        LinearLayout layout;
        CardView post_click;

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);

            call = mView.findViewById(R.id.call);
            dd_product = mView.findViewById(R.id.dd_name);
            dd_price = mView.findViewById(R.id.dd_age);
            dd_da = mView.findViewById(R.id.dd_gender);
            dd_gname = mView.findViewById(R.id.dd_address);
            dd_gnum = mView.findViewById(R.id.dd_contact);

            layout = mView.findViewById(R.id.layout);
            post_click = mView.findViewById(R.id.postdetails);

            post_click.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dd_product.setText(postname.getText());
                    int a = (dd_product.getVisibility() == View.GONE)? View.VISIBLE: View.GONE;

                    dd_price.setText(postage.getText());
                    int b = (dd_price.getVisibility() == View.GONE)? View.VISIBLE: View.GONE;

                    dd_da.setText(postgender.getText());
                    int c = (dd_da.getVisibility() == View.GONE)? View.VISIBLE: View.GONE;

                    dd_gname.setText(postaddress.getText());
                    int d = (dd_gname.getVisibility() == View.GONE)? View.VISIBLE: View.GONE;

                    dd_gnum.setText(postcontact.getText());
                    int e = (dd_gnum.getVisibility() == View.GONE)? View.VISIBLE: View.GONE;


                    TransitionManager.beginDelayedTransition(layout, new AutoTransition());
                    dd_product.setVisibility(a);
                    dd_price.setVisibility(b);
                    dd_da.setVisibility(c);
                    dd_gname.setVisibility(d);
                    dd_gnum.setVisibility(e);
                }
            });


        }

        public void setPost_id(String urlPost){
            post_id = mView.findViewById(R.id.post_id);
            Glide.with(context).load(urlPost).into(post_id);
        }

        public void setProfile_pic(String urlProfile){
            profile_pic = mView.findViewById(R.id.profile_pic);
            Glide.with(context).load(urlProfile).into(profile_pic);
        }
        public void setPostUsernames(String usernames){
            postUsernames = mView.findViewById(R.id.username);
            postUsernames.setText(usernames);
        }
        public void setPostDate(String date){
            postDate = mView.findViewById(R.id.date);
            postDate.setText(date);
        }

        public void setPostname(String products){
            postname = mView.findViewById(R.id.resume_name);
            postname.setText(products);
        }

        public void setPostage(String price){
            postage = mView.findViewById(R.id.resume_age);
            postage.setText(price);
        }

        public void setPostgender(String address){
            postgender= mView.findViewById(R.id.resume_gender);
            postgender.setText(address);
        }

        public void setPostaddress(String address){
            postaddress= mView.findViewById(R.id.resume_address);
            postaddress.setText(address);
        }


        public void setPostcontact(String address){
            postcontact = mView.findViewById(R.id.resume_contact);
            postcontact.setText(address);
        }


    }



}

