package com.zhaoweihao.architechturesample.seat;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhaoweihao.architechturesample.R;
import com.zhaoweihao.architechturesample.data.Leave;
import com.zhaoweihao.architechturesample.data.seat.Create;
import com.zhaoweihao.architechturesample.data.seat.Seat;
import com.zhaoweihao.architechturesample.data.seat.SeatSel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import sviolet.seatselectionview.demo.SeatSelectionActivity;

import static com.zhaoweihao.architechturesample.util.HttpUtil.*;

import static com.zhaoweihao.architechturesample.util.Utils.AssetJSONFile;

public class CreateActivity extends AppCompatActivity implements CreateContract.View{

    private CreateContract.Presenter presenter;

    private ArrayList<Seat> seatList;
    private SeatSel sealSel;

    private EditText code; // 密令
    private RadioGroup radioGroup;
    private Button button;
    private SwipeRefreshLayout swipeRefreshLayout;

    private static final int SEAT_MODE_ONE = 1;
    private static final int SEAT_MODE_TWO = 2;
    private static final int SEAT_MODE_THREE = 3;

    private int mode;
    private String classCode;

    private static final String TAG = "CreateActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_seat);

        new CreatePresenter(this, this);

        initViews(null);
        getSupportActionBar().setTitle("创建点名房间");

        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId){
                case R.id.rb_wen:
                    mode = SEAT_MODE_ONE;
                    break;
                case R.id.rb_dl:
                    mode = SEAT_MODE_TWO;
                    break;
                case R.id.rb_li:
                    mode = SEAT_MODE_THREE;
                    break;
                    default:
            }
        });

        swipeRefreshLayout.setEnabled(false);

        button.setOnClickListener(v -> {
            classCode = code.getText().toString();
            if (classCode.equals(""))
                Toast.makeText(this, "密令不能为空", Toast.LENGTH_SHORT).show();
            try {
                String jsonLocal = AssetJSONFile("seat.json", this);
                seatList = new Gson().fromJson(jsonLocal, new TypeToken<List<Seat>>() {
                }.getType());
            } catch (IOException e) {
                e.printStackTrace();
            }

            Create create = new Create();
            create.setClassCode(classCode);
            switch (mode) {
                case SEAT_MODE_ONE:
                    sealSel = seatList.get(0).getSeated();
                    break;
                case SEAT_MODE_TWO:
                    sealSel = seatList.get(1).getSeated();
                    break;
                case SEAT_MODE_THREE:
                    sealSel = seatList.get(2).getSeated();
                    break;
                default:
            }

            create.setSeatSel(sealSel);
            // 交给presenter
            presenter.create(create);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.start();
    }

    @Override
    public void showResult(Boolean status) {
        if (status) {
            runOnUiThread(() -> {
                Toast.makeText(this, "创建房间成功，请把密令分享给同学吧", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(CreateActivity.this, SeatSelectionActivity.class);
                intent.putExtra("code", classCode);
                intent.putExtra("mode", mode);
                startActivity(intent);
            });
        }
    }

    @Override
    public void startLoading() {
        swipeRefreshLayout.post(() ->{
                    swipeRefreshLayout.setEnabled(true);
                    swipeRefreshLayout.setRefreshing(true);
                }
                );
    }

    @Override
    public void stopLoading() {
        if (swipeRefreshLayout.isRefreshing()){
            swipeRefreshLayout.post(() -> {
                swipeRefreshLayout.setRefreshing(false);
                swipeRefreshLayout.setEnabled(false);
            });
        }

    }

    @Override
    public void showLoadError() {
        Toast.makeText(this, "创建失败", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setPresenter(CreateContract.Presenter presenter) {
        if (presenter != null) {
            this.presenter = presenter;
        }
    }

    @Override
    public void initViews(View view) {
        code = findViewById(R.id.et);
        radioGroup = findViewById(R.id.rg);
        button = findViewById(R.id.btn);
        swipeRefreshLayout = findViewById(R.id.refresh);
        setSupportActionBar(findViewById(R.id.toolbar));
    }
}
