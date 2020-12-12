package com.zhaoweihao.architechturesample.activity.example;

import android.Manifest;
import android.annotation.SuppressLint;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.qmuiteam.qmui.widget.QMUITopBarLayout;
import com.qmuiteam.qmui.widget.pullRefreshLayout.QMUICenterGravityRefreshOffsetCalculator;
import com.qmuiteam.qmui.widget.pullRefreshLayout.QMUIPullRefreshLayout;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.zhaoweihao.architechturesample.R;
import com.zhaoweihao.architechturesample.activity.MvpActivity;
import com.zhaoweihao.architechturesample.adapter.example.TestHttpAdapter;
import com.zhaoweihao.architechturesample.bean.Course;
import com.zhaoweihao.architechturesample.presenter.TestHttpPresenter;
import com.zhaoweihao.architechturesample.util.EventBusUtils;
import com.zhaoweihao.architechturesample.util.ToastUtil;
import com.zhaoweihao.architechturesample.view.uiview.TestHttpView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


/**
 * @author zhaowiehao
 * 测试http框架
 */
@EActivity(R.layout.activity_test_http)
public class TestHttpActivity extends MvpActivity<TestHttpPresenter<TestHttpView>> implements TestHttpView {

    //https://test.tanxinkui.cn/api/course/querySelectByCourseId?courseId=10

    /**
     * 网络请求例子
     */
    @Click
    void btn_get_test() {
        Log.d(TAG, "点击");
        presenter.testHttp("10");
    }

    /**
     * shareprefence存储例子
     */
    @Click
    void btn_sharepref_etst() {
        //测试shareprefence框架
        String get = sharedPreferences.username().get();
        ToastUtil.showShort(this, get);
    }

    /**
     * 权限请求例子
     */
    @SuppressLint("CheckResult")
    @Click
    void btn_permission_request() {
        new RxPermissions(this)
                .request(Manifest.permission.CAMERA)
                .subscribe(granted -> {
                    if (granted) {
                        // I can control the camera now
                        ToastUtil.showShort(this, "获得相机权限了");
                    } else {
                        // Oups permission denied
                        ToastUtil.showShort(this, "权限请求被拒绝");
                    }
                });
    }

    @ViewById
    TextView tv_show_test;

    @ViewById
    QMUIPullRefreshLayout pull_to_refresh;

    @ViewById
    QMUITopBarLayout topbar;

    @ViewById
    RecyclerView rv_course_list;

    private TestHttpAdapter adapter;

    private List<Course> courses = new ArrayList<>();


    @AfterViews
    void initView() {
        //绑定eventbus
        EventBusUtils.register(this);
        sharedPreferences.username().put("hello world");
        //设置下拉区域中间效果
        pull_to_refresh.setRefreshOffsetCalculator(new QMUICenterGravityRefreshOffsetCalculator());
        //设置下拉监听器
        pull_to_refresh.setOnPullListener(new QMUIPullRefreshLayout.OnPullListener() {
            @Override
            public void onMoveTarget(int offset) {

            }

            @Override
            public void onMoveRefreshView(int offset) {

            }

            @Override
            public void onRefresh() {
                ToastUtil.showShort(TestHttpActivity.this, "执行刷新操作");
                presenter.testHttp("10");
            }
        });

        initTopBar();
        rv_course_list.setLayoutManager(new LinearLayoutManager(this));
        adapter = new TestHttpAdapter(R.layout.course_item, courses);
        rv_course_list.setAdapter(adapter);

        //短按事件
        adapter.setOnItemClickListener((adapter, view, position) -> {
            Log.d(TAG, "onItemClick: ");
            ToastUtil.showShort(TestHttpActivity.this, "onItemClick === " + position);
        });
        //长按事件
        adapter.setOnItemLongClickListener((adapter, view, position) -> {
            Log.d(TAG, "onItemLongClick: ");
            ToastUtil.showShort(TestHttpActivity.this, "onItemLongClick === " + position);
            return false;
        });

        //子控件短按事件
        adapter.setOnItemChildClickListener((adapter, view, position) -> {
            Log.d(TAG, "onItemChildClick: ");
            Log.d(TAG, "view id === " + view.getId() + "btn_join id === " + R.id.btn_join);
            if (view.getId() == R.id.btn_join) {
                ToastUtil.showShort(this, "点击了加入按钮");
                Log.d(TAG, "点击了加入按钮");
            }
            ToastUtil.showShort(TestHttpActivity.this, "onItemChildClick === " + position + "view === " + view.getId());
        });

        adapter.setOnLoadMoreListener(() -> {
//                mRecyclerView.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        if (mCurrentCounter >= TOTAL_COUNTER) {
//                            //Data are all loaded.
//                            mQuickAdapter.loadMoreEnd();
//                        } else {
//                            if (isErr) {
//                                //Successfully get more data
//                                mQuickAdapter.addData(DataServer.getSampleData(PAGE_SIZE));
//                                mCurrentCounter = mQuickAdapter.getData().size();
//                                mQuickAdapter.loadMoreComplete();
//                            } else {
//                                //Get more data failed
//                                isErr = true;
//                                Toast.makeText(PullToRefreshUseActivity.this, R.string.network_err, Toast.LENGTH_LONG).show();
//                                mQuickAdapter.loadMoreFail();
//
//                            }
//                        }
//                    }
//
//                }, delayMillis);
            rv_course_list.postDelayed(() -> adapter.loadMoreEnd(), 2000);
        }, rv_course_list);



    }

    private void initTopBar() {
        topbar.addLeftImageButton(R.mipmap.angle_pointing_to_left_, R.id.qmui_topbar_item_left_back).setOnClickListener(v -> {
//                popBackStack();
            ToastUtil.showShort(TestHttpActivity.this, "点了返回键");
        });

        topbar.setTitle(TAG);

        // 切换其他情况的按钮
//        topbar.addRightImageButton(R.mipmap.icon_topbar_overflow, R.id.topbar_right_change_button).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                showBottomSheetList();
//            }
//        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //解绑eventbus
        EventBusUtils.unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(EventBusUtils.EventMessage message) {
        int tag = message.getTag();

        switch (tag) {
            case EventBusUtils.EVENT_SEND_INFORM:
                ToastUtil.showShort(this, "Evntbus消息 === 查询成功");
                break;
            default:
                break;
        }
    }



    @Override
    protected TestHttpPresenter<TestHttpView> createPresenter() {
        return new TestHttpPresenter<>(this, this);
    }

    @Override
    public void getDataFail(String msg) {

    }

    @Override
    public void querySuccess(int code, Object msg) {
        pull_to_refresh.finishRefresh();
        Log.d(TAG, msg.toString());
        tv_show_test.setText(msg.toString());
        Type listType = new TypeToken<ArrayList<Course>>(){}.getType();
        courses.clear();
        courses.addAll(new Gson().fromJson(msg.toString(), listType));
        adapter.notifyDataSetChanged();

        //发送Eventbus消息
        EventBusUtils.sendEventMsg(EventBusUtils.EVENT_SEND_INFORM);
    }
}
