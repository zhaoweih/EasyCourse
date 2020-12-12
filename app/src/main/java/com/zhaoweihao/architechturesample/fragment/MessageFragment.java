package com.zhaoweihao.architechturesample.fragment;

import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.callback.ItemDragAndSwipeCallback;
import com.chad.library.adapter.base.listener.OnItemDragListener;
import com.chad.library.adapter.base.listener.OnItemSwipeListener;
import com.zhaoweihao.architechturesample.R;
import com.zhaoweihao.architechturesample.activity.MessageAddressListActivity;
import com.zhaoweihao.architechturesample.activity.MessageInboxActivity;
import com.zhaoweihao.architechturesample.activity.MessageMemorandumActivity;
import com.zhaoweihao.architechturesample.activity.MessageSearchAllPeopleActivity;
import com.zhaoweihao.architechturesample.activity.MessageValidationListActivity;
import com.zhaoweihao.architechturesample.adapter.MessageAdapter;
import com.zhaoweihao.architechturesample.adapter.MessageAddressListAdapter;
import com.zhaoweihao.architechturesample.bean.MessageUi;
import com.zhaoweihao.architechturesample.bean.ValidationMesgEvent;
import com.zhaoweihao.architechturesample.database.Memorandum;
import com.zhaoweihao.architechturesample.database.User;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MessageFragment extends Fragment {
    @BindView(R.id.fm_search_board)
    com.zhaoweihao.architechturesample.ui.SearchBoardLayout fm_search_board;
    @BindView(R.id.fm_recyclerView)
    RecyclerView fm_recyclerView;
    private MessageAdapter mAdapter;
    private List<MessageUi> MessageUiList = new ArrayList<>();
    private int num = 0;
    private Handler mainHandler = new Handler(Looper.getMainLooper());
    private Handler handler;
    private Timer timer;
    private TimerTask timerTask;
    private int expireMemorandumNotifiedNum = 0;

    public MessageFragment() {
    }

    public static MessageFragment newInstance() {
        return new MessageFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_message, container, false);
        ButterKnife.bind(this, view);
        fm_search_board.setSearchTip("找人");
        detectRedDotChange();
        EventBus.getDefault().register(this);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        init();
    }
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

    }

    public void init() {
        fm_search_board.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), MessageSearchAllPeopleActivity.class);
                startActivity(intent);
            }
        });
        LinearLayoutManager layoutManager2 = new LinearLayoutManager(getContext());
        fm_recyclerView.setLayoutManager(layoutManager2);
        initAdapter(num);
        initMessageUiList();

    }

    private void initAdapter(int num) {
        Log.v("tanxkkkx","validationMesgNum"+num);
        OnItemDragListener onItemDragListener = new OnItemDragListener() {
            @Override
            public void onItemDragStart(RecyclerView.ViewHolder viewHolder, int pos) {
            }

            @Override
            public void onItemDragMoving(RecyclerView.ViewHolder source, int from, RecyclerView.ViewHolder target, int to) {
            }

            @Override
            public void onItemDragEnd(RecyclerView.ViewHolder viewHolder, int pos) {
            }
        };

        OnItemSwipeListener onItemSwipeListener = new OnItemSwipeListener() {
            @Override
            public void onItemSwipeMoving(Canvas canvas, RecyclerView.ViewHolder viewHolder, float fl1, float fl2, boolean bl) {
            }

            @Override
            public void onItemSwipeStart(RecyclerView.ViewHolder viewHolder, int pos) {
            }

            @Override
            public void clearView(RecyclerView.ViewHolder viewHolder, int pos) {
            }

            @Override
            public void onItemSwiped(RecyclerView.ViewHolder viewHolder, int pos) {
            }
        };
        mAdapter = new MessageAdapter(MessageUiList, num);

        ItemDragAndSwipeCallback itemDragAndSwipeCallback = new ItemDragAndSwipeCallback(mAdapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(itemDragAndSwipeCallback);
        itemTouchHelper.attachToRecyclerView(fm_recyclerView);

        //开启拖拽
        mAdapter.enableDragItem(itemTouchHelper, R.id.lm_ib_drag, true);
        mAdapter.setOnItemDragListener(onItemDragListener);

        /*开启滑动删除
        mAdapter.enableSwipeItem();
        mAdapter.setOnItemSwipeListener(onItemSwipeListener);
         */
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                switch (position) {
                    case 0:
                        startActivity(new Intent(getContext(), MessageAddressListActivity.class));
                        break;
                    case 1:
                        startActivity(new Intent(getContext(), MessageInboxActivity.class));
                        break;
                    case 2:
                        startActivity(new Intent(getContext(), MessageMemorandumActivity.class));
                        break;
                    case 3:
                        startActivity(new Intent(getContext(), MessageValidationListActivity.class));
                        break;
                    default:
                        break;
                }
            }
        });
        fm_recyclerView.setAdapter(mAdapter);

    }

    private void initMessageUiList() {
        MessageUiList.clear();
        String[] Title = new String[]{"通讯录", "收件箱", "待办事项", "验证消息"};
        MessageUi mu1, mu2, mu3, mu4, mu5;
        mu1 = setData(Title[0], getContext().getResources().getDrawable(R.drawable.addresslist));
        mu2 = setData(Title[1], getContext().getResources().getDrawable(R.drawable.receivemesg));
        mu3 = setData(Title[2], getContext().getResources().getDrawable(R.drawable.countdown));
        //mu4 = setData(Title[3], getContext().getResources().getDrawable(R.drawable.readingrank));
        mu5 = setData(Title[3], getContext().getResources().getDrawable(R.drawable.checkinfo));
        MessageUiList.add(mu1);
        MessageUiList.add(mu2);
        MessageUiList.add(mu3);
        //MessageUiList.add(mu4);
        MessageUiList.add(mu5);
    }

    private MessageUi setData(String title, Drawable drawable) {
        MessageUi ui = new MessageUi();
        ui.setTitle(title);
        ui.setDrawable(drawable);
        return ui;
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void setText(ValidationMesgEvent validationMesgEvent) {
        this.num = validationMesgEvent.getMesgNum();
        mainHandler.post(new Runnable() {
            @Override
            public void run() {
                initAdapter(num);
            }
        });
    }

    private void detectRedDotChange() {


        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 12) {
                    init();
                }
                super.handleMessage(msg);
            }
        };
        timer = new Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                Message message = new Message();
                List<Memorandum> memorandumTemp = new ArrayList<>();
                memorandumTemp.clear();
                int expireMemoNotifiedNow = expireMemorandumNotifiedNum;
                int expireNotifiedTemp = 0;
                memorandumTemp.addAll(DataSupport.where("UserId=?", "" + DataSupport.findLast(User.class).getUserId()).find(Memorandum.class));
                for (Memorandum md : memorandumTemp) {
                    if (md.getNotify() && ifIsExPire(md.getDate().getTime())) {
                        expireNotifiedTemp++;
                    }
                }
                if (expireNotifiedTemp != expireMemoNotifiedNow) {
                    expireMemorandumNotifiedNum = expireNotifiedTemp;
                    message.what = 12;
                }
                handler.sendMessage(message);
            }
        };
        timer.schedule(timerTask, 200, 200);
    }

    private Boolean ifIsExPire(long endTime) {
        Date date = new Date();
        long longExpend = endTime - date.getTime();
        if (longExpend < 0) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        if (timerTask != null) {
            timerTask.cancel();
            timerTask = null;
        }
        if (timer != null) {
            timer.cancel();
            timer.purge();
            timer = null;
        }
        handler.removeMessages(11);
        handler.removeMessages(12);
    }
}
