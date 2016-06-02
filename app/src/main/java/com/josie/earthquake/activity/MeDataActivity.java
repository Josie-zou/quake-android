package com.josie.earthquake.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.josie.earthquake.R;
import com.josie.earthquake.model.User;
import com.josie.earthquake.utils.HttpClientUtils;
import com.josie.earthquake.utils.UrlUtils;
import com.ta.utdid2.android.utils.StringUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Josie on 16/5/10.
 */
public class MeDataActivity extends Activity {

    private Toolbar toolbar;
    private EditText name;
    private EditText mail;
    private EditText mobile;
    private EditText address;
    private EditText job;
    private Button sure;
    private String id;
    private User user;
    private String url;
    private Map<String, String> params;
    private String response;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.me_data);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        user = (User) bundle.getSerializable("user");
        id = String.valueOf(user.getId());
        initView();
        initEvent();
        setData();
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    Bundle bundle = msg.getData();
                    String data = bundle.getString("data");
                    Toast.makeText(MeDataActivity.this, data, Toast.LENGTH_LONG).show();
                    break;
                case 2:
                    Toast.makeText(getApplicationContext(), "保存完毕", Toast.LENGTH_LONG).show();
                    break;
            }
        }
    };

    private void setData() {
        name.setText(user.getUsername().trim());
        mail.setText(user.getMailAdress().trim());
        mobile.setText(user.getPhoneNumber().trim());
        job.setText(user.getPositon().trim());
        address.setText(user.getWorkPlace().trim());
    }

    private void initView() {
        name = (EditText) findViewById(R.id.edit_name);
        mail = (EditText) findViewById(R.id.edit_mail);
        mobile = (EditText) findViewById(R.id.edit_mobile);
        job = (EditText) findViewById(R.id.edit_job);
        address = (EditText) findViewById(R.id.edit_address);
        sure = (Button) findViewById(R.id.sure);
        toolbar = (Toolbar) findViewById(R.id.medata_toolbar);
        toolbar.inflateMenu(R.menu.menu_main);
        toolbar.setTitle("Quake Eye");
        toolbar.setTitleTextColor(Color.WHITE);
    }

    private void initEvent() {
        sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (StringUtils.isEmpty(name.getText().toString().trim())) {
                    Toast.makeText(MeDataActivity.this, "用户名不能为空", Toast.LENGTH_LONG).show();
                } else if (StringUtils.isEmpty(mail.getText().toString().trim())) {
                    Toast.makeText(MeDataActivity.this, "邮箱不能为空", Toast.LENGTH_LONG).show();
                } else if (StringUtils.isEmpty(mobile.getText().toString().trim())) {
                    Toast.makeText(MeDataActivity.this, "手机号码不能为空", Toast.LENGTH_LONG).show();
                } else {
                    new Thread(updateRunnable).start();
                }
            }
        });
    }

    Runnable updateRunnable = new Runnable() {
        @Override
        public void run() {
            url = UrlUtils.UpdateUserUrl;
            params = new HashMap<String, String>();
            params.put("id", String.valueOf(user.getId()));
            params.put("username", name.getText().toString().trim());
            params.put("mailAdress", mail.getText().toString().trim());
            params.put("phoneNumber", mobile.getText().toString().trim());
            params.put("positon", job.getText().toString().trim());
            params.put("workPlace", address.getText().toString().trim());
            HttpClientUtils httpClientUtils = new HttpClientUtils();
            try {
                response = httpClientUtils.doPost(url, params);
                JSONObject jsonObject = new JSONObject(response);
                int code = jsonObject.getInt("code");
                if (code == 0) {
                    handler.sendEmptyMessage(2);
                } else {
                    String msg = jsonObject.getString("msg");
                    Bundle bundle = new Bundle();
                    bundle.putString("data", msg);
                    Message message = new Message();
                    message.what = 1;
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
