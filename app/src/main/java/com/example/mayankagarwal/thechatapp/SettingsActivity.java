package com.example.mayankagarwal.thechatapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SettingsActivity extends AppCompatActivity {

    private Toolbar mToolbar;

    private TextView mDisplayName, mDisplayPhone, mDisplayCity, mDisplayState;
    private Button mEditBtn;
    private ProgressDialog mProgressDialog;


    private DatabaseReference mUserDatabase;
    private FirebaseUser mCurrentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        //Instances

        mDisplayName = (TextView)findViewById(R.id.settings_display_name);
        mDisplayPhone = (TextView)findViewById(R.id.settings_display_phone);
        mDisplayCity = (TextView)findViewById(R.id.settings_display_city);
        mDisplayState = (TextView)findViewById(R.id.settings_display_state);

        //mEditBtn = (Button) findViewById(R.id.settings_edit_btn);

        //Toolbar
        mToolbar = (Toolbar)findViewById(R.id.settings_appbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Settings");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Firebase
        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        String currrent_uid = mCurrentUser.getUid();

        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("users").child(currrent_uid);

        mUserDatabase.keepSynced(true);

        mProgressDialog = new ProgressDialog(this);

        mProgressDialog.setTitle("Loading Details");
        mProgressDialog.setMessage("Please, Wait while the details are being loaded!");
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.show();

        mUserDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                final String name = dataSnapshot.child("name").getValue().toString();
                final String phone = dataSnapshot.child("phone").getValue().toString();
                final String city = dataSnapshot.child("city").getValue().toString();
                final String state = dataSnapshot.child("state").getValue().toString();
                mDisplayName.setText(name);
                mDisplayPhone.setText(phone);
                mDisplayCity.setText(city);
                mDisplayState.setText(state);
                mProgressDialog.dismiss();

//                mEditBtn.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        Intent edit = new Intent(SettingsActivity.this, EditProfile.class);
//                        edit.putExtra("name", name);
//                        edit.putExtra("phone", phone);
//                        edit.putExtra("city", city);
//                        edit.putExtra("state", state);
//                        startActivity(edit);
//                        finish();
//
//                    }
//                });

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }

        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        if (item.getItemId() == android.R.id.home){
            Intent back = new Intent(SettingsActivity.this,MainActivity.class);
            startActivity(back);
            finish();
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent back = new Intent(SettingsActivity.this,MainActivity.class);
        startActivity(back);
        finish();
    }
}
