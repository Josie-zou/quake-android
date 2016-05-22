package com.josie.earthquake.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.josie.earthquake.R;
import com.josie.earthquake.model.User;
import com.josie.earthquake.utils.HttpClientUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

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
    private User user;
    private String id;
    private String url;
    private Map<String, String> params;
    private String response;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.me_data);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        id = bundle.getString("id");
        initView();
        initEvent();
        new Thread(getUserRunnable).start();
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    setData();
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

    Runnable getUserRunnable = new Runnable() {
        @Override
        public void run() {
            url = "http://192.168.1.122:8080/api/user/get?";
            params = new HashMap<>();
            params.put("id", id);
            HttpClientUtils httpClientUtils = new HttpClientUtils();
            try {
                response = httpClientUtils.doPost(url, params);
                parseData();
                handler.sendEmptyMessage(1);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    };

    private void parseData() throws JSONException {
        JSONObject jsonObject = new JSONObject(response);
        JSONObject data = jsonObject.getJSONObject("data");
        user = new User();
        user.setId(data.getInt("id"));
        user.setUsername(data.getString("username"));
        user.setMailAdress(data.getString("mailAdress"));
        user.setNickName(data.getString("nickName"));
        user.setPhoneNumber(data.getString("phoneNumber"));
        user.setPositon(data.getString("positon"));
        user.setWorkPlace(data.getString("workPlace"));
        user.setPrivilege(data.getInt("privilege"));
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
                new Thread(updateRunnable).start();
            }
        });
    }

    Runnable updateRunnable = new Runnable() {
        @Override
        public void run() {
            url = "http://192.168.1.122:8080/user/update?";
            params = new HashMap<String, String>();
            params.put("id", id);
            params.put("username", name.getText().toString().trim());
            params.put("mailAdress", mail.getText().toString().trim());
            params.put("phoneNumber", mobile.getText().toString().trim());
            params.put("positon", job.getText().toString().trim());
            params.put("workPlace", address.getText().toString().trim());
            HttpClientUtils httpClientUtils = new HttpClientUtils();
            try {
                httpClientUtils.doPost(url, params);
                handler.sendEmptyMessage(2);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };

}
