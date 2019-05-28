package com.example.maizen.Diary;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.maizen.Diary.FatSecretImplementation.FatSecretGet;
import com.example.maizen.Diary.FatSecretImplementation.FatSecretSearch;
import com.example.maizen.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class Search_food extends AppCompatActivity implements Search_adapter.ListItemClickListener {

    private SearchView searchView;
    //private TextView resultTextView;
    private FatSecretSearch mFatSecretSearch;

    private RecyclerView rvFoodSearch;
    private RecyclerView.Adapter adapter;
    private List<ListItem> ListItems;
    private String meal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_food);

        Bundle bundle = getIntent().getExtras();


        // extract the meal type
        meal = bundle.getString("meal");

        mFatSecretSearch = new FatSecretSearch(); // method.search

        searchView = findViewById(R.id.search_field);

        // add recycler view from layout xml
        rvFoodSearch = findViewById(R.id.rv_foods);
        rvFoodSearch.setHasFixedSize(true);
        rvFoodSearch.setLayoutManager(new LinearLayoutManager(this));

        // perform set on query text listener event
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                String queryText = searchView.getQuery().toString();

                //Toast.makeText(Search_food.this, queryText, Toast.LENGTH_SHORT).show();

                ListItems = new ArrayList<>();

                new makeSearchQuery().execute(queryText);

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                return false;
            }

        });
    } // end onCreate

    public void setSearchAdapter() {
        // making recycler view adapter
        adapter = new Search_adapter(getApplicationContext(), ListItems, this);

        // setting adapter
        rvFoodSearch.setAdapter(adapter);
    }

    @Override
    public void onListItemClick(int clickedItemIndex, ListItem clickedFood) {

        Intent intent = new Intent(this, GetFoodInfo.class);
        intent.putExtra("food_id", clickedFood.getID());
        intent.putExtra("meal", meal);
        startActivity(intent);
        //finish();
    }

    // create AsyncTask to do the query the background
    public class makeSearchQuery extends AsyncTask<String, Void, JSONObject> {

        //Override the doInBackground method to perform the query.
        @Override
        protected JSONObject doInBackground( String... params) {
            String queryText = params[0];
            JSONObject searchResult = null;
            try {
                searchResult = mFatSecretSearch.searchFood(queryText, 0);
            }
            catch (Exception exception) {
                Toast.makeText(Search_food.this, "Invalid Input", Toast.LENGTH_SHORT).show();

        }
            return searchResult;
        }

        private String brand_name;
        // Update the UI from the results
        @Override
        protected void onPostExecute(JSONObject searchResult) {
            if (searchResult != null ) {

                try {
                    JSONArray foods_array = searchResult.getJSONArray("food");
                    String foods_string = foods_array.toString(4);

                    // create the ListItem and add it to foodsarray
                    for(int i = 0; i < foods_array.length(); i++) {
                        JSONObject o = foods_array.getJSONObject(i);

                        String food_type = o.getString("food_type");

                        if (food_type.equals("Generic")){
                            brand_name = "Generic";
                        }
                        else {
                            brand_name = o.getString("brand_name");
                        }

                        ListItem item = new ListItem(
                                o.getString("food_name"),
                                brand_name,
                                o.getString("food_description"),
                                o.getString("food_id"),
                                o.getString("food_type"),
                                o.getString("food_url")
                        );
                        ListItems.add(item);
                    }

                    setSearchAdapter();

                }
                catch (JSONException e){
                    e.printStackTrace();
                    Toast.makeText(Search_food.this, "JSON parse error", Toast.LENGTH_SHORT).show();
                }


            }
        }

    } // end async task

}
