package com.example.maizen.Diary;

// class object for holding data in each ListItem in the recyclerView
public class ListItem {
    private String food_name;
    private String food_brand;
    private String food_description;
    private String food_id;
    private String food_type;
    private String food_url;

    // constructor
    public ListItem(String food_name, String food_brand, String food_description,
                    String food_id, String food_type, String food_url){
        this.food_name = food_name;
        this.food_brand = food_brand;
        this.food_description = food_description;
        this.food_id = food_id;
        this.food_type = food_type;
        this.food_url = food_url;
    }

    // add getters for private variables
    public String getID() { return food_id; }

    public String getBrand() { return food_brand; }

    public String getName() {
        return food_name;
    }

    public String getFoodDescription() {
        return food_description;
    }

}
