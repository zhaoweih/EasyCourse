package com.zhaoweihao.architechturesample.reading;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.zhaoweihao.architechturesample.R;
import com.zhaoweihao.architechturesample.data.ZhihuDaily;

import java.util.ArrayList;

/**
 * Created by zhao weihao on 2018/4/5.
 */

public class ZhihuDailyFragment extends Fragment implements ZhihuDailyContract.View{

    private ZhihuDailyContract.Presenter presenter;
    private ZhihuDailyAdapter adapter;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout refreshLayout;

    public ZhihuDailyFragment() {

    }

    public static ZhihuDailyFragment newInstance() {return new ZhihuDailyFragment();}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_zhihu_daily,container,false);

        initViews(view);

        presenter.requestArticles(true);

        refreshLayout.setOnRefreshListener(() -> {
            presenter.requestArticles(true);
            adapter.notifyDataSetChanged();
            stopLoading();
        });

        return view;
    }

    @Override
    public void setPresenter(ZhihuDailyContract.Presenter presenter) {
        if (presenter != null) {
            this.presenter = presenter;
        }

    }

    @Override
    public void showResult(final ArrayList<ZhihuDaily.Story> articleList) {

        getActivity().runOnUiThread(() -> {
            if (adapter == null) {
                adapter = new ZhihuDailyAdapter(getActivity(), articleList);
                recyclerView.setAdapter(adapter);
            }
            else {
                adapter.notifyDataSetChanged();
            }
            adapter.setItemClickListener((v, position) -> {
                Intent intent = new Intent(getActivity(), DetailActivity.class);
                ArrayList<ZhihuDaily.Story> articles = presenter.getArticles();
                intent.putExtra("id",articles.get(position).getId());
                getActivity().startActivity(intent);
            });
            adapter.setItemLongClickListener((view, position) -> presenter.copyToClipboard(position));
        });
    }

    @Override
    public void startLoading() {
        refreshLayout.post(() -> refreshLayout.setRefreshing(true));
    }

    @Override
    public void stopLoading() {
        if (refreshLayout.isRefreshing()){
            refreshLayout.post(() -> refreshLayout.setRefreshing(false));
        }
    }

    @Override
    public void initViews(View view) {
        recyclerView = view.findViewById(R.id.zhihu_daily_rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        refreshLayout = view.findViewById(R.id.refresh);

    }

    @Override
    public void showLoadError() {
        Snackbar.make(recyclerView, "加载失败", Snackbar.LENGTH_SHORT)
                .setAction("重试", view -> presenter.requestArticles(false)).show();
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.start();
    }
}
