package com.josie.earthquake.activity;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;


import com.josie.earthquake.R;

import com.josie.earthquake.adapter.ViewPagerAdapter;
import com.josie.earthquake.fragment.FirstFragment;
import com.josie.earthquake.fragment.FourthFragment;
import com.josie.earthquake.fragment.SecondFragment;
import com.josie.earthquake.fragment.ThirdFragment;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.PushAgent;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends FragmentActivity {

    private PushAgent pushAgent;
    private LinearLayout linearLayout;
    private LayoutInflater layoutInflater;
    private Bundle bundle;


    private ImageButton imageButton1 = null;
    private ImageButton imageButton2 = null;
    private ImageButton imageButton3 = null;
    private ImageButton imageButton4 = null;


    public ViewPager viewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pushAgent = PushAgent.getInstance(this);
        pushAgent.onAppStart();
        pushAgent.enable(new IUmengRegisterCallback() {
            @Override
            public void onRegistered(final String s) {
                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        Log.e("device token", s);
                    }
                });
            }
        });
        Intent intent = getIntent();
        bundle = intent.getExtras();
        initView();
        initEvents();
        initData();

    }

    private void initView() {
        viewPager = (ViewPager) this.findViewById(R.id.view_pager);
//        imageButton1 = (ImageButton) this.findViewById(R.id.main);
        imageButton2 = (ImageButton) this.findViewById(R.id.chart);
        imageButton3 = (ImageButton) this.findViewById(R.id.shangchuan);
        imageButton4 = (ImageButton) this.findViewById(R.id.me);

        layoutInflater = LayoutInflater.from(this);
        List<Fragment> fragments = new ArrayList<>();
//        Fragment firstFragment = FirstFragment.instance();
        Fragment secondFragment = SecondFragment.instance();
        secondFragment.setArguments(bundle);
        Fragment fourthFragment = FourthFragment.instance();
        fourthFragment.setArguments(bundle);
        Fragment thirdFragment = ThirdFragment.instance();
        thirdFragment.setArguments(bundle);

//        fragments.add(firstFragment);
        fragments.add(secondFragment);
        fragments.add(fourthFragment);
        fragments.add(thirdFragment);


        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager(), fragments);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(0);
        viewPager.setOffscreenPageLimit(3);

//        imageButton1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                viewPager.setCurrentItem(0);
//            }
//        });
        imageButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(0);
            }
        });
        imageButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(1);
            }
        });
        imageButton4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(2);
            }
        });

    }

    private void initEvents() {

    }

    private void initData() {

    }

//    class PageClickerListener implements View.OnClickListener {
//
//        private int index;
//
//        public PageClickerListener(int position) {
//            this.index = position;
//        }
//
//        @Override
//        public void onClick(View v) {
//            // TODO Auto-generated method stub
//            viewPager.setCurrentItem(index);
////            mCurrentIndex = index;
//        }
//
//        class ViewPageChangeListener implements ViewPager.OnPageChangeListener {
//
//            //            int offsetOne = mOffset * 2 + mCursorImgWidth;
////            int offsetTwo = mOffset * 3 + mCursorImgWidth;
////            int offsetThree = mOffset * 4 + mCursorImgWidth;
//            Animation animation = null;
//
//            @Override
//            public void onPageSelected(int arg0) {
//                // TODO Auto-generated method stub
//
//            }
//
//            @Override
//            public void onPageScrollStateChanged(int arg0) {
//                // TODO Auto-generated method stub
//
//            }
//
//            @Override
//            public void onPageScrolled(int arg0, float arg1, int arg2) {
//                // TODO Auto-generated method stub
//
//            }
//
//        }

//    }


}


