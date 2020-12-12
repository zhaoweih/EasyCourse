package com.zhaoweihao.architechturesample.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.zhaoweihao.architechturesample.R;

import java.io.File;
import java.io.FileNotFoundException;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author tanxinkui
 * @description 放大头像
 * @time 2019/1/16 10:49
 */
public class MagnifyPhotoLayout extends FrameLayout {
    @BindView(R.id.acpl_fl_background)
    FrameLayout acpl_fl_background;
    @BindView(R.id.acpl_iv_photo)
    ImageView acpl_iv_photo;
    @BindView(R.id.acpl_fl_main)
    FrameLayout acpl_fl_main;

    public MagnifyPhotoLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.layout_magnify_photo, this);
        ButterKnife.bind(this);
        closePhoto();
        acpl_fl_main.setVisibility(View.INVISIBLE);
    }

    public void setPhoto(Bitmap bitmap) {
        try {
            acpl_fl_main.setVisibility(View.VISIBLE);
            acpl_iv_photo.setImageBitmap(bitmap);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getContext(),"该图片不支持预览！",Toast.LENGTH_SHORT).show();
        }
    }

    public void setImageFromUri(File file){
        try {
            acpl_fl_main.setVisibility(View.VISIBLE);
            acpl_iv_photo.setImageURI(Uri.fromFile(file));
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getContext(),"该图片不支持预览！",Toast.LENGTH_SHORT).show();
        }
    }

    private void closePhoto() {
        acpl_fl_background.setOnClickListener(view -> {
            acpl_fl_main.setVisibility(View.INVISIBLE);
        });
    }

}
