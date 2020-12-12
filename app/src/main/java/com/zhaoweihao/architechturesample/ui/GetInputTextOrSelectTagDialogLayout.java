package com.zhaoweihao.architechturesample.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;
import com.zhaoweihao.architechturesample.R;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import timeselector.inter.CustomListener;
import timeselector.view.TimePickerView;

public class GetInputTextOrSelectTagDialogLayout extends LinearLayout {
    @BindView(R.id.lgiet_fl)
    FrameLayout lgiet_fl;
    @BindView(R.id.lgiet_tv_title)
    TextView lgiet_tv_title;
    @BindView(R.id.lgiet_iv)
    ImageView lgiet_iv;
    private QMUIDialog.EditTextDialogBuilder editTextDialogBuilder;
    private QMUIDialog.CheckableDialogBuilder tagQmuiDialog;
    private int selectTagId = 0;
    private String[] tags;
    private String finalText;
    //显示选择截止日期
    TimePickerView pvCustomTime;
    //截止时间
    Date expireDate;

    public GetInputTextOrSelectTagDialogLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.layout_get_input_edit_text, this);
        ButterKnife.bind(this);
        setClickListener();
    }

    private void setClickListener() {
        lgiet_fl.setOnClickListener(view -> {
            if (editTextDialogBuilder != null) {
                editTextDialogBuilder.show();
            }
            if (tags != null) {
                tagQmuiDialog = new QMUIDialog.CheckableDialogBuilder(getContext())
                        .setTitle("请选择您的标签:")
                        .setCheckedIndex(selectTagId)
                        .addItems(tags, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if (lgiet_iv.getVisibility() == VISIBLE) {
                                    lgiet_iv.setVisibility(INVISIBLE);
                                }
                                finalText = tags[i];
                                lgiet_tv_title.setText("标签：" + tags[i]);
                                selectTagId = i;
                                dialogInterface.dismiss();
                            }
                        });
                tagQmuiDialog.show();
            }
            if (pvCustomTime != null) {
                pvCustomTime.show();
            }
        });
        lgiet_iv.setVisibility(VISIBLE);
    }

    public String getFinalInputText() {
        if (lgiet_iv.getVisibility() == INVISIBLE) {
            return finalText;
        } else {
            return null;
        }
    }
    public long getFinalLongTime(){
        return expireDate.getTime();
    }

    public int getFinalTagID() {
        return selectTagId;
    }

    public void setTagText(String[] tags) {
        lgiet_tv_title.setHint("请选择标签");
        this.tags = tags;

    }
    public void setTagDefaultText(String[] tags,String DefaultText) {
        lgiet_tv_title.setText(DefaultText);
        this.tags = tags;
    }

    public Boolean ifAlready() {
        if (lgiet_iv.getVisibility() == INVISIBLE) {
            return true;
        } else {
            return false;
        }
    }

    public void setHintText(String TextPrefix, String hintText, String DialogTitle, String dialogHintText) {
        lgiet_tv_title.setHint(hintText);
        editTextDialogBuilder = new QMUIDialog.EditTextDialogBuilder(getContext())
                .setTitle(DialogTitle)
                .setPlaceholder(dialogHintText)
                .addAction("取消", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        dialog.dismiss();
                    }
                })
                .addAction("确定", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        if (!TextUtils.isEmpty(editTextDialogBuilder.getEditText().getText().toString())) {
                            lgiet_tv_title.setText(TextPrefix + editTextDialogBuilder.getEditText().getText().toString());
                            finalText=editTextDialogBuilder.getEditText().getText().toString();
                            if (lgiet_iv.getVisibility() == VISIBLE) {
                                lgiet_iv.setVisibility(INVISIBLE);
                            }
                            dialog.dismiss();
                        } else {
                            Toast.makeText(getContext(), "请输入！", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    public void setDefaultText(String TextPrefix, String DefaultText, String DialogTitle, String dialogHintText) {
        lgiet_tv_title.setText(DefaultText);
        editTextDialogBuilder = new QMUIDialog.EditTextDialogBuilder(getContext())
                .setTitle(DialogTitle)
                .setPlaceholder(dialogHintText)
                .addAction("取消", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        dialog.dismiss();
                    }
                })
                .addAction("确定", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        if (!TextUtils.isEmpty(editTextDialogBuilder.getEditText().getText().toString())) {
                            lgiet_tv_title.setText(TextPrefix + editTextDialogBuilder.getEditText().getText().toString());
                            finalText=editTextDialogBuilder.getEditText().getText().toString();
                            if (lgiet_iv.getVisibility() == VISIBLE) {
                                lgiet_iv.setVisibility(INVISIBLE);
                            }
                            dialog.dismiss();
                        } else {
                            Toast.makeText(getContext(), "请输入！", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void setTime(String prefixString,String hintText,int addDays) {
        lgiet_iv.setVisibility(VISIBLE);
        lgiet_tv_title.setText("");
        lgiet_tv_title.setHint(hintText);
        initCustomTimePicker(prefixString,addDays);
    }
    public void setTimeWhithDefaultText(String prefixString,String Text,int addDays) {
        lgiet_iv.setVisibility(VISIBLE);
        lgiet_tv_title.setText(Text);
        initCustomTimePicker(prefixString,addDays);
    }
    public String getTimeString(){
        return lgiet_tv_title.getText().toString();
    }

    public Date getExpireDate() {
        return expireDate;
    }


    private String getTime(Date date) {
        //可根据需要自行截取数据显示
        // SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        //SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd  HH:mm");
        return format.format(date);
    }

    private void initCustomTimePicker(String PrefixString,int addDays) {

        //系统当前时间
        Calendar selectorDate = Calendar.getInstance();
        Calendar startDate = Calendar.getInstance();
        startDate.add(Calendar.DAY_OF_MONTH, addDays);
        //startDate.set(2000, 1, 23);
        //startDate.set();
        //系统当前时间
        Calendar endDate = Calendar.getInstance();
        endDate.set(2028, 2, 28,23,59);
        //时间选择器 ，自定义布局
        pvCustomTime = new TimePickerView.Builder(getContext(), new TimePickerView.OnTimeSelectorListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//选中事件回调
                lgiet_tv_title.setText(PrefixString+ getTime(date));
                expireDate = date;
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
                                        lgiet_iv.setVisibility(INVISIBLE);
                                        pvCustomTime.dismiss();
                                    }
                                });
                            }
                        }).setType(new boolean[]{true, true, true,true, true, false })
                .setLabel("-", "-", "", "：", "")
                .isCenterLabel(false)
                // .setDividerColor(0xFF24AD9D)
                .build();
    }

}
