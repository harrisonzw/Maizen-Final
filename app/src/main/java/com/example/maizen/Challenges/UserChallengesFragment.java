package com.example.maizen.Challenges;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.maizen.Database.Database;
import com.example.maizen.R;

import java.sql.ResultSet;
import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class UserChallengesFragment extends Fragment {
    private View view;
    private Database db = new Database();
    private ArrayList<String> positions = new ArrayList<>();
    private ArrayList<String> challenge_names = new ArrayList<>();
    private ArrayList<String> mImageURLs = new ArrayList<>();
    private final String walkingURL = "http://www.clker.com/cliparts/G/X/E/E/J/n/walking-man-black-th.png";
    private final String runningURL = "https://cdn2.iconfinder.com/data/icons/people-80/96/Picture13-512.png";
    private final String swimmingURL = "https://img.icons8.com/metro/420/swimming.png";
    private final String bballURL = "http://cdn.onlinewebfonts.com/svg/img_543065.png";
    private final String soccerURL = "http://cdn.onlinewebfonts.com/svg/img_531819.png";
    private final String tennisURL = "http://simpleicon.com/wp-content/uploads/tennis.png";
    private final String cyclingURL = "https://www.pinclipart.com/picdir/middle/158-1583079_cycle-recycle-refresh-repeat-icon-cycling-icon-png.png";
    private final String pingpongURL = "https://www.pngarts.com/files/3/Ping-Pong-PNG-Background-Image.png";
    private final String golfURL = "https://img.icons8.com/metro/420/golf.png";
    private final String gymnasticsURL = "https://pngimg.com/uploads/gymnastics/gymnastics_PNG71.png";
    private final String handballURL = "https://banner2.kisspng.com/20181130/xsv/kisspng-uodl-handball-clip-art-illustration-vector-graphic-lucyampgonza-on-emaze-5c01970ee86453.9097641215436080789519.jpg";
    private final String lacrosseURL = "https://s3.amazonaws.com/peoplepng/wp-content/uploads/2018/03/15155430/Lacrosse-PNG-Photos-1024x538.png";
    private final String racquetballURL = "https://www.pngrepo.com/download/143262/racquetball-silhouette.png";
    private final String skateboardURL = "https://cdn.iconscout.com/icon/premium/png-256-thumb/skateboarding-4-560541.png";
    private ArrayList<Double> duration = new ArrayList<>();
    private ArrayList<String> time_of_day = new ArrayList<>();
    private ArrayList<String> likes_count = new ArrayList<>();
    private ArrayList<String> activity = new ArrayList<>();
    private ArrayList<String> author = new ArrayList<>();
    private ArrayList<Integer> challenge_id = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_user_challenges, container, false);

        initImageBitmaps();

        return view;
    }

    private void initImageBitmaps() {
        ResultSet rst = db.getUserCreatedChallenges();

        try {
            for (int i = 0; i < 10; ++i) {
                rst.next();

                challenge_names.add(rst.getString(2));
                positions.add(Integer.toString(i + 1) + ". ");
                likes_count.add(rst.getString(7));
                duration.add(rst.getDouble(6));
                author.add(rst.getString(3));
                activity.add(rst.getString(4));
                time_of_day.add(rst.getString(5));
                challenge_id.add(rst.getInt(1));

                switch (rst.getString(4)) {
                    case "walking":
                        mImageURLs.add(walkingURL);
                        break;
                    case "running":
                        mImageURLs.add(runningURL);
                        break;
                    case "swimming":
                        mImageURLs.add(swimmingURL);
                        break;
                    case "basketball":
                        mImageURLs.add(bballURL);
                        break;
                    case "soccer":
                        mImageURLs.add(soccerURL);
                        break;
                    case "tennis":
                        mImageURLs.add(tennisURL);
                        break;
                    case "cycling":
                        mImageURLs.add(cyclingURL);
                        break;
                    case "ping pong":
                        mImageURLs.add(pingpongURL);
                        break;
                    case "golf":
                        mImageURLs.add(golfURL);
                        break;
                    case "gymnastics":
                        mImageURLs.add(gymnasticsURL);
                        break;
                    case "handball":
                        mImageURLs.add(handballURL);
                        break;
                    case "lacrosse":
                        mImageURLs.add(lacrosseURL);
                        break;
                    case "racquetball":
                        mImageURLs.add(racquetballURL);
                        break;
                    case "skateboard":
                        mImageURLs.add(skateboardURL);
                        break;
                }
            }
        } catch (Exception ex) {

        }


        initRecyclerView();
    }

    private void initRecyclerView() {
        RecyclerView recyclerView = view.findViewById(R.id.challenges_list);
        UserChallengesRecyclerView adapter = new UserChallengesRecyclerView(challenge_names,
                positions,
                mImageURLs,
                duration,
                activity,
                time_of_day,
                author,
                likes_count,
                challenge_id,
                getContext());
        recyclerView.setAdapter(adapter);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                mLayoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);
    }
}
