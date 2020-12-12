package com.zhaoweihao.architechturesample.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zhaoweihao.architechturesample.R;
import com.zhaoweihao.architechturesample.bean.Detail;
import com.zhaoweihao.architechturesample.contract.HomeMoreZhiHuDailyDetailContract;


/**
 * Created by por on 2018/4/5.
 */
/**
*@description 首页-更多-知乎日报-详细界面
*@author
*@time 2019/1/28 14:34
*/

public class HomeMoreZhiHuDailyDetailFragment extends Fragment implements HomeMoreZhiHuDailyDetailContract.View {

    private static final String TAG = "HomeMoreZhiHuDailyDetailFragment";

    private HomeMoreZhiHuDailyDetailContract.Presenter presenter;
    private SwipeRefreshLayout refreshLayout;
    private TextView tvTitle,tvImageSource;
    private WebView wvContent;
    private ImageView ivBigImage;

    public HomeMoreZhiHuDailyDetailFragment() {

    }

    public static HomeMoreZhiHuDailyDetailFragment newInstance() { return new HomeMoreZhiHuDailyDetailFragment(); }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail,container,false);

        initViews(view);

        presenter.loadArticle(true);

        refreshLayout.setOnRefreshListener(() -> {
            presenter.loadArticle(true);
            stopLoading();
        });

        return view;


    }

    @Override
    public void setPresenter(HomeMoreZhiHuDailyDetailContract.Presenter presenter) {
        if (presenter != null) {
            this.presenter = presenter;
        }
    }

    @Override
    public void initViews(View view) {
        refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refresh);
        tvTitle = (TextView) view.findViewById(R.id.title);
        tvImageSource = (TextView) view.findViewById(R.id.image_source);
        wvContent = (WebView) view.findViewById(R.id.content);
        ivBigImage = (ImageView) view.findViewById(R.id.image);
    }

    @Override
    public void showResult(final Detail detail) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tvTitle.setText(detail.getTitle());
                tvImageSource.setText(detail.getImageSource());

                wvContent.getSettings().setJavaScriptEnabled(true);
                String htmlData = "<link rel=\"stylesheet\" type=\"text/css\" href=\"style.css\" />" + detail.getBody();
                wvContent.loadDataWithBaseURL("file:///android_asset/", htmlData, "text/html", "UTF-8", null);
                Glide.with(getActivity()).load(detail.getImage()).into(ivBigImage);
            }
        });
    }

    @Override
    public void startLoading() {
        refreshLayout.post(() -> refreshLayout.setRefreshing(true));
    }

    @Override
    public void stopLoading() {
        if (refreshLayout.isRefreshing()) {
            refreshLayout.post(() -> refreshLayout.setRefreshing(false));
        }

    }

    @Override
    public void showLoadError() {

    }

    @Override
    public String getArticleId() {
        String id = String.valueOf(getActivity().getIntent().getIntExtra("id",0));
        return id;
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.start();
    }

}
