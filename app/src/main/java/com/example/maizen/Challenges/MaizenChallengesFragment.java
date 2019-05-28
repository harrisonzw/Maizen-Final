package com.example.maizen.Challenges;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.maizen.Database.Database;
import com.example.maizen.R;

import java.sql.ResultSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

public class MaizenChallengesFragment extends Fragment {
    private View challenges_view;
    private Database db = new Database();
    private View challengeCardView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        challenges_view = inflater.inflate(R.layout.fragment_maizen_challenges, container, false);

        String currentUserID = getActivity().getSharedPreferences("userID", 0).getString("userID", "");
        ResultSet completedChallenges = db.getUserCompletedChallenges(currentUserID);

        try {
            while (completedChallenges.next()) ;
        } catch (Exception e) {
            // set specific challenge cards to be greyed out
            greyOutChallengeCard(1);
            greyOutChallengeCard(2);
            greyOutChallengeCard(4);
            greyOutChallengeCard(5);
            greyOutChallengeCard(7);
            greyOutChallengeCard(8);
            greyOutChallengeCard(10);
            greyOutChallengeCard(11);
        }

        // set onclick listener for all the challenge cards
        setCardOnClickListener();

        return challenges_view;
    }

    private void setCardOnClickListener() {
        GridLayout grid = challenges_view.findViewById(R.id.challenge_grid);
        int childCount = grid.getChildCount();

        for (int i = 0; i < childCount; i++) {
            final CardView challenge_card = (CardView) grid.getChildAt(i);
            challenge_card.setTag(i);
            challenge_card.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    challengeCardView = view;

                    // Initialize a new instance of LayoutInflater service
                    LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                    // Inflate the custom layout/view
                    View popupView = inflater.inflate(R.layout.activity_challenges_popup, null);

                    // Set the popup window text view
                    TextView popup_title = popupView.findViewById(R.id.challenge_popup_title);
                    TextView popup_description = popupView.findViewById(R.id.challenge_popup_description);

                    int maizen_challenge_resID = getResources().getIdentifier("Maizen_Challenge" + Integer.toString((Integer) view.getTag()), "id", getActivity().getPackageName());
                    TextView challenge_tv = challenges_view.findViewById(maizen_challenge_resID);
                    popup_title.setText(challenge_tv.getText().toString());

                    String[] early_and_night = {"2", "5", "15"};
                    String[] calories = {"500", "1000", "2000"};
                    String[] num_different_activities = {"3", "7", "10"};
                    String[] num_miles = {"2", "3", "4"};
                    String[] days = {"3", "7", "30"};
                    switch (view.getId()) {
                        case R.id.Challenge_Card0:
                        case R.id.Challenge_Card1:
                        case R.id.Challenge_Card2:
                            popup_description.setText("Record " + early_and_night[((Integer) view.getTag()) % 3] + " miles in the morning (5AM - 11AM)");
                            break;
                        case R.id.Challenge_Card3:
                        case R.id.Challenge_Card4:
                        case R.id.Challenge_Card5:
                            popup_description.setText("Record " + early_and_night[((Integer) view.getTag()) % 3] + " miles at night (8 PM - 12 AM)");
                            break;
                        case R.id.Challenge_Card6:
                        case R.id.Challenge_Card7:
                        case R.id.Challenge_Card8:
                            popup_description.setText("Lose " + calories[((Integer) view.getTag()) % 3] + " calories through " + num_different_activities[((Integer) view.getTag()) % 3] + " different physical activities");
                            break;
                        case R.id.Challenge_Card9:
                        case R.id.Challenge_Card10:
                        case R.id.Challenge_Card11:
                            popup_description.setText("Record " + num_miles[((Integer) view.getTag()) % 3] + " miles " + days[((Integer) view.getTag()) % 3] + " days in a row");
                            break;
                    }

                    // Initialize a new instance of popup window
                    final PopupWindow popupWindow = new PopupWindow(
                            popupView,
                            ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT
                    );

                    // close popup window when touching outside the window
                    popupWindow.setOutsideTouchable(true);

                    // Get a reference for the popup view close button
                    Button cancelButton = popupView.findViewById(R.id.challenge_popup_cancel);

                    // Get reference for popup view accept button
                    Button acceptButton = popupView.findViewById(R.id.challenge_popup_accept);

                    // Set a click listener for the popup window close button
                    cancelButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            // Dismiss the popup window
                            popupWindow.dismiss();
                        }
                    });

                    // Set a click listener for the popup window accept button
                    acceptButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String currentUserID = getContext().getSharedPreferences("userID", 0).getString("userID", "");
                            db.acceptChallenge(currentUserID, (int) challengeCardView.getTag());
                            Toast.makeText(getContext(), "Added Challenge!", Toast.LENGTH_SHORT).show();
                            popupWindow.dismiss();
                        }
                    });

                    // Finally, show the popup window at the center location of root relative layout
                    popupWindow.showAtLocation(challenges_view.findViewById(R.id.challenges_layout), Gravity.CENTER, 0, 0);
                }
            });
        }
    }

    private void greyOutChallengeCard(int challenge_number) {
        // find the resource ID number for the challenge card by the passed in challenged_number integer
        int challenge_card_resID = getResources().getIdentifier("Challenge_Card" + Integer.toString(challenge_number), "id", getActivity().getPackageName());
        CardView card = challenges_view.findViewById(challenge_card_resID);
        // set the background color of the card itself
        card.setCardBackgroundColor(getResources().getColor(R.color.grey_transparent));
        // remove the shadow of the card
        card.setCardElevation(0);

        // find the resource ID number for the maizen challenge by the passed in challenged_number integer
        int maizen_challenge_resID = getResources().getIdentifier("Maizen_Challenge" + Integer.toString(challenge_number), "id", getActivity().getPackageName());
        TextView tv = challenges_view.findViewById(maizen_challenge_resID);
        // set the text color of the text view
        tv.setTextColor(getResources().getColor(R.color.grey_transparent));

        // set the challenge icon color to grey_transparent
        for (Drawable drawable : tv.getCompoundDrawables()) {
            if (drawable != null) {
                drawable.setColorFilter(new PorterDuffColorFilter(ContextCompat.getColor(tv.getContext(), R.color.grey_transparent), PorterDuff.Mode.SRC_IN));
            }
        }


    }
}
