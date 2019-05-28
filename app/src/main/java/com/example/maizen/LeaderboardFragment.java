package com.example.maizen;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.maizen.Database.Database;

import java.sql.ResultSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class LeaderboardFragment extends Fragment {
    private Database db = new Database();
    private int user_R_ID;
    private int steps_R_id;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_leaderboard, container, false);

        try {
            ResultSet top10 = db.getTop10();

            int resultNumber = 1;
            while (top10.next()) {
                String userID = top10.getString(1);
                String name_string = getString(R.string.Name, top10.getString(2), top10.getString(3));
                int steps = top10.getInt(4);
                String steps_string = String.format(getResources().getString(R.string.steps), steps);

                if (getActivity() != null) {
                    user_R_ID = getResources().getIdentifier("User" + Integer.toString(resultNumber),
                            "id",
                            getActivity().getPackageName());

                    steps_R_id = getResources().getIdentifier("user" + Integer.toString(resultNumber) + "Steps",
                            "id",
                            getActivity().getPackageName());
                }

                TextView userView = view.findViewById(user_R_ID);
                TextView stepsView = view.findViewById(steps_R_id);

                userView.setText(name_string);
                stepsView.setText(steps_string);
                ++resultNumber;
            }
        } catch (Exception ex) {
            TextView place;
            place = view.findViewById(R.id.User1);
            place.setText(ex.getMessage());
        }

        return view;
    }
}