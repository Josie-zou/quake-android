package com.josie.earthquake.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.josie.earthquake.R;
import com.josie.earthquake.model.User;
import com.josie.earthquake.utils.HttpClientUtils;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.PushAgent;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import jp.wasabeef.blurry.Blurry;

/**
 * Created by Josie on 16/4/27.
 */
public class LoginActivity extends Activity {

    private RelativeLayout relativeLayout;
    private ImageView imageView;
    private EditText username;
    private EditText password;
    private Button login;
    private Button register;
    private PushAgent pushAgent;
    private boolean firstOpen = true;
    private Map<String, String> params;
    private String url;
    private String response;
    private User user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        initView();
        initEvents();
        new Thread(loginRunnable).start();

    }


    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    if (user == null) {
                        break;
                    }
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("user", user);
                    intent.putExtras(bundle);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    break;
                case 2:
                    Bundle bundle1 = msg.getData();
                    String data = bundle1.getString("data");
                    Toast.makeText(LoginActivity.this, data, Toast.LENGTH_LONG).show();
            }
        }
    };

    private void initView() {
        imageView = (ImageView) findViewById(R.id.image);
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        login = (Button) findViewById(R.id.login);
        register = (Button) findViewById(R.id.register);
    }

    private void initEvents() {
        imageView.postDelayed(new Runnable() {
            @Override
            public void run() {
                imageView.setBackgroundResource(R.drawable.background);
                Blurry.with(LoginActivity.this).radius(25).capture(imageView).into(imageView);
            }
        }, 0);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firstOpen = false;
                new Thread(loginRunnable).start();
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }

    private void getData() {
        url = "http://192.168.1.122:8080/api/login?";
        params = new HashMap<>();
        params.put("account", username.getText().toString().trim());
        params.put("password", password.getText().toString().trim());
    }

    Runnable loginRunnable = new Runnable() {
        @Override
        public void run() {
            getData();
            try {
                response = HttpClientUtils.doPost(url, params);
                JSONObject jsonObject = new JSONObject(response);
                int code = jsonObject.getInt("code");
                Message message = new Message();
                if (code == 0) {
                    message.what = 1;
                    JSONObject data = jsonObject.getJSONObject("data");
                    user = new User();
                    user.setId(data.getInt("id"));
                    user.setPrivilege(data.getInt("privilege"));
                    user.setUsername(data.getString("username"));
                    user.setPhoneNumber(data.getString("phoneNumber"));
                    user.setMailAdress(data.getString("mailAdress"));
                    user.setPositon(data.getString("positon"));
                    user.setWorkPlace(data.getString("workPlace"));
                    user.setPassword(data.getString("password"));
                    user.setQq(data.getString("qq"));
                    handler.sendMessage(message);
                } else if (firstOpen == false) {
                    String data = jsonObject.getString("msg");
                    Bundle bundle = new Bundle();
                    bundle.putString("data", data);
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


}

