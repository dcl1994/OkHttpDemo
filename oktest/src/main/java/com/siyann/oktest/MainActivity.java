package com.siyann.oktest;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
    private Button button;
    private TextView mytextview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        button.setOnClickListener(v -> {
            Toast.makeText(MainActivity.this, "被点击了", Toast.LENGTH_SHORT).show();
        });

    }

    /**
     * 初始化
     */
    private void init() {
        button = (Button) findViewById(R.id.mybtn);
        mytextview = (TextView) findViewById(R.id.mytextview);

    }
}
