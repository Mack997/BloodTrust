package com.example.mayankagarwal.thechatapp;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


/**
 * A simple {@link Fragment} subclass.
 */
public class RequestsFragment extends Fragment {


    private RecyclerView mRequestlist;
    private View mMainView;
    private DatabaseReference donorRequestRef;
    private FirebaseAuth mAuth;
    String current_user_id;

    private DatabaseReference usersRef;


    public RequestsFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mMainView =  inflater.inflate(R.layout.fragment_requests, container, false);

        mAuth = FirebaseAuth.getInstance();
        current_user_id = mAuth.getCurrentUser().getUid();

        donorRequestRef = FirebaseDatabase.getInstance().getReference().child("Donor_req").child(current_user_id);
        usersRef = FirebaseDatabase.getInstance().getReference().child("users");

        mRequestlist = (RecyclerView)mMainView.findViewById(R.id.request_list);

        mRequestlist.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());

        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);

        mRequestlist.setLayoutManager(linearLayoutManager);

        return mMainView;
    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<Request, RequestViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Request, RequestViewHolder>(
                Request.class,
                R.layout.request_single_layout,
                RequestViewHolder.class,
                donorRequestRef
        ) {
            @Override
            protected void populateViewHolder(final RequestViewHolder requestviewHolder, Request model, int position) {

                final String list_user_id = getRef(position).getKey();

                usersRef.child(list_user_id).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        final String username = dataSnapshot.child("name").getValue().toString();
                        final String bloodGroup = dataSnapshot.child("blood_group").getValue().toString();

                        requestviewHolder.setName(username);
                        requestviewHolder.setBloodGroup(bloodGroup);

                        requestviewHolder.mView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent profileIntent = new Intent(getContext(), ProfileActivity.class);
                                profileIntent.putExtra("user_id", list_user_id);
                                startActivity(profileIntent);
                            }
                        });

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


            }
        };

        mRequestlist.setAdapter(firebaseRecyclerAdapter);
    }

    public static class RequestViewHolder extends RecyclerView.ViewHolder{

        View mView;

        public RequestViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }

        public void setName(String name) {
            TextView userName = (TextView)mView.findViewById(R.id.request_single_name);
            userName.setText(name);
        }

        public void setBloodGroup(String bloodGroup){
            TextView blood_group = (TextView)mView.findViewById(R.id.request_single_blood_group);
            blood_group.setText(bloodGroup);
        }
    }

}
