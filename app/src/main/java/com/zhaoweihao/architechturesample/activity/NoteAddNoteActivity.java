package com.zhaoweihao.architechturesample.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.google.gson.Gson;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;
import com.zhaoweihao.architechturesample.R;
import com.zhaoweihao.architechturesample.base.BaseActivity;
import com.zhaoweihao.architechturesample.bean.AddNote;
import com.zhaoweihao.architechturesample.bean.RestResponse;
import com.zhaoweihao.architechturesample.database.User;
import com.zhaoweihao.architechturesample.ui.MagnifyPhotoLayout;
import com.zhaoweihao.architechturesample.ui.TitleLayout;
import com.zhaoweihao.architechturesample.util.Constant;
import com.zhaoweihao.architechturesample.util.HttpUtil;

import org.litepal.crud.DataSupport;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
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
 * @description 笔记-添加笔记
 * @time 2019/2/15 14:56
 */
public class NoteAddNoteActivity extends BaseActivity {
    @BindView(R.id.anan_titleLayout)
    TitleLayout anan_titleLayout;

    @BindView(R.id.anan_et_add_content)
    EditText anan_et_add_content;

    @BindView(R.id.anan_iv_1)
    ImageView anan_iv_1;

    @BindView(R.id.anan_iv_2)
    ImageView anan_iv_2;

    @BindView(R.id.anan_iv_3)
    ImageView anan_iv_3;

    @BindView(R.id.anan_iv_4)
    ImageView anan_iv_4;

    @BindView(R.id.anan_iv_5)
    ImageView anan_iv_5;

    @BindView(R.id.anan_iv_6)
    ImageView anan_iv_6;

    @BindView(R.id.anan_iv_add1)
    ImageView anan_iv_add1;

    @BindView(R.id.anan_iv_add2)
    ImageView anan_iv_add2;

    @BindView(R.id.anan_iv_delete1)
    ImageView anan_iv_delete1;

    @BindView(R.id.anan_iv_delete2)
    ImageView anan_iv_delete2;

    @BindView(R.id.anan_iv_ifAddTitle)
    ImageView anan_iv_ifAddTitle;

    @BindView(R.id.anan_iv_ifSelectTag)
    ImageView anan_iv_ifSelectTag;

    @BindView(R.id.anan_iv_ifWriteTag)
    ImageView anan_iv_ifWriteTag;

    @BindView(R.id.anan_tv_note_title)
    TextView anan_tv_note_title;

    @BindView(R.id.anan_tv_note_tag)
    TextView anan_tv_note_tag;

    @BindView(R.id.anan_fl_add_title)
    FrameLayout anan_fl_add_title;

    @BindView(R.id.anan_fl_add_tag)
    FrameLayout anan_fl_add_tag;

    @BindView(R.id.anan_maginfyPhotoLayout)
    MagnifyPhotoLayout anan_maginfyPhotoLayout;
    private static final int CROP_RESULT_CODE = 13;
    private static final int ALBUM_RESULT_CODE = 14;
    private String IMAGE_FILE_LOCATION_1 = "file:///" + Environment.getExternalStorageDirectory().getPath() + "/temp1.jpg";
    private String IMAGE_FILE_LOCATION_2 = "file:///" + Environment.getExternalStorageDirectory().getPath() + "/temp2.jpg";
    private String IMAGE_FILE_LOCATION_3 = "file:///" + Environment.getExternalStorageDirectory().getPath() + "/temp3.jpg";
    private String IMAGE_FILE_LOCATION_4 = "file:///" + Environment.getExternalStorageDirectory().getPath() + "/temp4.jpg";
    private String IMAGE_FILE_LOCATION_5 = "file:///" + Environment.getExternalStorageDirectory().getPath() + "/temp5.jpg";
    private String IMAGE_FILE_LOCATION_6 = "file:///" + Environment.getExternalStorageDirectory().getPath() + "/temp6.jpg";
    private String[] imageLocation = {IMAGE_FILE_LOCATION_1, IMAGE_FILE_LOCATION_2, IMAGE_FILE_LOCATION_3, IMAGE_FILE_LOCATION_4, IMAGE_FILE_LOCATION_5, IMAGE_FILE_LOCATION_6};
    private ArrayList<Uri> imageUri = new ArrayList<>();
    OkHttpClient client = new OkHttpClient();

    private QMUIDialog.EditTextDialogBuilder setTitle;
    private QMUIDialog.CheckableDialogBuilder selectTag;
    private int selectTagId = 0;
    private Button finishButton;
    private ArrayList<File> finalfile = new ArrayList<>();
    private String localVideoUri;
    private static final int OPEN_CANMER = 15;
    private Handler handler;
    private Timer timer;
    private TimerTask timerTask;
    private ArrayList<ImageView> ananImageViews = new ArrayList<>();
    private int theRestUploadTime;
    private String finalUploadResouceString;
    private QMUITipDialog tipDialog;
    String[] itemString = {"草稿", "共享笔记", "私有笔记"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_add_note);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        tipDialog = new QMUITipDialog.Builder(NoteAddNoteActivity.this)
                .setTipWord("正在上传笔记...")
                .setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
                .create();
        for (int i = 0; i < imageLocation.length; i++) {
            imageUri.add(Uri.parse(imageLocation[i]));
        }
        ananImageViews.add(anan_iv_1);
        ananImageViews.add(anan_iv_2);
        ananImageViews.add(anan_iv_3);
        ananImageViews.add(anan_iv_4);
        ananImageViews.add(anan_iv_5);
        ananImageViews.add(anan_iv_6);
        anan_iv_1.setVisibility(View.GONE);
        anan_iv_2.setVisibility(View.GONE);
        anan_iv_3.setVisibility(View.GONE);
        anan_iv_4.setVisibility(View.GONE);
        anan_iv_5.setVisibility(View.GONE);
        anan_iv_6.setVisibility(View.GONE);
        anan_iv_add1.setOnClickListener(view -> pick());
        anan_iv_delete1.setVisibility(View.INVISIBLE);
        anan_iv_delete2.setVisibility(View.INVISIBLE);
        anan_iv_add2.setVisibility(View.GONE);
        anan_iv_add2.setOnClickListener(view -> pick());
        anan_titleLayout.setTitle("添加笔记");
        finishButton = anan_titleLayout.getSettingButton();
        finishButton.setText("完成");
        finishButton.setVisibility(View.INVISIBLE);
        finishButton.setOnClickListener(view -> submitNote());
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 11) {
                    if (finishButton.getVisibility() == View.INVISIBLE) {
                        finishButton.setVisibility(View.VISIBLE);
                    }
                } else {
                    if (finishButton.getVisibility() == View.VISIBLE) {
                        finishButton.setVisibility(View.INVISIBLE);
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
                if (anan_iv_ifWriteTag.getVisibility() == View.INVISIBLE && anan_iv_ifSelectTag.getVisibility() == View.INVISIBLE && anan_iv_ifAddTitle.getVisibility() == View.INVISIBLE) {
                    message.what = 11;
                } else {
                    message.what = 12;
                }

                handler.sendMessage(message);
            }
        };
        timer.schedule(timerTask, 200, 200);
        anan_fl_add_title.setOnClickListener(view -> {
            setTitle = new QMUIDialog.EditTextDialogBuilder(NoteAddNoteActivity.this)
                    .setTitle("笔记标题")
                    .setPlaceholder("请输入笔记标题！")
                    .addAction("取消", new QMUIDialogAction.ActionListener() {
                        @Override
                        public void onClick(QMUIDialog dialog, int index) {
                            dialog.dismiss();
                        }
                    })
                    .addAction("确定", new QMUIDialogAction.ActionListener() {
                        @Override
                        public void onClick(QMUIDialog dialog, int index) {

                            if (TextUtils.isEmpty(setTitle.getEditText().getText().toString())) {
                                Toast.makeText(NoteAddNoteActivity.this, "请输入您的笔记标题！", Toast.LENGTH_SHORT).show();
                            } else {
                                dialog.dismiss();
                                anan_tv_note_title.setText(setTitle.getEditText().getText().toString());
                                if (anan_iv_ifAddTitle.getVisibility() == View.VISIBLE) {
                                    anan_iv_ifAddTitle.setVisibility(View.INVISIBLE);
                                }
                            }

                        }
                    });
            setTitle.show();
        });
        anan_fl_add_tag.setOnClickListener(view -> {
            selectTag = new QMUIDialog.CheckableDialogBuilder(NoteAddNoteActivity.this)
                    .setTitle("请选择您的标签:")
                    .setCheckedIndex(selectTagId)
                    .addItems(itemString, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            String s = itemString[i];
                            anan_tv_note_tag.setText(s);
                            isNeedNewTag();
                            selectTagId = i;
                            dialogInterface.dismiss();
                        }
                    });
            selectTag.show();
        });
        anan_et_add_content.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        anan_et_add_content.setGravity(Gravity.TOP);
        anan_et_add_content.setSingleLine(false);
        anan_et_add_content.setHorizontallyScrolling(false);
        anan_et_add_content.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (anan_iv_ifWriteTag.getVisibility() == View.VISIBLE) {
                    anan_iv_ifWriteTag.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (TextUtils.isEmpty(anan_et_add_content.getText().toString())) {
                    anan_iv_ifWriteTag.setVisibility(View.VISIBLE);
                } else {
                    if (anan_iv_ifWriteTag.getVisibility() == View.VISIBLE) {
                        anan_iv_ifWriteTag.setVisibility(View.INVISIBLE);
                    }
                }
            }
        });
    }

    private void submitNote() {

        if (finalfile.size() == 0) {
            AllDataReadyToAddFinalNote();
        } else {
            theRestUploadTime = finalfile.size();
            uploadFile(finalfile.get(0));
        }
        tipDialog.show();

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
                //uploadString.substring(8, uploadString.length())
                //finalUploadResouceString.add(uploadString.substring(8, uploadString.length()));
                if (TextUtils.isEmpty(finalUploadResouceString)) {
                    finalUploadResouceString = uploadString.substring(8, uploadString.length());
                } else {
                    finalUploadResouceString = finalUploadResouceString + "," + uploadString.substring(8, uploadString.length());
                }
                theRestUploadTime = --theRestUploadTime;
                if (theRestUploadTime > 0) {
                    uploadFile(finalfile.get(finalfile.size() - theRestUploadTime));
                } else {
                    AllDataReadyToAddFinalNote();
                }

            }
        });
    }

    private void AllDataReadyToAddFinalNote() {
        AddNote addNote = new AddNote();
        addNote.setTitle(anan_tv_note_title.getText().toString());
        addNote.setContent(anan_et_add_content.getText().toString());
        addNote.setTag(anan_tv_note_tag.getText().toString());
        if(selectTagId==1){
            addNote.setIs_shared("true");
        }else {
            addNote.setIs_shared("false");
        }
        addNote.setUser_id(DataSupport.findLast(User.class).getUserId());
        addNote.setTime(System.currentTimeMillis());
        if (finalfile.size() == 0) {

        } else {
            addNote.setResoucrs(finalUploadResouceString);
        }

        String json = new Gson().toJson(addNote);
        HttpUtil.sendPostRequest(Constant.ADD_NOTE_URL, json, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String body = response.body().string();
                //解析json数据组装RestResponse对象
                RestResponse restResponse = new Gson().fromJson(body, RestResponse.class);
                if (restResponse.getCode() == Constant.SUCCESS_CODE) {

                    runOnUiThread(()->{
                        tipDialog.dismiss();
                        Toast.makeText(NoteAddNoteActivity.this,"成功上传笔记！",Toast.LENGTH_SHORT).show();
                        finish();
                    });
                }
            }
        });


    }


    private void pick() {
        openSysAlbum();
    }


    private void showPick() {
        if (finalfile.size() > 0 && finalfile.size() < 3) {
            anan_iv_delete1.setVisibility(View.VISIBLE);
            anan_iv_delete2.setVisibility(View.INVISIBLE);
            anan_iv_delete1.setOnClickListener(view -> {
                if (finalfile.size() > 0) {
                    finalfile.remove(finalfile.size() - 1);
                    clearSelectImage(finalfile.size());
                }
            });
        }
        if (finalfile.size() >= 3 && finalfile.size() <= 6) {
            anan_iv_delete2.setVisibility(View.VISIBLE);
            anan_iv_delete1.setVisibility(View.INVISIBLE);
            anan_iv_delete2.setOnClickListener(view -> {
                finalfile.remove(finalfile.size() - 1);
                clearSelectImage(finalfile.size());
            });
        }
        Boolean hideAdd2Button = false;
        switch (finalfile.size()) {
            case 0:
                break;
            case 6:
                try {
                    anan_iv_6.setImageBitmap(BitmapFactory.decodeStream(this.getContentResolver().openInputStream(imageUri.get(5))));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                imageCilck(anan_iv_6, finalfile.get(5));
                anan_iv_add2.setVisibility(View.INVISIBLE);
                imageLongClick(anan_iv_6, 5);
                hideAdd2Button = true;
                anan_iv_6.setVisibility(View.VISIBLE);
            case 5:
                try {
                    anan_iv_5.setImageBitmap(BitmapFactory.decodeStream(this.getContentResolver().openInputStream(imageUri.get(4))));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                imageCilck(anan_iv_5, finalfile.get(4));
                imageLongClick(anan_iv_5, 4);
                anan_iv_5.setVisibility(View.VISIBLE);
            case 4:
                try {
                    anan_iv_4.setImageBitmap(BitmapFactory.decodeStream(this.getContentResolver().openInputStream(imageUri.get(3))));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                imageCilck(anan_iv_4, finalfile.get(3));
                imageLongClick(anan_iv_4, 3);
                anan_iv_4.setVisibility(View.VISIBLE);
            case 3:
                try {
                    anan_iv_3.setImageBitmap(BitmapFactory.decodeStream(this.getContentResolver().openInputStream(imageUri.get(2))));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                imageCilck(anan_iv_3, finalfile.get(2));
                anan_iv_3.setVisibility(View.VISIBLE);
                imageLongClick(anan_iv_3, 2);
                anan_iv_add1.setVisibility(View.INVISIBLE);
                if (!hideAdd2Button) {
                    anan_iv_add2.setVisibility(View.VISIBLE);
                }
            case 2:
                try {
                    anan_iv_2.setImageBitmap(BitmapFactory.decodeStream(this.getContentResolver().openInputStream(imageUri.get(1))));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                imageCilck(anan_iv_2, finalfile.get(1));
                imageLongClick(anan_iv_2, 1);
                anan_iv_2.setVisibility(View.VISIBLE);
            case 1:
                try {
                    anan_iv_1.setImageBitmap(BitmapFactory.decodeStream(this.getContentResolver().openInputStream(imageUri.get(0))));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                imageCilck(anan_iv_1, finalfile.get(0));
                imageLongClick(anan_iv_1, 0);
                anan_iv_1.setVisibility(View.VISIBLE);
                break;
            default:
                break;
        }
    }

    private void imageCilck(ImageView imageView, File file) {
        imageView.setOnClickListener(view -> {
            anan_maginfyPhotoLayout.setImageFromUri(file);
        });
    }

    private void imageLongClick(ImageView imageView, int fileIndex) {
        imageView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
               /* new QMUIDialog.MessageDialogBuilder(NoteAddNoteActivity.this)
                        .setTitle("删除该图片？")
                        .setMessage("您确定删除该图片？")
                        .addAction("取消", new QMUIDialogAction.ActionListener() {
                            @Override
                            public void onClick(QMUIDialog dialog, int index) {
                                dialog.dismiss();
                            }
                        })
                        .addAction("确定", new QMUIDialogAction.ActionListener() {
                            @Override
                            public void onClick(QMUIDialog dialog, int index) {
                                dialog.dismiss();
                                finalfile.remove(fileIndex);
                                clearSelectImage(finalfile.size());
                            }
                        })
                        .show();*/
                return false;
            }
        });
    }

    private void clearSelectImage(int num) {
        for (int i = 0; i < num; i++) {
            ananImageViews.get(i).setVisibility(View.VISIBLE);
            try {
                ananImageViews.get(i).setImageBitmap(BitmapFactory.decodeStream(this.getContentResolver().openInputStream(imageUri.get(i))));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            imageCilck(ananImageViews.get(i), finalfile.get(i));
            imageLongClick(ananImageViews.get(i), i);
        }
        if (num == 0) {
            anan_iv_delete1.setVisibility(View.INVISIBLE);
            anan_iv_delete2.setVisibility(View.INVISIBLE);
        }
        if (num < 3 && num > 0) {
            anan_iv_delete1.setVisibility(View.VISIBLE);
            anan_iv_delete2.setVisibility(View.INVISIBLE);
            anan_iv_add2.setVisibility(View.INVISIBLE);
            anan_iv_add1.setVisibility(View.VISIBLE);
        }
        if (num >= 3 && num < 6) {
            anan_iv_delete2.setVisibility(View.VISIBLE);
            anan_iv_delete1.setVisibility(View.INVISIBLE);
            anan_iv_add2.setVisibility(View.VISIBLE);
            anan_iv_add1.setVisibility(View.INVISIBLE);
        }
        if (num == 6) {
            anan_iv_delete2.setVisibility(View.VISIBLE);
            anan_iv_delete1.setVisibility(View.INVISIBLE);
            anan_iv_add2.setVisibility(View.INVISIBLE);
            anan_iv_add1.setVisibility(View.INVISIBLE);
        }
        for (int i = num; i < 6; i++) {
            ananImageViews.get(i).setVisibility(View.GONE);
        }
    }

    private void isNeedNewTag() {
        if (anan_iv_ifSelectTag.getVisibility() == View.VISIBLE) {
            anan_iv_ifSelectTag.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case OPEN_CANMER:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {
                    Toast.makeText(NoteAddNoteActivity.this, "相机权限禁用了。请务必开启相机权", Toast.LENGTH_SHORT).show();
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case CROP_RESULT_CODE:
                if (resultCode == RESULT_OK) {
                    String[] tempFileName = {"temp1.jpg", "temp2.jpg", "temp3.jpg", "temp4.jpg", "temp5.jpg", "temp6.jpg"};

                    finalfile.add(new File(Environment.getExternalStorageDirectory(), tempFileName[finalfile.size()]));

                    showPick();
                }
                //showhead();
                // uploadFile(new File(Environment.getExternalStorageDirectory(), "temp.jpg"));
                break;
            case ALBUM_RESULT_CODE:
                /*if (resultCode == RESULT_OK) {
                    ArrayList<ImageFile> list = data.getParcelableArrayListExtra(Constant.RESULT_PICK_IMAGE);
                    // Toast.makeText(NoteAddNoteActivity.this, "Url:" + list.get(0).getPath(), Toast.LENGTH_SHORT).show();
                    String url = "https://test.tanxinkui.cn/api/stuffs/upload";
                    //上传单个文件
                    for (int i = 0; i < list.size(); i++) {
                        finalfile.add(new File(list.get(i).getPath()));
                        Log.v("taxiik", "Url:" + list.get(i).getPath());
                        if (!new File(list.get(i).getPath()).exists()) {
                            ToastUtil.getInstance(this).showToast("文件不存在");
                        }
                    }
                    localVideoUri = url;
                    try {
                        showPick();
                    } catch (Exception e) {
                        e.printStackTrace();
                        ToastUtil.getInstance(this).showToast("图片太大，无法支持预览！");
                    }
                }*/
                if (data != null) {
                    // 相册
                    cropPic(data.getData());
                }
                break;
            default:
                break;
        }
    }

    private void openSysAlbum() {
        requestReadExternalPermission();
        Intent albumIntent = new Intent(Intent.ACTION_PICK);
        albumIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(albumIntent, ALBUM_RESULT_CODE);
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
        cropIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri.get(finalfile.size()));
        // 图片输出格式
        cropIntent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        // 头像识别 会启动系统的拍照时人脸识别
        // cropIntent.putExtra("noFaceDetection", true);
        startActivityForResult(cropIntent, CROP_RESULT_CODE);
    }
    @Override
    protected void onDestroy(){
        super.onDestroy();
        if (timerTask != null){
            timerTask.cancel();
            timerTask = null;
        }
        if (timer != null){
            timer.cancel();
            timer.purge();
            timer = null;
        }
        handler.removeMessages(11);
        handler.removeMessages(12);
    }


}
