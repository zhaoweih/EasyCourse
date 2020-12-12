package com.zhaoweihao.architechturesample.activity;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;
import com.zhaoweihao.architechturesample.R;
import com.zhaoweihao.architechturesample.adapter.NoteAdapter;
import com.zhaoweihao.architechturesample.base.BaseActivity;
import com.zhaoweihao.architechturesample.bean.RestResponse;
import com.zhaoweihao.architechturesample.bean.ShowNote;
import com.zhaoweihao.architechturesample.database.User;
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
 * @author
 * @description 笔记-笔记-各类笔记详细界面
 * @time 2019/3/1 14:22
 */
public class NoteNoteBookClassifiedActivity extends BaseActivity {
    @BindView(R.id.annbc_titleLayout)
    TitleLayout annbc_titleLayout;
    @BindView(R.id.annbc_empty_view)
    LinearLayout annbc_empty_view;
    @BindView(R.id.annbc_recyclerView)
    RecyclerView annbc_recyclerView;
    @BindView(R.id.annbc_fl_delete_control)
    FrameLayout annbc_fl_delete_control;
    @BindView(R.id.annbc_tv_count_num)
    TextView annbc_tv_count_num;
    @BindView(R.id.annbc_rb_select_note)
    QMUIRoundButton annbc_rb_select_note;
    @BindView(R.id.annbc_rb_delete_note)
    QMUIRoundButton annbc_rb_delete_note;
    private QMUITipDialog mLoadingDialog;
    private NoteAdapter noteAdapter;
    private List<ShowNote> NoteUiList = new ArrayList<>();
    private List<ShowNote> NoteListTemp = new ArrayList<>();
    private ArrayList<ShowNote> NoteListT = new ArrayList<>();
    private ArrayList<ShowNote> NoteListT2 = new ArrayList<>();
    private Button editBtn;
    private static final int NOTE_READING_MODE = 0;
    private static final int NOTE_MODE_EDIT = 1;
    private int mEditMode = 0;
    private int totalNum;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_note_book_classified);
        ButterKnife.bind(this);

        mLoadingDialog = new QMUITipDialog.Builder(NoteNoteBookClassifiedActivity.this)
                .setTipWord("正在删除笔记...")
                .setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
                .create();
        try {
            NoteUiList = (List<ShowNote>) getIntent().getSerializableExtra("noteList");
        } catch (Exception e) {
            e.printStackTrace();
        }
        annbc_titleLayout.setTitle("我的" + getIntent().getStringExtra("noteTag"));
        editBtn = annbc_titleLayout.getSettingButton();
        editBtn.setText("编辑");
        editBtn.setOnClickListener(view -> setController());
        annbc_rb_select_note.setOnClickListener(view -> selectAllNote());
        annbc_rb_delete_note.setOnClickListener(view -> deleteNote());
        initAdapter(false);
    }

    private void setController() {
        mEditMode = mEditMode == NOTE_READING_MODE ? NOTE_MODE_EDIT : NOTE_READING_MODE;
        annbc_fl_delete_control.setVisibility(mEditMode == NOTE_READING_MODE ? View.GONE : View.VISIBLE);
        editBtn.setText(mEditMode == NOTE_READING_MODE ? "编辑" : "取消");
        setAdapterChange(mEditMode == NOTE_MODE_EDIT);
    }

    private void setAdapterChange(Boolean isNeedShowSelectImage) {
        for (int i = 0; i < NoteUiList.size(); i++) {
            NoteUiList.get(i).setSelected(false);
        }
        if (isNeedShowSelectImage) {
            initAdapter(true);
        } else {
            initAdapter(false);
        }
        refreshCount();
    }

    private void initAdapter(Boolean isNeedShowSelectImage) {
        LinearLayoutManager layoutManager2 = new LinearLayoutManager(NoteNoteBookClassifiedActivity.this);
        annbc_recyclerView.setLayoutManager(layoutManager2);
        noteAdapter = (isNeedShowSelectImage ? new NoteAdapter(NoteUiList, true) :
                new NoteAdapter(NoteUiList));
        annbc_empty_view.setVisibility(NoteUiList.size() == 0 ? View.VISIBLE : View.INVISIBLE);
        editBtn.setVisibility(NoteUiList.size() != 0 ? View.VISIBLE : View.INVISIBLE);
        if(annbc_empty_view.getVisibility()==View.VISIBLE){
            annbc_fl_delete_control.setVisibility(View.INVISIBLE);
        }
        noteAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if (view.getId() == R.id.ln_iv_selector) {
                    NoteUiList.get(position).setSelected((NoteUiList.get(position).getSelected() == null) || (!NoteUiList.get(position).getSelected()));
                    view.setBackground(NoteUiList.get(position).getSelected() ? getResources().getDrawable(R.drawable.note_selected) :
                            getResources().getDrawable(R.drawable.note_selectable));
                    refreshCount();
                } else {
                    Intent intent = new Intent(NoteNoteBookClassifiedActivity.this, NoteShowDetailActivity.class);
                    intent.putExtra("note", NoteUiList.get(position));
                    intent.putExtra("avatar", noteAdapter.getAvatar().get(position));
                    intent.putExtra("name", noteAdapter.getName().get(position));
                    intent.putExtra("teaOrStu", noteAdapter.getStuOrTea().get(position));
                    intent.putExtra("commentList", (Serializable) noteAdapter.getNoteCommentList().get(position));
                    startActivity(intent);
                }
            }
        });

        annbc_recyclerView.setAdapter(noteAdapter);
    }

    private void selectAllNote() {
        for (int i = 0; i < NoteUiList.size(); i++) {
            NoteUiList.get(i).setSelected(true);
        }
        initAdapter(true);
        refreshCount();
    }

    private void deleteNote() {
        if (totalNum == 0) {
            Toast.makeText(NoteNoteBookClassifiedActivity.this, "请先选择要删除的笔记！", Toast.LENGTH_SHORT).show();
        } else {
            new QMUIDialog.MessageDialogBuilder(NoteNoteBookClassifiedActivity.this)
                    .setTitle("删除笔记")
                    .setMessage("确定要删除" + totalNum + "条笔记吗？")
                    .addAction("取消", new QMUIDialogAction.ActionListener() {
                        @Override
                        public void onClick(QMUIDialog dialog, int index) {
                            dialog.dismiss();
                        }
                    })
                    .addAction(0, "确定", QMUIDialogAction.ACTION_PROP_NEGATIVE, new QMUIDialogAction.ActionListener() {
                        @Override
                        public void onClick(QMUIDialog dialog, int index) {
                            dialog.dismiss();
                            mLoadingDialog.show();
                            for (int i = 0,j=0; i < NoteUiList.size(); i++) {
                                if (NoteUiList.get(i).getSelected() != null&&NoteUiList.get(i).getSelected()) {
                                    j++;
                                    deleteSelectedNote(i, totalNum ==j );
                                    Log.v("tanxiddd","is bb"+(totalNum == j)+"jude:"+j);
                                }
                            }
                        }
                    })
                    .show();


        }

    }
    private void clearNoteList(){

        for (int i = 0; i < NoteUiList.size(); i++) {
            if (NoteUiList.get(i).getSelected() != null) {
                if (!NoteUiList.get(i).getSelected()) {
                    NoteListT2.add(NoteUiList.get(i));
                    Log.v("tanxiddd","add"+i);
                }
            }
        }
        NoteUiList=NoteListT2;
    }

    private void refreshCount() {
        totalNum = 0;
        for (int i = 0; i < NoteUiList.size(); i++) {
            if (NoteUiList.get(i).getSelected() != null) {
                if (NoteUiList.get(i).getSelected()) {
                    totalNum++;
                }
            }
        }
        annbc_tv_count_num.setText("" + totalNum);
    }

    private void deleteSelectedNote(int i, Boolean isLastOne) {
        HttpUtil.sendGetRequest(Constant.DELETE_NOTEBOOK_URL + NoteUiList.get(i).getId(), new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(() -> {
                    mLoadingDialog.dismiss();
                    Toast.makeText(NoteNoteBookClassifiedActivity.this, "删除失败，请检查网络！", Toast.LENGTH_SHORT).show();
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                runOnUiThread(() -> {
                    if (isLastOne) {
                        mLoadingDialog.dismiss();
                        Toast.makeText(NoteNoteBookClassifiedActivity.this, "删除成功！", Toast.LENGTH_SHORT).show();
                        clearNoteList();
                        initAdapter(true);
                        refreshCount();
                    }
                });
            }
        });
    }

}
