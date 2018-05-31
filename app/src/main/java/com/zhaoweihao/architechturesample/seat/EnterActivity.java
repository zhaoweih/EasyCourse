package com.zhaoweihao.architechturesample.seat;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.zhaoweihao.architechturesample.R;

import sviolet.seatselectionview.demo.SeatSelectionActivity;

public class EnterActivity extends AppCompatActivity {

    private EditText editText;

    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter);

        initViews();

        button.setOnClickListener(v -> {
            String code = editText.getText().toString();
            if (code.equals("")){
                Toast.makeText(this, "密令不能为空", Toast.LENGTH_SHORT).show();
                return;
            }

            Intent intent = new Intent(EnterActivity.this, SeatSelectionActivity.class);
            intent.putExtra("code", code);
            startActivity(intent);


        });
    }

    private void initViews() {
        editText = findViewById(R.id.et);
        button = findViewById(R.id.btn);
    }
}
