package com.example.maizen.Diary;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.example.maizen.DiaryFragment;

import com.example.maizen.MainActivity;
import com.example.maizen.R;
import com.example.maizen.Diary.FatSecretImplementation.FatSecretGet;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class GetFoodInfo extends AppCompatActivity {

    private FatSecretGet mFatSecretGet;
    private TextView foodData;
    private TextView foodname_text, foodtype_text, calorie_text, fat_text, saturatedFat_text, polyFat_text,
            monoFat_text, cholesterol_text, sodium_text, carb_text, sugar_text, fiber_text, protein_text;
    private Long food_id;
    private String meal;

    private String[] serving_description_list;

    private Spinner spinner;

    private JSONArray servings_list;
    private int position_int = 0;
    private String food_name;

    private Button addFoodButton;

    private DatabaseReference dbRef;

    private String userID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_food_info);

        userID = this.getSharedPreferences("userID", 0).getString("userID", "");

        foodname_text = findViewById(R.id.foodname_text);
        foodtype_text = findViewById(R.id.foodtype_text);
        calorie_text = findViewById(R.id.calories_text);
        fat_text = findViewById(R.id.totalFat_text);
        saturatedFat_text = findViewById(R.id.saturatedFat_text);
        polyFat_text = findViewById(R.id.polyFat_text);
        monoFat_text = findViewById(R.id.monoFat_text);
        cholesterol_text = findViewById(R.id.cholesterol_text);
        sodium_text = findViewById(R.id.sodium_text);
        carb_text = findViewById(R.id.carb_text);
        sugar_text = findViewById(R.id.sugar_text);
        fiber_text = findViewById(R.id.fiber_text);
        protein_text = findViewById(R.id.protein_text);
        addFoodButton = findViewById(R.id.add_button);

        spinner = findViewById(R.id.spinner);

        Bundle bundle = getIntent().getExtras();

        mFatSecretGet = new FatSecretGet();

        // extract the food id and meal type
        food_id = Long.valueOf(bundle.getString("food_id"));
        meal = bundle.getString("meal");


        JSONObject searchResult = mFatSecretGet.getFood(food_id);


        try {
            if (searchResult != null) {
                Log.e("JSON", searchResult.toString());
                food_name = searchResult.getString("food_name");
                String food_type = searchResult.getString("food_type");
                final JSONObject servings = searchResult.getJSONObject("servings");
                foodname_text.setText(food_name);

                String food_brand = ((food_type.equals("Brand"))) ? searchResult.getString("brand_name") : food_type;

                foodtype_text.setText(food_brand);

                // some food only have one serving option
                servings_list = new JSONArray();
                Object thisObject = servings.get("serving");
                if (thisObject instanceof JSONArray) {
                    servings_list = (JSONArray) servings.get("serving");
                }
                else if (thisObject instanceof  JSONObject) {
                    servings_list.put(servings.get("serving"));
                }

                serving_description_list = new String[servings_list.length()];

                // get the data according to serving size from json
                for (int i = 0; i < servings_list.length(); i++) {
                    JSONObject this_serving = (JSONObject) servings_list.get(i);
                    serving_description_list[i] = this_serving.getString("serving_description");
                }

                // set the spinner adapter with the serving list
                ArrayAdapter<String> spinner_adapter = new ArrayAdapter<String>(this,
                        android.R.layout.simple_spinner_item, serving_description_list);

                spinner_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(spinner_adapter);

                // listen to spinner item selection
                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        int selected_position = parent.getSelectedItemPosition();
                        try {
                            JSONObject this_serving = (JSONObject) servings_list.get(selected_position);
                            displayFoodData(this_serving);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

                JSONObject serving_0 = (JSONObject) servings_list.get(0);
                displayFoodData(serving_0);


                addFoodButton.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        Toast.makeText(GetFoodInfo.this, "Added to Food Diary", Toast.LENGTH_SHORT).show();

                        try {
                            JSONObject foodData = (JSONObject) servings_list.get(position_int);
                            String calories = foodData.getString("calories");

                            Map<String, Object> map = new HashMap<>();
                            map.put("calories" ,  calories);
                            map.put("food_name", food_name);
                            map.put("carbohydrate", foodData.getString("carbohydrate"));
                            map.put("protein", foodData.getString("protein"));
                            map.put("fat", foodData.getString("fat"));
                            map.put("serving_description", foodData.getString("serving_description"));

                            //Toast.makeText(GetFoodInfo.this, map.toString(), Toast.LENGTH_SHORT).show();

                            Log.i("map", map.toString());

                            String userID = getSharedPreferences("userID", 0).getString("userID", "");

                            // get the database reference
                            //dbRef = FirebaseDatabase.getInstance().getReference("Diary").child(meal).child("food_list");

                            dbRef = FirebaseDatabase.getInstance().getReference("Users")
                                    .child(userID).child("Diary").child(meal).child("food_list");

                            /*DatabaseReference newChildRef = dbRef.push();
                            String key = newChildRef.getKey();
                            dbRef.child(key).setValue(map)*/;

                            // add the food to the database under the right section
                            DatabaseReference ref1 = dbRef.child(food_name);
                            ref1.setValue(map);
                        }
                        catch (JSONException e) { e.printStackTrace(); }

                        /*DiaryFragment this_fragment = new DiaryFragment();
                        LinearLayout v = findViewById(R.id.get_food_page);
                        getSupportFragmentManager().beginTransaction().replace(v.getId(), this_fragment).commit();*/

                        finish();

                    }

                });
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(GetFoodInfo.this, "JSON parse error", Toast.LENGTH_SHORT).show();
        }
    } // end onCreate

    public void getSelectedServing(View v) {
        position_int = spinner.getSelectedItemPosition();
        //Toast.makeText(this, position_int, Toast.LENGTH_SHORT).show();
    }

    public void displayFoodData(JSONObject serving_data) {

        try {
            if (serving_data != null) {
                calorie_text.setText(serving_data.getString("calories"));
                fat_text.setText(serving_data.getString("fat") + "g");
                protein_text.setText(serving_data.getString("protein")+ "g");
                carb_text.setText(serving_data.getString("carbohydrate")+ "g");


                if (serving_data.has("saturated_fat")) {
                    saturatedFat_text.setText(serving_data.getString("saturated_fat") + "g");
                } else {saturatedFat_text.setText("-"); }

                if (serving_data.has("polyunsaturated_fat")) {
                    polyFat_text.setText(serving_data.getString("polyunsaturated_fat")+ "g");
                } else {polyFat_text.setText("-"); }

                if (serving_data.has("monounsaturated_fat")) {
                    monoFat_text.setText(serving_data.getString("monounsaturated_fat")+ "g");
                } else {monoFat_text.setText("-"); }

                if (serving_data.has("cholesterol")) {
                    cholesterol_text.setText(serving_data.getString("cholesterol")+ "mg");
                } else {cholesterol_text.setText("-"); }

                if (serving_data.has("sodium")) {
                    sodium_text.setText(serving_data.getString("sodium")+ "mg");
                } else {sodium_text.setText("-"); }

                if (serving_data.has("sugar")) {
                    sugar_text.setText(serving_data.getString("sugar")+ "g");
                } else {sugar_text.setText("-"); }

                if (serving_data.has("fiber")) {
                    fiber_text.setText(serving_data.getString("fiber")+ "g");
                } else {fiber_text.setText("-"); }

            }
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(GetFoodInfo.this, "JSON parse error", Toast.LENGTH_SHORT).show();
        }
    }


    // create AsyncTask to do the query the background
    public class makeQuery extends AsyncTask<Long, Void, JSONObject> {

        //Override the doInBackground method to perform the query.
        @Override
        protected JSONObject doInBackground(Long... params) {
            Long food_id = params[0];

            JSONObject searchResult = null;
            try {
                searchResult = mFatSecretGet.getFood(food_id);
            } catch (Exception exception) {
                //Toast.makeText(GetFoodInfo.this, "Invalid Input", Toast.LENGTH_SHORT).show();

            }
            return searchResult;
        }

        // Update the UI from the results
        @Override
        protected void onPostExecute(JSONObject searchResult) {

            try {
                if (searchResult != null) {

                    String food_name = searchResult.getString("food_name");
                    JSONObject servings = searchResult.getJSONObject("servings");

                    JSONObject serving = servings.getJSONObject("serving");
                    String calories = serving.getString("calories");
                    String carbohydrate = serving.getString("carbohydrate");
                    String protein = serving.getString("protein");
                    String fat = serving.getString("fat");
                    String serving_description = serving.getString("serving_description");
                    Log.e("serving_description", serving_description);
                    /**
                     * Displays results in the LogCat
                     */
                    Log.e("food_name", food_name);
                    Log.e("calories", calories);
                    Log.e("carbohydrate", carbohydrate);
                    Log.e("protein", protein);
                    Log.e("fat", fat);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(GetFoodInfo.this, "JSON parse error", Toast.LENGTH_SHORT).show();
            }
        }

    } // end async task

}