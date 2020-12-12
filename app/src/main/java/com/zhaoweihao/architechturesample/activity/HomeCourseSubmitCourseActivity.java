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
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.qmuiteam.qmui.widget.dialog.QMUIBottomSheet;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;
import com.zhaoweihao.architechturesample.R;
import com.zhaoweihao.architechturesample.base.BaseActivity;
import com.zhaoweihao.architechturesample.bean.RestResponse;
import com.zhaoweihao.architechturesample.bean.course.Submit;
import com.zhaoweihao.architechturesample.contract.HomeCourseSubmitCourseContract;
import com.zhaoweihao.architechturesample.database.User;
import com.zhaoweihao.architechturesample.presenter.HomeCourseSubmitCoursePresenter;
import com.zhaoweihao.architechturesample.ui.MagnifyPhotoLayout;
import com.zhaoweihao.architechturesample.ui.NetWorkImageView;
import com.zhaoweihao.architechturesample.util.Constant;

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

/**
 * @author
 * @description 首页-课程-添加（教师登陆才有添加按钮）-教师提交课程
 * @time 2019/1/28 13:02
 */
public class HomeCourseSubmitCourseActivity extends BaseActivity implements HomeCourseSubmitCourseContract.View {
    @BindView(R.id.as_upload_cover)
    LinearLayout as_upload_cover;

    @BindView(R.id.as_iv)
    NetWorkImageView as_iv;

    @BindView(R.id.as_magnify_layout)
    MagnifyPhotoLayout as_magnify_layout;

    private QMUIBottomSheet qmuiBottomSheet;

    private HomeCourseSubmitCourseContract.Presenter presenter;

    private TextView teacherId;

    private EditText courseName, teacherName, password, description;

    private Toolbar toolbar;

    private File file;

    private Boolean firstOpenFlag = true;

    private String class_image;

    private static final int CAMERA_RESULT_CODE = 12;
    private static final int CROP_RESULT_CODE = 13;
    private static final int ALBUM_RESULT_CODE = 14;
    private static final int OPEN_CANMER = 15;

    private String IMAGE_FILE_LOCATION = "file:///" + Environment.getExternalStorageDirectory().getPath() + "/CourseCoverTemp.jpg";

    private Uri imgUriOri;

    private Uri imageUri = Uri.parse(IMAGE_FILE_LOCATION);

    OkHttpClient client = new OkHttpClient();
    private QMUITipDialog tipDialog ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit);
        ButterKnife.bind(this);
        // 配置presenter
        new HomeCourseSubmitCoursePresenter(this, this);
        initButtomSheet();
        initViews(null);
        initProgress();

        /**
         * tecId : 26
         * teacherId : 20151120
         * courseName : 牛津和爱因斯坦的搏斗
         * teacherName : 赵威豪
         * password : 123456
         * description : 讲述牛津和爱因斯坦的斗争
         */
        com.zhaoweihao.architechturesample.database.User user = DataSupport.findLast(com.zhaoweihao.architechturesample.database.User.class);

        if (user == null) {
            return;
        }

        if (user.getTeacherId() == null) {
            Toast.makeText(this, "你不是老师身份", Toast.LENGTH_SHORT).show();
            return;
        }

        //toolbar.setTitle("提交课程");
        getSupportActionBar().setTitle("提交课程");

        teacherId.setText(user.getTeacherId());

        teacherName.setText(user.getName());
        as_upload_cover.setOnClickListener(view -> {
            qmuiBottomSheet.show();
        });
        toolbar.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.submit:
                    String courseNameText = courseName.getText().toString();
                    String teacherNameText = teacherName.getText().toString();
                    String descriptionText = description.getText().toString();
                    String passwordText = password.getText().toString();
                    if (courseNameText.equals("") || teacherNameText.equals("") || descriptionText.equals("") || passwordText.equals("")) {
                        Toast.makeText(this, "不能为空", Toast.LENGTH_SHORT).show();
                    } else {
                        AlertDialog alert = new AlertDialog.Builder(this)
                                .setIcon(R.drawable.warming)
                                .setTitle("温馨提示")
                                .setMessage("确定要提交课程吗？")
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {//设置确定按钮
                                    @Override//处理确定按钮点击事件
                                    public void onClick(DialogInterface dialog, int which) {
                                        Submit submit = new Submit();
                                        submit.setTecId(user.getUserId());
                                        submit.setTeacherId(user.getTeacherId());
                                        submit.setCourseName(courseNameText);
                                        submit.setTeacherName(teacherNameText);
                                        submit.setDescription(descriptionText);
                                        submit.setPassword(passwordText);
                                        if(!firstOpenFlag){
                                            uploadFile(new File(Environment.getExternalStorageDirectory(), "CourseCoverTemp.jpg"),submit);
                                        }else {
                                            Toast.makeText(HomeCourseSubmitCourseActivity.this, "请先选择图片！", Toast.LENGTH_SHORT).show();
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
                    break;
            }
            return true;
        });

    }
    private void initProgress(){
        tipDialog = new QMUITipDialog.Builder(HomeCourseSubmitCourseActivity.this)
                .setTipWord("正在上传...")
                .setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
                .create();
    }

    private void initButtomSheet() {
        qmuiBottomSheet = new QMUIBottomSheet.BottomListSheetBuilder(HomeCourseSubmitCourseActivity.this)
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
                                        Toast.makeText(HomeCourseSubmitCourseActivity.this, "请先选择图片！", Toast.LENGTH_SHORT).show();

                                    } else {
                                        try {
                                            as_magnify_layout.setPhoto(BitmapFactory.decodeStream(this.getContentResolver().openInputStream(imageUri)));
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
        int checkCallPhonePermission = ContextCompat.checkSelfPermission(HomeCourseSubmitCourseActivity.this, Manifest.permission.CAMERA);
        if (checkCallPhonePermission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(HomeCourseSubmitCourseActivity.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, OPEN_CANMER);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        qmuiBottomSheet.dismiss();
        switch (requestCode) {
            case CAMERA_RESULT_CODE:
               /* tempFile = new File(Environment.getExternalStorageDirectory(), imgName);
                cropPic(Uri.fromFile(tempFile));*/
                //适配 Android7.0+
                if(data!=null){
                    cropPic(getImageContentUri(file));
                }
                break;
            case CROP_RESULT_CODE:
                if(data!=null){
                    showhead();
                }
                // 裁剪时,这样设置 cropIntent.putExtra("return-data", false); 处理方案如下

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
            as_iv.setImageBitmap(BitmapFactory.decodeStream(this.getContentResolver().openInputStream(imageUri)));
            firstOpenFlag=false;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    /**
     * 上传文件
     */
    private void uploadFile(File file,Submit submit) {
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
                class_image=uploadString.substring(8, uploadString.length());
               /*  ContentValues values = new ContentValues();
                values.put("avatar", uploadString.substring(8, uploadString.length()));
                DataSupport.update(User.class, values, DataSupport.findLast(User.class).getId());*/
                //modifyAatar();
                submit.setClass_image(class_image);
                // 交给presenter去进行网络请求，各自负责的功能清晰
                presenter.submit(submit);
            }
        });
    }

    @Override
    public void setPresenter(HomeCourseSubmitCourseContract.Presenter presenter) {
        if (presenter != null) {
            this.presenter = presenter;
        }
    }

    @Override
    public void initViews(View view) {
        teacherId = findViewById(R.id.teacher_id);
        courseName = findViewById(R.id.course_name);
        teacherName = findViewById(R.id.teacher_name);
        password = findViewById(R.id.password);
        description = findViewById(R.id.description);

        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.start();
    }

    @Override
    public void showResult(Boolean status) {
        runOnUiThread(() -> {
            if (status) {
                Toast.makeText(this, "提交课程成功", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "提交课程失败", Toast.LENGTH_SHORT).show();

            }
            tipDialog.dismiss();
        });
    }

    @Override
    public void startLoading() {

    }

    @Override
    public void stopLoading() {

    }

    @Override
    public void showLoadError() {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.course_submit, menu);
        return true;
    }
}
