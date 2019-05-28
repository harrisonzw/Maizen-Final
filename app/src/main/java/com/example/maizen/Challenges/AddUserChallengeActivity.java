package com.example.maizen.Challenges;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.maizen.Database.Database;
import com.example.maizen.R;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


public class AddUserChallengeActivity extends AppCompatActivity {
    private Database db = new Database();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user_challenge);

        Toolbar tb = findViewById(R.id.add_user_challenge_toolbar);
        setSupportActionBar(tb);

        getSupportActionBar().setTitle(getIntent().getStringExtra("challenge_name"));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ImageView challenge_picture = findViewById(R.id.activity_img);
        Glide.with(this).asBitmap().load(getIntent().getStringExtra("img_url")).into(challenge_picture);

        TextView time_of_day_tv = findViewById(R.id.time_of_day);

        String time_of_day = "";
        int arrayName_ID = getResources().getIdentifier("time_of_day_options", "array", this.getPackageName());
        String[] items = getResources().getStringArray(arrayName_ID);
        switch (getIntent().getStringExtra("time_of_day")) {
            case "morning":
                time_of_day = items[0];
                break;
            case "noon":
                time_of_day = items[1];
                break;
            case "afternoon":
                time_of_day = items[2];
                break;
            case "night":
                time_of_day = items[3];
                break;
            case "all_day":
                time_of_day = items[4];
                break;
        }

        time_of_day_tv.setText("Time of Day: " + time_of_day);

        TextView duration = findViewById(R.id.duration);
        double numHours = getIntent().getExtras().getDouble("duration") / 60;
        double roundOff = Math.round(numHours * 10.0) / 10.0;
        duration.setText("Duration: " + Double.toString(roundOff) + " hours");

        TextView activity = findViewById(R.id.activity);
        String capatalized = getIntent().getStringExtra("activity").substring(0, 1).toUpperCase() +
                getIntent().getStringExtra("activity").substring(1);
        activity.setText(capatalized);

        Button accept_button = findViewById(R.id.accept_user_challenge);

        accept_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String currentUserID = getSharedPreferences("userID", 0).getString("userID", "");
                db.acceptChallenge(currentUserID, getIntent().getExtras().getInt("challenge_id"));
                finish();
                Toast.makeText(getApplicationContext(), "Added Challenge!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}