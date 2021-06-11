package com.cranium.sushiteiapps.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.cranium.sushiteiapps.fragment.CommentFragment;
import com.cranium.sushiteiapps.fragment.HotMenuFragment;
import com.cranium.sushiteiapps.fragment.WaitingListFragment;


/**
 * Created by Dayona on 5/31/17.
 */
public class PagerAdapter extends FragmentStatePagerAdapter {

    int numOfTabs;

    public PagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.numOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        super.getPageTitle(position);

        switch (position) {
            case 0:
                HotMenuFragment hotMenuFragment = new HotMenuFragment();
                return hotMenuFragment;
            case 1:
                WaitingListFragment waitingListFragment = new WaitingListFragment();
                return waitingListFragment;
            case 2:
                CommentFragment commentFragment = new CommentFragment();
                return commentFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return numOfTabs;
    }


}
