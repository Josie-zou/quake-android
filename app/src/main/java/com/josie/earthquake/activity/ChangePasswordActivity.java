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
public class ChangePasswordActivity extends Activity {

    private EditText originPassword;
    private EditText newPassword;
    private EditText passwordAgain;
    private Button button;
    private Toolbar toolbar;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_password);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        user = (User) bundle.getSerializable("user");
        initView();
        initEvent();
    }

    private void initView() {
        originPassword = (EditText) findViewById(R.id.edit_origin_password);
        newPassword = (EditText) findViewById(R.id.edit_new_password);
        passwordAgain = (EditText) findViewById(R.id.edit_confirm_password);
        button = (Button) findViewById(R.id.submit);
        toolbar = (Toolbar) findViewById(R.id.changePassword_toolbar);
        toolbar.inflateMenu(R.menu.menu_main);
        toolbar.setTitle("Quake Eye");
        toolbar.setTitleTextColor(Color.WHITE);
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    Toast.makeText(ChangePasswordActivity.this, "修改密码成功", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(ChangePasswordActivity.this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    break;
                case 2:
                    Bundle bundle = msg.getData();
                    String data = bundle.getString("data");
                    Toast.makeText(ChangePasswordActivity.this, data, Toast.LENGTH_LONG).show();
                    break;
            }
        }
    };

    private void initEvent() {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (StringUtils.isEmpty(originPassword.getText().toString().trim())) {
                    Toast.makeText(ChangePasswordActivity.this, "初始密码不能为空", Toast.LENGTH_LONG).show();
                } else if (StringUtils.isEmpty(newPassword.getText().toString().trim()) || StringUtils.isEmpty(passwordAgain.getText().toString().trim())) {
                    Toast.makeText(ChangePasswordActivity.this, "新密码不能为空", Toast.LENGTH_LONG).show();
                } else if (!newPassword.getText().toString().trim().equals(passwordAgain.getText().toString().trim())) {
                    Toast.makeText(ChangePasswordActivity.this, "两次输入密码不一致", Toast.LENGTH_LONG).show();
                } else {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            String url = UrlUtils.ChangePasswordUrl;
                            Map<String, String> params = new HashMap<String, String>();
                            params.put("password", originPassword.getText().toString().trim());
                            params.put("newPassword", newPassword.getText().toString().trim());
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
                    }).start();
                }
            }
        });
    }
}
