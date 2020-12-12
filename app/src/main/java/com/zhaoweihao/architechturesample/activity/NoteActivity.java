package com.zhaoweihao.architechturesample.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;
import com.zhaoweihao.architechturesample.R;
import com.zhaoweihao.architechturesample.base.BaseActivity;
import com.zhaoweihao.architechturesample.bean.RestResponse;
import com.zhaoweihao.architechturesample.bean.ShowNote;
import com.zhaoweihao.architechturesample.database.User;
import com.zhaoweihao.architechturesample.ui.SearchBoardLayout;
import com.zhaoweihao.architechturesample.ui.TitleLayout;
import com.zhaoweihao.architechturesample.util.Constant;
import com.zhaoweihao.architechturesample.util.HttpUtil;

import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * @author tanxinkui
 * @description 笔记-笔记-（笔记种类的界面：草稿，共享笔记，私有笔记）
 * @time 2019/1/27 21:33
 */
public class NoteActivity extends BaseActivity {
    @BindView(R.id.an_title)
    TitleLayout an_title;
    @BindView(R.id.an_search_board)
    SearchBoardLayout an_search_board;
    @BindView(R.id.an_fl_draft)
    FrameLayout an_fl_draft;
    @BindView(R.id.an_fl_share_note)
    FrameLayout an_fl_share_note;
    @BindView(R.id.an_fl_private_note)
    FrameLayout an_fl_private_note;
    @BindView(R.id.an_tv_draft)
    TextView an_tv_draft;
    @BindView(R.id.an_tv_share)
    TextView an_tv_share;
    @BindView(R.id.an_tv_private)
    TextView an_tv_private;

    private QMUITipDialog tipDialog;
    private List<ShowNote> NoteUiList;
    private List<ShowNote> NoteListDraftTemp;
    private List<ShowNote> NoteListShareTemp;
    private List<ShowNote> NoteListPrivateTemp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        ButterKnife.bind(this);
        init();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    private void refreshArrayList() {
        NoteUiList = new ArrayList<>();
        NoteListDraftTemp = new ArrayList<>();
        NoteListShareTemp = new ArrayList<>();
        NoteListPrivateTemp = new ArrayList<>();
    }

    private void init() {
        an_title.setTitle("笔记");
        an_search_board.setOnClickListener(view -> goSearch());
        an_fl_draft.setOnClickListener(view -> goClassifiedNote("草稿"));
        an_fl_share_note.setOnClickListener(view -> goClassifiedNote("共享笔记"));
        an_fl_private_note.setOnClickListener(view -> goClassifiedNote("私有笔记"));
        initLoading();
    }

    private void initLoading() {
        tipDialog = new QMUITipDialog.Builder(NoteActivity.this)
                .setTipWord("正在加载...")
                .setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
                .create();
        tipDialog.show();
        if (DataSupport.findLast(User.class) != null) {
            toLoadNote();
        }
    }

    private void toLoadNote() {
        refreshArrayList();
        //Constant.GET_NOTE_URL + "?user_id=" + DataSupport.findLast(User.class).getUserId()
        HttpUtil.sendGetRequest(Constant.GET_PERSONAL_ALL_NOTE_URL + DataSupport.findLast(User.class).getUserId(), new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(() -> {
                    tipDialog.dismiss();
                    Toast.makeText(NoteActivity.this, "加载错误，请重试！", Toast.LENGTH_SHORT).show();
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String body = response.body().string();
                //解析json数据组装RestResponse对象
                RestResponse restResponse = JSON.parseObject(body, RestResponse.class);
                //RestResponse restResponse = new Gson().fromJson(body, RestResponse.class);
                if (restResponse.getCode() == Constant.SUCCESS_CODE) {
                    NoteUiList.clear();
                    NoteUiList = JSON.parseArray(restResponse.getPayload().toString(), ShowNote.class);
                    NoteListDraftTemp.clear();
                    NoteListShareTemp.clear();
                    NoteListPrivateTemp.clear();
                    for (int i = 0; i < NoteUiList.size(); i++) {
                        if (NoteUiList.get(i).getTag().equals("草稿")) {
                            NoteListDraftTemp.add(NoteUiList.get(i));
                        }
                        if (NoteUiList.get(i).getTag().equals("共享笔记")) {
                            NoteListShareTemp.add(NoteUiList.get(i));
                        }
                        if (NoteUiList.get(i).getTag().equals("私有笔记")) {
                            NoteListPrivateTemp.add(NoteUiList.get(i));
                        }
                    }

                    /*NoteUiList.addAll(new Gson().fromJson(restResponse.getPayload().toString(), new TypeToken<List<ShowNote>>() {
                    }.getType()));*/
                    runOnUiThread(() -> {
                        refreshNum();
                        tipDialog.dismiss();
                    });
                }
                if (restResponse.getCode() != Constant.SUCCESS_CODE) {
                    runOnUiThread(() -> {
                        refreshNum();
                        Toast.makeText(NoteActivity.this, "暂无笔记！", Toast.LENGTH_SHORT).show();
                        tipDialog.dismiss();
                    });
                }
            }
        });
    }

    private void refreshNum() {
        an_tv_draft.setText("" + NoteListDraftTemp.size());
        an_tv_share.setText("" + NoteListShareTemp.size());
        an_tv_private.setText("" + NoteListPrivateTemp.size());
    }

    private void goSearch() {
        if (NoteUiList.size() == 0) {
            Toast.makeText(NoteActivity.this, "暂无笔记可搜索！", Toast.LENGTH_SHORT).show();
        } else {
            goNextActivity("NoteNoteBookSearchPersonalActivity", null, NoteUiList);
        }

    }

    private void goClassifiedNote(String tag) {
        if (tag.equals("草稿")) {
            goNextActivity("NoteNoteBookClassifiedActivity", tag, NoteListDraftTemp);
        }
        if (tag.equals("共享笔记")) {
            goNextActivity("NoteNoteBookClassifiedActivity", tag, NoteListShareTemp);
        }
        if (tag.equals("私有笔记")) {
            goNextActivity("NoteNoteBookClassifiedActivity", tag, NoteListPrivateTemp);
        }

    }

    private void goNextActivity(String className, String extraTag, List<ShowNote> noteList) {
        Intent intent = null;
        try {
            intent = new Intent(NoteActivity.this, Class.forName("com.zhaoweihao.architechturesample.activity." + className));
            if (!TextUtils.isEmpty(extraTag)) {
                intent.putExtra("noteList", (Serializable) noteList);
                intent.putExtra("noteTag", extraTag);
            } else {
                intent.putExtra("noteList", (Serializable) noteList);
            }
            startActivity(intent);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

}
