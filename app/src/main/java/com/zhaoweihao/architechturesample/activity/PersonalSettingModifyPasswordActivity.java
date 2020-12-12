package com.zhaoweihao.architechturesample.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.widget.Toast;


import com.blankj.utilcode.util.EncryptUtils;
import com.google.gson.Gson;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;
import com.zhaoweihao.architechturesample.R;
import com.zhaoweihao.architechturesample.base.BaseActivity;
import com.zhaoweihao.architechturesample.bean.ModifyPassword;
import com.zhaoweihao.architechturesample.bean.ResetPassword;
import com.zhaoweihao.architechturesample.bean.RestResponse;
import com.zhaoweihao.architechturesample.database.User;
import com.zhaoweihao.architechturesample.ui.EditTextLayout;
import com.zhaoweihao.architechturesample.ui.TitleLayout;
import com.zhaoweihao.architechturesample.util.Constant;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static com.zhaoweihao.architechturesample.util.HttpUtil.sendPostRequest;

import org.litepal.crud.DataSupport;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.text.InputType.TYPE_CLASS_TEXT;
import static com.zhaoweihao.architechturesample.util.Utils.log;

/**
 * @author tanxinkui
 * @description 个人-设置-修改密码
 * @time 2019/1/9 23:20
 */
public class PersonalSettingModifyPasswordActivity extends BaseActivity {
    private static final Class thisClass = PersonalSettingModifyPasswordActivity.class;
    @BindView(R.id.apsmp_title)
    TitleLayout apsmp_title;
    @BindView(R.id.apsmp_old_password)
    EditTextLayout apsmp_old_password;
    @BindView(R.id.apsmp_new_password)
    EditTextLayout apsmp_new_password;
    @BindView(R.id.apsmp_retype_new_password)
    EditTextLayout apsmp_retype_new_password;
    @BindView(R.id.apsmp_qmui_confirm)
    QMUIRoundButton apsmp_qmui_confirm;
    private QMUITipDialog tipDialog,tipSucceedDialog,tipFailDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_setting_modify_password);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        apsmp_title.setTitle("修改密码");
        //apsmp_old_password.initWithArgs("答案:", "请输入旧密码!", InputType.TYPE_NUMBER_VARIATION_PASSWORD | TYPE_CLASS_TEXT, 10, true, true, false);
        apsmp_old_password.initWithArgs("答案:", ""+DataSupport.findLast(User.class).getQuestion(), InputType.TYPE_NUMBER_VARIATION_PASSWORD | TYPE_CLASS_TEXT, 10, true, true, false);
        apsmp_new_password.initWithArgs("新密码:", "请输入新密码!", InputType.TYPE_NUMBER_VARIATION_PASSWORD | TYPE_CLASS_TEXT, 10, true, true, false);
        apsmp_retype_new_password.initWithArgs("确认密码：", "请确认密码!", InputType.TYPE_NUMBER_VARIATION_PASSWORD | TYPE_CLASS_TEXT, 10, true, true, false);
        apsmp_qmui_confirm.setOnClickListener(view -> modifyPwd());
        tipFailDialog =new QMUITipDialog.Builder(PersonalSettingModifyPasswordActivity.this)
                .setTipWord("修改失败，请检答案！")
                .setIconType(QMUITipDialog.Builder.ICON_TYPE_FAIL)
                .create();
        tipSucceedDialog= new QMUITipDialog.Builder(PersonalSettingModifyPasswordActivity.this)
                .setTipWord("修改成功,请重新登录！")
                .setIconType(QMUITipDialog.Builder.ICON_TYPE_SUCCESS)
                .create();
        tipDialog=new QMUITipDialog.Builder(PersonalSettingModifyPasswordActivity.this)
                .setTipWord("正在修改密码！")
                .setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
                .create();

    }

    private Boolean isNewPwdEmpty() {
        return (apsmp_new_password.getEditTextString().equals(""));
    }

    private Boolean isReTypeCorrect() {
        return (!apsmp_new_password.getEditTextString().equals(apsmp_retype_new_password.getEditTextString()));
    }

    private Boolean isOldAndNewPwdSame() {
        return (apsmp_old_password.getEditTextString().equals(apsmp_new_password.getEditTextString()));
    }

    private void modifyPwd() {
        if (isOldAndNewPwdSame()) {
            toastTip("新密码与原密码必须不一致！");
        } else if (isNewPwdEmpty()) {
            toastTip("新密码不为空！");
        } else if (isReTypeCorrect()) {
            toastTip("两次输入新密码不一致！");
        } else {
            tipDialog.show();
            requestModify();
        }
    }

    private void toastTip(String toastString) {
        Toast.makeText(PersonalSettingModifyPasswordActivity.this, "" + toastString, Toast.LENGTH_LONG).show();
    }

    private void requestModify() {
        /*ModifyPassword modifyPassword = new ModifyPassword();
        modifyPassword.setId(DataSupport.findLast(User.class).getUserId());
        modifyPassword.setNewPassword(apsmp_new_password.getEditTextString());
        modifyPassword.setOldPassword(apsmp_old_password.getEditTextString());*/
        ResetPassword resetPassword=new ResetPassword();
        resetPassword.setAnswer(apsmp_old_password.getEditTextString());
        resetPassword.setUsername(DataSupport.findLast(User.class).getUsername());
        resetPassword.setNew_password(addSalt(apsmp_new_password.getEditTextString()));
        String json = new Gson().toJson(resetPassword);
        sendPostRequest(Constant.RESRT_PASSWORD_URL, json, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //网络错误处理
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String body = response.body().string();
                log(thisClass, "body" + body);
                //解析json数据组装RestResponse对象
                RestResponse restResponse = new Gson().fromJson(body, RestResponse.class);
                // 修复登录密码不正确异常
                if (restResponse == null) {
                    tipDialog.dismiss();
                    runOnUiThread(() -> {

                    });
                    return;
                }
                log(thisClass, "response" + restResponse);
                log(thisClass, "getCode" + restResponse.getCode());
                if (restResponse.getCode() == 500) {
                    tipDialog.dismiss();
                    try {
                        //切换主进程更新UI
                        runOnUiThread(() -> {
                           tipFailDialog.show();
                            dismissFailDialog();
                        });

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
                if (restResponse.getCode() == 400) {
                    tipDialog.dismiss();
                    try {
                        //切换主进程更新UI
                        runOnUiThread(() -> {
                            tipFailDialog.show();
                            dismissFailDialog();
                        });

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
                // code 200等于登录成功
                if (restResponse.getCode() ==200) {
                    tipDialog.dismiss();
                    try {
                        runOnUiThread(() -> {
                            tipSucceedDialog.show();
                            dismissSucceedDialog();
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
    @Override
    protected void onPause(){
        super.onPause();
        tipSucceedDialog.dismiss();
    }
    private void dismissFailDialog(){
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
               tipFailDialog.dismiss();
            }
        }, 2000);
    }
    private String addSalt(String plainPassword){
        //md5加密
        String md5PlainPassword = EncryptUtils.encryptMD5ToString(plainPassword);
        //md5密码后加盐
        String salt = "6NCkDWrVJy5K9v2w";
        String saltPassword = md5PlainPassword + salt;
        //加盐后的密码再MD5加密(最终密码)
        return EncryptUtils.encryptMD5ToString(saltPassword);
    }
    private void dismissSucceedDialog(){
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                tipSucceedDialog.dismiss();
                DataSupport.findLast(User.class).delete();
                finish();
                Intent intent=new Intent(PersonalSettingModifyPasswordActivity.this,MainLoginActivity.class);
                startActivity(intent);
            }
        }, 2000);
    }


}
