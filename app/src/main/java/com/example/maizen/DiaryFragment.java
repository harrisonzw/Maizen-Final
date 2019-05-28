package com.example.maizen;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.example.maizen.Diary.Diary_row;
import com.example.maizen.Diary.Diary_row_adapter;
import com.example.maizen.Diary.Search_food;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;


public class DiaryFragment extends Fragment implements View.OnClickListener {

    private TextView totalCalories;
    private int totalCal_int;
    private CardView breakfastCard, lunchCard, dinnerCard, snackCard;
    private Button breakfastButton, lunchButton, dinnerButton, snackButton;
    private ListView breakfast_listview, lunch_listview, dinner_listview, snack_listview;
    private TextView breakfast_calories_textview, lunch_calories_textview, dinner_calories_textview, snack_calories_textview;
    final FirebaseDatabase database = FirebaseDatabase.getInstance();

    private String userID;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Context context = container.getContext();
        View dairyView = inflater.inflate(R.layout.fragment_diary, container, false);

        totalCalories = dairyView.findViewById(R.id.total_calories_textview);
        // define the card view objects
        breakfastCard = dairyView.findViewById(R.id.breakfast_card);
        lunchCard = dairyView.findViewById(R.id.lunch_card);
        dinnerCard = dairyView.findViewById(R.id.dinner_card);
        snackCard = dairyView.findViewById(R.id.snack_card);

        breakfastButton = dairyView.findViewById(R.id.breakfast_button);
        lunchButton = dairyView.findViewById(R.id.lunch_button);
        dinnerButton = dairyView.findViewById(R.id.dinner_button);
        snackButton = dairyView.findViewById(R.id.snack_button);

        breakfast_listview = dairyView.findViewById(R.id.breakfast_listview);
        lunch_listview = dairyView.findViewById(R.id.lunch_listview);
        dinner_listview = dairyView.findViewById(R.id.dinner_listview);
        snack_listview = dairyView.findViewById(R.id.snack_listview);

        breakfast_calories_textview = dairyView.findViewById(R.id.breakfast_calories_textview);
        lunch_calories_textview = dairyView.findViewById(R.id.lunch_calories_textview);
        dinner_calories_textview = dairyView.findViewById(R.id.dinner_calories_textview);
        snack_calories_textview = dairyView.findViewById(R.id.snack_calories_textview);

        // do something when they are clicked
        breakfastCard.setOnClickListener(this);
        lunchCard.setOnClickListener(this);
        dinnerCard.setOnClickListener(this);
        snackCard.setOnClickListener(this);

        breakfastButton.setOnClickListener(this);
        lunchButton.setOnClickListener(this);
        dinnerButton.setOnClickListener(this);
        snackButton.setOnClickListener(this);

        userID = this.getActivity().getSharedPreferences("userID", 0).getString("userID", "");

        DatabaseReference newRef = database.getReference("Users").child(userID).child("Diary");
        newRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                totalCal_int = 0;
                HashMap<String, Object> data = (HashMap<String,Object>) dataSnapshot.getValue();
                Log.i("Database", data.toString());

                // get the foods for each meal
                if (data.get("breakfast").equals("food_list")) { populateList("breakfast", null); }
                else {
                    HashMap<String, Object> breakfast_data = (HashMap<String,Object>) data.get("breakfast");
                    HashMap<String, Object> breakfast_foods = (HashMap<String,Object>) breakfast_data.get("food_list");
                    populateList("breakfast", breakfast_foods);
                }
                if (data.get("lunch").equals("food_list")) { populateList("lunch", null); }
                else {
                    HashMap<String, Object> lunch_data = (HashMap<String,Object>) data.get("lunch");
                    HashMap<String, Object> lunch_foods = (HashMap<String,Object>) lunch_data.get("food_list");
                    populateList("lunch", lunch_foods);
                }

                if (data.get("dinner").equals("food_list")) { populateList("dinner", null); }
                else {
                    HashMap<String, Object> dinner_data = (HashMap<String,Object>) data.get("dinner");
                    HashMap<String, Object> dinner_foods = (HashMap<String,Object>) dinner_data.get("food_list");
                    populateList("dinner", dinner_foods);
                }

                if (data.get("snack").equals("food_list")) { populateList("snack", null); }
                else {
                    HashMap<String, Object> snack_data = (HashMap<String,Object>) data.get("snack");
                    HashMap<String, Object> snack_foods = (HashMap<String,Object>) snack_data.get("food_list");
                    populateList("snack", snack_foods);
                }
                totalCalories.setText(Integer.toString(totalCal_int));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w("Database", "Failed to read value.", databaseError.toException());
            }
        });


        return dairyView;
    }

    public void populateList(String meal_str,  HashMap<String, Object> data) {

        final ArrayList<Diary_row> food_rows = new ArrayList<>();

        int meal_calories = 0;

        if (data != null) {

            Iterator<Map.Entry<String, Object>> it = data.entrySet().iterator();
            while (it.hasNext()) {
                HashMap<String, String> food = (HashMap<String, String>) it.next().getValue();
                String name = (String) food.get("food_name");
                String serving = food.get("serving_description");
                String calories = food.get("calories");
                meal_calories += Integer.parseInt(calories);
                totalCal_int += Integer.parseInt(calories);

                Diary_row row = new Diary_row(meal_str, name, calories, serving);
                food_rows.add(row);
            }
        }

        Diary_row_adapter this_adapter = new Diary_row_adapter(
                getActivity().getApplicationContext(), food_rows, userID
        );


        switch(meal_str) {
            case "breakfast":
                breakfast_calories_textview.setText(Integer.toString(meal_calories));
                breakfast_listview.setAdapter(this_adapter);


                break;
            case "lunch":
                lunch_calories_textview.setText(Integer.toString(meal_calories));
                lunch_listview.setAdapter(this_adapter);
                break;

            case "dinner":
                dinner_calories_textview.setText(Integer.toString(meal_calories));
                dinner_listview.setAdapter(this_adapter);
                break;

            case "snack":
                snack_calories_textview.setText(Integer.toString(meal_calories));
                snack_listview.setAdapter(this_adapter);
                break;
        }

        /*breakfast_calories_textview.setText(Integer.toString(breakfast_calories));
        breakfast_listview.setAdapter(this_adapter);*/
    }


    @Override
    public void onClick(View v) {

        Intent i;
        switch (v.getId()) {
            case R.id.breakfast_card:
                //breakfastCard.setCardBackgroundColor(Color.parseColor("#0f5998"));
                i = new Intent(getActivity(), Search_food.class);
                i.putExtra("meal", "breakfast");
                startActivity(i);
                break;
            case R.id.breakfast_button:
                i = new Intent(getActivity(), Search_food.class);
                i.putExtra("meal", "breakfast");
                startActivity(i);
                break;
            case R.id.lunch_card:
                i = new Intent(getActivity(), Search_food.class);
                i.putExtra("meal", "lunch");
                startActivity(i);
                break;
            case R.id.lunch_button:
                i = new Intent(getActivity(), Search_food.class);
                i.putExtra("meal", "lunch");
                startActivity(i);
                break;
            case R.id.dinner_card :
                i = new Intent(getActivity(), Search_food.class);
                i.putExtra("meal", "dinner");
                startActivity(i);
                break;
            case R.id.dinner_button:
                i = new Intent(getActivity(), Search_food.class);
                i.putExtra("meal", "dinner");
                startActivity(i);
                break;
            case R.id.snack_card:
                i = new Intent(getActivity(), Search_food.class);
                i.putExtra("meal", "snack");
                startActivity(i);
                break;
            case R.id.snack_button:
                i = new Intent(getActivity(), Search_food.class);
                i.putExtra("meal", "snack");
                startActivity(i);
                break;
            default:
                break;
        }

    }
}