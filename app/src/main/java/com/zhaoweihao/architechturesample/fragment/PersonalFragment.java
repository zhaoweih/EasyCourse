package com.zhaoweihao.architechturesample.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.qmuiteam.qmui.widget.QMUIRadiusImageView;
import com.zhaoweihao.architechturesample.R;
import com.zhaoweihao.architechturesample.activity.HomeActivityActivity;
import com.zhaoweihao.architechturesample.activity.HomeCourseActivity;
import com.zhaoweihao.architechturesample.activity.HomeResourceActivity;
import com.zhaoweihao.architechturesample.activity.MainLoginActivity;
import com.zhaoweihao.architechturesample.activity.NoteActivity;
import com.zhaoweihao.architechturesample.activity.PersonalSettingActivity;
import com.zhaoweihao.architechturesample.activity.PersonalSettingCountManagementActivity;
import com.zhaoweihao.architechturesample.database.User;
import com.zhaoweihao.architechturesample.ui.NetWorkImageView;
import com.zhaoweihao.architechturesample.util.CheckLogin;
import com.zhaoweihao.architechturesample.util.Constant;

import org.litepal.crud.DataSupport;

import java.io.FileNotFoundException;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PersonalFragment extends Fragment {

    @BindView(R.id.fl_course)
    FrameLayout course;
    @BindView(R.id.fl_ps)
    FrameLayout personal;
    @BindView(R.id.fl_note)
    FrameLayout fl_note;
    @BindView(R.id.fl_resource)
    FrameLayout fl_resource;
    @BindView(R.id.fi_fl_setting)
    FrameLayout fi_fl_setting;
    @BindView(R.id.fl_activity)
    FrameLayout fl_activity;
    @BindView(R.id.nf_ivqrcode)    //generate QrCode
            ImageView nf_ivqrcode;
    @BindView(R.id.if_qrcode)     //  QrCode
            ImageView if_qrcode;
    @BindView(R.id.nf_llayoutQrcode)//show QrCode layout
            LinearLayout nf_llayoutQrcode;
    @BindView(R.id.nf_llayouthiddenqrcode)//hidden qrcode
            LinearLayout nf_llayouthiddenqrcode;
    @BindView(R.id.nf_tvqrcodedescription) // show invitation code
            TextView nf_tvqrcodedescription;
    @BindView(R.id.fp_im_head)
    NetWorkImageView fp_im_head;
    @BindView(R.id.fp_tv_name)
    TextView fp_tv_name;
    private static final String IMAGE_FILE_LOCATION = "file:///" + Environment.getExternalStorageDirectory().getPath() + "/temp.jpg";
    private Uri imageUri = Uri.parse(IMAGE_FILE_LOCATION);

    public PersonalFragment() {

    }

    public static PersonalFragment newInstance() {
        return new PersonalFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_personal, container, false);
        ButterKnife.bind(this, view);
        try {
            if (getUserVisibleHint()) {//界面可见时
                initSetOnClickListener();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return view;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
    }

    private void initSetOnClickListener() {
        course.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), HomeCourseActivity.class);
            startActivity(intent);
        });
        personal.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), PersonalSettingCountManagementActivity.class);
            startActivity(intent);
        });
        fl_resource.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), HomeResourceActivity.class);
            startActivity(intent);
        });
        fi_fl_setting.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), PersonalSettingActivity.class);
            startActivity(intent);
        });
        fl_activity.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), HomeActivityActivity.class);
            startActivity(intent);
        });
        nf_ivqrcode.setOnClickListener(v -> {
            if (nf_llayoutQrcode.getVisibility() == View.INVISIBLE) {
                nf_llayoutQrcode.setVisibility(View.VISIBLE);
                nf_llayouthiddenqrcode.setVisibility(View.VISIBLE);
                generateQrCode();
            } else {
                nf_llayouthiddenqrcode.setVisibility(View.INVISIBLE);
                nf_llayoutQrcode.setVisibility(View.INVISIBLE);
            }

        });
        nf_llayouthiddenqrcode.setOnClickListener(v -> {
            nf_llayoutQrcode.setVisibility(View.INVISIBLE);
            nf_llayouthiddenqrcode.setVisibility(View.INVISIBLE);
        });
        fl_note.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), NoteActivity.class);
            startActivity(intent);
        });
    }

    private void setHead() {
        User user = DataSupport.findLast(User.class);
        if (user == null) {
            return;
        }
        if (user.getAvatar() != null) {
            fp_im_head.setImageURL(Constant.getBaseUrl() + "upload/" + DataSupport.findLast(User.class).getAvatar());
        }
    }

    @Override
    public void onResume() {
        super.onResume();


    }

    private void init() {
        if (CheckLogin.ifUserLogin()) {
            fp_tv_name.setText(DataSupport.findLast(User.class).getName());
        } else {
            Intent intent = new Intent(getActivity(), MainLoginActivity.class);
            startActivity(intent);
        }
        setHead();
    }
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if(hidden){

        } else {
            init();
        }
    }

    public void generateQrCode() {
        try {
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            //生成二维码的内容 例如是教师编号
            String content = DataSupport.findLast(User.class).getUsername();
            Spannable invitationCode = new SpannableString("邀请码:" + content);
            showInvitationCode(invitationCode, (4 + content.length()));
            //生成二维码的bitmap
            Bitmap bitmap = barcodeEncoder.encodeBitmap(content, BarcodeFormat.QR_CODE, 400, 400);
            if_qrcode.setImageBitmap(bitmap);
        } catch (Exception e) {
        }
    }

    public void showInvitationCode(Spannable invitationCode, int totalLength) {

        //Spannable string = new SpannableString("修改背景色、粗体、字体大小");
        // 背景色
        //invitationCode.setSpan(new BackgroundColorSpan(Color.RED), 2, 5, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        // 粗体
        invitationCode.setSpan(new StyleSpan(Typeface.BOLD), 4, totalLength, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        // 字体大小
        invitationCode.setSpan(new AbsoluteSizeSpan(50), 4, totalLength, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        // 显示
        invitationCode.setSpan(new ForegroundColorSpan(Color.parseColor("#1296db")), 4, totalLength, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        nf_tvqrcodedescription.setText(invitationCode);

    }
}

