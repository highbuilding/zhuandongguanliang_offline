package com.nuaa.utils;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.friendlyarm.AndroidSDK.GPIOEnum;
import com.friendlyarm.AndroidSDK.HardwareControler;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by where1993 on 2018/3/31.
 */

public class CountNum {
    private static final String TAG = "GPIODemo";
    private Timer timer = new Timer();
    private int step = 0;
    //private Map<String, Integer> demoGPIOPins = new HashMap<String, Integer>();


    double time=0;
    Timer timer2,timer3;
    int mark = 0;
    int num=0;
    Integer value = 63;   //63对应着m3上的13号引脚，64对应15号引脚

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    timer.cancel();
                    break;
                case 2:
                    double time2 = time/100;

                    break;
            }
            super.handleMessage(msg);
        }
    };

    class num_task extends TimerTask {   //用于检测高电平
        public void run(){
            if(HardwareControler.getGPIOValue(value)==1){
                Log.v("GPIO","High");
                mark=1;
                timer2.cancel();
                timer3 = new Timer();
                timer3.schedule(new xiaodou(),1,1);
            }
        }
    }
    class xiaodou extends TimerTask{   //用于消抖
        public void run(){
            if(mark<10&&HardwareControler.getGPIOValue(value)==1){
                mark++;
            }else if(mark==10){
                timer3.cancel();
                mark=0;

                Message message = new Message();
                message.what = 2;
                handler.sendMessage(message);
                while (HardwareControler.getGPIOValue(value)==1);
                timer2=new Timer();
                timer2.schedule(new num_task(),10,10);
            }else {
                timer3.cancel();
                timer2=new Timer();
                timer2.schedule(new num_task(),10,10);
                mark=0;
            }
        }
    }
    private TimerTask init_task = new TimerTask() {   //用于初始化io
        public void run() {
            Log.d(TAG, "init_task " + step);
            if (step == 1) {
                if (HardwareControler.setGPIODirection(value, GPIOEnum.IN) == 0) {
                } else {
                    Log.v("TimerTask", String.format("setGPIODirection(%d) failed", value));
                }
                step ++;
            }else if (step == 2) {
                Message message = new Message();
                message.what = 1;
                handler.sendMessage(message);
            }
        }
    };
    
    //单独用于统计时间，十毫秒一次，可能不是很准确，我们当时测试的时候，一分钟内误差不大。
    private TimerTask timer_time_task = new TimerTask() {

        @Override
        public void run() {
            time++;
        }
    };
}
