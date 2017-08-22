package com.example.bartek.log_work_android;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class SecondActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second_activity);
        Toast.makeText(SecondActivity.this, getIntent().getExtras().getString("string"), Toast.LENGTH_LONG).show();
    }
}
