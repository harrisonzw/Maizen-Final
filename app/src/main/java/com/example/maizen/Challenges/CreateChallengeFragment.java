package com.example.maizen.Challenges;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.maizen.Database.Database;
import com.example.maizen.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class CreateChallengeFragment extends Fragment {
    private Database db = new Database();
    private View view;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_create_challenge, container, false);

        Button submitButton = view.findViewById(R.id.SubmitButton);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Spinner activitySpinner = view.findViewById(R.id.activity_spinner);
                String activity_name = activitySpinner.getSelectedItem().toString();

                Spinner durationSpinner = view.findViewById(R.id.duration_spinner);
                String duration = durationSpinner.getSelectedItem().toString();

                Spinner time_of_day_spinner = view.findViewById(R.id.time_of_day_spinner);
                String time_of_day = time_of_day_spinner.getSelectedItem().toString();

                EditText challenge_edit_text = view.findViewById(R.id.nameChallenge);
                String challenge_name = challenge_edit_text.getText().toString();

                String currentUserID = getContext().getSharedPreferences("userID", 0).getString("userID", "");
                db.insertUserChallenge(currentUserID, challenge_name, activity_name, time_of_day, duration);
            }
        });

        return view;
    }
}
