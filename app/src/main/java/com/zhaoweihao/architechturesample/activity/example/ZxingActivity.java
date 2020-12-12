package com.zhaoweihao.architechturesample.activity.example;



import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;

import android.os.Bundle;


import android.view.View;
import android.widget.ImageView;


import com.google.zxing.BarcodeFormat;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.journeyapps.barcodescanner.CaptureActivity;

import com.zhaoweihao.architechturesample.R;

/**
 * @author zhaoweihao
 * 二维码示例
 */
public class ZxingActivity extends CaptureActivity implements View.OnClickListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zxing);

        findViewById(R.id.scan_qrcode).setOnClickListener(this);
        findViewById(R.id.generate_qrcode).setOnClickListener(this);
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }


    }

    /**
     * 得到二维码扫描结果
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                //扫描取消
                finish();
                //Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            } else {
                //扫描成功后的结果
                String content = result.getContents();

                //Toast.makeText(this, "Scanned: " + content, Toast.LENGTH_LONG).show();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }



    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.scan_qrcode: {
                //跳转扫描二维码页面
                IntentIntegrator integrator = new IntentIntegrator(ZxingActivity.this);
                integrator.initiateScan();
            }
            break;
            //生成二维码
            case R.id.generate_qrcode: {
                try {
                    BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                    //生成二维码的内容 例如是教师编号
                    String content = "20151911";
                    //生成二维码的bitmap
                    Bitmap bitmap = barcodeEncoder.encodeBitmap(content, BarcodeFormat.QR_CODE, 400, 400);
                    ImageView imageViewQrCode = (ImageView) findViewById(R.id.qrcode_image);
                    imageViewQrCode.setImageBitmap(bitmap);
                } catch (Exception e) {

                }
            }
            break;
            default:
                break;
        }
    }
}


