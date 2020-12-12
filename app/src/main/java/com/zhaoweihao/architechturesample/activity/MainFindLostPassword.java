package com.zhaoweihao.architechturesample.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.widget.Toast;

import com.blankj.utilcode.util.EncryptUtils;
import com.google.gson.Gson;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;
import com.zhaoweihao.architechturesample.R;
import com.zhaoweihao.architechturesample.base.BaseActivity;
import com.zhaoweihao.architechturesample.bean.ResetPassword;
import com.zhaoweihao.architechturesample.bean.RestResponse;
import com.zhaoweihao.architechturesample.database.User;
import com.zhaoweihao.architechturesample.ui.EditTextLayout;
import com.zhaoweihao.architechturesample.ui.TitleLayout;
import com.zhaoweihao.architechturesample.util.Constant;

import org.litepal.crud.DataSupport;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static android.text.InputType.TYPE_CLASS_TEXT;
import static com.zhaoweihao.architechturesample.util.HttpUtil.sendPostRequest;
import static com.zhaoweihao.architechturesample.util.Utils.log;

public class MainFindLostPassword extends BaseActivity {
    private static final Class thisClass = MainFindLostPassword.class;
    @BindView(R.id.amflp_title)
    TitleLayout amflp_title;
    @BindView(R.id.amflp_username)
    EditTextLayout amflp_username;
    @BindView(R.id.amflp_old_password)
    EditTextLayout amflp_old_password;
    @BindView(R.id.amflp_new_password)
    EditTextLayout amflp_new_password;
    @BindView(R.id.amflp_retype_new_password)
    EditTextLayout amflp_retype_new_password;
    @BindView(R.id.amflp_qmui_confirm)
    QMUIRoundButton amflp_qmui_confirm;
    private QMUITipDialog tipDialog, tipSucceedDialog, tipFailDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_find_lost_password);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        amflp_title.setTitle("找回密码");
        //amflp_old_password.initWithArgs("答案:", "请输入旧密码!", InputType.TYPE_NUMBER_VARIATION_PASSWORD | TYPE_CLASS_TEXT, 10, true, true, false);
        amflp_username.initWithArgs("用户名:", "请输入您的用户名!", InputType.TYPE_NUMBER_VARIATION_PASSWORD | TYPE_CLASS_TEXT, 10, true, true, false);
        amflp_old_password.initWithArgs("答案:", "请输入您设置的答案!", InputType.TYPE_NUMBER_VARIATION_PASSWORD | TYPE_CLASS_TEXT, 10, true, true, false);
        amflp_new_password.initWithArgs("新密码:", "请输入新密码!", InputType.TYPE_NUMBER_VARIATION_PASSWORD | TYPE_CLASS_TEXT, 10, true, true, false);
        amflp_retype_new_password.initWithArgs("确认密码：", "请确认密码!", InputType.TYPE_NUMBER_VARIATION_PASSWORD | TYPE_CLASS_TEXT, 10, true, true, false);
        amflp_qmui_confirm.setOnClickListener(view -> modifyPwd());
        tipFailDialog = new QMUITipDialog.Builder(MainFindLostPassword.this)
                .setTipWord("修改失败，请检答案！")
                .setIconType(QMUITipDialog.Builder.ICON_TYPE_FAIL)
                .create();
        tipSucceedDialog = new QMUITipDialog.Builder(MainFindLostPassword.this)
                .setTipWord("修改成功,请重新登录！")
                .setIconType(QMUITipDialog.Builder.ICON_TYPE_SUCCESS)
                .create();
        tipDialog = new QMUITipDialog.Builder(MainFindLostPassword.this)
                .setTipWord("正在修改密码！")
                .setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
                .create();

    }

    private Boolean isNewPwdEmpty() {
        return (amflp_new_password.getEditTextString().equals(""));
    }

    private Boolean isReTypeCorrect() {
        return (!amflp_new_password.getEditTextString().equals(amflp_retype_new_password.getEditTextString()));
    }

    private Boolean isOldAndNewPwdSame() {
        return (amflp_old_password.getEditTextString().equals(amflp_new_password.getEditTextString()));
    }

    private void modifyPwd() {
        if (isOldAndNewPwdSame()) {
            toastTip("新密码与原密码必须不一致！");
        } else if (isNewPwdEmpty()) {
            toastTip("新密码不为空！");
        } else if (isReTypeCorrect()) {
            toastTip("两次输入新密码不一致！");
        } else {
            if(TextUtils.isEmpty( amflp_username.getEditTextString())){
                toastTip("用户名不为空！");
            }else {
                tipDialog.show();
                requestModify();
            }
        }
    }

    private void toastTip(String toastString) {
        Toast.makeText(MainFindLostPassword.this, "" + toastString, Toast.LENGTH_LONG).show();
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

    private void requestModify() {
        /*ModifyPassword modifyPassword = new ModifyPassword();
        modifyPassword.setId(DataSupport.findLast(User.class).getUserId());
        modifyPassword.setNewPassword(amflp_new_password.getEditTextString());
        modifyPassword.setOldPassword(amflp_old_password.getEditTextString());*/
        ResetPassword resetPassword = new ResetPassword();
        resetPassword.setAnswer(amflp_old_password.getEditTextString());
        resetPassword.setUsername(amflp_username.getEditTextString());
        resetPassword.setNew_password(addSalt(amflp_new_password.getEditTextString()));
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
                if (restResponse.getCode() == 200) {
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
    protected void onPause() {
        super.onPause();
        tipSucceedDialog.dismiss();
    }

    private void dismissFailDialog() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                tipFailDialog.dismiss();
            }
        }, 2000);
    }

    private void dismissSucceedDialog() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                tipSucceedDialog.dismiss();
                DataSupport.findLast(User.class).delete();
                finish();
                Intent intent = new Intent(MainFindLostPassword.this, MainLoginActivity.class);
                startActivity(intent);
            }
        }, 2000);
    }

}
