package com.vector.diyprogress;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.SeekBar;

public class MainActivity extends AppCompatActivity {

    private SeekBar seekBar;
    private BubbleProgressBar mBubbleProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBubbleProgressBar = (BubbleProgressBar) findViewById(R.id.progress);

        final BubbleView bubbleView = (BubbleView) findViewById(R.id.bubble);

        seekBar = (SeekBar) findViewById(R.id.seek_bar);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mBubbleProgressBar.setProgress(progress);
                bubbleView.setProgressText(progress + "%");

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


    }

    public void start(View view) {
        new SecondTimer(100) {

            @Override
            public void onFinish() {

            }

            @Override
            protected void onTicker(long second) {
                mBubbleProgressBar.setProgress((int) second);
            }
        }.start();
    }
}
