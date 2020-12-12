package com.zhaoweihao.architechturesample.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentValues;
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
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.qmuiteam.qmui.widget.dialog.QMUIBottomSheet;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;
import com.zhaoweihao.architechturesample.R;
import com.zhaoweihao.architechturesample.base.BaseActivity;
import com.zhaoweihao.architechturesample.bean.RestResponse;
import com.zhaoweihao.architechturesample.bean.UserProfieInformation;
import com.zhaoweihao.architechturesample.database.User;
import com.zhaoweihao.architechturesample.ui.MagnifyPhotoLayout;
import com.zhaoweihao.architechturesample.ui.NetWorkImageView;
import com.zhaoweihao.architechturesample.ui.TitleLayout;
import com.zhaoweihao.architechturesample.util.Constant;
import com.zhaoweihao.architechturesample.util.HttpUtil;
import com.zhaoweihao.architechturesample.util.Utils;

import org.litepal.crud.DataSupport;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

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

import static com.zhaoweihao.architechturesample.util.HttpUtil.sendPostRequest;

/**
 * @author tanxinkui
 * @description 个人-设置-账号管理界面
 * @time 2019/1/11 14:50
 */
public class PersonalSettingCountManagementActivity extends BaseActivity {
    @BindView(R.id.apscm_title)
    TitleLayout apscm_title;
    @BindView(R.id.apscm_fl_head)
    FrameLayout apscm_fl_head;
    @BindView(R.id.apscm_fl_name)
    FrameLayout apscm_fl_name;
    @BindView(R.id.apscm_fl_sex)
    FrameLayout apscm_fl_sex;
    @BindView(R.id.apscm_fl_description)
    FrameLayout apscm_fl_description;
    @BindView(R.id.apscm_fl_phone)
    FrameLayout apscm_fl_phone;
    @BindView(R.id.apscm_fl_school)
    FrameLayout apscm_fl_school;
    @BindView(R.id.apscm_qmui_exit)
    QMUIRoundButton apscm_qmui_exit;
    @BindView(R.id.apscm_tv_school)
    TextView apscm_tv_school;
    @BindView(R.id.apscm_tv_sex)
    TextView apscm_tv_sex;
    @BindView(R.id.apscm_tv_name)
    TextView apscm_tv_name;
    @BindView(R.id.apscm_tv_description)
    TextView apscm_tv_description;
    @BindView(R.id.apscm_tv_phone)
    TextView apscm_tv_phone;
    @BindView(R.id.apscm_tv_id)
    TextView apscm_tv_id;
    @BindView(R.id.apscm_iv_head)
    NetWorkImageView apscm_iv_head;
    @BindView(R.id.apscm_mp_magnify_photo)
    MagnifyPhotoLayout apscm_mp_magnify_photo;
    private QMUITipDialog tipDialog, tipSucceedDialog;
    private QMUIBottomSheet qmuiBottomSheet;
    private Boolean headFlag = true;
    private static final Class thisClass = PersonalSettingCountManagementActivity.class;
    private static final int CAMERA_RESULT_CODE = 12;
    private static final int CROP_RESULT_CODE = 13;
    private static final int ALBUM_RESULT_CODE = 14;
    private static final int OPEN_CANMER = 15;
    private File file;
    //private String imgName = "profile_picture";
    private Uri imgUriOri;
    // 裁剪属性 cropIntent.putExtra("return-data", false); 时，使用自定义接收图片的Uri

    private String IMAGE_FILE_LOCATION = "file:///" + Environment.getExternalStorageDirectory().getPath() + "/temp.jpg";
    private Uri imageUri = Uri.parse(IMAGE_FILE_LOCATION);
    OkHttpClient client = new OkHttpClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_setting_count_management);
        ButterKnife.bind(this);
        requestCameraPermission();
        getAvatar();
    }

    @Override
    protected void onResume() {
        super.onResume();
        init();
    }

    /**
     * oldPath 和 newPath必须是新旧文件的绝对路径
     */


    private void getAvatar() {
        if (!TextUtils.isEmpty(DataSupport.findLast(User.class).getAvatar())) {
            apscm_iv_head.setImageURL(Constant.getBaseUrl() + "upload/" + DataSupport.findLast(User.class).getAvatar());
            Log.v("头像ee", "" + Constant.getBaseUrl() + "upload/" + DataSupport.findLast(User.class).getAvatar());
        }

    }

    private void getModifyAvatar() {
        HttpUtil.sendGetRequest(Constant.GET_AVATAR_URL + "?id=" + DataSupport.findLast(User.class).getUserId(), new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String body = response.body().string();
                RestResponse restResponse = new Gson().fromJson(body, RestResponse.class);
                if (restResponse.getCode() == Constant.FAIL_CODE) {
                    runOnUiThread(() -> Toast.makeText(PersonalSettingCountManagementActivity.this, "暂无头像00", Toast.LENGTH_SHORT).show());
                } else {
                    if (restResponse.getPayload() != null) {

                        runOnUiThread(() -> apscm_iv_head.setImageURL(Constant.getBaseUrl() + "upload/" + restResponse.getPayload().toString()));
                        Log.v("头像dd", Constant.getBaseUrl() + "upload/" + restResponse.getPayload().toString());

                    } else {
                        Log.v("头像获取222body", "" + body);
                        runOnUiThread(() -> Toast.makeText(PersonalSettingCountManagementActivity.this, "暂无头像11", Toast.LENGTH_SHORT).show());
                    }

                }

            }
        });
    }

    private void modifyAatar() {
        HttpUtil.sendGetRequest(Constant.MODIFY_AVATAR_URL + "?id=" + DataSupport.findLast(User.class).getUserId() + "&avatarUrl=" + DataSupport.findLast(User.class).getAvatar(), new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    String body = response.body().string();
                    RestResponse restResponse = new Gson().fromJson(body, RestResponse.class);
                    if (restResponse.getCode() == Constant.SUCCESS_CODE) {
                        getModifyAvatar();
                        Log.v("头像获取修改2body", "" + body);
                        runOnUiThread(() -> Toast.makeText(PersonalSettingCountManagementActivity.this, "成功修改头像！", Toast.LENGTH_SHORT).show());
                    } else {
                        Log.v("头像获取修改失败2body", "" + body);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.v("头像获取修改ssssbody", "" + e.toString());
                } catch (JsonSyntaxException e) {
                    e.printStackTrace();
                    Log.v("头像获取修改sssdddsbody", "" + e.toString());
                }
            }
        });
    }

    public void init() {
        apscm_title.setTitle("账号管理");
        User userCurrent = DataSupport.findLast(User.class);
        apscm_tv_name.setText(userCurrent.getName());
        if (userCurrent.getSex() == 0) {
            apscm_tv_sex.setText("男");
        } else {
            apscm_tv_sex.setText("女");
        }
        if (userCurrent.getDescrition() != null) {
            apscm_tv_description.setText(userCurrent.getDescrition());
        }
        if (userCurrent.getPhone() != null) {
            apscm_tv_phone.setText(userCurrent.getPhone());
        }
        apscm_tv_school.setText(userCurrent.getSchool());
        apscm_qmui_exit.setOnClickListener(view -> {
            new QMUIDialog.MessageDialogBuilder(PersonalSettingCountManagementActivity.this)
                    .setTitle("退出登录")
                    .setMessage("确定要保存账号信息并退出登录吗？")
                    .addAction("取消", new QMUIDialogAction.ActionListener() {
                        @Override
                        public void onClick(QMUIDialog dialog, int index) {
                            dialog.dismiss();
                        }
                    })
                    .addAction(0, "退出", QMUIDialogAction.ACTION_PROP_NEGATIVE, new QMUIDialogAction.ActionListener() {
                        @Override
                        public void onClick(QMUIDialog dialog, int index) {
                            dialog.dismiss();
                            request();
                        }
                    })
                    .show();
        });
        apscm_tv_id.setText(getCurrentUserId(userCurrent));
        apscm_fl_description.setOnClickListener(view -> goNextActivity("com.zhaoweihao.architechturesample.activity.PersonalSettingDescriptionActivity"));
        apscm_fl_phone.setOnClickListener(view -> goNextActivity("com.zhaoweihao.architechturesample.activity.PersonalSettingPhoneActivity"));
        tipSucceedDialog = new QMUITipDialog.Builder(PersonalSettingCountManagementActivity.this)
                .setTipWord("保存成功！")
                .setIconType(QMUITipDialog.Builder.ICON_TYPE_SUCCESS)
                .create();
        tipDialog = new QMUITipDialog.Builder(PersonalSettingCountManagementActivity.this)
                .setTipWord("正在保存用户信息！")
                .setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
                .create();
        qmuiBottomSheet = new QMUIBottomSheet.BottomListSheetBuilder(PersonalSettingCountManagementActivity.this)
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
                                    apscm_mp_magnify_photo.setPhoto(apscm_iv_head.getBitmaptoMagnify());
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

        apscm_fl_head.setOnClickListener(view -> {
            if (headFlag) {
                qmuiBottomSheet.show();
                headFlag = false;
            } else {
                qmuiBottomSheet.dismiss();
                headFlag = true;
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case OPEN_CANMER:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {
                    Toast.makeText(PersonalSettingCountManagementActivity.this, "相机权限禁用了。请务必开启相机权", Toast.LENGTH_SHORT).show();
                }
                break;
            case 0: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted
                    // request successfully, handle you transactions
                } else {
                    // permission denied
                    // request failed
                }
                return;
            }
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
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

    private void requestCameraPermission() {
        int checkCallPhonePermission = ContextCompat.checkSelfPermission(PersonalSettingCountManagementActivity.this, Manifest.permission.CAMERA);
        if (checkCallPhonePermission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(PersonalSettingCountManagementActivity.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, OPEN_CANMER);
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
                    uploadFile(new File(Environment.getExternalStorageDirectory(), "temp.jpg"));
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

    private void showhead() {
        try {
            apscm_iv_head.setImageBitmap(BitmapFactory.decodeStream(this.getContentResolver().openInputStream(imageUri)));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
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

    /**
     * 上传文件
     */
    private void uploadFile(File file) {

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
                ContentValues values = new ContentValues();
                values.put("avatar", uploadString.substring(8, uploadString.length()));
                DataSupport.update(User.class, values, DataSupport.findLast(User.class).getId());
                modifyAatar();
            }
        });
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
        cropIntent.putExtra("aspectX", 1);
        cropIntent.putExtra("aspectY", 1);
        // 裁剪输出大小
        cropIntent.putExtra("outputX", 320);
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


    private void dismissSucceedDialog() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                tipSucceedDialog.dismiss();
                onExit();
            }
        }, 2000);

    }

    private String getCurrentUserId(User user) {
        if (user.getStudentId() != null) {
            return user.getStudentId();
        } else {
            return user.getTeacherId();
        }
    }

    private void goNextActivity(String className) {
        Intent intent = null;
        try {
            intent = new Intent(PersonalSettingCountManagementActivity.this, Class.forName(className));
            startActivity(intent);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void request() {
        tipDialog.show();
        User userCurrent = DataSupport.findLast(User.class);
        UserProfieInformation userProfieInformation = new UserProfieInformation();
        userProfieInformation.setId(userCurrent.getUserId());
        userProfieInformation.setStudentId(userCurrent.getStudentId());
        userProfieInformation.setTeacherId(userCurrent.getTeacherId());
        userProfieInformation.setClassId(userCurrent.getClassId());
        userProfieInformation.setDepartment(userCurrent.getDepartment());
        userProfieInformation.setEducation(userCurrent.getEducation());
        userProfieInformation.setDate(userCurrent.getDate());
        userProfieInformation.setSchool(userCurrent.getSchool());
        userProfieInformation.setSex(userCurrent.getSex());
        userProfieInformation.setName(userCurrent.getName());
        userProfieInformation.setPhone(userCurrent.getPhone());
        userProfieInformation.setDescrition(userCurrent.getDescrition());
        String json = new Gson().toJson(userProfieInformation);
        sendPostRequest(Constant.MODIFY_PROFILE_URL, json, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //网络错误处理
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String body = response.body().string();
                //解析json数据组装RestResponse对象
                RestResponse restResponse = new Gson().fromJson(body, RestResponse.class);
                // 修复登录密码不正确异常
                if (restResponse == null) {
                    runOnUiThread(() -> {

                    });
                    return;
                }

                if (restResponse.getCode() == Constant.FAIL_ORIGIN_CODE) {

                    try {
                        //切换主进程更新UI
                        runOnUiThread(() -> {
                        });

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                // code 200等于登录成功
                if (restResponse.getCode() == Constant.SUCCESS_CODE) {
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

    private void onExit() {
        DataSupport.findLast(com.zhaoweihao.architechturesample.database.User.class).delete();
        finish();
        Intent intent = new Intent();
        // 为Intent设置Action、Category属性
        intent.setAction(Intent.ACTION_MAIN);
        // "android.intent.action.MAIN"
        intent.addCategory(Intent.CATEGORY_HOME);
        //"android.intent.category.HOME"
        startActivity(intent);
        //startActivity(new Intent(PersonalSettingCountManagementActivity.this, WelcomeActivity.class));
        //finish();
    }
}


/**
 * 图片文件操作
 * <p>
 * 将Bitmap 图片保存到本地路径，并返回路径
 *
 * @param fileName 文件名称
 * @param bitmap   图片
 * @param资源类型，参照 MultimediaContentType 枚举，根据此类型，保存时可自动归类
 * <p>
 * Bitmap 转 字节数组
 * @param bm
 * @return 将Bitmap 图片保存到本地路径，并返回路径
 * @param fileName 文件名称
 * @param bitmap   图片
 * @param资源类型，参照 MultimediaContentType 枚举，根据此类型，保存时可自动归类
 * <p>
 * Bitmap 转 字节数组
 * @param bm
 * @return 将Bitmap 图片保存到本地路径，并返回路径
 * @param fileName 文件名称
 * @param bitmap   图片
 * @param资源类型，参照 MultimediaContentType 枚举，根据此类型，保存时可自动归类
 * <p>
 * Bitmap 转 字节数组
 * @param bm
 * @return 将Bitmap 图片保存到本地路径，并返回路径
 * @param fileName 文件名称
 * @param bitmap   图片
 * @param资源类型，参照 MultimediaContentType 枚举，根据此类型，保存时可自动归类
 * <p>
 * Bitmap 转 字节数组
 * @param bm
 * @return 将Bitmap 图片保存到本地路径，并返回路径
 * @param fileName 文件名称
 * @param bitmap   图片
 * @param资源类型，参照 MultimediaContentType 枚举，根据此类型，保存时可自动归类
 * <p>
 * Bitmap 转 字节数组
 * @param bm
 * @return 将Bitmap 图片保存到本地路径，并返回路径
 * @param fileName 文件名称
 * @param bitmap   图片
 * @param资源类型，参照 MultimediaContentType 枚举，根据此类型，保存时可自动归类
 * <p>
 * Bitmap 转 字节数组
 * @param bm
 * @return 将Bitmap 图片保存到本地路径，并返回路径
 * @param fileName 文件名称
 * @param bitmap   图片
 * @param资源类型，参照 MultimediaContentType 枚举，根据此类型，保存时可自动归类
 * <p>
 * Bitmap 转 字节数组
 * @param bm
 * @return
 */
/* class FileUtilcll {

 *//**
 * 将Bitmap 图片保存到本地路径，并返回路径
 *
 * @param fileName 文件名称
 * @param bitmap   图片
 * @param资源类型，参照 MultimediaContentType 枚举，根据此类型，保存时可自动归类
 *//*
    public static String saveFile(Context c, String fileName, Bitmap bitmap) {
        return saveFile(c, "", fileName, bitmap);
    }

    public static String saveFile(Context c, String filePath, String fileName, Bitmap bitmap) {
        byte[] bytes = bitmapToBytes(bitmap);
        return saveFile(c, filePath, fileName, bytes);
    }

    *//**
 * Bitmap 转 字节数组
 *
 * @param bm
 * @return
 *//*
    public static byte[] bitmapToBytes(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        return baos.toByteArray();
    }

    public static String saveFile(Context c, String filePath, String fileName, byte[] bytes) {
        String fileFullName = "";
        FileOutputStream fos = null;
        String dateFolder = new SimpleDateFormat("yyyyMMddHHmmss", Locale.CHINA)
                .format(new Date());
        try {
            String suffix = "";
            if (filePath == null || filePath.trim().length() == 0) {
                filePath = Environment.getExternalStorageDirectory() + "/cxs/" + dateFolder + "/";
            }
            File file = new File(filePath);
            if (!file.exists()) {
                file.mkdirs();
            }
            File fullFile = new File(filePath, fileName + suffix);
            fileFullName = fullFile.getPath();
            fos = new FileOutputStream(new File(filePath, fileName + suffix));
            fos.write(bytes);
        } catch (Exception e) {
            fileFullName = "";
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    fileFullName = "";
                }
            }
        }
        return fileFullName;
    }

}*/

