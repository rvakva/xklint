package com.rvakva.linttest.linttest;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    TextView tv_name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        tv_name = findViewById(R.id.tv_name);

        Log.d(TAG, "onCreate: ");

        tv_name.setText(1 + "\n" +
                2+ "\n");

        new Thread(new Runnable() {
            @Override
            public void run() {

            }
        });

        Toast.makeText(this,getResources().getString(R.string.app_name),Toast.LENGTH_LONG).show();

        LineBean lineBean = new LineBean();


    }
}
