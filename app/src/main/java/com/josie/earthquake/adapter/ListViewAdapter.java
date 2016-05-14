package com.josie.earthquake.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.josie.earthquake.R;
import com.josie.earthquake.model.QuakeInfo;

import java.util.ArrayList;
import java.util.List;

import co.lujun.androidtagview.TagContainerLayout;

/**
 * Created by Josie on 16/5/13.
 */
public class ListViewAdapter extends BaseAdapter {

    private List<QuakeInfo> quakeInfos;//定义数据。
    private List<String> tags = new ArrayList<>();
    private LayoutInflater layoutInflater;//定义Inflater,加载我们自定义的布局。
    private Context context;

    public ListViewAdapter(List<QuakeInfo> quakeInfos, LayoutInflater layoutInflater, Context context){
        this.quakeInfos = quakeInfos;
        this.layoutInflater = layoutInflater;
        this.context = context;
    }

    @Override
    public int getCount() {
        return quakeInfos.size();
    }

    @Override
    public Object getItem(int position) {
        return quakeInfos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            tags.add("hello");
            convertView = layoutInflater.inflate(R.layout.record_list_item, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.icon = (ImageView) convertView.findViewById(R.id.dataImage);
            viewHolder.dataType = (TextView) convertView.findViewById(R.id.dataType);
            viewHolder.dataTitle = (TextView) convertView.findViewById(R.id.dataTitle);
            viewHolder.dataDetail = (TextView) convertView.findViewById(R.id.dataDetail);
            viewHolder.tagContainerLayout = (TagContainerLayout) convertView.findViewById(R.id.tagcontainerLayout1);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.icon.setBackgroundResource(R.drawable.voc);
        viewHolder.dataType.setText(quakeInfos.get(position).getType());
        viewHolder.dataTitle.setText(quakeInfos.get(position).getTitle());
        viewHolder.dataDetail.setText(quakeInfos.get(position).getDescription());
        viewHolder.tagContainerLayout.setTag("test");

        return convertView;    }

    //这个ViewHolder只能服务于当前这个特定的adapter，因为ViewHolder里会指定item的控件，不同的ListView，item可能不同，所以ViewHolder写成一个私有的类
    private class ViewHolder {
        ImageView icon;
        TextView dataType;
        TextView dataTitle;
        TextView dataDetail;
        TagContainerLayout tagContainerLayout;

    }
}

