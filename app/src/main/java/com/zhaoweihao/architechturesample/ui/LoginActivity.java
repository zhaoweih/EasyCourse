package com.zhaoweihao.architechturesample.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.zhaoweihao.architechturesample.R;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Toast.makeText(this, "aaa", Toast.LENGTH_SHORT).show();
    }
}
