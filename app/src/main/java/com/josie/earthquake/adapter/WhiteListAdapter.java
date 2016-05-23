package com.josie.earthquake.adapter;

import android.app.FragmentManager;
import android.content.Context;
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

import java.util.List;


/**
 * Created by Josie on 16/5/22.
 */
public class WhiteListAdapter extends BaseAdapter {
    private List<WhiteList> whiteLists;
    private Context context;
    private LayoutInflater layoutInflater;
    private FragmentManager fragmentManager;

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
            viewHolder.updateButton = (ImageButton) convertView.findViewById(R.id.item_update);
            viewHolder.deleteButton = (ImageButton) convertView.findViewById(R.id.item_delete);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.itemText.setText(whiteLists.get(position).getUrl().toString().trim());
        viewHolder.operaterText.setText("operator: " + whiteLists.get(position).getUsername().toString().trim());
        initEvent(viewHolder);

        return convertView;
    }

    private void initEvent(ViewHolder viewHolder) {
        viewHolder.updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO
                Log.e("test", "update");
                showUpdateData();
//                Toast.makeText(context, "update", Toast.LENGTH_LONG).show();
//                new AlertDialog.Builder(context).setTitle("wodelistview").setMessage("hello").show();
//                MyDialogFragment fragment = MyDialogFragment.newInstance(4, 5, true, true);
//                fragment.show(fragmentManager, "sign up");
//                NiftyDialogBuilder dialogBuilder = NiftyDialogBuilder.getInstance(context);
//                dialogBuilder.withTitle("Earthquake Eye")
//                        .withButton1Text("OK")
//                        .withButton2Text("cancel")
//                        .setButton1Click(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                Toast.makeText(context, "ok", Toast.LENGTH_SHORT).show();
//                            }
//                        })
//                        .setButton2Click(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                Toast.makeText(context, "cancel", Toast.LENGTH_SHORT).show();
//                            }
//                        }).show();
            }
        });

        viewHolder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO
                Log.e("delete", "delete");

                NiftyDialogBuilder dialogBuilder = NiftyDialogBuilder.getInstance(context);
                dialogBuilder.withTitle("Earthquake Eye")
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
                                Toast.makeText(context, "cancel", Toast.LENGTH_SHORT).show();
                            }
                        }).show();
            }
        });
    }

    private void showUpdateData() {
        new AlertDialog.Builder(context).setTitle("dlsfjs").setMessage("heoo").show();
    }

    private class ViewHolder {
        private TextView itemText;
        private TextView operaterText;
        private ImageButton updateButton;
        private ImageButton deleteButton;
    }
}
