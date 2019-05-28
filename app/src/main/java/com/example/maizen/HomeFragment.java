
package com.example.maizen;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.maizen.Challenges.LogChallengesActivity;
import com.example.maizen.Database.Database;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.sql.ResultSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


public class HomeFragment extends Fragment implements SensorEventListener {
    /* settings, logout, profile, challenges, stats */

    private final static double walkingFactor = 0.57;
    private final double NUM_INCHES_IN_MILE = 63360;
    private TextView step_counter_tv;
    private SensorManager sensorManager;
    private Sensor mSensor;
    private Database db = new Database();
    private TextView distance_tv;
    private TextView calories_tv;
    private TextView current_challenge_tv;
    private TextView challenge_progress_tv;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View homeView = inflater.inflate(R.layout.fragment_home, container, false);

        if (getActivity() != null) {
            // Get an instance of the SensorManager
            sensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        }

        mSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        step_counter_tv = homeView.findViewById(R.id.num_steps);
        distance_tv = homeView.findViewById(R.id.steps_distance);
        calories_tv = homeView.findViewById(R.id.num_calories_burned);
        current_challenge_tv = homeView.findViewById(R.id.currentChallenge);
        challenge_progress_tv = homeView.findViewById(R.id.challengeProgress);

        TextView welcome = homeView.findViewById(R.id.home_welcome);

        String currentUserID = getActivity().getSharedPreferences("userID", 0).getString("userID", "");
        try {
            ResultSet name = db.getName(currentUserID);
            String name_string = getString(R.string.home_welcome, name.getString(1));
            welcome.setText(name_string);
        } catch (Exception ex) {
            Context context = getContext();
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, ex.getMessage(), duration);
            toast.show();
        }

        // Show the user's challenge progress
        ResultSet currentChallengeRst = db.getCurrentChallengeInfo(currentUserID);

        // no challenge
        try {
            String challenge_string = "None";
            String progress_string = getString(R.string.challenge_progress, "0");

            if (currentChallengeRst.next()) {
                Integer challenge_id = currentChallengeRst.getInt(1);

                String challenge_name = currentChallengeRst.getString(2);
                String activity = currentChallengeRst.getString(3);
                String time_of_day = currentChallengeRst.getString(4);
                Integer duration = currentChallengeRst.getInt(5);

                challenge_string = challenge_name;

                ResultSet challengeProgressRst = db.getChallengeProgress(currentUserID, activity, time_of_day);


                double percentprogress = 0;
                if (challengeProgressRst != null) {
                    double percentProgress = challengeProgressRst.getDouble(1) / (double) duration * 100;

                    if (percentProgress > 100) {
                        percentProgress = 100;
                        db.insertCompletedChallenge(currentUserID, challenge_id);
                    }
                }

                progress_string = getString(R.string.challenge_progress, Double.toString(percentprogress));
            }

            current_challenge_tv.setText(challenge_string);
            challenge_progress_tv.setText(progress_string);

        } catch (Exception ex) {
            Toast.makeText(getContext(), "Error retreiving challenge", Toast.LENGTH_SHORT).show();
        }

        FloatingActionButton mFab = homeView.findViewById(R.id.log_activity);
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), LogChallengesActivity.class));
            }
        });

        return homeView;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (isAdded()) {
            step_counter_tv.setText(String.format(getResources().getString(R.string.HomeStepCount), (int) event.values[0]));
            distance_tv.setText(String.format(getResources().getString(R.string.HomeDistance), getDistanceTravelled((int) event.values[0])));
            calories_tv.setText(String.format(getResources().getString(R.string.HomeCalories), getCaloriesBurned((int) event.values[0])));
        }
    }

    private String getDistanceTravelled(int numSteps) {
        double step_length = 0.415 * 68;

        double distance = ((double) numSteps * step_length) / NUM_INCHES_IN_MILE;

        return String.format("%.2f", distance);
    }

    private int getCaloriesBurned(int numSteps) {
        double calories_burned_per_mile = walkingFactor * 150;
        double step_length = 0.415 * 68;
        double num_steps_per_mile = NUM_INCHES_IN_MILE / step_length;
        double conversionFactor = calories_burned_per_mile / num_steps_per_mile;

        return (int) (numSteps * conversionFactor);


    }


    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        sensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_FASTEST);
    }
}