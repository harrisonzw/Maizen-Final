package com.example.maizen.Diary;

public class Diary_row {

    private String food_name;
    private String calories;
    private String serving_size;
    private String meal;

    public Diary_row(String meal, String food_name, String calories, String serving_size){
        this.food_name = food_name;
        this.calories = calories;
        this.serving_size = serving_size;
        this.meal = meal;
    }

    public String get_name() {
        return food_name;
    }

    public String get_calories() {
        return calories;
    }

    public String get_serving() {
        return serving_size;
    }

    public String get_meal() {return meal;}
}
