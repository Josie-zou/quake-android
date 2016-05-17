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
    private TextView forget;
    private PushAgent pushAgent;
    private Map<String, String> params;
    private String url;
    private String response;
    private int id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        initView();
        initEvents();


    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    try {
                        parseUser();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if (id == 0){
                        break;
                    }
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("id", String.valueOf(id));
                    intent.putExtras(bundle);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    break;
            }
        }
    };

    private void parseUser() throws JSONException {
        JSONObject jsonObject = new JSONObject(response);
        JSONObject data = jsonObject.getJSONObject("data");
        id = data.getInt("id");

    }


    private void initView() {
        imageView = (ImageView) findViewById(R.id.image);
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        login = (Button) findViewById(R.id.login);
        register = (Button) findViewById(R.id.register);
        forget = (TextView) findViewById(R.id.forget);
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
        url = "http://192.168.1.122:8080/login?";
        params = new HashMap<>();
        params.put("account", username.getText().toString().trim());
        params.put("password", password.getText().toString().trim());
    }

    Runnable loginRunnable = new Runnable() {
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


}

