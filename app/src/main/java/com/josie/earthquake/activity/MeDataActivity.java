package com.josie.earthquake.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.josie.earthquake.R;

/**
 * Created by Josie on 16/5/10.
 */
public class MeDataActivity extends Activity {

    private EditText name;
    private EditText mail;
    private EditText mobile;
    private EditText address;
    private EditText job;
    private Button sure;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.me_data);
        initView();
        initEvent();
    }

    private void initView() {
        name = (EditText) findViewById(R.id.edit_name);
        mail = (EditText) findViewById(R.id.edit_mail);
        mobile = (EditText) findViewById(R.id.edit_mobile);
        job = (EditText) findViewById(R.id.edit_job);
        address = (EditText) findViewById(R.id.edit_address);
        sure = (Button) findViewById(R.id.sure);
    }

    private void initEvent() {
        sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //网络操作，延时，加载
            }
        });
    }

}
