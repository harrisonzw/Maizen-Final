package com.example.maizen;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.maizen.Database.Database;

import java.sql.ResultSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class ProfileFragment extends Fragment {
    private Database db = new Database();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        // Get the current user's ID
        String currentUserID = getActivity().getSharedPreferences("userID", 0).getString("userID", "");

        // Fill in the user's basic info
        try {
            // Query the database for the user's name
            ResultSet userInfoRst = db.getUserInfo(currentUserID);
            if (userInfoRst.next()) {
                // Create the user's full name string
                String firstName = userInfoRst.getString(2);
                String lastName = userInfoRst.getString(3);
                String fullName = "  " + firstName + " " + lastName;

                // Set the name text
                TextView nameView = view.findViewById(R.id.name);
                nameView.setText(fullName);


                // Create the user's full name string
                Integer heightFeet = userInfoRst.getInt(9);
                Integer heightInches = userInfoRst.getInt(10);
                String height = "  " + Integer.toString(heightFeet) + "' " +
                        Integer.toString(heightInches) + "''";

                // Set the height text
                TextView heightView = view.findViewById(R.id.height);
                heightView.setText(height);


                // Create the user's weight string
                String weightPounds = userInfoRst.getString(8);
                String weight = "  " + weightPounds + " pounds";

                // Set the weight text
                TextView weightView = view.findViewById(R.id.weightPounds);
                weightView.setText(weight);


                // Create the user's birth date string
                Integer birthMonth = userInfoRst.getInt(4);
                Integer birthDay = userInfoRst.getInt(5);
                Integer birthYear = userInfoRst.getInt(6);
                String birthDate = "  " + Integer.toString(birthMonth) + "/" +
                        Integer.toString(birthDay) + "/" +
                        Integer.toString(birthYear);

                // Set the birth date text
                TextView birthDateView = view.findViewById(R.id.birthDate);
                birthDateView.setText(birthDate);
            }
        } catch (Exception ex) {
            Context context = getContext();
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, ex.getMessage(), duration);
            toast.show();
        }

        // Fill in the user's step statistics
        try {
            // Query the database for the user's name
            ResultSet userStepsStatsRst = db.getUserStepStatistics(currentUserID);
            if (userStepsStatsRst.next()) {
                // Get the user's minimum steps in a day
                Integer minSteps =  userStepsStatsRst.getInt(1);

                // Set the steps text
                TextView minStepsView = view.findViewById(R.id.minSteps);
                minStepsView.setText(Integer.toString(minSteps));


                // Get the user's average steps in a day
                Integer avgSteps = userStepsStatsRst.getInt(2);

                // Set the steps text
                TextView avgStepsView = view.findViewById(R.id.avgSteps);
                avgStepsView.setText(Integer.toString(avgSteps));


                // Get the user's maximum steps in a day
                Integer maxSteps = userStepsStatsRst.getInt(3);

                // Set the steps text
                TextView maxStepsView = view.findViewById(R.id.maxSteps);
                maxStepsView.setText(Integer.toString(maxSteps));
            }
        } catch (Exception ex) {
            Context context = getContext();
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, ex.getMessage(), duration);
            toast.show();
        }

        // Fill in the user's number of completed Maizen challenges
        try {
            // Query the database for the user's number of completed Maizen challenges
            ResultSet maizenChallengesCountRst = db.getCompletedMaizenChallengeCount(currentUserID);
            if (maizenChallengesCountRst.next()) {
                // Get the user's minimum steps in a day
                Integer count =  maizenChallengesCountRst.getInt(1);

                // Set the count text
                TextView maizenChallengesCountView = view.findViewById(R.id.maizenChallenges);
                maizenChallengesCountView.setText(Integer.toString(count));
            }
        } catch (Exception ex) {
            Context context = getContext();
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, ex.getMessage(), duration);
            toast.show();
        }

        // Fill in the user's number of completed user challenges
        try {
            // Query the database for the user's user's number of completed user challenges
            ResultSet userChallengesCountRst = db.getCompletedUserChallengeCount(currentUserID);
            if (userChallengesCountRst.next()) {
                // Get the user's count
                Integer count = userChallengesCountRst.getInt(1);

                // Set the count text
                TextView userChallengesCountView = view.findViewById(R.id.userChallenges);
                userChallengesCountView.setText(Integer.toString(count));
            }
        } catch (Exception ex) {
            Context context = getContext();
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, ex.getMessage(), duration);
            toast.show();
        }

        return view;
    }
}
