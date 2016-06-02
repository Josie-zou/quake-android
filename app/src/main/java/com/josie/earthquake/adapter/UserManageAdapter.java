package com.josie.earthquake.adapter;

import android.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import com.josie.earthquake.model.User;
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
 * Created by Josie on 16/5/24.
 */
public class UserManageAdapter extends BaseAdapter {
    private String url;
    private Map<String, String> params;
    private List<User> users;
    private Context context;
    private LayoutInflater layoutInflater;
    private FragmentManager fragmentManager;

    public UserManageAdapter(Context context, List<User> users, LayoutInflater layoutInflater, android.app.FragmentManager fragmentManager) {
        this.context = context;
        this.users = users;
        this.layoutInflater = layoutInflater;
        this.fragmentManager = fragmentManager;
    }

    @Override
    public int getCount() {
        return users.size();
    }

    @Override
    public Object getItem(int position) {
        return users.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.user_manage_list_item, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.username = (TextView) convertView.findViewById(R.id.user_name);
            viewHolder.mail = (TextView) convertView.findViewById(R.id.user_mail);
            viewHolder.mobile = (TextView) convertView.findViewById(R.id.user_mobile);
            viewHolder.manageButton = (ImageButton) convertView.findViewById(R.id.user_manage);
            viewHolder.deleteButton = (ImageButton) convertView.findViewById(R.id.user_delete);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.username.setText(users.get(position).getUsername().toString().trim());
        viewHolder.mail.setText(users.get(position).getMailAdress().toString().trim());
        viewHolder.mobile.setText(users.get(position).getPhoneNumber().toString().trim());
        initEvent(viewHolder, users.get(position).getId());

        return convertView;
    }

    private void initEvent(final ViewHolder viewHolder, final int id) {
        viewHolder.manageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final NiftyDialogBuilder dialogBuilder1 = NiftyDialogBuilder.getInstance(context);
                dialogBuilder1.withTitle("Earthquake Eye")
                        .withMessage("你要把该用户升级为管理员吗？")
                        .withDialogColor("#6699CC")
                        .withMessageColor("#FFFFFF")
                        .withButton1Text("OK")
                        .withButton2Text("cancel")
                        .setButton1Click(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                manageUser(id);
                                dialogBuilder1.dismiss();
                            }
                        })
                        .setButton2Click(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialogBuilder1.dismiss();
                            }
                        }).show();
            }
        });

        viewHolder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final NiftyDialogBuilder dialogBuilder = NiftyDialogBuilder.getInstance(context);
                dialogBuilder.withTitle("Earthquake Eye")
                        .withMessage("你确定要删除该用户？")
                        .withButton1Text("OK")
                        .withButton2Text("cancel")
                        .setButton1Click(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                deleteUser(id);
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
                    Toast.makeText(context, "升级管理员成功", Toast.LENGTH_LONG).show();
                    break;
                case 2:
                    Toast.makeText(context, "删除用户成功", Toast.LENGTH_LONG).show();
                    getData();
                    break;
                case 3:
                    Bundle bundle = msg.getData();
                    String data = bundle.getString("data");
                    Toast.makeText(context, data, Toast.LENGTH_LONG).show();
                    break;
                case 4:
                    notifyDataSetChanged();
            }
        }
    };

    private void getData() {
        url = UrlUtils.GetAllUserUrl;
        params = new HashMap<>();
        new Thread(runnable).start();
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            try {
                String response = HttpClientUtils.doPost(url, params);
                users = new ArrayList<>();
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
        JSONArray datas = jsonObject.getJSONArray("data");
        for (int i = 0; i < datas.length(); i++) {
            User user = new User();
            JSONObject jsonObject1 = (JSONObject) datas.get(i);
            user.setId(jsonObject1.getInt("id"));
            user.setPhoneNumber(jsonObject1.getString("mailAdress"));
            user.setMailAdress(jsonObject1.getString("phoneNumber"));
            user.setUsername(jsonObject1.getString("username"));
            users.add(user);
        }
    }

    private void manageUser(final int id) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String url = UrlUtils.SetManagerUrl;
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

    private void deleteUser(final int id) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String url = UrlUtils.DeleteUserUrl;
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

    private class ViewHolder {
        private TextView username;
        private TextView mail;
        private TextView mobile;
        private ImageButton manageButton;
        private ImageButton deleteButton;
    }

}
