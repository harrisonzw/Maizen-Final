package com.example.maizen;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.maizen.Challenges.ChallengesFragment;
import com.example.maizen.Database.Database;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;

import java.sql.ResultSet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawer;
    private Database db = new Database();
    private FirebaseAuth mfirebaseAuth;
    private BottomNavigationView bottomNav;

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =

            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    bottomNav.getMenu().setGroupCheckable(0, true, true);

                    Fragment currentFragment = getSupportFragmentManager().
                            findFragmentByTag(Integer.toString(item.getItemId()));

                    // Don't load the fragment if you're currently on this fragment
                    if (currentFragment == null) {
                        Fragment selectedFragment = null;

                        switch (item.getItemId()) {
                            case R.id.navigation_home:
                                selectedFragment = new HomeFragment();
                                break;
                            case R.id.navigation_leaderboard:
                                selectedFragment = new LeaderboardFragment();
                                break;
                            case R.id.navigation_diary:
                                selectedFragment = new DiaryFragment();
                                break;
                        }

                        if (selectedFragment != null) {
                            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                    selectedFragment, Integer.toString(item.getItemId())).commit();
                        }

                    }

                    return true;
                }
            };

    /* If user pressed back button on their phone and navigation side menu is open, then close the navigation side menu */
    /* Otherwise, if the user pressed back button on their phone and navigation side isn't open, then close the app */
    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void initializeActionBar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this,
                drawer,
                toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);

        drawer.addDrawerListener(toggle);
        toggle.syncState();
    }

    private void initializeNavigationView() {
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        TextView nameTextView = navigationView.getHeaderView(0).findViewById(R.id.name);
        String currentUserID = getSharedPreferences("userID", 0).getString("userID", "");

        try {
            ResultSet name = db.getName(currentUserID);
            String name_string = getString(R.string.Name, name.getString(1), name.getString(2));
            nameTextView.setText(name_string);
        } catch (Exception ex) {
            Context context = getApplicationContext();
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, ex.getMessage(), duration);
            toast.show();
        }

        bottomNav = findViewById(R.id.navigation_bar);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //initializing firebase authentication object
        FirebaseApp.initializeApp(this);
        mfirebaseAuth = FirebaseAuth.getInstance();
        drawer = findViewById(R.id.drawer_layout);

        initializeActionBar();
        initializeNavigationView();

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new HomeFragment()).commit();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.nav_logout) {
            mfirebaseAuth.signOut();
            finish();
            //starting login activity
            startActivity(new Intent(this, LoginActivity.class));

            return true;
        }

        Fragment currentFragment = getSupportFragmentManager().
                findFragmentByTag(Integer.toString(item.getItemId()));

        bottomNav.getMenu().setGroupCheckable(0, false, true);

        // Don't load the fragment if you're currently on this fragment
        if (currentFragment == null) {
            Fragment selectedFragment = null;

            switch (item.getItemId()) {
                case R.id.nav_settings:
                    selectedFragment = new SettingsFragment();
                    break;
                case R.id.nav_challenges:
                    selectedFragment = new ChallengesFragment();
                    break;
                case R.id.nav_profile:
                    selectedFragment = new ProfileFragment();
                    break;
            }

            if (selectedFragment != null) {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        selectedFragment, Integer.toString(item.getItemId())).commit();
            }
        }

        drawer.closeDrawer(GravityCompat.START);

        return true;
    }
}