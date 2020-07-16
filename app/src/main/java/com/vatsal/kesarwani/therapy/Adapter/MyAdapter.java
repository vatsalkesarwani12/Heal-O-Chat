package com.vatsal.kesarwani.therapy.Adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.vatsal.kesarwani.therapy.Fragment.CureFragment;
import com.vatsal.kesarwani.therapy.Fragment.PostFragment;
import com.vatsal.kesarwani.therapy.Fragment.ChatFragment;

public class MyAdapter extends FragmentPagerAdapter {

    private Context myContext;
    int totalTabs;

    public MyAdapter(@NonNull FragmentManager fm, Context myContext, int totalTabs) {
        super(fm);
        this.myContext = myContext;
        this.totalTabs = totalTabs;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                PostFragment postFragment = new PostFragment();
                return postFragment;
            case 1:
                CureFragment cureFragment = new CureFragment();
                return cureFragment;
            case 2:
                ChatFragment chatFragment = new ChatFragment();
                return chatFragment;
            default:return null;
        }
    }

    @Override
    public int getCount() {
        return totalTabs;
    }
}
