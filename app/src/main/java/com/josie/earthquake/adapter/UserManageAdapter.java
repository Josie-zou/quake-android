package com.josie.earthquake.adapter;

import android.app.FragmentManager;
import android.content.Context;
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

import java.util.List;

/**
 * Created by Josie on 16/5/24.
 */
public class UserManageAdapter extends BaseAdapter {
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
            viewHolder.manageButton = (ImageButton) convertView.findViewById(R.id.user_manage);
            viewHolder.deleteButton = (ImageButton) convertView.findViewById(R.id.user_delete);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.username.setText(users.get(position).getUsername().toString().trim());
        viewHolder.mail.setText(users.get(position).getMailAdress().toString().trim());
        viewHolder.mobile.setText(users.get(position).getPhoneNumber().toString().trim());
        initEvent(viewHolder);

        return convertView;
    }

    private void initEvent(final ViewHolder viewHolder) {
        viewHolder.manageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO
                Log.e("test", "update");
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
                                Toast.makeText(context, "ok", Toast.LENGTH_SHORT).show();
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
                //TODO
                Log.e("delete", "delete");

                final NiftyDialogBuilder dialogBuilder = NiftyDialogBuilder.getInstance(context);
                dialogBuilder.withTitle("Earthquake Eye")
                        .withMessage("你确定要删除该用户？")
                        .withButton1Text("OK")
                        .withButton2Text("cancel")
                        .setButton1Click(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Toast.makeText(context, "ok", Toast.LENGTH_SHORT).show();
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

    private class ViewHolder {
        private TextView username;
        private TextView mail;
        private TextView mobile;
        private ImageButton manageButton;
        private ImageButton deleteButton;
    }
}
