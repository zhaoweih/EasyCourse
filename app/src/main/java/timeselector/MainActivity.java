package timeselector;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import timeselector.inter.CustomListener;
import timeselector.view.TimePickerView;

import com.zhaoweihao.architechturesample.R;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btn_customTime;
    TimePickerView pvCustomTime;
    TextView tv_sendtopic_date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeselector);

        /**時間選擇器*/
        initCustomTimePicker();

       /* btn_customTime = (Button) findViewById(R.id.btn_customTime);
        btn_customTime.setOnClickListener(this);*/
        tv_sendtopic_date=(TextView)findViewById(R.id.tv_sendtopic_date);
        tv_sendtopic_date.setOnClickListener(this);
    }


    private void initCustomTimePicker() {

        //系统当前时间
        Calendar selectorDate = Calendar.getInstance();
        Calendar startDate = Calendar.getInstance();
        startDate.set(2000, 1, 23);
        //系统当前时间
        Calendar endDate = Calendar.getInstance();
        endDate.set(2050, 2, 28);
        //时间选择器 ，自定义布局
        pvCustomTime = new TimePickerView.Builder(this, new TimePickerView.OnTimeSelectorListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//选中事件回调
                tv_sendtopic_date.setText(getTime(date));
            }
        })
                .setDate(selectorDate)
                .setRangDate(startDate, endDate)
                .setLayoutRes(R.layout.pickerview_custom_time,
                        new CustomListener() {
                            @Override
                            public void customLayout(View v) {
                                final TextView tvSubmit = (TextView) v.findViewById(R.id.tv_finish);
                                tvSubmit.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        pvCustomTime.returnData();
                                        pvCustomTime.dismiss();
                                    }
                                });
                            }
                        }).setType(new boolean[]{true, true, true, true, true, false})
                .setLabel("年", "月", "日", "时", "分")
                .isCenterLabel(false)
                .setDividerColor(0xFF24AD9D)
                .build();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tv_sendtopic_date && pvCustomTime != null)
            pvCustomTime.show();
    }

    private String getTime(Date date) {
        //可根据需要自行截取数据显示
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return format.format(date);
    }
}
