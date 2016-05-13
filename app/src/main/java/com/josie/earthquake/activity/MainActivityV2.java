package com.josie.earthquake.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.josie.earthquake.R;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.PushAgent;

/**
 * Created by Josie on 16/5/5.
 */
public class MainActivityV2 extends Activity {
    private PushAgent pushAgent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pushAgent = PushAgent.getInstance(this);
        pushAgent.onAppStart();
        pushAgent.enable(new IUmengRegisterCallback() {
            @Override
            public void onRegistered(final String s) {
                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        Log.e("device token", s);
                    }
                });
            }
        });
    }
}
