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
import static android.text.InputType.TYPE_CLASS_TEXT;


/**
*@description 个人-设置-修改电话界面
*@author tanxinkui
*@time 2019/1/11 23:28
*/
public class PersonalSettingPhoneActivity extends BaseActivity {
    @BindView(R.id.apsp_title)
    TitleLayout apsp_title;
    @BindView(R.id.apsp_edit_text)
    EditTextLayout apsp_edit_text;
    private QMUITipDialog tipSucceedDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_setting_phone);
        ButterKnife.bind(this);
        init();
    }
    private void init(){
        apsp_title.setTitle("修改描述");
        apsp_title.getSettingButton().setText("保存");
        apsp_title.getSettingButton().setOnClickListener(view->onSave());
        apsp_edit_text.initWithArgs("新号码:","请输入新号码！",InputType.TYPE_CLASS_PHONE|TYPE_CLASS_TEXT,11,false,false);
        tipSucceedDialog= new QMUITipDialog.Builder(PersonalSettingPhoneActivity.this)
                .setTipWord("修改成功,重新登录后生效！")
                .setIconType(QMUITipDialog.Builder.ICON_TYPE_SUCCESS)
                .create();
    }
    private void onSave(){
        ContentValues values = new ContentValues();
        values.put("phone",apsp_edit_text.getEditTextString());
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
