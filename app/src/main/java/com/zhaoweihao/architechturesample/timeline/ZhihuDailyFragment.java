package com.zhaoweihao.architechturesample.timeline;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zhaoweihao.architechturesample.R;
import com.zhaoweihao.architechturesample.data.ZhihuDailyNewsQuestion;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

public class ZhihuDailyFragment extends Fragment  implements ZhihuDailyContract.View{

    private ZhihuDailyContract.Presenter mPresenter;

    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mRefreshLayout;
    private View mEmptyView;
    private FloatingActionButton fab;

//    private ZhihuDailyNewsAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;

    private int mYear, mMonth, mDay;

    private boolean mIsFirstLoad = true;
    private int mListSize = 0;

    public ZhihuDailyFragment() {}

    public static ZhihuDailyFragment newInstance() {
        return new ZhihuDailyFragment();
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Calendar c = Calendar.getInstance();
        c.setTimeZone(TimeZone.getTimeZone("GMT+08"));
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_timeline_page, container, false);

        initViews(view);

        mRefreshLayout.setOnRefreshListener(() -> {
            Calendar c = Calendar.getInstance();
            c.setTimeZone(TimeZone.getTimeZone("GMT+08"));
            mPresenter.loadNews(true,true,c.getTimeInMillis());
        });

//        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//                if (dy > 0) {
//                    fab.hide();
//                    if (mLayoutManager.findLastCompletelyVisibleItemPosition() == mListSize - 1) {
//                        loadMore();
//                    }
//                } else {
//                    fab.show();
//                }
//            }
//        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
        Calendar c = Calendar.getInstance();
        c.setTimeZone(TimeZone.getTimeZone("GMT+08"));
        c.set(mYear, mMonth, mDay);
        setLoadingIndicator(mIsFirstLoad);
        if (mIsFirstLoad) {
            mPresenter.loadNews(true, false, c.getTimeInMillis());
            mIsFirstLoad = false;
        } else {
            mPresenter.loadNews(false, false, c.getTimeInMillis());
        }
    }

    @Override
    public boolean isActive() {
        return isAdded() && isResumed();
    }

    @Override
    public void setLoadingIndicator(boolean active) {
        mRefreshLayout.post(() -> mRefreshLayout.setRefreshing(active));
    }

//    @Override
//    public void showResult(@NonNull List<ZhihuDailyNewsQuestion> list) {
//        if (mAdapter == null) {
//            mAdapter = new ZhihuDailyNewsAdapter(list, getContext());
//            mAdapter.setItemClickListener((v, i) -> {
//
//            });
//            mRecyclerView.setAdapter(mAdapter);
//        } else {
//            mAdapter.updateData(list);
//        }
//    }

//    private void loadMore() {
//        Calendar c = Calendar.getInstance();
//        c.set(mYear, mMonth, --mDay);
//        mPresenter.loadNews(true, false, c.getTimeInMillis());
//    }

    @Override
    public void setPresenter(ZhihuDailyContract.Presenter presenter) {
        if (presenter != null) {
            mPresenter = presenter;
        }
    }

    @Override
    public void initViews(View view) {
        mRefreshLayout = view.findViewById(R.id.refresh_layout);
        mRefreshLayout.setColorSchemeColors(ContextCompat.getColor(getContext(), R.color.colorAccent));
        mRecyclerView = view.findViewById(R.id.recycler_view);
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mEmptyView = view.findViewById(R.id.empty_view);
        fab = getActivity().findViewById(R.id.fab);

    }
}
