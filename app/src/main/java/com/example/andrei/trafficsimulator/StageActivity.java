package com.example.andrei.trafficsimulator;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class StageActivity extends AppCompatActivity {

    private Button btnIncreaseTLSpeed;
    private Button btnDecreaseTLSpeed;
    private Button btnAddSpecialCar,btnSimulateCollision;

    private TrafficView enviroment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stage);
        enviroment=(TrafficView)findViewById(R.id.enviroment);

        btnIncreaseTLSpeed=(Button)findViewById(R.id.btnIncreaseTrafficLightSpeed);
        btnDecreaseTLSpeed=(Button)findViewById(R.id.btnDecreaseTrafficLightSpeed);
        btnAddSpecialCar=(Button)findViewById(R.id.btnSpecial);
        btnSimulateCollision=(Button)findViewById(R.id.btnCollision);

        btnIncreaseTLSpeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.util.Log.i("Info",  "*****ON Increase Speed ");
                android.util.Log.i("Info",  "old intervalRedGreenColor="+TrafficView.intervalRedGreenColor);
                android.util.Log.i("Info",  "old intervalYellowColor="+TrafficView.intervalYellowColor);
                if(TrafficView.intervalRedGreenColor-TrafficView.intervalChangeRate>0) {
                    TrafficView.intervalRedGreenColor -= TrafficView.intervalChangeRate;
                }
                if(TrafficView.intervalYellowColor-TrafficView.intervalChangeRate>0) {
                    TrafficView.intervalYellowColor -= TrafficView.intervalChangeRate;
                }

                Pedestrian.walkingCoordsRate++;
                android.util.Log.i("Info",  "new intervalRedGreenColor="+TrafficView.intervalRedGreenColor);
                android.util.Log.i("Info",  "new intervalYellowColor="+TrafficView.intervalYellowColor);
                Toast.makeText(StageActivity.this, "Red/Green time(sec)="+TrafficView.intervalRedGreenColor/1000.0+ ", Yellow time(sec)="+TrafficView.intervalYellowColor/1000., Toast.LENGTH_LONG).show();

            }
        });

        btnDecreaseTLSpeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.util.Log.i("Info",  "*****ON Decrease Speed ");
                android.util.Log.i("Info",  " old intervalRedGreenColor="+TrafficView.intervalRedGreenColor);
                android.util.Log.i("Info",  "old intervalYellowColor="+TrafficView.intervalYellowColor);
                TrafficView.intervalRedGreenColor+=TrafficView.intervalChangeRate;
                TrafficView.intervalYellowColor+=TrafficView.intervalChangeRate;
                android.util.Log.i("Info",  " new intervalRedGreenColor="+TrafficView.intervalRedGreenColor);
                android.util.Log.i("Info",  "new intervalYellowColor="+TrafficView.intervalYellowColor);
                if(Pedestrian.walkingCoordsRate-1>0){
                    Pedestrian.walkingCoordsRate--;
                }
                Toast.makeText(StageActivity.this, "Red/Green time(sec)="+TrafficView.intervalRedGreenColor/1000.0+ ", Yellow time(sec)="+TrafficView.intervalYellowColor/1000.0, Toast.LENGTH_LONG).show();

            }
        });

        btnAddSpecialCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enviroment.addSpecialCar();
            }
        });

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left_top_corner,R.anim.fade_out);
    }
}
