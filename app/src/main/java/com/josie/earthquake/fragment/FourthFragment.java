package com.josie.earthquake.fragment;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
import com.josie.earthquake.activity.LoginActivity;
import com.josie.earthquake.activity.ShowView;
import com.josie.earthquake.adapter.ChartDataAdapter;
import com.josie.earthquake.listItem.BarChartItem;
import com.josie.earthquake.listItem.ChartItem;
import com.josie.earthquake.listItem.LineChartItem;
import com.josie.earthquake.listItem.PieChartItem;
import com.umeng.message.PushAgent;

import java.util.ArrayList;

import jp.wasabeef.blurry.Blurry;

/**
 * Created by Josie on 16/5/4.
 */
public class FourthFragment extends Fragment {

    private int itemCount = 12;
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
//        initEvent();
    }

    private void initView() {
        toolbar = (Toolbar) getView().findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.menu_main);
        toolbar.setTitle("Quake Eye");
        toolbar.setTitleTextColor(Color.WHITE);

        ListView listView = (ListView) getView().findViewById(R.id.listView);
        ArrayList<ChartItem> list = new ArrayList<ChartItem>();

        for (int i = 0; i < 10; i++) {
            if (i % 3 == 0) {
                list.add(new LineChartItem(generateDataLine(i + 1), getContext()));
            } else if (i % 3 == 1) {
                list.add(new BarChartItem(generateDataBar(i + 1), getContext()));
            } else if (i % 3 == 2) {
                list.add(new PieChartItem(generateDataPie(i + 1), getContext()));
            }
        }

        ChartDataAdapter chartDataAdapter = new ChartDataAdapter(getContext(), list);
        listView.setAdapter(chartDataAdapter);
    }

    /**
     * generates a random ChartData object with just one DataSet
     *
     * @return
     */
    private LineData generateDataLine(int cnt) {

        ArrayList<Entry> e1 = new ArrayList<Entry>();

        for (int i = 0; i < 12; i++) {
            e1.add(new Entry((int) (Math.random() * 65) + 40, i));
        }

        LineDataSet d1 = new LineDataSet(e1, "New DataSet " + cnt + ", (1)");
        d1.setLineWidth(4.0f);
        d1.setCircleRadius(5.5f);
        d1.setColor(getResources().getColor(R.color.colorBlue));
        d1.setCircleColor(getResources().getColor(R.color.colorBlue));
        d1.setHighLightColor(Color.rgb(244, 117, 117));
        d1.setValueTextColor(Color.BLUE);
        d1.setValueTextSize(15f);
        d1.setDrawValues(true);
        d1.setAxisDependency(YAxis.AxisDependency.LEFT);

        ArrayList<Entry> e2 = new ArrayList<Entry>();

        for (int i = 0; i < 12; i++) {
            e2.add(new Entry((int) (Math.random() * 65) + 40, i));
        }

        LineDataSet d2 = new LineDataSet(e2, "New DataSet " + cnt + ", (2)");
        d2.setLineWidth(4.0f);
        d2.setCircleRadius(5.5f);
        d2.setHighLightColor(Color.rgb(244, 117, 117));
        d2.setColor(getResources().getColor(R.color.colorGreen));
        d2.setCircleColor(getResources().getColor(R.color.colorGreen));
//        d2.setDrawValues(false);
        d2.setValueTextColor(Color.GREEN);
        d2.setValueTextSize(15f);
        d2.setDrawValues(true);
        d2.setAxisDependency(YAxis.AxisDependency.LEFT);


        ArrayList<ILineDataSet> sets = new ArrayList<ILineDataSet>();
        sets.add(d1);
        sets.add(d2);

        LineData cd = new LineData(getMonths(), sets);
        return cd;
    }

    /**
     * generates a random ChartData object with just one DataSet
     *
     * @return
     */
    private BarData generateDataBar(int cnt) {

        ArrayList<BarEntry> entries = new ArrayList<BarEntry>();

        for (int i = 0; i < 12; i++) {
            entries.add(new BarEntry((int) (Math.random() * 70) + 30, i));
        }

        BarDataSet d = new BarDataSet(entries, "New DataSet " + cnt);
        d.setBarSpacePercent(20f);
        d.setColors(ColorTemplate.VORDIPLOM_COLORS);
        d.setHighLightAlpha(255);

        BarData cd = new BarData(getMonths(), d);
        return cd;
    }

    /**
     * generates a random ChartData object with just one DataSet
     *
     * @return
     */
    private PieData generateDataPie(int cnt) {

        ArrayList<Entry> entries = new ArrayList<Entry>();

        for (int i = 0; i < 4; i++) {
            entries.add(new Entry((int) (Math.random() * 70) + 40, i));
        }

        PieDataSet d = new PieDataSet(entries, "");

        // space between slices
        d.setSliceSpace(2f);
        d.setColors(ColorTemplate.VORDIPLOM_COLORS);

        PieData cd = new PieData(getQuarters(), d);
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
