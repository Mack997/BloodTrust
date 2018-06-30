package com.example.mayankagarwal.thechatapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.PatternMatcher;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    private TextInputLayout mName, mEmail, mCity, mState, mPassword, mPhoneNumber;
    private Spinner mBloodGroup;
    private Button mCreate;
    private FirebaseAuth mAuth;
    private Toolbar mtoolbar;
    private ProgressDialog mRegProgress;

    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

        mName = (TextInputLayout) findViewById(R.id.reg_name);
        mEmail = (TextInputLayout) findViewById(R.id.reg_email);
        mCity = (TextInputLayout) findViewById(R.id.reg_city);
        mState = (TextInputLayout) findViewById(R.id.reg_state);
        mPassword = (TextInputLayout) findViewById(R.id.reg_password);
        mBloodGroup = (Spinner) findViewById(R.id.reg_spinner);
        mCreate = (Button) findViewById(R.id.reg_btn);
        mPhoneNumber = (TextInputLayout) findViewById(R.id.reg_phone);

        mRegProgress = new ProgressDialog(this);

        mtoolbar =(Toolbar)findViewById(R.id.register_toolbar);
        setSupportActionBar(mtoolbar);
        getSupportActionBar().setTitle("Create Account");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mCreate.setOnClickListener(new View.OnClickListener() { 
            @Override
            public void onClick(View view) {

                String name = mName.getEditText().getText().toString().trim();
                String email = mEmail.getEditText().getText().toString().trim();
                String city = mCity.getEditText().getText().toString().trim();
                String state = mState.getEditText().getText().toString().trim();
                String phone = mPhoneNumber.getEditText().getText().toString().trim();
                String password = mPassword.getEditText().getText().toString().trim();
                String blood_group = String.valueOf(mBloodGroup.getSelectedItem());

                if (!TextUtils.isEmpty(name) || !TextUtils.isEmpty(email) || !TextUtils.isEmpty(phone) || !TextUtils.isEmpty(city) || !TextUtils.isEmpty(state)
                        || !TextUtils.isEmpty(password) || !TextUtils.isEmpty(blood_group)){

                    mRegProgress.setTitle("Registering User");
                    mRegProgress.setMessage("Please, Wait while we are registering you!");
                    mRegProgress.setCanceledOnTouchOutside(false);
                    mRegProgress.show();
                    register_user(name,email,phone,blood_group,city,state,password);

                  }else{
                       Toast.makeText(RegisterActivity.this, "Please Fill in the details", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void register_user(final String name, final String email, final String phone,final String blood_group,  final String city,
                               final String state, final String password) {

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();
                            String uid = current_user.getUid();

                            mDatabase = FirebaseDatabase.getInstance().getReference().child("users").child(uid);

                            String device_token =FirebaseInstanceId.getInstance().getToken();

                            HashMap<String, String> usermap = new HashMap<String, String>();
                            usermap.put("name", name);
                            usermap.put("email", email);
                            usermap.put("phone", phone);
                            usermap.put("city", city);
                            usermap.put("state", state);
                            usermap.put("password", password);
                            usermap.put("blood_group", blood_group);

                            mDatabase.setValue(usermap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){

                                        mRegProgress.dismiss();
                                        Intent mainIntent = new Intent(RegisterActivity.this, LoginActivity.class);
                                        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(mainIntent);
                                        finish();
                                    }
                                }
                            });
                        }
                        else {

                            mRegProgress.hide();
                            String TAG = "FIREBASE_EXCEPTION";
                            FirebaseException e = (FirebaseException)task.getException();
                            Log.d(TAG, "Reason: " +  e.getMessage());
                            Toast.makeText(RegisterActivity.this, "Cannot Register, Please " +
                                    "check your details and try again...", Toast.LENGTH_LONG).show();

                        }
                    }
                });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        if (item.getItemId() == android.R.id.home){
            Intent back = new Intent(RegisterActivity.this,StartActivity.class);
            startActivity(back);
            finish();
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent back = new Intent(RegisterActivity.this,StartActivity.class);
        startActivity(back);
        finish();
    }
}

