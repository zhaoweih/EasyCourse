package com.zhaoweihao.architechturesample.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.blankj.utilcode.util.TimeUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;
import com.zhaoweihao.architechturesample.R;
import com.zhaoweihao.architechturesample.adapter.HomeActivityAdapter;
import com.zhaoweihao.architechturesample.base.BaseActivity;
import com.zhaoweihao.architechturesample.bean.AddAndShowActivity;
import com.zhaoweihao.architechturesample.bean.HuoDong;
import com.zhaoweihao.architechturesample.bean.RestResponse;
import com.zhaoweihao.architechturesample.bean.ShowNote;
import com.zhaoweihao.architechturesample.database.User;
import com.zhaoweihao.architechturesample.ui.TitleLayout;
import com.zhaoweihao.architechturesample.util.Constant;
import com.zhaoweihao.architechturesample.util.HttpUtil;

import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
*@description 首页-活动
*@author
*@time 2019/1/28 13:06
*/
public class HomeActivityActivity extends BaseActivity {


    @BindView(R.id.ahhd_title)
    TitleLayout ahhd_title;

    @BindView(R.id.ahhd_recyclerView)
    RecyclerView ahhd_recyclerView;

    private HomeActivityAdapter homeActivityAdapter;
    private QMUITipDialog tipDialog;
    private List<AddAndShowActivity> HuoDongList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_huo_dong);
        ButterKnife.bind(this);
    }
    @Override
    protected void onResume(){
        super.onResume();
        init();
    }

    private void init() {
        tipDialog = new QMUITipDialog.Builder(HomeActivityActivity.this)
                .setTipWord("正在加载活动...")
                .setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
                .create();
        ahhd_title.setTitle("活动");
        Button button=ahhd_title.getSettingButton();
        button.setText("添加");
        if(DataSupport.findLast(User.class).getStudentId()==null){
            button.setVisibility(View.VISIBLE);
        }else {
            button.setVisibility(View.INVISIBLE);
        }
        button.setOnClickListener(view ->addActivity());
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        ahhd_recyclerView.setLayoutManager(layoutManager);
        initAdapterData();
       // initAdapter();
    }
    /**
    *以下方法用于:添加活动的页面，Activity
    *by txk
    */
    private void addActivity(){
        Intent intent=new Intent(HomeActivityActivity.this,HomeActivitySubmitActivity.class);
        startActivity(intent);
    }
    private void initAdapterData() {
        tipDialog.show();

        /*for (int i = 1; i < 20; i++) {
            HuoDongList.add(initList(R.drawable.school,"标签","标题标题标题标题标题标题标题标题标题标题标题标题标题标题标题标题1",
                    "2015.08.20-2015.08.21"));
        }*/
        HttpUtil.sendGetRequest(Constant.GET_ACTIVITIES_URL, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                tipDialog.dismiss();
                runOnUiThread(()->{
                    Toast.makeText(HomeActivityActivity.this, "加载异常！", Toast.LENGTH_SHORT).show();
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String body = response.body().string();
                //解析json数据组装RestResponse对象
                RestResponse restResponse =JSON.parseObject(body, RestResponse.class);
                //RestResponse restResponse = new Gson().fromJson(body, RestResponse.class);
                if (restResponse.getCode() == Constant.SUCCESS_CODE) {
                    HuoDongList.clear();
                    HuoDongList=JSON.parseArray(restResponse.getPayload().toString(),AddAndShowActivity.class);
                    /*NoteUiList.addAll(new Gson().fromJson(restResponse.getPayload().toString(), new TypeToken<List<ShowNote>>() {
                    }.getType()));*/
                    runOnUiThread(()->{
                        initAdapter();
                        tipDialog.dismiss();
                    });
                }
            }
        });
    }
    private HuoDong initList(int drawable,String tag,String title,String duration){
        HuoDong huoDong=new HuoDong();
        huoDong.setDrawable(getResources().getDrawable(drawable));
        huoDong.setTag(tag);
        huoDong.setTitle(title);
        huoDong.setDuration(duration);
        return huoDong;
    }

    private void initAdapter() {

        homeActivityAdapter = new HomeActivityAdapter(HuoDongList);
        /*homeActivityAdapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);
        homeActivityAdapter.isFirstOnly(false);*/
       /* homeActivityAdapter.setEnableLoadMore(false);
        homeActivityAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override public void onLoadMoreRequested() {
                ahhd_recyclerView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                    }

                }, 1000);
            }
        }, ahhd_recyclerView);*/
        homeActivityAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent=new Intent(HomeActivityActivity.this,HomeActivityDetailActivity.class);
                intent.putExtra("activityDetail", HuoDongList.get(position));
                startActivity(intent);

            }
        });

        ahhd_recyclerView.setAdapter(homeActivityAdapter);
    }


}
