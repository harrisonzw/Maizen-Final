package com.example.maizen;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.maizen.Database.Database;
import com.example.maizen.Diary.GetFoodInfo;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.sql.ResultSet;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;



public class LoginActivity extends AppCompatActivity {

    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    // view objects
    private TextView textViewUserName;
    private FirebaseAuth mFirebaseAuth;
    public static final int RC_SIGN_IN = 1;
    private FirebaseAuth.AuthStateListener mAuthStateListner;
    private CountDownLatch done;
    private Database db = new Database();

    List<AuthUI.IdpConfig> providers = Arrays.asList(
            new AuthUI.IdpConfig.EmailBuilder().build(),
            new AuthUI.IdpConfig.PhoneBuilder().build(),
            new AuthUI.IdpConfig.GoogleBuilder().build(),
            new AuthUI.IdpConfig.FacebookBuilder().build()
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        done = new CountDownLatch(1);
        setContentView(R.layout.activity_firebase);
        mFirebaseAuth=FirebaseAuth.getInstance();
        mAuthStateListner=new FirebaseAuth.AuthStateListener() {

            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = mFirebaseAuth.getCurrentUser();
                if (user!=null)
                {
                    Toast.makeText(LoginActivity.this, "User Signed In", Toast.LENGTH_SHORT).show();

                    String currentUserID = user.getUid();
                    SharedPreferences.Editor editor = getSharedPreferences("userID", 0).edit();
                    editor.putString("userID", currentUserID);
                    editor.apply();

                    try {


                        /*DatabaseReference ref = database.getReference(currentUserID).child("Diary");
                        ref.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            done.countDown();
                            HashMap<String, Object> data = (HashMap<String,Object>) dataSnapshot.getValue();
                            if (data == null){
                                Toast.makeText(getApplicationContext(), "User exist", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                Toast.makeText(getApplicationContext(), "No such user", Toast.LENGTH_SHORT).show();
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Log.w("Database", "Failed to read value.", databaseError.toException());
                        }
                    });
                        try {
                            done.await(); //it will wait till the response is received from firebase.
                        } catch(InterruptedException e) {
                            e.printStackTrace();
                        }*/

                        ResultSet rst = db.getName(currentUserID);

                        if (!rst.next()) {
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            finish();
                        }
                    }

                    catch (Exception ex) {
                        // Redirect user to the registration form
                        Intent intent = new Intent(getApplicationContext(), RegistrationFormActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
                else {
                    startActivityForResult(
                            AuthUI.getInstance()
                                    .createSignInIntentBuilder()
                                    .setIsSmartLockEnabled(false)
                                    .setAvailableProviders(providers)
                                    .setLogo(R.drawable.logo_with_text)
                                    .build(),
                            RC_SIGN_IN
                    );
                }
            }
        };

    }

    @Override
    protected void onResume() {
        super.onResume();
        mFirebaseAuth.addAuthStateListener(mAuthStateListner);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mFirebaseAuth.removeAuthStateListener(mAuthStateListner);

    }

    public void signout(View view) {
        AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(LoginActivity.this, "User Signed Out", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
    }
}