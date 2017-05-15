package com.example.andrei.trafficsimulator;

import android.os.CountDownTimer;
import android.widget.Toast;

import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

/**
 * Created by corina on 5/13/17.
 */
public class CounterClass extends CountDownTimer {
    public static boolean waitingYellow =false;
    public static boolean waitingRG=true;


    public CounterClass(long millisInFuture, long countDownInterval) {
        super(millisInFuture, countDownInterval);
        android.util.Log.i("Info",  "*****ON Constructor ");
        if(TrafficLight.allLightColorYellow()){
            waitingYellow=true;
        }else{
            waitingRG=true;
        }
    }


    @Override
    public void onTick(long millisUntilFinished) {

        long millis = millisUntilFinished;
        String hms = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(millis),
                TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),
                TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
    }

    @Override
    public void onFinish() {
        android.util.Log.i("Info",  "*****ON FINISH ");
        if(TrafficLight.allLightColorYellow()){
            waitingYellow=false;
            TrafficLight.changeLightColors();
        }
        else {
            waitingRG=false;
            TrafficLight.setSemaforsToYellow();
        }
    }
}
