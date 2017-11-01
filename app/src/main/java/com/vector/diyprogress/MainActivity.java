package com.vector.diyprogress;

import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Toast;

import com.vector.library.BubbleProgressBar;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.FileCallBack;

import java.io.File;

import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private SeekBar seekBar;
    private BubbleProgressBar mBubbleProgressBar;
    private Button mBtnStart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        OkHttpUtils.getInstance()
                .init(this)
                .debug(true, "okHttp")
                .timeout(20 * 1000);


        mBtnStart = (Button) findViewById(R.id.btn_start);

        mBubbleProgressBar = (BubbleProgressBar) findViewById(R.id.progress);


        seekBar = (SeekBar) findViewById(R.id.seek_bar);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mBubbleProgressBar.setProgress(progress);

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
        OkHttpUtils.get()
                .url("http://dldir1.qq.com/weixin/android/weixin6513android1100.apk")
                .build()
                .execute(new FileCallBack(Environment.getExternalStorageDirectory().getAbsolutePath(), "yimiao.apk") {
                    @Override
                    public void inProgress(float progress, long total, int id) {
                        int round = Math.round(progress * 100);
                        mBubbleProgressBar.setProgress(round);
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e, int id) {
                        Toast.makeText(MainActivity.this, validateError(e, response), Toast.LENGTH_SHORT).show();
                        mBtnStart.setEnabled(true);
                    }

                    @Override
                    public void onResponse(File response, int id) {
                        mBtnStart.setEnabled(true);
                    }

                    @Override
                    public void onBefore(Request request, int id) {
                        super.onBefore(request, id);
                        mBtnStart.setEnabled(false);

                    }
                });


    }
}
