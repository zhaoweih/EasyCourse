package com.zhaoweihao.architechturesample.activity;

import android.content.ContentValues;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;

import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;
import com.zhaoweihao.architechturesample.R;
import com.zhaoweihao.architechturesample.base.BaseActivity;
import com.zhaoweihao.architechturesample.database.User;
import com.zhaoweihao.architechturesample.ui.EditTextLayout;
import com.zhaoweihao.architechturesample.ui.TitleLayout;

import org.litepal.crud.DataSupport;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author tanxinkui
 * @description 个人-设置-修改描述界面
 * @time 2019/1/11 23:29
 */

public class PersonalSettingDescriptionActivity extends BaseActivity {
    @BindView(R.id.apsd_title)
    TitleLayout apsd_title;
    @BindView(R.id.apsd_edit_text)
    EditTextLayout apsd_edit_text;
    private QMUITipDialog tipSucceedDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_setting_descriptoin);
        ButterKnife.bind(this);
        init();

    }

    private void init() {
        apsd_title.setTitle("修改描述");
        apsd_title.getSettingButton().setText("保存");
        apsd_title.getSettingButton().setOnClickListener(view->onSave());
        apsd_edit_text.initWithArgs("新描述:","请输入新描述！",InputType.TYPE_CLASS_TEXT,15,false,false);
        tipSucceedDialog= new QMUITipDialog.Builder(PersonalSettingDescriptionActivity.this)
                .setTipWord("修改成功,重新登录后生效！")
                .setIconType(QMUITipDialog.Builder.ICON_TYPE_SUCCESS)
                .create();
    }
    private void onSave(){
        ContentValues values = new ContentValues();
        values.put("descrition",apsd_edit_text.getEditTextString());
        DataSupport.update(User.class, values, DataSupport.findLast(User.class).getId());
        tipSucceedDialog.show();
        dismissSucceedDialog();
    }
    private void dismissSucceedDialog(){
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                tipSucceedDialog.dismiss();
                finish();
            }
        }, 2000);
    }


}
