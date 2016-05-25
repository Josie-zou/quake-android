package com.josie.earthquake.adapter;

import android.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.gitonway.lee.niftymodaldialogeffects.lib.NiftyDialogBuilder;
import com.josie.earthquake.R;
import com.josie.earthquake.fragment.MyDialogFragment;
import com.josie.earthquake.model.WhiteList;
import com.josie.earthquake.utils.HttpClientUtils;

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
public class WhiteListAdapter extends BaseAdapter {
    private List<WhiteList> whiteLists;
    private Context context;
    private LayoutInflater layoutInflater;
    private FragmentManager fragmentManager;
    private String url;
    private Map<String, String> params;

    public WhiteListAdapter(Context context, List<WhiteList> whiteLists, LayoutInflater layoutInflater, android.app.FragmentManager fragmentManager) {
        this.context = context;
        this.whiteLists = whiteLists;
        this.layoutInflater = layoutInflater;
        this.fragmentManager = fragmentManager;
    }

    @Override
    public int getCount() {
        return whiteLists.size();
    }

    @Override
    public Object getItem(int position) {
        return whiteLists.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.white_list_item, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.itemText = (TextView) convertView.findViewById(R.id.item_text);
            viewHolder.operaterText = (TextView) convertView.findViewById(R.id.item_operater);
            viewHolder.deleteButton = (ImageButton) convertView.findViewById(R.id.item_delete);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.itemText.setText(whiteLists.get(position).getUrl().toString().trim());
        viewHolder.operaterText.setText("operator: " + whiteLists.get(position).getUsername().toString().trim());
        initEvent(viewHolder, whiteLists.get(position).getId());

        return convertView;
    }

    private void initEvent(final ViewHolder viewHolder, final int id) {
        viewHolder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final NiftyDialogBuilder dialogBuilder = NiftyDialogBuilder.getInstance(context);
                dialogBuilder.withTitle("Earthquake Eye")
                        .withMessage("Are you sure?")
                        .withButton1Text("OK")
                        .withButton2Text("cancel")
                        .setButton1Click(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                deleteWhiteList(id);
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
        });
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    Toast.makeText(context, "删除规则成功", Toast.LENGTH_LONG).show();
                    getData();
                    break;
                case 2:
                    Bundle bundle = msg.getData();
                    String data = bundle.getString("data");
                    Toast.makeText(context, data, Toast.LENGTH_LONG).show();
                    break;
                case 3:
                    notifyDataSetChanged();
                    break;
            }
        }
    };

    private void getData() {
        url = "http://192.168.1.122:8080/api/whitelist/getAll?";
        params = new HashMap<>();
        new Thread(runnable).start();
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            try {
                String response = HttpClientUtils.doPost(url, params);
                whiteLists = new ArrayList<>();
                JSONObject jsonObject = new JSONObject(response);
                int code = jsonObject.getInt("code");
                if (code == 0) {
                    parseResponse(jsonObject);
                    handler.sendEmptyMessage(3);
                } else {
                    String data = jsonObject.getString("msg");
                    Bundle bundle = new Bundle();
                    bundle.putString("data", data);
                    Message message = new Message();
                    message.setData(bundle);
                    message.what = 2;
                    handler.sendMessage(message);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    private void parseResponse(JSONObject jsonObject) throws JSONException {
        JSONArray datas = jsonObject.getJSONArray("data");
        for (int i = 0; i < datas.length(); i++) {
            WhiteList whiteList = new WhiteList();
            JSONObject jsonObject1 = (JSONObject) datas.get(i);
            whiteList.setId(jsonObject1.getInt("id"));
            whiteList.setUrl(jsonObject1.getString("url"));
            whiteList.setUsername(jsonObject1.getString("username"));
            whiteLists.add(whiteList);
        }
    }

    private void deleteWhiteList(final int id) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String url = "http://192.168.1.122:8080/api/whitelist/delete?";
                Map<String, String> params = new HashMap<String, String>();
                params.put("id", String.valueOf(id));
                try {
                    String response = HttpClientUtils.doPost(url, params);
                    JSONObject jsonObject = new JSONObject(response);
                    int code = jsonObject.getInt("code");
                    if (code == 0) {
                        handler.sendEmptyMessage(1);
                    } else {
                        String data = jsonObject.getString("msg");
                        Bundle bundle = new Bundle();
                        bundle.putString("data", data);
                        Message message = new Message();
                        message.what = 2;
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

    private class ViewHolder {
        private TextView itemText;
        private TextView operaterText;
        private ImageButton deleteButton;
    }
}
