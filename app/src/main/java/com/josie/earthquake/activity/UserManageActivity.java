package com.josie.earthquake.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.josie.earthquake.R;
import com.josie.earthquake.adapter.FilterRuleAdapter;
import com.josie.earthquake.adapter.UserManageAdapter;
import com.josie.earthquake.model.User;
import com.josie.earthquake.model.WhiteList;
import com.josie.earthquake.utils.HttpClientUtils;
import com.melnykov.fab.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Josie on 16/5/22.
 */
public class UserManageActivity extends Activity {

    private ListView listView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private Toolbar toolbar;
    private List<User> result;
    private String url;
    private String id;
    private String response;
    private Map<String, String> params;
    private UserManageAdapter userManageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_manage);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        id = bundle.getString("id");
        initView();
        initData();
        initEvent();
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    userManageAdapter = new UserManageAdapter(UserManageActivity.this, result, getLayoutInflater(), getFragmentManager());
                    listView.setAdapter(userManageAdapter);
                    break;
                case 2:
                    Bundle bundle = msg.getData();
                    String data = bundle.getString("data");
                    Toast.makeText(UserManageActivity.this, data, Toast.LENGTH_LONG).show();
                    break;
            }
        }
    };

    private void initView() {
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.userManage_refresh);
        listView = (ListView) findViewById(R.id.userManage_listview);
        toolbar = (Toolbar) findViewById(R.id.userManage_toolbar);
        toolbar.inflateMenu(R.menu.menu_main);
        toolbar.setTitle("Quake Eye");
        toolbar.setTitleTextColor(Color.WHITE);

    }

    private void initData() {
        url = "http://192.168.1.122:8080/api/user/getall?";
        params = new HashMap<>();
        new Thread(runnable).start();
    }

    private void initEvent() {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                        result = new ArrayList<User>();
                        initData();
                    }
                }, 3000);
            }
        });
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            try {
                response = HttpClientUtils.doPost(url, params);
                result = new ArrayList<>();
                JSONObject jsonObject = new JSONObject(response);
                int code = jsonObject.getInt("code");
                if (code == 0) {
                    parseResponse(jsonObject);
                    handler.sendEmptyMessage(1);
                } else {
                    String data = jsonObject.getString("msg");
                    Bundle bundle = new Bundle();
                    bundle.putString("data", data);
                    Message message = new Message();
                    message.setData(bundle);
                    message.what = 2;
                    handler.sendMessage(message);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    private void parseResponse(JSONObject jsonObject) throws JSONException {
        JSONArray datas = jsonObject.getJSONArray("data");
        for (int i = 0; i < datas.length(); i++) {
            User user = new User();
            JSONObject jsonObject1 = (JSONObject) datas.get(i);
            user.setId(jsonObject1.getInt("id"));
            user.setPhoneNumber(jsonObject1.getString("mailAdress"));
            user.setMailAdress(jsonObject1.getString("phoneNumber"));
            user.setUsername(jsonObject1.getString("username"));
            result.add(user);
        }
    }
}
