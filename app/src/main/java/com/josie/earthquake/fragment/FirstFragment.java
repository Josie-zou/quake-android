package com.josie.earthquake.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SeekBar;

import com.josie.earthquake.R;
import com.roger.match.library.MatchButton;
import com.roger.match.library.MatchTextView;

/**
 * Created by Josie on 16/5/4.
 */
public class FirstFragment extends android.support.v4.app.Fragment {
    private View view;
    private SeekBar mSeekBar;
    private MatchTextView mMatchTextView;
    private MatchButton button;


    public static FirstFragment instance() {
        FirstFragment view = new FirstFragment();
        return view;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null) {
                parent.removeView(view);
            }
            return view;
        }
        View rootView = null;
        rootView = inflater.inflate(R.layout.first_fragment, container, false);
        view = rootView;
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        mMatchTextView = (MatchTextView) getView().findViewById(R.id.mMatchTextView);
        mSeekBar = (SeekBar) getView().findViewById(R.id.mSeekBar);
        button = (MatchButton) getView().findViewById(R.id.mButton);

        mSeekBar.setProgress(100);
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mMatchTextView.setProgress(progress * 1f / 100);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                MatchDialog matchDialog = new MatchDialog();
//                matchDialog.show(getFragmentManager(), "matchDialog");
//            }
//        });
    }

}
