package com.example.andrei.trafficsimulator;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class LaunchActivity extends AppCompatActivity implements View.OnClickListener{

    private Button btnStart,btnExit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);
        setupComponents();


    }

    private void setupComponents() {
        btnStart=(Button) findViewById(R.id.btn_start);
        btnExit=(Button)findViewById(R.id.btn_exit);
        btnStart.setOnClickListener(this);
        btnExit.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_start:
                startActivity(new Intent(getApplicationContext(),StageActivity.class));
                overridePendingTransition(R.anim.fade_in,R.anim.slide_out_left_top_corner);
                break;
            case R.id.btn_exit:

                break;
        }
    }
}
