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
import com.josie.earthquake.adapter.WhiteListAdapter;
import com.josie.earthquake.model.FilterRule;
import com.josie.earthquake.model.User;
import com.josie.earthquake.model.WhiteList;
import com.josie.earthquake.utils.HttpClientUtils;
import com.josie.earthquake.utils.UrlUtils;
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
public class FilterRuleActivity extends Activity {

    private ListView listView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private FloatingActionButton add;
    private Toolbar toolbar;
    private List<FilterRule> result;
    private String url;
    private User user;
    private String id;
    private String response;
    private Map<String, String> params;
    private FilterRuleAdapter filterRuleAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.white_list);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        User user = (User) bundle.getSerializable("user");
        id = String.valueOf(user.getId());
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
                    filterRuleAdapter = new FilterRuleAdapter(FilterRuleActivity.this, result, getLayoutInflater(), getFragmentManager());
                    listView.setAdapter(filterRuleAdapter);
                    break;
                case 2:
                    Toast.makeText(FilterRuleActivity.this, "添加rule成功", Toast.LENGTH_LONG).show();
                    initData();
                    filterRuleAdapter.notifyDataSetChanged();
                    break;
                case 3:
                    Bundle bundle = msg.getData();
                    String data = bundle.getString("data");
                    Toast.makeText(FilterRuleActivity.this, data, Toast.LENGTH_LONG).show();
                    break;
            }
        }
    };

    private void initView() {
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.whiteRefresh);
        listView = (ListView) findViewById(R.id.ruleList);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.menu_main);
        toolbar.setTitle("Quake Eye");
        toolbar.setTitleTextColor(Color.WHITE);
        add = (FloatingActionButton) findViewById(R.id.add);

    }

    private void initData() {
        url = UrlUtils.GetAllFilterUrl;
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
                        result = new ArrayList<FilterRule>();
                        initData();
                    }
                }, 3000);
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog builder = new AlertDialog.Builder(FilterRuleActivity.this).create();
                builder.show();
                Window window = builder.getWindow();
                window.clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
                window.setContentView(R.layout.my_dialog);
                LayoutInflater layoutInflater = getLayoutInflater();
                View view = layoutInflater.inflate(R.layout.my_dialog, null);
                TextView textView = (TextView) window.findViewById(R.id.dialog_title);
                final EditText editText = (EditText) window.findViewById(R.id.dialog_edittext);
                Button sure = (Button) window.findViewById(R.id.dialog_sure);
                Button cancel = (Button) window.findViewById(R.id.dialog_cancel);
                editText.setFocusable(true);
                sure.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        addFilterRule(editText.getText().toString().trim());
                        builder.dismiss();
                    }
                });
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        builder.dismiss();
                    }
                });
            }
        });
    }

    private void addFilterRule(final String rule) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String url = UrlUtils.AddFilterUrl;
                    Map<String, String> params = new HashMap<>();
                    params.put("rule", rule);
                    String response = HttpClientUtils.doPost(url, params);
                    JSONObject jsonObject = new JSONObject(response);
                    int code = jsonObject.getInt("code");
                    if (code == 0) {
                        handler.sendEmptyMessage(2);
                    } else {
                        String data = jsonObject.getString("msg");
                        Message message = new Message();
                        Bundle bundle = new Bundle();
                        bundle.putString("data", data);
                        message.setData(bundle);
                        message.what = 3;
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

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            try {
                response = "";
                response = HttpClientUtils.doPost(url, params);
                parseResponse();
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
                FilterRule filterRule = new FilterRule();
                JSONObject jsonObject1 = (JSONObject) datas.get(i);
                filterRule.setId(jsonObject1.getInt("id"));
                filterRule.setRule(jsonObject1.getString("rule"));
                filterRule.setUsername(jsonObject1.getString("username"));
                result.add(filterRule);
            }
            handler.sendEmptyMessage(1);
        } else {
            String data = jsonObject.getString("msg");
            Message message = new Message();
            Bundle bundle = new Bundle();
            bundle.putString("data", data);
            message.setData(bundle);
            message.what = 3;
            handler.sendMessage(message);
        }

    }
}
