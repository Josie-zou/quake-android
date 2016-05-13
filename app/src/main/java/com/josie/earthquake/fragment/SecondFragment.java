package com.josie.earthquake.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringDef;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.josie.earthquake.R;
import com.josie.earthquake.activity.WebViewActivity;
import com.yalantis.phoenix.PullToRefreshView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import jp.wasabeef.blurry.Blurry;

/**
 * Created by Josie on 16/5/4.
 */
public class SecondFragment extends android.support.v4.app.Fragment {
    private View view;
    private ListView listView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private SimpleAdapter simpleAdapter;
    private List<Map<String, Object>> result;
    private boolean loadingMore = true;

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
        initData();
        initEvent();
    }

    private void initView() {
        listView = (ListView) getView().findViewById(R.id.dataList);
        swipeRefreshLayout = (SwipeRefreshLayout) getView().findViewById(R.id.pull_to_refresh);
    }

    private void initData() {
        simpleAdapter = new SimpleAdapter(getContext(), getData(), R.layout.record_list_item, new String[]{"image", "type", "title", "detail"}, new int[]{R.id.dataImage, R.id.dataType, R.id.dataTitle, R.id.dataDetail});
        listView.setAdapter(simpleAdapter);
    }

    private List<Map<String, Object>> getData() {
        result = new ArrayList<Map<String, Object>>();
        Map<String, Object> data1 = new HashMap<>();
        data1.put("image", R.drawable.voc);
        data1.put("type", "微博");
        data1.put("title", "title1");
        data1.put("detail", "detail1");

        Map<String, Object> data2 = new HashMap<>();
        data2.put("image", R.drawable.voc);
        data2.put("type", "微博");
        data2.put("title", "title1");
        data2.put("detail", "detail1");

        Map<String, Object> data3 = new HashMap<>();
        data3.put("image", R.drawable.voc);
        data3.put("type", "微博");
        data3.put("title", "title1");
        data3.put("detail", "detail1");

        Map<String, Object> data4 = new HashMap<>();
        data4.put("image", R.drawable.voc);
        data4.put("type", "微博");
        data4.put("title", "title1");
        data4.put("detail", "detail1");

        Map<String, Object> data5 = new HashMap<>();
        data5.put("image", R.drawable.voc);
        data5.put("type", "微博");
        data5.put("title", "title1");
        data5.put("detail", "detail1");

        Map<String, Object> data6 = new HashMap<>();
        data6.put("image", R.drawable.voc);
        data6.put("type", "微博");
        data6.put("title", "title1");
        data6.put("detail", "detail1");

        result.add(data1);
        result.add(data2);
        result.add(data3);
        result.add(data4);
        result.add(data5);
        result.add(data6);
        return result;
    }

    private void initEvent() {


        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }, 3000);
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle bundle = new Bundle();
                Intent intent = new Intent(getActivity(), WebViewActivity.class);
                startActivity(intent);
            }
        });

    }
}
