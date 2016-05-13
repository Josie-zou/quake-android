package com.josie.earthquake.adapter;

import android.content.Context;
import android.content.Intent;
import android.service.voice.VoiceInteractionService;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.josie.earthquake.R;
import com.josie.earthquake.activity.LoginActivity;
import com.josie.earthquake.activity.MeDataActivity;

import java.util.List;

/**
 * Created by Josie on 16/5/10.
 */
public class SettingAdapter extends BaseAdapter {

    private List<String> item;//定义数据。
    private LayoutInflater mInflater;//定义Inflater,加载我们自定义的布局。
    private Context context;

    /*
    定义构造器，在Activity创建对象Adapter的时候将数据data和Inflater传入自定义的Adapter中进行处理。
    */
    public SettingAdapter(LayoutInflater inflater, List<String> data, Context context) {
        mInflater = inflater;
        item = data;
        this.context = context;
    }

    @Override
    public int getCount() {
        return item.size();
    }

    @Override
    public Object getItem(int position) {
        return item.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.setting_list_item, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.textView = (TextView) convertView.findViewById(R.id.settingItem);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.textView.setText(item.get(position));
        initEvent(viewHolder, position);
        return convertView;
    }

    private void initEvent(final ViewHolder viewHolder, final int position) {

    }

    //这个ViewHolder只能服务于当前这个特定的adapter，因为ViewHolder里会指定item的控件，不同的ListView，item可能不同，所以ViewHolder写成一个私有的类
    private class ViewHolder {
        TextView textView;

    }
}
