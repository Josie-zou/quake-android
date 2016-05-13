package com.josie.earthquake.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.josie.earthquake.fragment.FirstFragment;
import com.josie.earthquake.fragment.FourthFragment;
import com.josie.earthquake.fragment.SecondFragment;
import com.josie.earthquake.fragment.ThirdFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Josie on 16/5/5.
 */
public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    private List<Fragment> fragments;
    private int size;
    Context mcContext;

    public ViewPagerAdapter(FragmentManager fm, List<Fragment> list) {
        super(fm);
        fragments = list;
        size = list.size();
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
//        super.destroyItem(container, position, object);
    }

    @Override
    public Object instantiateItem(View collection, int position) {
        return fragments.get(position % size);
    }

    @Override
    public Fragment getItem(int position) {
        Log.e("positioln" + position, Integer.toString(position));
//        if (position == 0){
//            return new FirstFragment();
//        } else if (position ==1){
//            return new SecondFragment();
//        } else if (position ==2){
//            return new ThirdFragment();
//        } else if (position == 3){
//            return new FourthFragment();
//        }
//        return null;
        return fragments.get(position);
    }

}
