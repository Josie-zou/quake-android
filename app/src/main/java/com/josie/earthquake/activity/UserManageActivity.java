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
    private List<WhiteList> result;
    private String url;
    private String id;
    private String response;
    private Map<String, String> params;
    private FilterRuleAdapter filterRuleAdapter;

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

    //TODO
//    Handler handler = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//            switch (msg.what) {
//                case 1:
//                    filterRuleAdapter = new FilterRuleAdapter(UserManageActivity.this, result, getLayoutInflater(), getFragmentManager());
//                    listView.setAdapter(filterRuleAdapter);
//                    break;
//            }
//        }
//    };

    private void initView() {
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.userManage_refresh);
        listView = (ListView) findViewById(R.id.userManage_listview);
        toolbar = (Toolbar) findViewById(R.id.userManage_toolbar);
        toolbar.inflateMenu(R.menu.menu_main);
        toolbar.setTitle("Quake Eye");
        toolbar.setTitleTextColor(Color.WHITE);

    }

    private void initData() {
        url = "http://192.168.1.122:8080/api/whitelist/getAll?";
        params = new HashMap<>();
        params.put("id", id);
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
                        result = new ArrayList<WhiteList>();
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
                parseResponse();
                //TODO
//                handler.sendEmptyMessage(1);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    private void parseResponse() throws JSONException {
        result = new ArrayList<>();
        JSONObject jsonObject = new JSONObject(response);
        int code = jsonObject.getInt("code");
        if (code == 0) {
            JSONArray datas = jsonObject.getJSONArray("data");
            for (int i = 0; i < datas.length(); i++) {
                WhiteList whiteList = new WhiteList();
                JSONObject jsonObject1 = (JSONObject) datas.get(i);
                whiteList.setId(jsonObject1.getInt("id"));
                whiteList.setUrl(jsonObject1.getString("url"));
                whiteList.setUsername(jsonObject1.getString("username"));
                result.add(whiteList);
            }
        }

    }
}
