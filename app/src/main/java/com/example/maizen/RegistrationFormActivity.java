package com.example.maizen;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.maizen.Database.Database;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.sql.Ref;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;

public class RegistrationFormActivity extends AppCompatActivity {

    private DatabaseReference dbRef;
    public Button submitButton;
    private Database db = new Database();
    private String currentUserID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_form);

        currentUserID = getSharedPreferences("userID", 0).getString("userID", "");

        submitButton = findViewById(R.id.SubmitButton);

        submitButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                createNewUser(view);
                addNewUserToFirebase(view);
                createUserDiaryFirebase(view);
            }
        });
    }

    public void createNewUser(View view) {
        // Retrieve user inputs
        String firstName = ((TextView) findViewById(R.id.FirstNameEntry)).getText().toString();
        String lastName = ((TextView) findViewById(R.id.LastNameEntry)).getText().toString();
        String dayOfBirth = ((TextView) findViewById(R.id.DOBEntry)).getText().toString();
        String monthOfBirth = ((TextView) findViewById(R.id.MOBEntry)).getText().toString();
        String yearOfBirth = ((TextView) findViewById(R.id.YOBEntry)).getText().toString();
        String gender = ((TextView) findViewById(R.id.GenderEntry)).getText().toString();
        String weight = ((TextView) findViewById(R.id.WeightEntry)).getText().toString();
        String heightFeet = ((TextView) findViewById(R.id.HeightFeetEntry)).getText().toString();
        String heightInches = ((TextView) findViewById(R.id.HeightInchesEntry)).getText().toString();

        try {
            db.insertUser(firstName, lastName, dayOfBirth, monthOfBirth, yearOfBirth, gender, weight, heightFeet, heightInches, currentUserID);
        }

        catch (Exception e) {
            Context context = getApplicationContext();
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, e.getMessage(), duration);
            toast.show();
            return;
        }

        Context context = getApplicationContext();
        String successMessage = "Successfully created a new account!";
        int duration = Toast.LENGTH_LONG;

        Toast toast = Toast.makeText(context, successMessage, duration);
        toast.show();

    }

    // get the data from the form and create a new user profile in Firebase database
    public void addNewUserToFirebase(View view){

        String firstName = ((TextView) findViewById(R.id.FirstNameEntry)).getText().toString();
        String lastName = ((TextView) findViewById(R.id.LastNameEntry)).getText().toString();
        String dayOfBirth = ((TextView) findViewById(R.id.DOBEntry)).getText().toString();
        String monthOfBirth = ((TextView) findViewById(R.id.MOBEntry)).getText().toString();
        String yearOfBirth = ((TextView) findViewById(R.id.YOBEntry)).getText().toString();
        String gender = ((TextView) findViewById(R.id.GenderEntry)).getText().toString();
        String weight = ((TextView) findViewById(R.id.WeightEntry)).getText().toString();
        String heightFeet = ((TextView) findViewById(R.id.HeightFeetEntry)).getText().toString();
        String heightInches = ((TextView) findViewById(R.id.HeightInchesEntry)).getText().toString();
        String currentUserID = getSharedPreferences("userID", 0).getString("userID", "");

        // put the data from the from into a map object
        Map<String, Object> map = new HashMap<>();
        map.put("user_ID", currentUserID);
        map.put("first_name" ,  firstName);
        map.put("last_name", lastName);
        map.put("birth_day", dayOfBirth);
        map.put("birth_month", monthOfBirth);
        map.put("birth_year", yearOfBirth);
        map.put("gender", gender);
        map.put("weight", weight);
        map.put("height_feet", heightFeet);
        map.put("height_inches", heightInches);

        Log.i("map", map.toString());

        // get the database reference
        dbRef = FirebaseDatabase.getInstance()
                .getReference("Users").child(currentUserID).child("User_info");

        try {
            // add the food to the database under the right section
            dbRef.setValue(map);
        }

        catch (Exception e) {
            Context context = getApplicationContext();
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, e.getMessage(), duration);
            toast.show();
            return;
        }

    }

    public void createUserDiaryFirebase(View view) {

        // get the database reference
        DatabaseReference Ref1 = FirebaseDatabase.getInstance().getReference("Users").child(currentUserID).child("Diary");

        // put the data from the from into a map object
        Map<String, Object> DiaryMap = new HashMap<>();
        Map<String, Object> FoodlistMap = new HashMap<>();
        FoodlistMap.put("Dummy", "value");

        DiaryMap.put("breakfast", FoodlistMap);
        DiaryMap.put("lunch", FoodlistMap);
        DiaryMap.put("dinner", FoodlistMap);
        DiaryMap.put("snack", FoodlistMap);

        try {
            // add the diary to firebase
            Ref1.setValue(DiaryMap);
        }

        catch (Exception e) {
            Context context = getApplicationContext();
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, e.getMessage(), duration);
            toast.show();
            return;
        }
        // Redirect the user to the home page
        Intent homeActivity = new Intent(this, MainActivity.class);
        startActivity(homeActivity);
    }
}
