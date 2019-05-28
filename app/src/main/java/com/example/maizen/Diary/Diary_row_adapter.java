
package com.example.maizen.Diary;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.content.Context;
import android.widget.Toast;

import com.example.maizen.Database.Database;
import com.example.maizen.Diary.Diary_row;
import com.example.maizen.R;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.sql.Ref;
import java.util.ArrayList;
import java.util.HashMap;

import androidx.annotation.NonNull;

public class Diary_row_adapter extends ArrayAdapter<Diary_row> implements View.OnClickListener{

    private String userID;
    private ArrayList<Diary_row> dataSet;
    Context mContext;

    // View lookup cachep
    private static class ViewHolder {
        TextView txtName;
        TextView txtServing;
        TextView txtCalories;
        ImageView deleteButton;
    }

    public Diary_row_adapter(Context context, ArrayList<Diary_row> data, String userID ) {
        super(context, R.layout.meal_diary_row, data);
        this.dataSet = data;
        this.mContext = context;
        this.userID = userID;
    }

    @Override
    public void onClick(View v) {

        int position=(Integer) v.getTag();
        Object object= getItem(position);
        final Diary_row dataModel=(Diary_row) object;

        switch (v.getId())
        {
            case R.id.delete_button:
                DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("Users")
                        .child(userID).child("Diary")
                        .child(dataModel.get_meal()).child("food_list");

                DatabaseReference newRef =  dbRef.child(dataModel.get_name());
                newRef.removeValue();

                Toast.makeText(mContext, "Deleted from Diary", Toast.LENGTH_SHORT).show();
                break;
        }
    }



    private int lastPosition = -1;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Diary_row row = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.meal_diary_row, parent, false);
            viewHolder.txtName = (TextView) convertView.findViewById(R.id.food_name_text);
            viewHolder.txtServing = (TextView) convertView.findViewById(R.id.serving_text);
            viewHolder.txtCalories = (TextView) convertView.findViewById(R.id.calories_text);
            viewHolder.deleteButton = (ImageView) convertView.findViewById(R.id.delete_button);

            result=convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }

        //Animation animation = AnimationUtils.loadAnimation(mContext, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
        //result.startAnimation(animation);
        lastPosition = position;

        viewHolder.txtName.setText(row.get_name());
        viewHolder.txtServing.setText(row.get_serving());
        viewHolder.txtCalories.setText(row.get_calories());
        viewHolder.deleteButton.setOnClickListener(this);
        viewHolder.deleteButton.setTag(position);
        //// Changed here
        //convertView.setOnClickListener(this);
        //viewHolder.info.setTag(position);
        // Return the completed view to render on screen
        return convertView;
    }
}
