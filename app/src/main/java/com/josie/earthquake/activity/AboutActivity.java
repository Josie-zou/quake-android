package com.josie.earthquake.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.josie.earthquake.R;

/**
 * Created by Josie on 16/5/10.
 */
public class AboutActivity extends Activity {

    private TextView about;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        about = (TextView) findViewById(R.id.about);
    }
}
