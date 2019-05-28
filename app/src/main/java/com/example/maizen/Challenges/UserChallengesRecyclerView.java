package com.example.maizen.Challenges;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.maizen.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class UserChallengesRecyclerView extends RecyclerView.Adapter<UserChallengesRecyclerView.ViewHolder> {

    private ArrayList<String> positions;
    private ArrayList<String> challenge_names;
    private ArrayList<String> mImages;
    private ArrayList<Double> duration;
    private ArrayList<String> time_of_day;
    private ArrayList<String> likes_count;
    private ArrayList<String> activity;
    private ArrayList<String> author;
    private ArrayList<Integer> challenge_id;

    private Context mContext;

    public UserChallengesRecyclerView(ArrayList<String> challenge_names,
                                      ArrayList<String> positions,
                                      ArrayList<String> images,
                                      ArrayList<Double> duration,
                                      ArrayList<String> activity,
                                      ArrayList<String> time_of_day,
                                      ArrayList<String> author,
                                      ArrayList<String> likes_count,
                                      ArrayList<Integer> challenge_id,
                                      Context mContext) {
        this.challenge_names = challenge_names;
        this.positions = positions;
        this.mImages = images;
        this.mContext = mContext;
        this.duration = duration;
        this.activity = activity;
        this.time_of_day = time_of_day;
        this.likes_count = likes_count;
        this.author = author;
        this.challenge_id = challenge_id;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_challenge_row, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        Glide.with(mContext).asBitmap().load(mImages.get(position)).into(holder.image);
        holder.challenge_name_tv.setText(challenge_names.get(position));
        holder.position_tv.setText(positions.get(position));
        holder.author_tv.setText("Created By: " + author.get(position));
        holder.likes_tv.setText(likes_count.get(position) + " likes");

        holder.parent_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mIntent = new Intent(mContext, AddUserChallengeActivity.class);
                mIntent.putExtra("challenge_name", challenge_names.get(position));
                mIntent.putExtra("time_of_day", time_of_day.get(position));
                mIntent.putExtra("activity", activity.get(position));
                mIntent.putExtra("duration", duration.get(position));
                mIntent.putExtra("img_url", mImages.get(position));
                mIntent.putExtra("challenge_id", challenge_id.get(position));
                mContext.startActivity(mIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return challenge_names.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView position_tv;
        TextView challenge_name_tv;
        RelativeLayout parent_layout;
        TextView likes_tv;
        TextView author_tv;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.activity_pic);
            position_tv = itemView.findViewById(R.id.position);
            likes_tv = itemView.findViewById(R.id.challenge_num_likes);
            author_tv = itemView.findViewById(R.id.challenge_author);
            challenge_name_tv = itemView.findViewById(R.id.challenge_name);
            parent_layout = itemView.findViewById(R.id.users_challenges_layout);

        }
    }
}