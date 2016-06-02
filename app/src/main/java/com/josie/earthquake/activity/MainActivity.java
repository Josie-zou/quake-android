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
        imageButton2 = (ImageButton) this.findViewById(R.id.chart);
        imageButton3 = (ImageButton) this.findViewById(R.id.shangchuan);
        imageButton4 = (ImageButton) this.findViewById(R.id.me);

        layoutInflater = LayoutInflater.from(this);
        List<Fragment> fragments = new ArrayList<>();
        Fragment secondFragment = SecondFragment.instance();
        secondFragment.setArguments(bundle);
        Fragment fourthFragment = FourthFragment.instance();
        fourthFragment.setArguments(bundle);
        Fragment thirdFragment = ThirdFragment.instance();
        thirdFragment.setArguments(bundle);

        fragments.add(secondFragment);
        fragments.add(fourthFragment);
        fragments.add(thirdFragment);


        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager(), fragments);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(0);
        viewPager.setOffscreenPageLimit(3);
        imageButton2.setImageResource(R.drawable.main_press2);

        imageButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetResource();
                viewPager.setCurrentItem(0);

            }
        });
        imageButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetResource();
                viewPager.setCurrentItem(1);

            }
        });
        imageButton4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetResource();
                viewPager.setCurrentItem(2);
            }
        });

    }

    private void resetResource() {
        imageButton2.setImageResource(R.drawable.main2);
        imageButton3.setImageResource(R.drawable.chart);
        imageButton4.setImageResource(R.drawable.setting);
    }

    private void initEvents() {
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        resetResource();
                        imageButton2.setImageResource(R.drawable.main_press2);
                        break;
                    case 1:
                        resetResource();
                        imageButton3.setImageResource(R.drawable.chart_press);
                        break;
                    case 2:
                        resetResource();
                        imageButton4.setImageResource(R.drawable.setting_press);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void initData() {

    }


}


