package com.josie.earthquake.fragment;


import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.josie.earthquake.R;
import com.josie.earthquake.adapter.ChartDataAdapter;
import com.josie.earthquake.listItem.BarChartItem;
import com.josie.earthquake.listItem.ChartItem;
import com.josie.earthquake.listItem.LineChartItem;
import com.josie.earthquake.listItem.PieChartItem;
import com.josie.earthquake.utils.HttpClientUtils;
import com.josie.earthquake.utils.UrlUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Josie on 16/5/4.
 */
public class FourthFragment extends Fragment {

    private int itemCount = 12;
    private ListView listView;
    private ArrayList<ChartItem> list;
    private ArrayList<Entry> lineValues;
    private ArrayList<BarEntry> barValues;
    private ArrayList<String> lineKeys;
    private ArrayList<String> pieKeys1;
    private ArrayList<Entry> pieValues1;
    private ArrayList<String> pieKeys2;
    private ArrayList<Entry> pieValues2;
    private LineData lineData;
    private BarData barData;
    private PieData pieData1;
    private PieData pieData2;
    private View view;
    private Toolbar toolbar;

    public static FourthFragment instance() {
        FourthFragment view = new FourthFragment();
        return view;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.show_view, container, false);
        }
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        initView();
        setHasOptionsMenu(true);
        new Thread(getLineData).start();
        new Thread(getPieData1).start();
        new Thread(getPieData2).start();
//        initEvent();
    }

    private void initView() {
        toolbar = (Toolbar) getView().findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.menu_main);
        toolbar.setTitle("Quake Eye");
        toolbar.setTitleTextColor(Color.WHITE);

        listView = (ListView) getView().findViewById(R.id.listView);
        list = new ArrayList<ChartItem>();
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    lineData = generateDataLine();
                    barData = generateDataBar();
                    list.add(new LineChartItem(lineData, getContext()));
                    list.add(new BarChartItem(barData, getContext()));
                    break;
                case 2:
                    pieData1 = generateDataPie("信息来源分布", pieValues1, pieKeys1);
                    list.add(new PieChartItem(pieData1, getContext()));
                    break;
                case 3:
                    pieData2 = generateDataPie("信息采集关键字分布", pieValues2, pieKeys2);
                    list.add(new PieChartItem(pieData2, getContext()));
                    ChartDataAdapter chartDataAdapter = new ChartDataAdapter(getContext(), list);
                    listView.setAdapter(chartDataAdapter);
                    break;
                case 5:
                    Bundle bundle = msg.getData();
                    String data = bundle.getString("data");
                    Toast.makeText(getContext(), data, Toast.LENGTH_LONG).show();
                    break;

            }
        }
    };

    /**
     * generates a random ChartData object with just one DataSet
     *
     * @return
     */
    private LineData generateDataLine() {

        ArrayList<Entry> e1 = new ArrayList<Entry>();

        for (int i = 0; i < 12; i++) {
            e1.add(new Entry((int) (Math.random() * 65) + 40, i));
        }

        LineDataSet d1 = new LineDataSet(lineValues, "近期数据采集折线图");
        d1.setLineWidth(4.0f);
        d1.setCircleRadius(5.5f);
        d1.setColor(getResources().getColor(R.color.colorBlue));
        d1.setCircleColor(getResources().getColor(R.color.colorBlue));
        d1.setHighLightColor(Color.rgb(244, 117, 117));
        d1.setValueTextColor(Color.BLUE);
        d1.setValueTextSize(15f);
        d1.setDrawValues(false);
        d1.setAxisDependency(YAxis.AxisDependency.LEFT);


        ArrayList<ILineDataSet> sets = new ArrayList<ILineDataSet>();
        sets.add(d1);
//        sets.add(d2);

        LineData cd = new LineData(lineKeys, sets);
        return cd;
    }

    /**
     * generates a random ChartData object with just one DataSet
     *
     * @return
     */
    private BarData generateDataBar() {

        ArrayList<BarEntry> entries = new ArrayList<BarEntry>();

        for (int i = 0; i < 12; i++) {
            entries.add(new BarEntry((int) (Math.random() * 70) + 30, i));
        }

        BarDataSet d = new BarDataSet(barValues, "近期数据采集条形图");
        d.setBarSpacePercent(20f);
        d.setColors(ColorTemplate.VORDIPLOM_COLORS);
        d.setHighLightAlpha(255);

        BarData cd = new BarData(lineKeys, d);
        return cd;
    }

    /**
     * generates a random ChartData object with just one DataSet
     *
     * @return
     */
    private PieData generateDataPie(String title, ArrayList<Entry> pieValues, ArrayList<String> pieKeys) {

        PieDataSet d = new PieDataSet(pieValues, title);

        // space between slices
        d.setSliceSpace(2f);
        d.setColors(ColorTemplate.VORDIPLOM_COLORS);

        PieData cd = new PieData(pieKeys, d);
        return cd;
    }

    Runnable getLineData = new Runnable() {
        @Override
        public void run() {
            Map<String, String> params = new HashMap<>();
            try {
                String response = HttpClientUtils.doPost(UrlUtils.GetQuakeByDateURL, params);
                JSONObject jsonObject = new JSONObject(response);
                int code = jsonObject.getInt("code");
                if (code == 0) {
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    lineKeys = new ArrayList<>();
                    lineValues = new ArrayList<>();
                    barValues = new ArrayList<>();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject1 = (JSONObject) jsonArray.get(i);
                        Integer count = Integer.valueOf(jsonObject1.getString("count"));
                        Entry entry = new Entry(count, i);
                        barValues.add(new BarEntry(count, i));
                        lineValues.add(entry);
                        lineKeys.add(jsonObject1.getString("date"));
                    }
                    handler.sendEmptyMessage(1);
                } else {
                    String data = jsonObject.getString("msg");
                    Bundle bundle = new Bundle();
                    bundle.putString("data", data);
                    Message message = new Message();
                    message.what = 5;
                    message.setData(bundle);
                    handler.sendMessage(message);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    Runnable getPieData1 = new Runnable() {
        @Override
        public void run() {
            Map<String, String> params = new HashMap<>();
            try {
                String response = HttpClientUtils.doPost(UrlUtils.GetQuakeByTypeUrl, params);
                JSONObject jsonObject = new JSONObject(response);
                int code = jsonObject.getInt("code");
                if (code == 0) {
                    pieValues1 = new ArrayList<>();
                    pieKeys1 = new ArrayList<>();
                    parsePieData(jsonObject, "type", pieValues1, pieKeys1);
                    handler.sendEmptyMessage(2);
                } else {
                    String data = jsonObject.getString("msg");
                    Bundle bundle = new Bundle();
                    bundle.putString("data", data);
                    Message message = new Message();
                    message.what = 5;
                    message.setData(bundle);
                    handler.sendMessage(message);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    Runnable getPieData2 = new Runnable() {
        @Override
        public void run() {
            Map<String, String> params = new HashMap<>();
            try {
                String response = HttpClientUtils.doPost(UrlUtils.GetQuakeByKeywordsUrl, params);
                JSONObject jsonObject = new JSONObject(response);
                int code = jsonObject.getInt("code");
                if (code == 0) {
                    pieValues2 = new ArrayList<>();
                    pieKeys2 = new ArrayList<>();
                    parsePieData(jsonObject, "keywords", pieValues2, pieKeys2);
                    handler.sendEmptyMessage(3);
                } else {
                    String data = jsonObject.getString("msg");
                    Bundle bundle = new Bundle();
                    bundle.putString("data", data);
                    Message message = new Message();
                    message.what = 5;
                    message.setData(bundle);
                    handler.sendMessage(message);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    private void parsePieData(
            JSONObject jsonObject,
            String key,
            ArrayList<Entry> pieValues,
            ArrayList<String> pieKeys) throws JSONException {
        JSONArray jsonArray = jsonObject.getJSONArray("data");
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject1 = (JSONObject) jsonArray.get(i);
            Integer count = Integer.valueOf(jsonObject1.getString("count"));
            Entry entry = new Entry(count, i);
            pieValues.add(entry);
            String keyword = jsonObject1.getString(key);
            pieKeys.add(keyword);
        }
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
