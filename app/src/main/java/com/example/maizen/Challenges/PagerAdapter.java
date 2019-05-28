package com.example.maizen.Challenges;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class PagerAdapter extends FragmentStatePagerAdapter {
    private int numTabs;

    public PagerAdapter(FragmentManager fm, int numberOfTabs) {
        super(fm);
        numTabs = numberOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                UserChallengesFragment userChallenge = new UserChallengesFragment();
                return userChallenge;
            case 1:
                CreateChallengeFragment createChallengeFragment = new CreateChallengeFragment();
                return createChallengeFragment;
            case 2:
                MaizenChallengesFragment maizenchallenge = new MaizenChallengesFragment();
                return maizenchallenge;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return numTabs;
    }


}