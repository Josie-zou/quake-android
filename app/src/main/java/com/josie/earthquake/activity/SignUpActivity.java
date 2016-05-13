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
import com.josie.earthquake.utils.HttpClientUtils;
import com.ta.utdid2.android.utils.StringUtils;

import org.json.JSONException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Josie on 16/5/12.
 */
public class SignUpActivity extends Activity {
    private EditText username;
    private EditText mail;
    private EditText mobile;
    private EditText password;
    private EditText passwordConfirm;
    private Button signup;
    private String response;
    private Map<String, String> params;
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);
        initView();
        initEvent();
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                    startActivity(intent);
                    break;
            }
        }
    };

    private void initView() {
        username = (EditText) findViewById(R.id.username);
        mail = (EditText) findViewById(R.id.mail);
        mobile = (EditText) findViewById(R.id.mobile);
        password = (EditText) findViewById(R.id.password);
        passwordConfirm = (EditText) findViewById(R.id.password_confirm);
        signup = (Button) findViewById(R.id.signup);

    }

    private void initEvent() {
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (StringUtils.isEmpty(mail.getText().toString()) && StringUtils.isEmpty(mobile.getText().toString())){
//                    Toast.makeText(this, "请输入密码或者邮箱地址", Toast.LENGTH_LONG).show();
                } else if (!passwordConfirm.getText().toString().trim().equals(password.getText().toString().trim())){
//                    Toast.makeText(this, "密码输入错误", Toast.LENGTH_LONG).show();
                } else {
                    new Thread(signupRunnable).start();
                }
            }
        });
    }

    Runnable signupRunnable = new Runnable() {
        @Override
        public void run() {
            getData();
            HttpClientUtils httpClientUtils = new HttpClientUtils();
            try {
                response = httpClientUtils.doPost(url, params);
                Message message = new Message();
                message.what = 1;
                handler.sendMessage(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };

    private void getData() {
        url = "http://192.168.1.122:8080/signup?";
        params = new HashMap<>();
        params.put("username", username.getText().toString().trim());
        params.put("password", password.getText().toString().trim());
        params.put("mobile", mobile.getText().toString().trim());
    }
}
