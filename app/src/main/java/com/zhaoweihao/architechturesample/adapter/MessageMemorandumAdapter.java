package com.zhaoweihao.architechturesample.adapter;

import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;
import com.zhaoweihao.architechturesample.R;
import com.zhaoweihao.architechturesample.database.Memorandum;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class MessageMemorandumAdapter extends BaseQuickAdapter<Memorandum, BaseViewHolder> {
    private QMUIRoundButton qmuiRoundButton;
    private ImageView lml_iv_mesg;

    public MessageMemorandumAdapter(@Nullable List<Memorandum> data) {
        super(R.layout.layout_memorandum_list, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Memorandum item) {
        Date date = new Date();
        helper.setImageDrawable(R.id.lml_iv_course_icon, getOrderDrawable(item.getTag()))
                .setText(R.id.lml_tv_title, item.getTitle())
                .setText(R.id.lml_tv_teacher_name, getTime(item.getDate()))
                .setText(R.id.lml_tv_setTag, item.getTag())
                .setText(R.id.lml_iv_collect, item.getTag())
                .setTextColor(R.id.lml_tv_setTag, getColor(item.getTag()));

        qmuiRoundButton = helper.getView(R.id.lml_iv_collect);
       // qmuiRoundButton.setTextColor(getColor(item.getTag()));
        lml_iv_mesg = helper.getView(R.id.lml_iv_mesg);
        qmuiRoundButton.setText(getTimeExpend(date.getTime(), item.getDate().getTime()));

        Log.v("radaheufia", date.toString() + item.getDate().toString());
    }

    private String getTimeExpend(long startTime, long endTime) {

        long longExpend = endTime - startTime;
        Log.v("radaheufia", "log" + longExpend);
        long longHours = longExpend / (60 * 60 * 1000);
        Log.v("radaheufia", "log" + longHours);
        long longMinutes = (longExpend - longHours * (60 * 60 * 1000)) / (60 * 1000);
        Log.v("radaheufia", "log" + longMinutes);
        // long longSeconds = (longExpend - longHours * (60 * 60 * 1000)-longMinutes*(60*1000)) /  1000;
        Log.v("radaheufia", "log" + longMinutes);
        if (longExpend < 0) {
            lml_iv_mesg.setVisibility(View.VISIBLE);
            return "已到期";
        } else {
            lml_iv_mesg.setVisibility(View.GONE);
        }
        if (longHours > 24) {
            long day = longHours / 24;
            long hour = longHours % 24;
            return "还剩：" + day + "天" + hour + "时" + longMinutes + "分";
        }
        return "还剩：" + longHours + "时" + longMinutes + "分";
    }


    private int getColor(String s) {
        int finalInt = 0;
        String[] importance = {"一般", "重要", "非常重要"};
        for (int i = 0; i < importance.length; i++) {
            if (importance[i].equals(s)) {
                finalInt = i;
            }
        }
        return getIntColor(finalInt);
    }

    private int getIntColor(int order) {
        Integer[] colors = {ContextCompat.getColor(mContext, R.color.yellow), ContextCompat.getColor(mContext, R.color.lightBlue),
                ContextCompat.getColor(mContext, R.color.red)};
        return colors[order];
    }

    private String getTime(Date date) {
        //可根据需要自行截取数据显示
        // SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        //SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd  HH:mm");
        return format.format(date);
    }

    private Drawable getDrawable(int order) {

        Drawable[] importancePic = {ContextCompat.getDrawable(mContext, R.drawable.memorandum_general),
                ContextCompat.getDrawable(mContext, R.drawable.memorandum_important),
                ContextCompat.getDrawable(mContext, R.drawable.memorandum_extremely_important)};
        return importancePic[order];
    }

    private Drawable getOrderDrawable(String s) {
        int finalInt = 0;
        String[] importance = {"一般", "重要", "非常重要"};
        for (int i = 0; i < importance.length; i++) {
            if (importance[i].equals(s)) {
                finalInt = i;
            }
        }
        return getDrawable(finalInt);
    }
}
