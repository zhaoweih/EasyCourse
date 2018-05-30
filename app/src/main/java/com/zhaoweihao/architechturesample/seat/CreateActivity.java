package com.zhaoweihao.architechturesample.seat;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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

import static com.zhaoweihao.architechturesample.util.HttpUtil.*;

import static com.zhaoweihao.architechturesample.util.Utils.AssetJSONFile;

public class CreateActivity extends AppCompatActivity implements CreateContract.View{

    private CreateContract.Presenter presenter;

    private ArrayList<Seat> seatList;
    private SeatSel sealSel;

    private static final int SEAT_MODE_ONE = 1;
    private static final int SEAT_MODE_TWO = 2;
    private static final int SEAT_MODE_THREE = 3;

    private static final String TAG = "CreateActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_seat);

        new CreatePresenter(this, this);

        String code = "20772";
        int mode = SEAT_MODE_ONE;

        try {
            String jsonLocal = AssetJSONFile("seat.json", this);
            seatList = new Gson().fromJson(jsonLocal, new TypeToken<List<Seat>>() {
            }.getType());
        } catch (IOException e) {
            e.printStackTrace();
        }

        Create create = new Create();
        create.setClassCode(code);
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

    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.start();
    }

    @Override
    public void showResult(Boolean status) {
        if (status)
            Log.d(TAG, "创建点名房间成功");
    }

    @Override
    public void startLoading() {

    }

    @Override
    public void stopLoading() {

    }

    @Override
    public void showLoadError() {

    }

    @Override
    public void setPresenter(CreateContract.Presenter presenter) {
        if (presenter != null) {
            this.presenter = presenter;
        }
    }

    @Override
    public void initViews(View view) {

    }
}
