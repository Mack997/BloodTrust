package com.example.mayankagarwal.thechatapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Set;

public class EditProfile extends AppCompatActivity {

    TextInputLayout mName, mPhone, mCity, mState;
    Button mUpdate;

    ProgressDialog mProgressDialog;
    private FirebaseAuth mAuth;
    private Toolbar mtoolbar;
    String currentUserId ;

    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        mAuth = FirebaseAuth.getInstance();

        currentUserId = mAuth.getCurrentUser().getUid();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("users").child(currentUserId);

        mName = (TextInputLayout) findViewById(R.id.edit_name);
        mPhone = (TextInputLayout) findViewById(R.id.edit_phone);
        mCity = (TextInputLayout) findViewById(R.id.edit_city);
        mState = (TextInputLayout) findViewById(R.id.edit_state);

        String name = getIntent().getStringExtra("name");
        String phone = getIntent().getStringExtra("phone");
        String city = getIntent().getStringExtra("city");
        String state = getIntent().getStringExtra("state");

        mName.getEditText().setText(name);
        mPhone.getEditText().setText(phone);
        mCity.getEditText().setText(city);
        mState.getEditText().setText(state);

        mUpdate = (Button) findViewById(R.id.update_btn);

        mProgressDialog = new ProgressDialog(this);

        mtoolbar =(Toolbar)findViewById(R.id.register_toolbar);
        setSupportActionBar(mtoolbar);
        getSupportActionBar().setTitle("Update Details");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        mUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Ename = mName.getEditText().getText().toString().trim();
                String Ecity = mCity.getEditText().getText().toString().trim();
                String Estate = mState.getEditText().getText().toString().trim();
                String Ephone = mPhone.getEditText().getText().toString().trim();


                mProgressDialog.setTitle("Updating Details");
                mProgressDialog.setMessage("Please, Wait while we are Updating your Details!");
                mProgressDialog.setCanceledOnTouchOutside(false);
                mProgressDialog.show();

                HashMap<String, String> usermap = new HashMap<String, String>();
                usermap.put("name", Ename);
                usermap.put("phone", Ephone);
                usermap.put("city", Ecity);
                usermap.put("state", Estate);

                mDatabase.setValue(usermap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            mProgressDialog.dismiss();
                            Intent mainIntent = new Intent(EditProfile.this, SettingsActivity.class);
                            mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(mainIntent);
                            finish();

                        }else {
                            mProgressDialog.hide();
                            String TAG = "FIREBASE_EXCEPTION";
                            FirebaseException e = (FirebaseException)task.getException();
                            Log.d(TAG, "Reason: " +  e.getMessage());
                            Toast.makeText(EditProfile.this, "Cannot Update, Please " +
                                    "check your details and try again...", Toast.LENGTH_LONG).show();

                        }
                    }
                });


            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        if (item.getItemId() == android.R.id.home){
            Intent back = new Intent(EditProfile.this,SettingsActivity.class);
            startActivity(back);
            finish();
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent back = new Intent(EditProfile.this,SettingsActivity.class);
        startActivity(back);
        finish();
    }
}
