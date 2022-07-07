package com.bohdandroid.test;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class MyFragmentPageAdapter extends FragmentPagerAdapter {

    public static int pos = 0;

    private List<Fragment> myFragments;
    private ArrayList<String> categories;
    private Context context;

    public MyFragmentPageAdapter(Context c, FragmentManager fragmentManager, List<Fragment> myFrags, ArrayList<String> cats) {
        super(fragmentManager);
        myFragments = myFrags;
        this.categories = cats;
        this.context = c;
    }

    @Override
    public int getCount() {
        return myFragments.size();
    }

    @Override
    public Fragment getItem(int position) {
        return myFragments.get(position);
    }

    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    public static void setPos(int pos) {
        MyFragmentPageAdapter.pos = pos;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        setPos(position);
        return categories.get(position);
    }
}

