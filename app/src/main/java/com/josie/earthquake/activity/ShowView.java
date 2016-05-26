package com.josie.earthquake.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.ListView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Josie on 16/4/25.
 */
public class ShowView extends Activity {

    private int itemCount = 12;
    private ListView listView;
    private ArrayList<ChartItem> list;
    private ArrayList<Entry> lineValues;
    private ArrayList<BarEntry> barValues;
    private ArrayList<String> lineKeys;
    private ArrayList<String> pieKeys;
    private ArrayList<Entry> pieValues;
    private LineData lineData;
    private BarData barData;
    private PieData pieData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_view);
        listView = (ListView) findViewById(R.id.listView);
        list = new ArrayList<ChartItem>();
        new Thread(getLineData).start();
        new Thread(getPieData).start();

        for (int i = 0; i < 10; i++) {
            if (i % 3 == 0) {
//                list.add(new LineChartItem(generateDataLine(i + 1), getApplicationContext()));
            } else if (i % 3 == 1) {
//                list.add(new BarChartItem(generateDataBar(i + 1), getApplicationContext()));
            } else if (i % 3 == 2) {
//                list.add(new PieChartItem(generateDataPie(i + 1), getApplicationContext()));
            }
        }

//        ChartDataAdapter chartDataAdapter = new ChartDataAdapter(getApplicationContext(), list);
//        listView.setAdapter(chartDataAdapter);
//        initView();
//        initData();
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    lineData = generateDataLine();
                    barData = generateDataBar();
                    list.add(new LineChartItem(lineData, getApplicationContext()));
                    list.add(new BarChartItem(barData, getApplicationContext()));
                    break;
                case 2:
                    pieData = generateDataPie();
                    list.add(new PieChartItem(pieData, getApplicationContext()));
                    ChartDataAdapter chartDataAdapter = new ChartDataAdapter(getApplicationContext(), list);
                    listView.setAdapter(chartDataAdapter);
                    break;
                case 3:
                    Bundle bundle = msg.getData();
                    String data = bundle.getString("data");
                    Toast.makeText(ShowView.this, data, Toast.LENGTH_LONG).show();
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
        d1.setDrawValues(true);
        d1.setAxisDependency(YAxis.AxisDependency.LEFT);

//        ArrayList<Entry> e2 = new ArrayList<Entry>();
//
//        for (int i = 0; i < 12; i++) {
//            e2.add(new Entry((int) (Math.random() * 65) + 40, i));
//        }
//
//        LineDataSet d2 = new LineDataSet(e2, "New DataSet " + ", (2)");
//        d2.setLineWidth(4.0f);
//        d2.setCircleRadius(5.5f);
//        d2.setHighLightColor(Color.rgb(244, 117, 117));
//        d2.setColor(getResources().getColor(R.color.colorGreen));
//        d2.setCircleColor(getResources().getColor(R.color.colorGreen));
////        d2.setDrawValues(false);
//        d2.setValueTextColor(Color.GREEN);
//        d2.setValueTextSize(15f);
//        d2.setDrawValues(true);
//        d2.setAxisDependency(YAxis.AxisDependency.LEFT);


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
    private PieData generateDataPie() {

        ArrayList<Entry> entries = new ArrayList<Entry>();

        for (int i = 0; i < 4; i++) {
            entries.add(new Entry((int) (Math.random() * 70) + 40, i));
        }

        PieDataSet d = new PieDataSet(pieValues, "");

        // space between slices
        d.setSliceSpace(2f);
        d.setColors(ColorTemplate.VORDIPLOM_COLORS);

        PieData cd = new PieData(pieKeys, d);
        return cd;
    }

    private ArrayList<String> getQuarters() {

        ArrayList<String> q = new ArrayList<String>();
        q.add("1st Quarter");
        q.add("2nd Quarter");
        q.add("3rd Quarter");
        q.add("4th Quarter");

        return q;
    }

    private ArrayList<String> getMonths() {

        ArrayList<String> m = new ArrayList<String>();
        m.add("Jan");
        m.add("Feb");
        m.add("Mar");
        m.add("Apr");
        m.add("May");
        m.add("Jun");
        m.add("Jul");
        m.add("Aug");
        m.add("Sep");
        m.add("Okt");
        m.add("Nov");
        m.add("Dec");

        return m;
    }

    Runnable getLineData = new Runnable() {
        @Override
        public void run() {
            String url = "http://192.168.1.122:8080/api/quake/getByDate?";
            try {
                String response = HttpClientUtils.doPost(url, null);
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

    Runnable getPieData = new Runnable() {
        @Override
        public void run() {
            String url = "http://192.168.1.122:8080/api/quake/getByType?";
            try {
                String response = HttpClientUtils.doPost(url, null);
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
                        pieValues.add(entry);
                        pieKeys.add(jsonObject1.getString("date"));
                    }
                    handler.sendEmptyMessage(2);
                } else {
                    String data = jsonObject.getString("msg");
                    Bundle bundle = new Bundle();
                    bundle.putString("data", data);
                    Message message = new Message();
                    message.what = 3;
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
}
