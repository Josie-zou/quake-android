package com.josie.earthquake.fragment;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.annotation.StringDef;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.view.menu.MenuPopupHelper;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.gitonway.lee.niftymodaldialogeffects.lib.NiftyDialogBuilder;
import com.josie.earthquake.R;
import com.josie.earthquake.activity.WebViewActivity;
import com.josie.earthquake.adapter.ListViewAdapter;
import com.josie.earthquake.model.QuakeInfo;
import com.josie.earthquake.model.User;
import com.josie.earthquake.utils.HttpClientUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.wasabeef.blurry.Blurry;

/**
 * Created by Josie on 16/5/4.
 */
public class SecondFragment extends android.support.v4.app.Fragment {

    private int id;

    private View view;
    private ListView listView;
    private View footerView;
    private TextView footer;
    private ProgressBar progressBar;
    private SwipeRefreshLayout swipeRefreshLayout;
    private List<QuakeInfo> result;
    private List<QuakeInfo> currentResult;
    private boolean loadingMore = false;
    private boolean firstLoad = true;
    private int start;
    private int count;
    private String url;
    private String response;
    private Map<String, String> params;
    private ListViewAdapter listViewAdapter;
    private MyDialogFragment fragment;
    private Toolbar toolbar;
    private User user;

    public static SecondFragment instance() {
        SecondFragment view = new SecondFragment();
        return view;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null) {
                parent.removeView(view);
            }
            return view;
        }
        View rootView = null;
        rootView = inflater.inflate(R.layout.data_record, container, false);
        view = rootView;
        Bundle bundle1 = getArguments();
        user = (User) bundle1.getSerializable("user");
        id = user.getId();
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        initView();
        result = new ArrayList<>();
        firstLoad = true;
        getData();
        initEvent();
        setHasOptionsMenu(true);
    }

    private void initView() {
        listView = (ListView) getView().findViewById(R.id.dataList);
        swipeRefreshLayout = (SwipeRefreshLayout) getView().findViewById(R.id.pull_to_refresh);
        footerView = LayoutInflater.from(getContext()).inflate(R.layout.list_footer, null);
        footer = (TextView) footerView.findViewById(R.id.footer);
        progressBar = (ProgressBar) footerView.findViewById(R.id.progressBar);
        toolbar = (Toolbar) getView().findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.menu_main);
        toolbar.setTitle("Quake Eye");
        toolbar.setTitleTextColor(Color.WHITE);
    }

    private List<QuakeInfo> getData() {
        url = "http://192.168.1.122:8080/api/quake/getall?";
        start = result.size();
        count = 6;
        params = new HashMap<>();
        params.put("status", "0");
        params.put("start", Integer.toString(start));
        params.put("count", Integer.toString(count));
        new Thread(getFirstData).start();
        loadingMore = true;
        return result;

    }

    Runnable getFirstData = new Runnable() {
        @Override
        public void run() {
            if (firstLoad) {
                try {
                    response = HttpClientUtils.doPost(url, params);
                    parseResponse();
                    Message message = new Message();
                    message.what = 1;
                    handler.sendMessage(message);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                firstLoad = false;
            }
        }
    };

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    footerView.setVisibility(View.GONE);
                    listViewAdapter = new ListViewAdapter(result, getActivity().getLayoutInflater(), getContext());
                    listView.setAdapter(listViewAdapter);
                    break;
                case 2:
                    Toast.makeText(getContext(), "删除成功", Toast.LENGTH_LONG).show();
                    result = new ArrayList<>();
                    firstLoad = true;
                    getData();
                    break;
                case 3:
                    Toast.makeText(getContext(), "审核通过", Toast.LENGTH_LONG).show();
                    result = new ArrayList<>();
                    firstLoad = true;
                    getData();
                    break;
                case 4:
                    Bundle bundle = msg.getData();
                    String data = bundle.getString("data");
                    Toast.makeText(getContext(), data, Toast.LENGTH_LONG).show();
            }
        }
    };

    private void initEvent() {


        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                        result = new ArrayList<>();
                        firstLoad = true;
                        loadingMore = true;
                        getData();
                    }
                }, 3000);
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle bundle = new Bundle();
                bundle.putString("url", result.get(position).getJumpTo());
                Intent intent = new Intent(getActivity(), WebViewActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (!firstLoad && result != null && result.size() != 0) {
                    if ((totalItemCount - visibleItemCount == firstVisibleItem) && loadingMore && (listViewAdapter != null)) {
                        loadingMore = false;
                        listView.addFooterView(footerView);
                        getMoreData();
                        listViewAdapter.notifyDataSetChanged();
                        int count = listView.getCount();
                        Log.e("count", String.valueOf(count));
                        listView.setSelection(listView.getCount() - 1);
//                        listView.setSelectionAfterHeaderView();
//                        listView.setSelection(listView.getCount() - 1); //设置选中项
                    }
                }

            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                listView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (user.getPrivilege() != User.Privilege.Common.toInt() && result.get(position).getStatus().equals("未审核")) {
                            final NiftyDialogBuilder dialogBuilder = NiftyDialogBuilder.getInstance(getContext());
                            dialogBuilder.withTitle("Earthquake Eye")
                                    .withMessage("通过审核?")
                                    .withDialogColor("#6699CC")
                                    .withMessageColor("#FFFFFF")
                                    .withButton1Text("OK")
                                    .withButton2Text("cancel")
                                    .setButton1Click(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            verifyData(result.get(position).getId());
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
                        if (user.getPrivilege() != User.Privilege.Common.toInt() && result.get(position).getStatus().equals("已审核")) {
                            final NiftyDialogBuilder dialogBuilder = NiftyDialogBuilder.getInstance(getContext());
                            dialogBuilder.withTitle("Earthquake Eye")
                                    .withMessage("确定删除?")
                                    .withButton1Text("OK")
                                    .withButton2Text("cancel")
                                    .setButton1Click(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            deleteData(result.get(position).getId());
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
                    }
                }, 0);
                return true;
            }
        });
    }

    private void deleteData(final Integer id) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String verifyUrl = "http://192.168.1.122:8080/api/quake/examine/delete?";
                Map<String, String> params = new HashMap<String, String>();
                params.put("id", id.toString());
                try {
                    String response = HttpClientUtils.doPost(verifyUrl, params);
                    JSONObject jsonObject = new JSONObject(response);
                    int code = jsonObject.getInt("code");
                    if (code == 0) {
                        handler.sendEmptyMessage(2);
                    } else {
                        String msg = jsonObject.getString("msg");
                        Bundle bundle = new Bundle();
                        bundle.putString("data", msg);
                        Message message = new Message();
                        message.what = 4;
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

    private void verifyData(final Integer quakeId) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String verifyUrl = "http://192.168.1.122:8080/api/quake/examine/pass?";
                Map<String, String> params = new HashMap<String, String>();
                params.put("id", quakeId.toString());
                try {
                    String response = HttpClientUtils.doPost(verifyUrl, params);
                    JSONObject jsonObject = new JSONObject(response);
                    int code = jsonObject.getInt("code");
                    if (code == 0) {
                        handler.sendEmptyMessage(3);
                    } else {
                        String msg = jsonObject.getString("msg");
                        Bundle bundle = new Bundle();
                        bundle.putString("data", msg);
                        Message message = new Message();
                        message.what = 4;
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

    private void getMoreData() {
        start = result.size();
        params = new HashMap<>();
        params.put("status", "0");
        params.put("start", Integer.toString(start));
        params.put("count", Integer.toString(count));
        new Thread(getdataRunnable).start();
    }

    Runnable getdataRunnable = new Runnable() {
        @Override
        public void run() {
            try {
                response = HttpClientUtils.doPost(url, params);
                parseResponse();
                Message message = new Message();
                message.what = 1;
                handler.sendMessage(message);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    private void parseResponse() throws JSONException {
        currentResult = null;
        currentResult = new ArrayList<>();
        JSONObject jsonObject = new JSONObject(response);
        JSONArray datas = jsonObject.getJSONArray("data");
        if (datas == null || datas.length() < count || datas.length() == 0) {
            loadingMore = false;
        } else {
            loadingMore = true;
        }
        for (int i = 0; i < datas.length(); i++) {
            JSONObject jsonObject1 = (JSONObject) datas.get(i);
            QuakeInfo quakeInfo = new QuakeInfo();
            quakeInfo.setId(jsonObject1.getInt("id"));
            quakeInfo.setTitle(jsonObject1.getString("title"));
            quakeInfo.setDescription(jsonObject1.getString("description"));
            quakeInfo.setType(jsonObject1.getString("type"));
            quakeInfo.setManager(jsonObject1.getString("manager"));
            quakeInfo.setStatus(jsonObject1.getString("status"));
            quakeInfo.setJumpTo(jsonObject1.getString("jumpTo"));
            currentResult.add(quakeInfo);
        }
        result.addAll(currentResult);
        currentResult = null;
        response = null;
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
