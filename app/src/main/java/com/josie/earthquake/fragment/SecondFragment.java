package com.josie.earthquake.fragment;


import android.content.Intent;
import android.nfc.NfcEvent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.annotation.StringDef;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.josie.earthquake.R;
import com.josie.earthquake.activity.WebViewActivity;
import com.josie.earthquake.adapter.ListViewAdapter;
import com.josie.earthquake.model.QuakeInfo;
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
    private int id = 1;
    private View view;
    private ListView listView;
    private View footerView;
    private TextView footer;
    private ProgressBar progressBar;
    private SwipeRefreshLayout swipeRefreshLayout;
    private List<QuakeInfo> result;
    private boolean loadingMore = false;
    private int start;
    private int count;
    private String url;
    private String response;
    private Map<String, String> params;
    private ListViewAdapter listViewAdapter;
    private MyDialogFragment fragment;

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
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        initView();
        getData();
        initEvent();
    }

    private void initView() {
        listView = (ListView) getView().findViewById(R.id.dataList);
        swipeRefreshLayout = (SwipeRefreshLayout) getView().findViewById(R.id.pull_to_refresh);
        footerView = LayoutInflater.from(getContext()).inflate(R.layout.list_footer, null);
        footer = (TextView) footerView.findViewById(R.id.footer);
        progressBar = (ProgressBar) footerView.findViewById(R.id.progressBar);
    }

    private List<QuakeInfo> getData() {
        url = "http://192.168.1.122:8080/quake/getall?";
        result = new ArrayList<>();
        start = result.size();
        count = 6;
        params = new HashMap<>();
        params.put("id", Integer.toString(id));
        params.put("start", Integer.toString(start));
        params.put("count", Integer.toString(count));
        new Thread(getdataRunnable).start();
        loadingMore = true;
        return result;

    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    footerView.setVisibility(View.GONE);
                    Log.e("response", result.toString());
                    listViewAdapter = new ListViewAdapter(result, getActivity().getLayoutInflater(), getContext());
                    listView.setAdapter(listViewAdapter);
                    break;
                case 2:
                    fragment.show(getFragmentManager(), "test");
                    break;


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
                        result = null;
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
                if (result != null && result.size() != 0) {
                    if ((totalItemCount - visibleItemCount == firstVisibleItem) && loadingMore && (listViewAdapter != null)) {
                        listView.addFooterView(footerView);
                        getMoreData();
                        listViewAdapter.notifyDataSetChanged();
                        listView.setSelection(visibleItemCount + 1); //设置选中项
                    }
                }

            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
//                listView.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        fragment = MyDialogFragment.newInstance(4, 5, true, true, true, true);
//                        Message message = new Message();
//                        message.what = 2;
//                        handler.sendMessage(message);
//                    }
//                }, 3000);
                Toast.makeText(getContext(), "test", Toast.LENGTH_LONG).show();

                return true;
            }
        });
    }

    private void getMoreData() {
        start = result.size();
        params = new HashMap<>();
        params.put("id", Integer.toString(id));
        params.put("start", Integer.toString(start));
        params.put("count", Integer.toString(count));
        new Thread(getdataRunnable).start();
    }

    Runnable getdataRunnable = new Runnable() {
        @Override
        public void run() {
            HttpClientUtils httpClientUtils = new HttpClientUtils();
            try {
                response = httpClientUtils.doPost(url, params);
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
        JSONObject jsonObject = new JSONObject(response);
        JSONArray datas = jsonObject.getJSONArray("data");
        if (datas.length() < count || datas.length() == 0) {
            loadingMore = false;
        }
        for (int i = 0; i < datas.length(); i++) {
            JSONObject jsonObject1 = (JSONObject) datas.get(i);
            QuakeInfo quakeInfo = new QuakeInfo();
            quakeInfo.setId(jsonObject1.getInt("id"));
            quakeInfo.setTitle(jsonObject1.getString("title"));
            quakeInfo.setDescription(jsonObject1.getString("description"));
            quakeInfo.setType(jsonObject1.getString("type"));
            quakeInfo.setManager(jsonObject1.getInt("manager"));
            quakeInfo.setStatus(jsonObject1.getInt("status"));
            quakeInfo.setJumpTo(jsonObject1.getString("jumpTo"));
            String createTime = jsonObject1.get("createTime").toString();
            quakeInfo.setCreateTime(createTime);
            String publishTime = jsonObject1.get("publishTime").toString();
            quakeInfo.setPublishTime(publishTime);
            String verifyTime = jsonObject1.get("verifyTime").toString();
            quakeInfo.setVerifyTime(verifyTime);
            result.add(quakeInfo);
        }

    }

//    private class DialogAynTask extends AsyncTask {
//        MyDialogFragment fragment;
//
//        @Override
//        protected Object doInBackground(Object[] params) {
//            return params[0];
//        }
//
//        @Override
//        protected void onPostExecute(Object o) {
////            fragment = MyDialogFragment.newInstance(4, 5, true, true, true, true);
////            fragment.show(getFragmentManager(), o.toString());
//        }
//    }
}
