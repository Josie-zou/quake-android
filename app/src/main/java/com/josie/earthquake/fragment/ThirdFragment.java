package com.josie.earthquake.fragment;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.view.menu.MenuPopupHelper;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.gitonway.lee.niftymodaldialogeffects.lib.NiftyDialogBuilder;
import com.josie.earthquake.R;
import com.josie.earthquake.activity.AboutActivity;
import com.josie.earthquake.activity.ChangePasswordActivity;
import com.josie.earthquake.activity.FilterRuleActivity;
import com.josie.earthquake.activity.LoginActivity;
import com.josie.earthquake.activity.MeDataActivity;
import com.josie.earthquake.activity.UserManageActivity;
import com.josie.earthquake.activity.WebViewActivity;
import com.josie.earthquake.activity.WhiteListActivity;
import com.josie.earthquake.model.User;
import com.josie.earthquake.utils.HttpClientUtils;
import com.josie.earthquake.utils.UrlUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        User user = (User) bundle.getSerializable("user");
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
                startActivity(intent);
            }
        });

        whiteListManage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), WhiteListActivity.class);
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

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final NiftyDialogBuilder dialogBuilder = NiftyDialogBuilder.getInstance(getContext());
                dialogBuilder.withTitle("Earthquake Eye")
                        .withMessage("Are you sure?")
                        .withButton1Text("OK")
                        .withButton2Text("cancel")
                        .setButton1Click(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                logoutUser();
                                dialogBuilder.dismiss();
                            }
                        })
                        .setButton2Click(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialogBuilder.dismiss();
                            }
                        }).show();
            }
        });
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    break;
                case 2:
                    Bundle bundle = msg.getData();
                    String data = bundle.getString("data");
                    Toast.makeText(getContext(), data, Toast.LENGTH_LONG).show();
                    break;
            }
        }
    };

    private void logoutUser() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Map<String, String> params = new HashMap<String, String>();
                try {
                    String response = HttpClientUtils.doPost(UrlUtils.LogoutUrl, params);
                    JSONObject jsonObject = new JSONObject(response);
                    int code = jsonObject.getInt("code");
                    if (code == 0) {
                        handler.sendEmptyMessage(1);
                    } else {
                        String data = jsonObject.getString("msg");
                        Bundle bundle = new Bundle();
                        bundle.putString("data", data);
                        Message message = new Message();
                        message.what = 2;
                        message.setData(bundle);
                        handler.sendMessage(message);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).start();
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
