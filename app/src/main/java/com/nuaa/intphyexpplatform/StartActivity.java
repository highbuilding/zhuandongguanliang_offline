package com.nuaa.intphyexpplatform;


import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Timer;
import java.util.TimerTask;


public class StartActivity extends BaseActivity {
private TextView currentDateText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy年MM月dd日");
        String date = sDateFormat.format(new java.util.Date());
        currentDateText = (TextView) findViewById(R.id.current_date);
                currentDateText.setText("实验时间："+date);

        timer.schedule(task,2000);

    }
    Timer timer = new Timer();
    TimerTask task = new TimerTask() {

        @Override
        public void run() {
            // 需要做的事:发送消息
            Intent intent=new Intent(StartActivity.this,ListActivity.class);
            //Bundle bundle=new Bundle();
            startActivity(intent);
            finish();
        }
    };

}



