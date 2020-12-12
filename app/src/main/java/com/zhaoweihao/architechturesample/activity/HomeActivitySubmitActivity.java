package com.zhaoweihao.architechturesample.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.qmuiteam.qmui.widget.dialog.QMUIBottomSheet;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;
import com.zhaoweihao.architechturesample.R;
import com.zhaoweihao.architechturesample.base.BaseActivity;
import com.zhaoweihao.architechturesample.bean.AddAndShowActivity;
import com.zhaoweihao.architechturesample.bean.RestResponse;
import com.zhaoweihao.architechturesample.database.User;
import com.zhaoweihao.architechturesample.ui.GetInputTextOrSelectTagDialogLayout;
import com.zhaoweihao.architechturesample.ui.MagnifyPhotoLayout;
import com.zhaoweihao.architechturesample.ui.TitleLayout;
import com.zhaoweihao.architechturesample.util.Constant;
import com.zhaoweihao.architechturesample.util.HttpUtil;

import org.litepal.crud.DataSupport;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * @author
 * @description 首页-活动-添加活动
 * @time 2019/2/23 20:15
 */
public class HomeActivitySubmitActivity extends BaseActivity {
    @BindView(R.id.ahs_titleLayout)
    TitleLayout ahs_titleLayout;
    @BindView(R.id.ahs_input)
    GetInputTextOrSelectTagDialogLayout ahs_input;
    @BindView(R.id.ahs_tag)
    GetInputTextOrSelectTagDialogLayout ahs_tag;
    @BindView(R.id.ahs_tse)
    GetInputTextOrSelectTagDialogLayout ahs_tse;
    @BindView(R.id.ahs_time)
    GetInputTextOrSelectTagDialogLayout ahs_time;
    @BindView(R.id.ahs_time2)
    GetInputTextOrSelectTagDialogLayout ahs_time2;
    @BindView(R.id.ahs_et_add_content)
    EditText ahs_et_add_content;
    @BindView(R.id.ahs_iv)
    ImageView ahs_iv;
    @BindView(R.id.ahs_iv_cover)
    ImageView ahs_iv_cover;
    @BindView(R.id.ahs_upload_cover)
    LinearLayout ahs_upload_cover;
    @BindView(R.id.ahs_maginfyPhotoLayout)
    MagnifyPhotoLayout ahs_maginfyPhotoLayout;
    private QMUIBottomSheet qmuiBottomSheet;
    private Handler handler;
    private Timer timer;
    private TimerTask timerTask;
    private int addDays;
    private Boolean ifFistLoadTime2 = true;
    private File file;
    private Button button;

    private Boolean firstOpenFlag = true;
    private Boolean ifEditextAlready = false;

    private String class_image;

    private static final int CAMERA_RESULT_CODE = 12;
    private static final int CROP_RESULT_CODE = 13;
    private static final int ALBUM_RESULT_CODE = 14;
    private static final int OPEN_CANMER = 15;

    private String IMAGE_FILE_LOCATION = "file:///" + Environment.getExternalStorageDirectory().getPath() + "/ActivityCoverTemp.jpg";

    private Uri imgUriOri;

    private Uri imageUri = Uri.parse(IMAGE_FILE_LOCATION);

    OkHttpClient client = new OkHttpClient();
    private QMUITipDialog tipDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_submit);
        ButterKnife.bind(this);
        initViews();
        detectTime();
    }

    public void initViews() {
        tipDialog = new QMUITipDialog.Builder(HomeActivitySubmitActivity.this)
                .setTipWord("正在上传活动...")
                .setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
                .create();
        initEdtext();
        button = ahs_titleLayout.getSettingButton();
        button.setText("提交");
        button.setVisibility(View.INVISIBLE);
        button.setOnClickListener(v -> submit());
        ahs_upload_cover.setOnClickListener(v -> pickImage());
        ahs_titleLayout.setTitle("添加活动");
        ahs_input.setHintText("标题：", "请输入标题", "请输入标题", "标题不能为空！");
        ahs_tag.setHintText("标签：", "请输入标签", "请输入标签", "一般的标签为2~4个字，例如：课外活动");
        ahs_time.setTime("开始时间：", "请选择开始时间", 0);
        ahs_time2.setVisibility(View.GONE);
        //ahs_tse.setTagText(new String[]{"暂时没用1", "暂时没用2"});
    }

    private void pickImage() {
        initButtomSheet();
        qmuiBottomSheet.show();
    }

    private void initButtomSheet() {
        qmuiBottomSheet = new QMUIBottomSheet.BottomListSheetBuilder(HomeActivitySubmitActivity.this)
                .addItem(R.drawable.takephoto, "拍照", "拍照")
                .addItem(R.drawable.photoalbum, "从相册中选取", "从相册中选取", true)
                .addItem(R.drawable.originphoto, "查看原图", "查看原图")
                .addItem(R.drawable.cancel, "取消", "取消")
                .setOnSheetItemClickListener((QMUIBottomSheet dialog, View itemView, int position, String tag) -> {
                            switch (position) {
                                case 0:
                                    openSysCamera();
                                    break;
                                case 1:
                                    openSysAlbum();
                                    break;
                                case 2:
                                    qmuiBottomSheet.dismiss();
                                    if (firstOpenFlag) {
                                        Toast.makeText(HomeActivitySubmitActivity.this, "请先选择图片！", Toast.LENGTH_SHORT).show();

                                    } else {
                                        try {
                                            ahs_maginfyPhotoLayout.setPhoto(BitmapFactory.decodeStream(this.getContentResolver().openInputStream(imageUri)));
                                        } catch (FileNotFoundException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                    break;
                                case 3:
                                    qmuiBottomSheet.dismiss();
                                    break;
                                default:
                                    break;
                            }
                        }
                )
                .build();
    }

    private void openSysCamera() {

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(Environment.getExternalStorageDirectory(), imgName)));
        //startActivityForResult(cameraIntent, CAMERA_RESULT_CODE);
        try {
            file = createOriImageFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (file != null) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                imgUriOri = Uri.fromFile(file);
            } else {
                requestCameraPermission();
                imgUriOri = FileProvider.getUriForFile(this, getPackageName() + ".provider", file);
            }
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imgUriOri);
            startActivityForResult(cameraIntent, CAMERA_RESULT_CODE);
        }
    }

    private void openSysAlbum() {
        requestReadExternalPermission();
        Intent albumIntent = new Intent(Intent.ACTION_PICK);
        albumIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(albumIntent, ALBUM_RESULT_CODE);
    }

    /**
     * 创建原图像保存的文件
     *
     * @return
     * @throws IOException
     */
    private File createOriImageFile() throws IOException {
        String imgNameOri = "HomePic_" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File pictureDirOri = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES).getAbsolutePath() + "/OriPicture");
        if (!pictureDirOri.exists()) {
            pictureDirOri.mkdirs();
        }
        File image = File.createTempFile(
                imgNameOri,         /* prefix */
                ".jpg",      /* suffix */
                pictureDirOri       /* directory */
        );
        //imgPathOri = image.getAbsolutePath();
        return image;
    }

    private void requestCameraPermission() {
        int checkCallPhonePermission = ContextCompat.checkSelfPermission(HomeActivitySubmitActivity.this, Manifest.permission.CAMERA);
        if (checkCallPhonePermission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(HomeActivitySubmitActivity.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, OPEN_CANMER);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        qmuiBottomSheet.dismiss();
        switch (requestCode) {
            case CAMERA_RESULT_CODE:
                if (data != null) {
               /* tempFile = new File(Environment.getExternalStorageDirectory(), imgName);
                cropPic(Uri.fromFile(tempFile));*/
                    //适配 Android7.0+
                    cropPic(getImageContentUri(file));
                }
                break;
            case CROP_RESULT_CODE:
                if (data != null) {
                    // 裁剪时,这样设置 cropIntent.putExtra("return-data", false); 处理方案如下
                    showhead();
                }

                break;
            case ALBUM_RESULT_CODE:
                if (data != null) {
                    // 相册
                    cropPic(data.getData());
                }
                break;
            default:
                break;
        }
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

    /**
     * 从相册中选取的和直接拍摄的照片
     * 裁剪图片
     *
     * @param data
     */
    private void cropPic(Uri data) {
        if (data == null) {
            return;
        }
        Intent cropIntent = new Intent("com.android.camera.action.CROP");
        cropIntent.setDataAndType(data, "image/*");

        // 开启裁剪：打开的Intent所显示的View可裁剪
        cropIntent.putExtra("crop", "true");
        // 裁剪宽高比
        cropIntent.putExtra("aspectX", 2);
        cropIntent.putExtra("aspectY", 1);
        // 裁剪输出大小
        cropIntent.putExtra("outputX", 640);
        cropIntent.putExtra("outputY", 320);
        cropIntent.putExtra("scale", true);
        /**
         * return-data
         * 这个属性决定我们在 onActivityResult 中接收到的是什么数据，
         * 如果设置为true 那么data将会返回一个bitmap
         * 如果设置为false，则会将图片保存到本地并将对应的uri返回，当然这个uri得有我们自己设定。
         * 系统裁剪完成后将会将裁剪完成的图片保存在我们所这设定这个uri地址上。我们只需要在裁剪完成后直接调用该uri来设置图片，就可以了。
         */
        cropIntent.putExtra("return-data", false);
        // 当 return-data 为 false 的时候需要设置这句
        cropIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        // 图片输出格式
        cropIntent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        // 头像识别 会启动系统的拍照时人脸识别
        // cropIntent.putExtra("noFaceDetection", true);
        startActivityForResult(cropIntent, CROP_RESULT_CODE);
    }

    /**
     * 7.0以上获取裁剪 Uri
     *
     * @param imageFile
     * @return
     */
    private Uri getImageContentUri(File imageFile) {
        String filePath = imageFile.getAbsolutePath();
        Cursor cursor = getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Images.Media._ID},
                MediaStore.Images.Media.DATA + "=? ",
                new String[]{filePath}, null);

        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor
                    .getColumnIndex(MediaStore.MediaColumns._ID));
            Uri baseUri = Uri.parse("content://media/external/images/media");
            return Uri.withAppendedPath(baseUri, "" + id);
        } else {
            if (imageFile.exists()) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.DATA, filePath);
                return getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            } else {
                return null;
            }
        }
    }

    private void showhead() {
        try {
            ahs_iv_cover.setImageBitmap(BitmapFactory.decodeStream(this.getContentResolver().openInputStream(imageUri)));
            firstOpenFlag = false;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    /**
     * 上传文件
     */
    private void uploadFile(File file) {
        tipDialog.show();

        // 创建一个RequestBody，文件的类型是image/png multipart/form-data
        RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody multipartBody = new MultipartBody.Builder()
                // 设置type为"multipart/form-data"，不然无法上传参数
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", file.getName(), requestBody)
//                .addFormDataPart("comment", "上传一个图片哈哈哈哈")
                .build();
        Request request = new Request.Builder()
                .url(Constant.UPLOAD_SERVER_FILE_URL)
                .post(multipartBody)
                .build();
        client.newCall(request).enqueue(new Callback() {
            /**
             * 上传失败回调
             * @param call
             * @param e
             */
            @Override
            public void onFailure(Call call, IOException e) {
                System.out.println("失败");
                Log.d(TAG, "上传失败");
                tipDialog.dismiss();
            }

            /**
             * 上传成功回调
             * @param call
             * @param response
             * @throws IOException
             */
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String body = response.body().string();
                RestResponse restResponse = new Gson().fromJson(body, RestResponse.class);
                String uploadString = restResponse.getPayload().toString();
                class_image = uploadString.substring(8, uploadString.length());
                tipDialog.dismiss();
               /*  ContentValues values = new ContentValues();
                values.put("avatar", uploadString.substring(8, uploadString.length()));
                DataSupport.update(User.class, values, DataSupport.findLast(User.class).getId());*/
                //modifyAatar();
                //submit.setClass_image(class_image);
                // 交给presenter去进行网络请求，各自负责的功能清晰
                //presenter.submit(submit);
                allDataAlreadyToSubmit();
            }
        });
    }

    private void allDataAlreadyToSubmit() {
        AddAndShowActivity addAndShowActivity = new AddAndShowActivity();
        addAndShowActivity.setTime(System.currentTimeMillis());
        addAndShowActivity.setEnd_time(ahs_time2.getFinalLongTime());
        addAndShowActivity.setStart_time(ahs_time.getFinalLongTime());
        addAndShowActivity.setTags(ahs_tag.getFinalInputText());
        addAndShowActivity.setType("1");
        addAndShowActivity.setImg_url(class_image);
        addAndShowActivity.setTitle(ahs_input.getFinalInputText());
        addAndShowActivity.setContent(ahs_et_add_content.getText().toString());
        addAndShowActivity.setSender_id(DataSupport.findLast(User.class).getUserId());
        String json = new Gson().toJson(addAndShowActivity);
        HttpUtil.sendPostRequest(Constant.POST_ACTIVITY_URL, json, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                runOnUiThread(() -> {
                    Toast.makeText(HomeActivitySubmitActivity.this, "成功提交活动！", Toast.LENGTH_SHORT).show();
                    finish();
                });
            }
        });
    }

    private void initEdtext() {
        ahs_et_add_content.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        ahs_et_add_content.setGravity(Gravity.TOP);
        ahs_et_add_content.setSingleLine(false);
        ahs_et_add_content.setHorizontallyScrolling(false);
        ahs_et_add_content.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (ahs_iv.getVisibility() == View.VISIBLE) {
                    ahs_iv.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (TextUtils.isEmpty(ahs_et_add_content.getText().toString())) {
                    ahs_iv.setVisibility(View.VISIBLE);
                    ifEditextAlready = false;
                } else {
                    ifEditextAlready = true;
                    if (ahs_iv.getVisibility() == View.VISIBLE) {
                        ahs_iv.setVisibility(View.INVISIBLE);
                    }
                }
            }
        });
    }

    private void submit() {
        AlertDialog alert = new AlertDialog.Builder(this)
                .setIcon(R.drawable.warming)
                .setTitle("提交活动")
                .setMessage("确定要提交该活动吗？")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {//设置确定按钮
                    @Override//处理确定按钮点击事件
                    public void onClick(DialogInterface dialog, int which) {
                        if (!firstOpenFlag) {
                            uploadFile(new File(Environment.getExternalStorageDirectory(), "ActivityCoverTemp.jpg"));
                        } else {
                            Toast.makeText(HomeActivitySubmitActivity.this, "请先选择图片！", Toast.LENGTH_SHORT).show();
                        }

                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();//对话框关闭。
                    }
                }).create();
        alert.show();

    }

    private void detectTime() {


        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                //SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                Date date = new Date(System.currentTimeMillis());


                if (msg.what == 11) {
                    if (ifFistLoadTime2) {
                        Long s = (ahs_time.getExpireDate().getTime() - date.getTime()) / (24 * 60 * 60 * 1000);
                        addDays = s.intValue();
                        if (addDays == 0) {
                            addDays = addDays + 1;
                        } else {
                            addDays = addDays + 2;
                        }
                        ahs_time2.setTime("截止时间：", "请选择截止时间", addDays);
                        ifFistLoadTime2 = false;
                        ahs_time2.setVisibility(View.VISIBLE);
                    }
                }
                if (msg.what == 12) {
                    if (ahs_input.ifAlready() && ahs_tag.ifAlready() && ahs_time.ifAlready() && ahs_time2.ifAlready() && (!firstOpenFlag) && ifEditextAlready) {
                        if (button.getVisibility() == View.INVISIBLE) {
                            button.setVisibility(View.VISIBLE);
                        }
                    } else {
                        if (button.getVisibility() == View.VISIBLE) {
                            button.setVisibility(View.INVISIBLE);
                        }
                    }
                    if (((ahs_time.getExpireDate().getTime() - ahs_time2.getExpireDate().getTime()) / (24 * 60 * 60 * 1000)) >= 0) {
                        Long s = (ahs_time.getExpireDate().getTime() - date.getTime()) / (24 * 60 * 60 * 1000);
                        addDays = s.intValue();
                        if (addDays == 0) {
                            addDays = addDays + 1;
                        } else {
                            addDays = addDays + 2;
                        }
                        if (ahs_time2.getTimeString() != "") {
                            ahs_time2.setTime("截止时间：", "请选择截止时间", addDays);
                        }
                    }
                }
                super.handleMessage(msg);
            }
        };
        timer = new Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                Message message = new Message();
                if (ahs_time.getExpireDate() != null && ahs_time2.getExpireDate() == null) {
                    message.what = 11;
                    if (button.getVisibility() == View.VISIBLE) {
                        button.setVisibility(View.INVISIBLE);
                    }
                } else if (ahs_time.getExpireDate() != null && ahs_time2.getExpireDate() != null) {
                    message.what = 12;
                   /* Log.v("tanxinkui000","ahs_input.ifAlready()"+ahs_input.ifAlready());
                    Log.v("tanxinkui000","ahs_tag.ifAlready()"+ahs_tag.ifAlready());
                    Log.v("tanxinkui000","ahs_time.ifAlready()"+ahs_time.ifAlready());
                    Log.v("tanxinkui000","ahs_time2.ifAlready()"+ahs_time2.ifAlready());
                    Log.v("tanxinkui000","!firstOpenFlag"+!firstOpenFlag);
                    Log.v("tanxinkui000","button.getVisibility()"+(button.getVisibility()==View.INVISIBLE));
                    Log.v("tanxinkui000","ifEditextAlready"+ifEditextAlready);*/
                }

                handler.sendMessage(message);
            }
        };
        timer.schedule(timerTask, 200, 200);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timerTask != null) {
            timerTask.cancel();
            timerTask = null;
        }
        if (timer != null) {
            timer.cancel();
            timer.purge();
            timer = null;
        }
        handler.removeMessages(11);
        handler.removeMessages(12);
    }

}
