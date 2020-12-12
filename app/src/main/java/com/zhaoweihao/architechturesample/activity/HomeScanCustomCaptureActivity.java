package com.zhaoweihao.architechturesample.activity;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.Result;
import com.google.zxing.common.GlobalHistogramBinarizer;
import com.google.zxing.common.HybridBinarizer;
import com.journeyapps.barcodescanner.CaptureManager;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;
import com.zhaoweihao.architechturesample.R;
import com.zhaoweihao.architechturesample.bean.RestResponse;
import com.zhaoweihao.architechturesample.bean.UserInfoByUsername;
import com.zhaoweihao.architechturesample.bean.ValidationMesg;
import com.zhaoweihao.architechturesample.database.User;
import com.zhaoweihao.architechturesample.util.Constant;
import com.zhaoweihao.architechturesample.util.HttpUtil;

import org.litepal.crud.DataSupport;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static com.zhaoweihao.architechturesample.util.Utils.log;

/**
 * @author
 * @description 首页-扫码-（扫描，从相册选取）
 * @time 2019/1/28 14:25
 */
public class HomeScanCustomCaptureActivity extends Activity implements View.OnClickListener {
    @BindView(R.id.acc_btn_zxing_back)
    Button acc_btn_zxing_back;
    @BindView(R.id.acc_ib_manual_input)
    ImageButton acc_ib_manual_input;
    @BindView(R.id.acc_ib_album)
    ImageButton acc_ib_album;
    @BindView(R.id.acc_iv_forphoto)
    ImageView acc_iv_forphoto;
    private CaptureManager capture;
    private DecoratedBarcodeView barcodeScannerView;
    private String TAG = "HomeScanCustomCaptureActivity.class";
    private Boolean ifSendable = true;
    private Boolean ifAlreadyBeSent = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("txk", "路径go获取");
        setContentView(R.layout.activity_custom_capture);
        // 自定义布局
        ButterKnife.bind(this);
        initSetOnClickListener();
        barcodeScannerView = (DecoratedBarcodeView) findViewById(R.id.dbv_custom);

        capture = new CaptureManager(this, barcodeScannerView);
        capture.initializeFromIntent(getIntent(), savedInstanceState);
        capture.decode();
    }

    public void initSetOnClickListener() {
        acc_btn_zxing_back.setOnClickListener(this);
        acc_ib_album.setOnClickListener(this);
        acc_ib_manual_input.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.acc_btn_zxing_back:
                finish();
                break;
            case R.id.acc_ib_manual_input:
                gotoManual_input();
                break;
            case R.id.acc_ib_album:
                selectPhoto();
                break;
            default:
                break;
        }
    }

    private void gotoManual_input() {
        Intent intent = new Intent(HomeScanCustomCaptureActivity.this, HomeScanInputActivity.class);
        startActivity(intent);
    }

    public void selectPhoto() {

        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, 300);

    }

    @Override
    protected void onResume() {
        super.onResume();
        capture.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        capture.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        capture.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        capture.onSaveInstanceState(outState);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        capture.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.d(TAG, "requestCode=" + requestCode + "; --->" + permissions.toString()
                + "; grantResult=" + grantResults.toString());
        switch (requestCode) {
            case 0: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted
                    // request successfully, handle you transactions
                } else {
                    // permission denied
                    // request failed
                }
                return;
            }
            default:
                break;

        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return barcodeScannerView.onKeyDown(keyCode, event) || super.onKeyDown(keyCode, event);
    }

    private String getRealPathFromUri_AboveApi19(Context context, Uri uri) {
        String filePath = null;
        String wholeID = DocumentsContract.getDocumentId(uri);

        // 使用':'分割
        String id = wholeID.split(":")[1];

        String[] projection = {MediaStore.Images.Media.DATA};
        String selection = MediaStore.Images.Media._ID + "=?";
        String[] selectionArgs = {id};
        requestReadExternalPermission();
        Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                projection, selection, selectionArgs, null);
        int columnIndex = cursor.getColumnIndex(projection[0]);
        if (cursor.moveToFirst()) {
            filePath = cursor.getString(columnIndex);
        }
        cursor.close();
        return filePath;
    }

    @SuppressLint("NewApi")
    private void requestReadExternalPermission() {
        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "READ permission IS NOT granted...");

            if (shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)) {

                Log.d(TAG, "11111111111111");
            } else {
                // 0 是自己定义的请求coude
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
                Log.d(TAG, "222222222222");
            }
        } else {
            Log.d(TAG, "READ permission is granted...");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            //获取照片数据
            Bitmap camera = data.getParcelableExtra("data");
            acc_iv_forphoto.setImageBitmap(camera);
            Log.d("txk", "路径图片" + camera);
        }
        if (requestCode == 200) {
            if (data != null) {
                try {
                    Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(data.getData()));
                    acc_iv_forphoto.setImageBitmap(bitmap);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }

        if (requestCode == 300) {

            String photoPath = getRealPathFromUri_AboveApi19(this, data.getData());
            Log.d("txk", "路径?" + data.getData());
            if (photoPath == null) {
                Log.d("txk", "路径获取失败" + photoPath);
            } else {
                //解析图片
                Log.d("txk", "路径获取失败3" + photoPath);
                parsePhoto(photoPath);
            }
        }
    }


    private void parsePhoto(final String path) {

        AsyncTask myTask = new AsyncTask<String, Integer, String>() {
            @Override
            protected String doInBackground(String... params) {
                // 解析二维码/条码
                return QRCodeDecoder.syncDecodeQRCode(path);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                if (null == s) {
                    Toast.makeText(HomeScanCustomCaptureActivity.this, "无法识别", Toast.LENGTH_LONG).show();
                    Log.d(TAG, "路径neirong获取失败3" + s);
                } else {
                    // 识别出图片二维码/条码，内容为s
                    Log.d(TAG, "路径neirong获取成功3" + s);
                    // ifSendable(s);

                    if (TextUtils.isEmpty(s)) {
                        Toast.makeText(HomeScanCustomCaptureActivity.this, "请先输入对方的邀请码！", Toast.LENGTH_SHORT).show();
                    } else if (s.equals(DataSupport.findLast(User.class).getUsername())) {
                        Toast.makeText(HomeScanCustomCaptureActivity.this, "不能添加自己为好友！", Toast.LENGTH_SHORT).show();
                    } else {
                        getUserInfomation(s);
                    }
                    //Toast.makeText(HomeScanCustomCaptureActivity.this, "识别成功：" + s, Toast.LENGTH_LONG).show();
                }
            }
        }.execute(path);
    }

    private void ifSendable(String s) {
        HttpUtil.sendGetRequest(Constant.GET_ALL_FROM_ME_REQUEST_URL + DataSupport.findLast(User.class).getUsername() + "&token=" + DataSupport.findLast(User.class).getToken(),
                new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                        runOnUiThread(() -> {
                            Toast.makeText(HomeScanCustomCaptureActivity.this, "加载异常！", Toast.LENGTH_SHORT).show();
                        });
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String body = response.body().string();
                        //解析json数据组装RestResponse对象
                        RestResponse restResponse = JSON.parseObject(body, RestResponse.class);
                        //RestResponse restResponse = new Gson().fromJson(body, RestResponse.class);
                        runOnUiThread(() -> {
                            if (restResponse.getCode() == Constant.SUCCESS_CODE) {
                                List<ValidationMesg> mValidationMesg = JSON.parseArray(restResponse.getPayload().toString(), ValidationMesg.class);
                                if (mValidationMesg.size() != 0) {
                                    for (ValidationMesg mesg : mValidationMesg) {
                                        if (mesg.getTo_username().equals(s) && mesg.getIs_confirmed() == 0) {
                                            ifSendable = false;
                                        }
                                    }
                                }
                                ifisAlreadyBeenSent(s);
                            }
                        });
                    }
                });
    }

    private void ifisAlreadyBeenSent(String s) {
        HttpUtil.sendGetRequest(Constant.GET_ALL_FRIENDS_TO_ME_REQUEST_URL + DataSupport.findLast(User.class).getUsername() + "&token=" + DataSupport.findLast(User.class).getToken(),
                new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                        runOnUiThread(() -> {
                            Toast.makeText(HomeScanCustomCaptureActivity.this, "加载异常！", Toast.LENGTH_SHORT).show();
                        });
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String body = response.body().string();
                        //解析json数据组装RestResponse对象
                        RestResponse restResponse = JSON.parseObject(body, RestResponse.class);
                        //RestResponse restResponse = new Gson().fromJson(body, RestResponse.class);
                        runOnUiThread(() -> {
                            if (restResponse.getCode() == Constant.SUCCESS_CODE) {
                                List<ValidationMesg> mValidationMesg = JSON.parseArray(restResponse.getPayload().toString(), ValidationMesg.class);
                                if (mValidationMesg.size() != 0) {
                                    for (ValidationMesg mesg : mValidationMesg) {
                                        if (mesg.getFrom_username().equals(s) && mesg.getIs_confirmed() == 0) {
                                            ifSendable = false;
                                            ifAlreadyBeSent = true;
                                        }
                                    }
                                }
                                sendFriendRequest(s);
                            }
                        });
                    }
                });
    }

    private void sendFriendRequest(String s) {
        if (ifSendable) {
            HttpUtil.sendGetRequest(
                    Constant.SEND_FRIEND_REQUEST_URL + "from=" + DataSupport.findLast(User.class).getUsername() + "&to=" + s + "&token=" + DataSupport.findLast(User.class).getToken(),
                    new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            runOnUiThread(() -> {
                                Toast.makeText(HomeScanCustomCaptureActivity.this, "添加好友失败，找不到该用户！", Toast.LENGTH_LONG).show();
                            });
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            String body = response.body().string();
                            //解析json数据组装RestResponse对象
                            RestResponse restResponse = JSON.parseObject(body, RestResponse.class);
                            runOnUiThread(() -> {
                                if (restResponse.getCode() == Constant.SUCCESS_CODE) {
                                    Toast.makeText(HomeScanCustomCaptureActivity.this, "发送好友验证成功！", Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(HomeScanCustomCaptureActivity.this, "添加好友失败，找不到该用户！", Toast.LENGTH_LONG).show();
                                }

                            });
                        }
                    });
        } else {
            if (ifAlreadyBeSent) {
                Toast.makeText(HomeScanCustomCaptureActivity.this, "对方已发送过好友验证，正在等待您确认！", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(HomeScanCustomCaptureActivity.this, "您已发送过好友验证，正在等待好友确认！", Toast.LENGTH_LONG).show();
            }

        }


    }

    private void getUserInfomation(String username) {
        HttpUtil.sendGetRequest(Constant.GET_USERINFO_BY_USERNAME_AND_TOKEN_URL + username + "&token="
                + DataSupport.findLast(User.class).getToken(), new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(() -> {
                    Toast.makeText(HomeScanCustomCaptureActivity.this, "找不到该好友", Toast.LENGTH_SHORT).show();
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String body = response.body().string();
                RestResponse restResponse = JSON.parseObject(body, RestResponse.class);
                runOnUiThread(() -> {
                    if (restResponse.getCode() == Constant.SUCCESS_CODE) {
                        UserInfoByUsername userInfoByUsername = JSON.parseObject(restResponse.getPayload().toString(), UserInfoByUsername.class);
                        Intent intent = new Intent(HomeScanCustomCaptureActivity.this, MessageFriendProfileActivity.class);
                        intent.putExtra("userInfoByUsername", userInfoByUsername);
                        intent.putExtra("mode", "输入添加好友");
                        intent.putExtra("username", username);
                        startActivity(intent);
                    } else {
                        Toast.makeText(HomeScanCustomCaptureActivity.this, "找不到该好友", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}


class QRCodeDecoder {
    public static final Map<DecodeHintType, Object> HINTS = new EnumMap<>(DecodeHintType.class);

    static {
        List<BarcodeFormat> allFormats = new ArrayList<>();
        allFormats.add(BarcodeFormat.AZTEC);
        allFormats.add(BarcodeFormat.CODABAR);
        allFormats.add(BarcodeFormat.CODE_39);
        allFormats.add(BarcodeFormat.CODE_93);
        allFormats.add(BarcodeFormat.CODE_128);
        allFormats.add(BarcodeFormat.DATA_MATRIX);
        allFormats.add(BarcodeFormat.EAN_8);
        allFormats.add(BarcodeFormat.EAN_13);
        allFormats.add(BarcodeFormat.ITF);
        allFormats.add(BarcodeFormat.MAXICODE);
        allFormats.add(BarcodeFormat.PDF_417);
        allFormats.add(BarcodeFormat.QR_CODE);
        allFormats.add(BarcodeFormat.RSS_14);
        allFormats.add(BarcodeFormat.RSS_EXPANDED);
        allFormats.add(BarcodeFormat.UPC_A);
        allFormats.add(BarcodeFormat.UPC_E);
        allFormats.add(BarcodeFormat.UPC_EAN_EXTENSION);
        HINTS.put(DecodeHintType.TRY_HARDER, BarcodeFormat.QR_CODE);
        HINTS.put(DecodeHintType.POSSIBLE_FORMATS, allFormats);
        HINTS.put(DecodeHintType.CHARACTER_SET, "utf-8");
    }

    private QRCodeDecoder() {
    }

    /**
     * 同步解析本地图片二维码。该方法是耗时操作，请在子线程中调用。
     *
     * @param picturePath 要解析的二维码图片本地路径
     * @return 返回二维码图片里的内容 或 null
     */
    public static String syncDecodeQRCode(String picturePath) {
        return syncDecodeQRCode(getDecodeAbleBitmap(picturePath));
    }

    /**
     * 同步解析bitmap二维码。该方法是耗时操作，请在子线程中调用。
     *
     * @param bitmap 要解析的二维码图片
     * @return 返回二维码图片里的内容 或 null
     */
    public static String syncDecodeQRCode(Bitmap bitmap) {
        Result result = null;
        RGBLuminanceSource source = null;
        try {
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();
            int[] pixels = new int[width * height];
            bitmap.getPixels(pixels, 0, width, 0, 0, width, height);
            source = new RGBLuminanceSource(width, height, pixels);
            result = new MultiFormatReader().decode(new BinaryBitmap(new HybridBinarizer(source)), HINTS);
            return result.getText();
        } catch (Exception e) {
            e.printStackTrace();
            if (source != null) {
                try {
                    result = new MultiFormatReader().decode(new BinaryBitmap(new GlobalHistogramBinarizer(source)), HINTS);
                    return result.getText();
                } catch (Throwable e2) {
                    e2.printStackTrace();
                }
            }
            return null;
        }
    }

    /**
     * 将本地图片文件转换成可解码二维码的 Bitmap。为了避免图片太大，这里对图片进行了压缩。感谢 https://github.com/devilsen 提的 PR
     *
     * @param picturePath 本地图片文件路径
     * @return
     */
    private static Bitmap getDecodeAbleBitmap(String picturePath) {
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(picturePath, options);
            int sampleSize = options.outHeight / 400;
            if (sampleSize <= 0) {
                sampleSize = 1;
            }
            options.inSampleSize = sampleSize;
            options.inJustDecodeBounds = false;
            return BitmapFactory.decodeFile(picturePath, options);
        } catch (Exception e) {
            return null;
        }
    }
}



