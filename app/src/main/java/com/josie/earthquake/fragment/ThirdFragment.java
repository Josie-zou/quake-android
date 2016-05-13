package com.josie.earthquake.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.josie.earthquake.R;
import com.josie.earthquake.activity.AboutActivity;
import com.josie.earthquake.activity.ChangePasswordActivity;
import com.josie.earthquake.activity.MeDataActivity;

/**
 * Created by Josie on 16/5/4.
 */
public class ThirdFragment extends Fragment {
    private View view;
    private RelativeLayout relativeLayout;
    private Button changeData;
    private Button changePassword;
    private Button userManage;
    private Button logout;

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
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        initView();
        initData();
        initEvent();
    }

    private void initView() {
        changeData = (Button) getView().findViewById(R.id.changeData);
        changePassword = (Button) getView().findViewById(R.id.changePassword);
        userManage = (Button) getView().findViewById(R.id.userManage);
        logout = (Button) getView().findViewById(R.id.logout);
    }

    private void initData() {

    }

    private void initEvent() {
        changeData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MeDataActivity.class);
                startActivity(intent);
            }
        });

        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ChangePasswordActivity.class);
                startActivity(intent);
            }
        });

        userManage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AboutActivity.class);
            }
        });
    }
}
