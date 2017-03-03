package com.siyann.oktest;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button= (Button) findViewById(R.id.mybtn);
        button.setOnClickListener(v -> {
            Toast.makeText(MainActivity.this,"被点击了",Toast.LENGTH_SHORT).show();
        });
    }
}
