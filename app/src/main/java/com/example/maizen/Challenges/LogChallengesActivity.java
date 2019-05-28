package com.example.maizen.Challenges;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.maizen.Database.Database;
import com.example.maizen.R;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


public class LogChallengesActivity extends AppCompatActivity {
    private Database db = new Database();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_challenge);

        Toolbar tb = findViewById(R.id.log_challenges_toolbar);
        setSupportActionBar(tb);

        getSupportActionBar().setTitle("Record Your Activity");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Button recordButton = findViewById(R.id.record_button);

        recordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Spinner activitySpinner = findViewById(R.id.activity_spinner);
                String activity_name = activitySpinner.getSelectedItem().toString();

                Spinner durationSpinner = findViewById(R.id.duration_spinner);
                String duration = durationSpinner.getSelectedItem().toString();

                Spinner time_of_day_spinner = findViewById(R.id.time_of_day_spinner);
                String time_of_day = time_of_day_spinner.getSelectedItem().toString();

                String currentUserID = getSharedPreferences("userID", 0).getString("userID", "");

                db.insertUserActivity(currentUserID, activity_name, duration, time_of_day);
                Toast.makeText(getApplicationContext(), "Recorded!",
                        Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}