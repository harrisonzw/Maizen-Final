package com.example.maizen.Diary;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.maizen.R;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;


public class Search_adapter extends RecyclerView.Adapter<Search_adapter.FoodViewHolder> {


    //this context we will use to inflate the layout
    private Context mCtx;

    //we are storing all the ListItems in a list
    private List<ListItem> ListItemList;

    // add listener object to adapter
    final private ListItemClickListener mOnClickListener;

    //getting the context and ListItem list with constructor
    public Search_adapter(Context mCtx, List<ListItem> ListItemList, ListItemClickListener listener) {
        this.mCtx = mCtx;
        this.ListItemList = ListItemList;
        mOnClickListener = listener;

    }

    // add interface for click listening
    public interface ListItemClickListener {
        void onListItemClick(int clickedItemIndex, ListItem clickedFood);
    }

    @Override
    public FoodViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflating and returning our view holder
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.search_row, null);
        return new FoodViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FoodViewHolder holder, int position) {
        //getting the ListItem of the specified position
        ListItem ListItem = ListItemList.get(position);

        holder.food_name.setText(ListItem.getName());
        holder.food_brand.setText(ListItem.getBrand());
        holder.food_description.setText(ListItem.getFoodDescription());

    }


    @Override
    public int getItemCount() {
        return ListItemList.size();
    }

    class FoodViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView food_name, food_description, food_brand;
        //ImageView imageView;

        public FoodViewHolder(View ListItemView) {
            super(ListItemView);

            food_name = ListItemView.findViewById(R.id.food_name);
            food_description = ListItemView.findViewById(R.id.food_description);
            food_brand = ListItemView.findViewById(R.id.food_brand);

            ListItemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            ListItem ListItem = ListItemList.get(position);
            mOnClickListener.onListItemClick(position, ListItem);

        }

    }
}