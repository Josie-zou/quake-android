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
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.gitonway.lee.niftymodaldialogeffects.lib.NiftyDialogBuilder;
import com.josie.earthquake.R;
import com.josie.earthquake.model.FilterRule;
import com.josie.earthquake.model.WhiteList;
import com.josie.earthquake.utils.HttpClientUtils;
import com.josie.earthquake.utils.UrlUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Josie on 16/5/23.
 */
public class FilterRuleAdapter extends BaseAdapter {
    private List<FilterRule> filterRules;
    private Context context;
    private LayoutInflater layoutInflater;
    private FragmentManager fragmentManager;
    private String url;
    private Map<String, String> params;

    public FilterRuleAdapter(Context context, List<FilterRule> whiteLists, LayoutInflater layoutInflater, android.app.FragmentManager fragmentManager) {
        this.context = context;
        this.filterRules = whiteLists;
        this.layoutInflater = layoutInflater;
        this.fragmentManager = fragmentManager;
    }

    @Override
    public int getCount() {
        return filterRules.size();
    }

    @Override
    public Object getItem(int position) {
        return filterRules.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.filter_rule_list_item, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.itemText = (TextView) convertView.findViewById(R.id.item_text);
            viewHolder.operaterText = (TextView) convertView.findViewById(R.id.item_operater);
            viewHolder.updateButton = (ImageButton) convertView.findViewById(R.id.filter_item_update);
            viewHolder.deleteButton = (ImageButton) convertView.findViewById(R.id.item_delete);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.itemText.setText(filterRules.get(position).getRule().toString().trim());
        viewHolder.operaterText.setText("operator: " + filterRules.get(position).getUsername().toString().trim());
        initEvent(viewHolder, filterRules.get(position).getId());

        return convertView;
    }

    private void initEvent(final ViewHolder viewHolder, final int id) {
//        final String data = viewHolder.itemText.getText().toString().trim();
        viewHolder.updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = viewHolder.itemText.getText().toString().trim();
                final AlertDialog builder = new AlertDialog.Builder(context).create();
                builder.show();
                Window window = builder.getWindow();
                window.clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
                window.setContentView(R.layout.my_dialog);
                TextView textView = (TextView) window.findViewById(R.id.dialog_title);
                final EditText editText = (EditText) window.findViewById(R.id.dialog_edittext);
                Button sure = (Button) window.findViewById(R.id.dialog_sure);
                Button cancel = (Button) window.findViewById(R.id.dialog_cancel);
                editText.setText(text);
                editText.setFocusable(true);
                sure.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        updateFilterRule(editText.getText().toString().trim(), id);
                        builder.dismiss();
                    }
                });
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        builder.dismiss();
                    }
                });
            }
        });

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
                                deleteRule(id);
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
                    Toast.makeText(context, "更新规则成功", Toast.LENGTH_LONG).show();
                    getData();
                    break;
                case 2:
                    Toast.makeText(context, "删除规则成功", Toast.LENGTH_LONG).show();
                    getData();
                    break;
                case 3:
                    Bundle bundle = msg.getData();
                    String data = bundle.getString("data");
                    Toast.makeText(context, data, Toast.LENGTH_LONG).show();
                    break;
                case 4:
                    notifyDataSetChanged();
                    break;
            }
        }
    };

    private void updateFilterRule(final String data, final int id) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String url = UrlUtils.UpdateFilterUrl;
                Map<String, String> params = new HashMap<String, String>();
                params.put("id", String.valueOf(id));
                params.put("rule", data);
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
        }).start();
    }

    private void deleteRule(final int id) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String url = UrlUtils.DeleteFilterUrl;
                Map<String, String> params = new HashMap<String, String>();
                params.put("id", String.valueOf(id));
                try {
                    String response = HttpClientUtils.doPost(url, params);
                    JSONObject jsonObject = new JSONObject(response);
                    int code = jsonObject.getInt("code");
                    if (code == 0) {
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
        }).start();
    }

    private void getData() {
        url = UrlUtils.GetAllFilterUrl;
        params = new HashMap<>();
        new Thread(runnable).start();
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            try {
                String response = HttpClientUtils.doPost(url, params);
                filterRules = new ArrayList<>();
                JSONObject jsonObject = new JSONObject(response);
                int code = jsonObject.getInt("code");
                if (code == 0) {
                    parseResponse(jsonObject);
                    handler.sendEmptyMessage(4);
                } else {
                    String data = jsonObject.getString("msg");
                    Bundle bundle = new Bundle();
                    bundle.putString("data", data);
                    Message message = new Message();
                    message.setData(bundle);
                    message.what = 3;
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
        int code = jsonObject.getInt("code");
        if (code == 0) {
            JSONArray datas = jsonObject.getJSONArray("data");
            for (int i = 0; i < datas.length(); i++) {
                FilterRule filterRule = new FilterRule();
                JSONObject jsonObject1 = (JSONObject) datas.get(i);
                filterRule.setId(jsonObject1.getInt("id"));
                filterRule.setRule(jsonObject1.getString("rule"));
                filterRule.setUsername(jsonObject1.getString("username"));
                filterRules.add(filterRule);
            }
        }

    }

    private class ViewHolder {
        private TextView itemText;
        private TextView operaterText;
        private ImageButton updateButton;
        private ImageButton deleteButton;
    }
}
