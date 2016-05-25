package com.josie.earthquake.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;


import com.josie.earthquake.R;

/**
 * Created by Josie on 16/5/10.
 */
public class WebViewActivity extends Activity {
    private WebView webView;
    private String url;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.web_view);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        url = bundle.getString("url");
        initView();
        webView.loadUrl(url);
        initEvent();
    }

    private void initView() {
        webView = (android.webkit.WebView) findViewById(R.id.webView);
        toolbar = (Toolbar) findViewById(R.id.webview_toolbar);
        toolbar.inflateMenu(R.menu.menu_main);
        toolbar.setTitle("Quake Eye");
        toolbar.setTitleTextColor(Color.WHITE);
    }

    private void initEvent() {
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setBuiltInZoomControls(true);//设置使支持缩放
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                Toast.makeText(WebViewActivity.this, "Oh no! " + description, Toast.LENGTH_SHORT).show();
            }

        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {
            webView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
