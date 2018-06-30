package com.example.mayankagarwal.thechatapp;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.HashMap;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity {


    private TextView mProfileName, mProfileCity, mProfileState, mProfilePhone, mProfileEmail;
    private Button mProfileSendReqBtn, mCall;
    private DatabaseReference mUsersDatabase;

    private ProgressDialog mProgressDialog;
    private Toolbar toolbar;
    private CollapsingToolbarLayout collapsingToolbarLayout;

    private DatabaseReference mDonorReqDatabase;
    private DatabaseReference mFriendDatabase;
    private DatabaseReference mNotificationDatabase;

    private DatabaseReference mRootRef;

    private FirebaseUser mCurrent_user;

    private String mCurrent_state;
    String phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        final String user_id = getIntent().getStringExtra("user_id").toString();

        mRootRef = FirebaseDatabase.getInstance().getReference();

        mUsersDatabase = FirebaseDatabase.getInstance().getReference().child("users").child(user_id);
        mUsersDatabase.keepSynced(true);
        mDonorReqDatabase = FirebaseDatabase.getInstance().getReference().child("Donor_req");
        mDonorReqDatabase.keepSynced(true);
        mFriendDatabase = FirebaseDatabase.getInstance().getReference().child("Friends");
        mFriendDatabase.keepSynced(true);
        mNotificationDatabase = FirebaseDatabase.getInstance().getReference().child("notifications");
        mNotificationDatabase.keepSynced(true);
        mCurrent_user = FirebaseAuth.getInstance().getCurrentUser();


        mProfileName = (TextView) findViewById(R.id.profile_name);
        mProfileCity = (TextView) findViewById(R.id.profile_city);
        mProfileState = (TextView) findViewById(R.id.profile_state);
        mProfilePhone = (TextView) findViewById(R.id.profile_phone);
        mProfileEmail = (TextView) findViewById(R.id.profile_email);

        mProfileSendReqBtn = (Button) findViewById(R.id.profile_send_request);
        mCall = (Button) findViewById(R.id.profile_call);

        mCurrent_state = "not_sent";

        toolbar = (Toolbar) findViewById(R.id.profile_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Donor's Profile");


        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setTitle("Loading User Data");
        mProgressDialog.setMessage("Please wait while we load the user data.");
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.show();

        mUsersDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String display_name = dataSnapshot.child("name").getValue().toString();
                String email = dataSnapshot.child("email").getValue().toString();
                phone = dataSnapshot.child("phone").getValue().toString();
                String city = dataSnapshot.child("city").getValue().toString();
                String state = dataSnapshot.child("state").getValue().toString();

                mProfileCity.setText(city);
                mProfileEmail.setText(email);
                mProfileName.setText(display_name);
                mProfileState.setText(state);
                mProfilePhone.setText(phone);

                mProgressDialog.dismiss();


                if(mCurrent_user.getUid().equals(user_id)){

                    mProfileSendReqBtn.setEnabled(false);
                    mProfileSendReqBtn.setVisibility(View.INVISIBLE);
                    mCall.setEnabled(true);
                    mCall.setVisibility(View.INVISIBLE);
                }
                if (mCurrent_state.equals("not_sent")){
                    mCall.setVisibility(View.INVISIBLE);
                }


                //--------------- FRIENDS LIST / REQUEST FEATURE -----

                mDonorReqDatabase.child(mCurrent_user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        if(dataSnapshot.hasChild(user_id)){

                            String req_type = dataSnapshot.child(user_id).child("request_type").getValue().toString();

                            if(req_type.equals("received")){

                                mCurrent_state = "req_received";
                                mProfileSendReqBtn.setVisibility(View.INVISIBLE);
                                mProfileSendReqBtn.setEnabled(false);
                                mCall.setVisibility(View.VISIBLE);
                                mCall.setEnabled(true);


                            } else if(req_type.equals("sent")) {

                                mCurrent_state = "req_sent";
                                mProfileSendReqBtn.setText("Cancel Request");
                                mCall.setVisibility(View.INVISIBLE);

                            }

                            mProgressDialog.dismiss();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent my_callIntent = new Intent(Intent.ACTION_CALL);
                    my_callIntent.setData(Uri.parse("tel:" + phone));
                    //here the word 'tel' is important for making a call...
                    if (ActivityCompat.checkSelfPermission(ProfileActivity.this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(ProfileActivity.this, "Please grant the permission to call", Toast.LENGTH_SHORT).show();
                        requestPermission();

                    }else {
                        startActivity(my_callIntent);
                    }
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(getApplicationContext(), "Error in your phone call"+e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });




        mProfileSendReqBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mProfileSendReqBtn.setEnabled(false);

                // --------------- NOT FRIENDS STATE ------------

                if(mCurrent_state.equals("not_sent")){


                    DatabaseReference newNotificationref = mRootRef.child("notifications").child(user_id).push();
                    String newNotificationId = newNotificationref.getKey();

                    HashMap<String, String> notificationData = new HashMap<>();
                    notificationData.put("from", mCurrent_user.getUid());
                    notificationData.put("type", "request");

                    Map requestMap = new HashMap();
                    requestMap.put("Donor_req/" + mCurrent_user.getUid() + "/" + user_id + "/request_type", "sent");
                    requestMap.put("Donor_req/" + user_id + "/" + mCurrent_user.getUid() + "/request_type", "received");
                    requestMap.put("notifications/" + user_id + "/" + newNotificationId, notificationData);

                    mRootRef.updateChildren(requestMap, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {

                            if(databaseError != null){

                                Toast.makeText(ProfileActivity.this, "There was some error in sending request", Toast.LENGTH_SHORT).show();

                            } else {

                                mCurrent_state = "req_sent";
                                mProfileSendReqBtn.setText("Cancel Request");
                                mCall.setVisibility(View.INVISIBLE);

                            }
                            mProfileSendReqBtn.setEnabled(true);


                        }
                    });

                }
                // - -------------- CANCEL REQUEST STATE ------------

                if(mCurrent_state.equals("req_sent")){

                    mDonorReqDatabase.child(mCurrent_user.getUid()).child(user_id).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

                            mDonorReqDatabase.child(user_id).child(mCurrent_user.getUid()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {


                                    mProfileSendReqBtn.setEnabled(true);
                                    mCurrent_state = "not_sent";
                                    mProfileSendReqBtn.setText("Send Request");

                                }
                            });

                        }
                    });

                }


                // ------------ REQ RECEIVED STATE ----------

                if(mCurrent_state.equals("req_received")){

                    Map friendsMap = new HashMap();


                    friendsMap.put("Donor_req/" + mCurrent_user.getUid() + "/" + user_id, null);
                    friendsMap.put("Donor_req/" + user_id + "/" + mCurrent_user.getUid(), null);


                    mRootRef.updateChildren(friendsMap, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {


                            if(databaseError == null){
                                mProfileSendReqBtn.setVisibility(View.INVISIBLE);
                                mCall.setVisibility(View.VISIBLE);
                                mCall.setEnabled(true);

                            } else {

                                String error = databaseError.getMessage();

                                Toast.makeText(ProfileActivity.this, error, Toast.LENGTH_SHORT).show();
                            }

                        }
                    });

                }
            }
        });
    }

    public void requestPermission(){
        ActivityCompat.requestPermissions(ProfileActivity.this, new String[]{Manifest.permission.CALL_PHONE},1);
    }
}
