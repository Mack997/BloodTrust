package com.example.mayankagarwal.thechatapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;


public class DonorResults extends AppCompatActivity {

    private RecyclerView mDonorsList;
    private DatabaseReference mUsersDatabase;
    private ProgressDialog mProgressDialog;

    private FirebaseAuth mAuth;
    private Toolbar mtoolbar;
    private DatabaseReference mRootRef;
    private DatabaseReference mSearchRef;

    private String blood_group;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donor_results);

        mAuth = FirebaseAuth.getInstance();

        mSearchRef = FirebaseDatabase.getInstance().getReference().child("users");

        mRootRef = FirebaseDatabase.getInstance().getReference().child("users").child(mAuth.getCurrentUser().getUid());
        mRootRef.keepSynced(true);

        blood_group = getIntent().getStringExtra("blood_group");

        mtoolbar= (Toolbar)findViewById(R.id.user_appbar);
        setSupportActionBar(mtoolbar);
        getSupportActionBar().setTitle("Donors");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mUsersDatabase = FirebaseDatabase.getInstance().getReference().child("users");
        mUsersDatabase.keepSynced(true);

        mDonorsList= (RecyclerView)findViewById(R.id.users_list);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setReverseLayout(true);
        mDonorsList.setHasFixedSize(true);
        mDonorsList.setLayoutManager(linearLayoutManager);


        Query DonorSearchQuery = mSearchRef.orderByChild("blood_group").equalTo(blood_group);

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setTitle("Loading Users");
        mProgressDialog.setMessage("Please, Wait while the users are being loaded");
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.show();

        FirebaseRecyclerAdapter<Users,UsersViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Users, UsersViewHolder>(
                Users.class,
                R.layout.users_single_layout,
                UsersViewHolder.class,
                DonorSearchQuery) {

            @Override
            protected void populateViewHolder(UsersViewHolder usersViewHolder, Users users, int position) {

                usersViewHolder.setName(users.getName());
                usersViewHolder.setCity(users.getCity());
                mProgressDialog.dismiss();

                final String user_id = getRef(position).getKey();

                usersViewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent profileIntent = new Intent(DonorResults.this,ProfileActivity.class);
                        profileIntent.putExtra("user_id", user_id);
                        startActivity(profileIntent);
                    }
                });

            }
        };
        mDonorsList.setAdapter(firebaseRecyclerAdapter);
}

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    public static class UsersViewHolder extends RecyclerView.ViewHolder {

        View mView;

        public UsersViewHolder(View itemView) {
            super(itemView);

            mView = itemView;
        }

        public void setName(String name) {
            TextView userNameView = (TextView) mView.findViewById(R.id.user_single_name);
            userNameView.setText(name);
        }

        public void setCity(String city) {
            TextView userStatusView = (TextView) mView.findViewById(R.id.user_single_city);
            userStatusView.setText(city);
        }


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        if (item.getItemId() == android.R.id.home){
            Intent back = new Intent(DonorResults.this,MainActivity.class);
            startActivity(back);
            finish();
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent back = new Intent(DonorResults.this,MainActivity.class);
        startActivity(back);
        finish();
    }
}
