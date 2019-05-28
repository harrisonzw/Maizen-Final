package com.example.maizen;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.maizen.Database.Database;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class SettingsFragment extends Fragment {

    private String userID;
    private Button UpdateButton;
    private EditText firstNameText, lastNameText,
            heightFeetText, heightInchesText, weightText;
    private View settingsView;
    private Database db;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        db = new Database();
        userID = this.getActivity().getSharedPreferences("userID", 0).getString("userID", "");


        settingsView = inflater.inflate(R.layout.fragment_settings, container, false);

        UpdateButton = settingsView.findViewById(R.id.SubmitButton);

        firstNameText = settingsView.findViewById(R.id.FirstNameEntry);
        lastNameText = settingsView.findViewById(R.id.LastNameEntry);
        heightFeetText = settingsView.findViewById(R.id.HeightFeetEntry);
        heightInchesText = settingsView.findViewById(R.id.HeightInchesEntry);
        weightText = settingsView.findViewById(R.id.WeightEntry);


        UpdateButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view) {

                String first_name =  firstNameText.getText().toString();
                String last_name = lastNameText.getText().toString();
                String height_feet = heightFeetText.getText().toString();
                String height_inches = heightInchesText.getText().toString();
                String weight = weightText.getText().toString();

                if(!first_name.equals("") && !last_name.equals("")){
                    db.changeUserName(userID, first_name, last_name);
                }
                if (!height_feet.equals("") && !height_inches.equals("")){
                    db.changeUserHeight(userID, Integer.parseInt(height_feet), Integer.parseInt(height_inches));
                }
                if(!weight.equals("")){
                    db.changeUserWeight( userID ,Integer.parseInt(weight));
                }


                Toast.makeText(settingsView.getContext(), "Updated Profile", Toast.LENGTH_SHORT).show();
                // Redirect the user to the home page
                Intent homeActivity = new Intent(getActivity(), MainActivity.class);
                startActivity(homeActivity);

            }

        });
        return settingsView;

    }



}
