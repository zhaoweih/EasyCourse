package com.zhaoweihao.architechturesample.activity;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.qmuiteam.qmui.widget.dialog.QMUIBottomSheet;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;
import com.zhaoweihao.architechturesample.R;
import com.zhaoweihao.architechturesample.adapter.MessageMemorandumAdapter;
import com.zhaoweihao.architechturesample.adapter.MessageValidationListAdapter;
import com.zhaoweihao.architechturesample.base.BaseActivity;
import com.zhaoweihao.architechturesample.database.Memorandum;
import com.zhaoweihao.architechturesample.database.User;
import com.zhaoweihao.architechturesample.ui.TitleLayout;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author
 * @description 消息-待办事项
 * @time 2019/3/12 15:44
 */
public class MessageMemorandumActivity extends BaseActivity {
    @BindView(R.id.amm_recyclerView)
    RecyclerView amm_recyclerView;
    @BindView(R.id.amm_titleLayout)
    TitleLayout amm_titleLayout;
    @BindView(R.id.amm_empty_view)
    LinearLayout amm_empty_view;
    @BindView(R.id.amm_tv_selector)
    TextView amm_tv_selector;
    @BindView(R.id.amm_tv_selector2)
    TextView amm_tv_selector2;
    @BindView(R.id.amm_ly_selector)
    LinearLayout amm_ly_selector;
    @BindView(R.id.amm_ly_selector2)
    LinearLayout amm_ly_selector2;
    private Button addBtn;
    private List<Memorandum> memorandums = new ArrayList<>();
    private QMUIBottomSheet qmuiBottomSheet;
    private QMUIBottomSheet qmuiBottomSheet2;
    private MessageMemorandumAdapter messageMemorandumAdapter;
    private Timer timer;
    private TimerTask timerTask;
    private Handler myHandler;
    private String selector1 = "全部状态";
    private String selector2 = "全部重要性";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_memorandum);
        ButterKnife.bind(this);
        myHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                HandleMessageInformation(msg);
                super.handleMessage(msg);
            }
        };
        timer = new Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                runInformation();
            }
        };
        timer.schedule(timerTask, 30000, 30000);
        //延时1s，每隔500毫秒执行一次run方法
        setQmuiBottomSheet();
    }

    public void HandleMessageInformation(Message msg) {
        if (msg.what == 111) {
            initWithSelector();
        }
    }

    public void runInformation() {
        Message message = new Message();
        message.what = 111;
        myHandler.sendMessage(message);
    }

    @Override
    protected void onResume() {
        super.onResume();
        init();
    }

    private void init() {
        amm_titleLayout.setTitle("待办事项");
        addBtn = amm_titleLayout.getSettingButton();
        addBtn.setText("添加");
        addBtn.setOnClickListener(view -> {
            Log.v("tanbtn","123456");
            startActivity(new Intent(MessageMemorandumActivity.this, MessageMemorandumAddActivity.class));
        });
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        amm_recyclerView.setLayoutManager(layoutManager);
        initWithSelector();
        amm_ly_selector.setOnClickListener(view -> {
            qmuiBottomSheet.show();
        });
        amm_ly_selector2.setOnClickListener(view -> {
            qmuiBottomSheet2.show();
        });

    }

    private void setQmuiBottomSheet() {
        qmuiBottomSheet = new QMUIBottomSheet.BottomListSheetBuilder(MessageMemorandumActivity.this)
                .addItem(R.drawable.all, "全部状态", "全部状态")
                .addItem(R.drawable.expire, "已到期", "已到期")
                .addItem(R.drawable.running, "进行中", "进行中")
                .setOnSheetItemClickListener((QMUIBottomSheet dialog, View itemView, int position, String tag) -> {
                            selector1 = tag;
                            qmuiBottomSheet.dismiss();
                            initWithSelector();
                        }
                )
                .build();
        qmuiBottomSheet2 = new QMUIBottomSheet.BottomListSheetBuilder(MessageMemorandumActivity.this)
                .addItem(R.drawable.all, "全部重要性", "全部重要性")
                .addItem(R.drawable.memorandum_general, "一般", "一般")
                .addItem(R.drawable.memorandum_important, "重要", "重要")
                .addItem(R.drawable.memorandum_extremely_important, "非常重要", "非常重要", true)
                .setOnSheetItemClickListener((QMUIBottomSheet dialog, View itemView, int position, String tag) -> {
                            selector2 = tag;
                            qmuiBottomSheet2.dismiss();
                            initWithSelector();
                        }
                )
                .build();
    }

    private void initWithSelector() {
        amm_tv_selector.setText(selector1);
        amm_tv_selector2.setText(selector2);
        memorandums.clear();

        List<Memorandum> memorandumTemp = new ArrayList<>();
        memorandumTemp.clear();
        if (selector2.equals("全部重要性")) {
            memorandumTemp.addAll(DataSupport.where("UserId=?", "" + DataSupport.findLast(User.class).getUserId()).find(Memorandum.class));
            setSelectData(memorandumTemp);
        } else {
            memorandumTemp.addAll(DataSupport.where("UserId=? and tag=?",
                    DataSupport.findLast(User.class).getUserId() + "", selector2)
                    .find(Memorandum.class));
            setSelectData(memorandumTemp);
        }
        setAdapeter();

    }

    private void setSelectData(List<Memorandum> memorandumTemp) {
        if (selector1.equals("全部状态")) {
            memorandums = memorandumTemp;
        } else if (selector1.equals("已到期")) {
            for (Memorandum memorandum1 : memorandumTemp) {
                if (ifIsExPire(memorandum1.getDate().getTime())) {
                    memorandums.add(memorandum1);
                }
            }
        } else {
            for (Memorandum memorandum1 : memorandumTemp) {
                if (!ifIsExPire(memorandum1.getDate().getTime())) {
                    memorandums.add(memorandum1);
                }
            }
        }
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

    private void deleteMemo(int id) {
        DataSupport.delete(Memorandum.class, id);
        initWithSelector();
    }

    private List<Memorandum> sortList(List<Memorandum> memorandums1) {
        List<Memorandum> sorted = new ArrayList<>();
        sorted.clear();
        for (Memorandum ei : memorandums1) {
            if (ei.getTag().equals("非常重要")) {
                sorted.add(ei);
            }
        }
        for (Memorandum ei : memorandums1) {
            if (ei.getTag().equals("重要")) {
                sorted.add(ei);
            }
        }
        for (Memorandum ei : memorandums1) {
            if (ei.getTag().equals("一般")) {
                sorted.add(ei);
            }
        }
        return sorted;
    }

    private void setAdapeter() {
        List<Memorandum> sortTemp = new ArrayList<>();
        sortTemp.clear();
        sortTemp.addAll(memorandums);
        Log.v("tanxuuu","dd"+memorandums.size());
        memorandums.clear();
        memorandums = sortList(sortTemp);
        Log.v("tanxuuu","ddd"+memorandums.size());
        messageMemorandumAdapter = new MessageMemorandumAdapter(memorandums);
        if (memorandums.size() == 0) {
            amm_empty_view.setVisibility(View.VISIBLE);
        } else {
            amm_empty_view.setVisibility(View.GONE);
        }
        messageMemorandumAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent detailIntent=new Intent(MessageMemorandumActivity.this,MessageMemorandumDetailActivity.class);
                detailIntent.putExtra("MemorandumDetail",memorandums.get(position));
                startActivity(detailIntent);
            }
        });
        messageMemorandumAdapter.setOnItemLongClickListener(new BaseQuickAdapter.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(BaseQuickAdapter adapter, View view, int position) {
                new QMUIDialog.MessageDialogBuilder(MessageMemorandumActivity.this)
                        .setTitle("删除待办事项")
                        .setMessage("确定要删除该事项吗？")
                        .addAction("取消", new QMUIDialogAction.ActionListener() {
                            @Override
                            public void onClick(QMUIDialog dialog, int index) {
                                dialog.dismiss();
                            }
                        })
                        .addAction(0, "确定", QMUIDialogAction.ACTION_PROP_NEGATIVE, new QMUIDialogAction.ActionListener() {
                            @Override
                            public void onClick(QMUIDialog dialog, int index) {
                                dialog.dismiss();
                                deleteMemo(memorandums.get(position).getId());
                            }
                        })
                        .show();
                return false;
            }
        });
        amm_recyclerView.setAdapter(messageMemorandumAdapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timerTask != null) {
            timerTask.cancel();
            timerTask = null;
        }
        if (timer != null) {
            timer.cancel();
            timer.purge();
            timer = null;
        }
        myHandler.removeMessages(11);
        myHandler.removeMessages(12);
    }
}
