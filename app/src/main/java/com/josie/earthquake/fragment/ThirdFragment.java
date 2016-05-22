package com.josie.earthquake.fragment;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.josie.earthquake.R;
import com.josie.earthquake.activity.AboutActivity;
import com.josie.earthquake.activity.ChangePasswordActivity;
import com.josie.earthquake.activity.FilterRuleActivity;
import com.josie.earthquake.activity.MeDataActivity;
import com.josie.earthquake.activity.UserManageActivity;
import com.josie.earthquake.activity.WebViewActivity;

import java.util.List;

import co.lujun.androidtagview.TagContainerLayout;

/**
 * Created by Josie on 16/5/4.
 */
public class ThirdFragment extends Fragment {
    private View view;
    private RelativeLayout relativeLayout;
    private Button changeData;
    private Button changePassword;
    private Button userManage;
    private Button whiteListManage;
    private Button filterRule;
    private Button logout;
    private Bundle bundle;
    private Toolbar toolbar;
    private TagContainerLayout tagContainerLayout;
    private List<String> tags;

    public static ThirdFragment instance() {
        ThirdFragment view = new ThirdFragment();
        return view;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.setting, container, false);
        }
        bundle = getArguments();
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        initView();
        initData();
        initEvent();
        setHasOptionsMenu(true);
    }

    private void initView() {
        changeData = (Button) getView().findViewById(R.id.changeData);
        changePassword = (Button) getView().findViewById(R.id.changePassword);
        whiteListManage = (Button) getView().findViewById(R.id.whiteListManager);
        filterRule = (Button) getView().findViewById(R.id.filterRule);
        userManage = (Button) getView().findViewById(R.id.userManage);
        logout = (Button) getView().findViewById(R.id.logout);

        toolbar = (Toolbar) getView().findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.menu_main);
        toolbar.setTitle("Quake Eye");
        toolbar.setTitleTextColor(Color.WHITE);
    }

    private void initData() {

    }

    private void initEvent() {
        changeData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MeDataActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ChangePasswordActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        userManage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), UserManageActivity.class);
                intent.putExtras(bundle);
            }
        });

        whiteListManage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), WebViewActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        filterRule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), FilterRuleActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
        super.onCreateOptionsMenu(menu, inflater);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

        }
        return super.onOptionsItemSelected(item);
    }
}
