package com.zhaoweihao.architechturesample.seat;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.zhaoweihao.architechturesample.R;

import sviolet.seatselectionview.demo.SeatSelectionActivity;

public class EnterActivity extends AppCompatActivity {

    private EditText editText;

    private Button button;

    private ImageView imageView;

    private int courseId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter);

        courseId = getIntent().getIntExtra("courseId", 0);

        initViews();
        getSupportActionBar().setTitle("进入点名系统");

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                imageView.setImageResource(R.drawable.hunt);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        button.setOnClickListener(v -> {
            String code = editText.getText().toString();
            if (code.equals("")){
                Toast.makeText(this, "密令不能为空", Toast.LENGTH_SHORT).show();
                return;
            }

            Intent intent = new Intent(EnterActivity.this, SeatSelectionActivity.class);
            intent.putExtra("code", code);
            intent.putExtra("courseId", courseId);
            startActivity(intent);


        });
    }

    private void initViews() {
        editText = findViewById(R.id.et);
        button = findViewById(R.id.btn);
        imageView = findViewById(R.id.building);
        setSupportActionBar(findViewById(R.id.toolbar));

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return  true;
    }
}
