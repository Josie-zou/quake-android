package com.josie.earthquake.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DialogTitle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.github.alexkolpa.fabtoolbar.FabToolbar;
import com.gitonway.lee.niftymodaldialogeffects.lib.NiftyDialogBuilder;
import com.josie.earthquake.R;
import com.josie.earthquake.adapter.WhiteListAdapter;
import com.josie.earthquake.fragment.MyDialogFragment;
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
public class WhiteListActivity extends Activity {
    private ListView listView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private FloatingActionButton add;
    private Toolbar toolbar;
    private List<WhiteList> result;
    private String url;
    private String id;
    private String response;
    Map<String, String> params;
    private WhiteListAdapter whiteListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.white_list);
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
                    whiteListAdapter = new WhiteListAdapter(WhiteListActivity.this, result, getLayoutInflater(), getFragmentManager());
                    listView.setAdapter(whiteListAdapter);
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

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final NiftyDialogBuilder dialogBuilder1 = NiftyDialogBuilder.getInstance(WhiteListActivity.this);
                dialogBuilder1.withTitle("Earthquake Eye")
                        .withDialogColor("#6699CC")
                        .withMessageColor("#FFFFFF")
                        .withButton1Text("OK")
                        .withButton2Text("cancel")
                        .setButton1Click(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Toast.makeText(WhiteListActivity.this, "ok", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setButton2Click(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialogBuilder1.dismiss();
                            }
                        }).show();
            }
        });
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            HttpClientUtils httpClientUtils = new HttpClientUtils();
            try {
                response = httpClientUtils.doPost(url, params);
                parseResponse();
                handler.sendEmptyMessage(1);
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
