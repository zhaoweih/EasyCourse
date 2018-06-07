package com.zhaoweihao.architechturesample.vote;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.lixs.charts.BarChart.LBarChartView;
import com.lixs.charts.LineChartView;
import com.lixs.charts.PieChartView;
import com.lixs.charts.RadarChartView;
import com.zhaoweihao.architechturesample.R;
import com.zhaoweihao.architechturesample.data.vote.Select;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChartActivity extends AppCompatActivity {

    private int yellowColor = Color.argb(255, 253, 197, 53);
    private int greenColor = Color.argb(255, 27, 147, 76);
    private int redColor = Color.argb(255, 211, 57, 53);
    private int blueColor = Color.argb(255, 76, 139, 245);

    private Select select;

    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.tv_a)
    TextView choiceA;
    @BindView(R.id.tv_a_num)
    TextView numA;
    @BindView(R.id.tv_b)
    TextView choiceB;
    @BindView(R.id.tv_b_num)
    TextView numB;
    @BindView(R.id.tv_c)
    TextView choiceC;
    @BindView(R.id.tv_c_num)
    TextView numC;
    @BindView(R.id.tv_d)
    TextView choiceD;
    @BindView(R.id.tv_d_num)
    TextView numD;
    @BindView(R.id.pieView)
    PieChartView pieChartView;
    @BindView(R.id.radarView)
    RadarChartView radarChartView;
    @BindView(R.id.lineView)
    LineChartView lineChartView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);

        ButterKnife.bind(this);

        select = (Select) getIntent().getSerializableExtra("select");

        initData();

        initPieDatas();
        initRadarDatas();
        initLineDatas();

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    private void initData() {
        title.setText(select.getTitle());
        choiceA.setText(select.getChoiceA());
        choiceB.setText(select.getChoiceB());
        choiceC.setText(select.getChoiceC());
        choiceD.setText(select.getChoiceD());

        numA.setText(String.valueOf(select.getNumA()));
        numB.setText(String.valueOf(select.getNumB()));
        numC.setText(String.valueOf(select.getNumC()));
        numD.setText(String.valueOf(select.getNumD()));

    }

    private void initLineDatas() {

        List<Double> datas;
        List<String> description;

        double numA,numB,numC,numD;

        numA = Double.valueOf(select.getNumA());
        numB = Double.valueOf(select.getNumB());
        numC = Double.valueOf(select.getNumC());
        numD = Double.valueOf(select.getNumD());

        datas = new ArrayList<>();
        datas.add(numA);
        datas.add(numB);
        datas.add(numC);
        datas.add(numD);


        description = new ArrayList<>();
        description.add("A");
        description.add("B");
        description.add("C");
        description.add("D");

        lineChartView.setDatas(datas, description);
    }

    private void initRadarDatas() {
        List<Float> datas = new ArrayList<>();
        List<String> description = new ArrayList<>();

        int total = select.getNumA() + select.getNumB() + select.getNumC() + select.getNumD();

        datas.add((select.getNumA()*1.0f)/total);
        datas.add(select.getNumB()*1.0f/total);
        datas.add(select.getNumC()*1.0f/total);
        datas.add(select.getNumD()*1.0f/total);

        description.add("A");
        description.add("B");
        description.add("C");
        description.add("D");

        //点击动画开启
        radarChartView.setCanClickAnimation(true);
        radarChartView.setDatas(datas);
        radarChartView.setPolygonNumbers(4);
        radarChartView.setDescriptions(description);
    }

    private void initPieDatas() {

        List<Float> mRatios = new ArrayList<>();

        List<String> mDescription = new ArrayList<>();

        List<Integer> mArcColors = new ArrayList<>();

        int total = select.getNumA() + select.getNumB() + select.getNumC() + select.getNumD();

        mRatios.add((select.getNumA()*1.0f)/total);
        mRatios.add(select.getNumB()*1.0f/total);
        mRatios.add(select.getNumC()*1.0f/total);
        mRatios.add(select.getNumD()*1.0f/total);


        mArcColors.add(blueColor);
        mArcColors.add(redColor);
        mArcColors.add(yellowColor);
        mArcColors.add(greenColor);

        mDescription.add("A");
        mDescription.add("B");
        mDescription.add("C");
        mDescription.add("D");

        //点击动画开启
        pieChartView.setCanClickAnimation(true);
        pieChartView.setDatas(mRatios, mArcColors, mDescription);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
