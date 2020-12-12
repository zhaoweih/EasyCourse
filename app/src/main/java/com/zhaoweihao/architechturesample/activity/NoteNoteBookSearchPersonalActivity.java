package com.zhaoweihao.architechturesample.activity;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.kongzue.stacklabelview.StackLabel;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;
import com.zhaoweihao.architechturesample.R;
import com.zhaoweihao.architechturesample.adapter.NoteAdapter;
import com.zhaoweihao.architechturesample.base.BaseActivity;
import com.zhaoweihao.architechturesample.bean.RestResponse;
import com.zhaoweihao.architechturesample.bean.ShowNote;
import com.zhaoweihao.architechturesample.database.HistoryTag;
import com.zhaoweihao.architechturesample.database.User;
import com.zhaoweihao.architechturesample.ui.TitleLayout;
import com.zhaoweihao.architechturesample.util.Constant;
import com.zhaoweihao.architechturesample.util.HttpUtil;

import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
*@description 笔记-笔记本-搜索自己的笔记
*@author 
*@time 2019/3/1 15:20
*/
public class NoteNoteBookSearchPersonalActivity extends BaseActivity {
    @BindView(R.id.annbsp_search_board_input_layout)
    com.zhaoweihao.architechturesample.ui.SearchBoardInputLayout annbsp_search_board_input_layout;
    @BindView(R.id.annbsp_recyclerView)
    RecyclerView annbsp_recyclerView;
    @BindView(R.id.annbsp_empty_view)
    LinearLayout annbsp_empty_view;
    private QMUITipDialog tipDialog;
    private List<ShowNote> NoteUiList = new ArrayList<>();
    private List<ShowNote> NoteUiListTemp = new ArrayList<>();
    private NoteAdapter noteAdapter;
    private Handler handler;
    private Timer timer;
    private TimerTask timerTask;
    private int originalNum;
    private StackLabel mstackLabel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_note_book_search_personal);
        ButterKnife.bind(this);
        //先设置editext的hintText,然后是限制的历史记录数量，然后是搜索的页面的标签，（标签设置完，在HistoryTag备注一下:note_search_personal_all）
        annbsp_search_board_input_layout.initWithArgs("搜索",10,"note_search_personal_all");
        List<HistoryTag> allData = DataSupport.where("tagTag=?", "note_search_personal_all").find(com.zhaoweihao.architechturesample.database.HistoryTag.class);
        originalNum = allData.size();
        try {
            NoteUiList =(List<ShowNote>) getIntent().getSerializableExtra("noteList");
        } catch (Exception e) {
            e.printStackTrace();
        }
        mstackLabel = annbsp_search_board_input_layout.getStackbel();
        detectInput("note_search_personal_all");
        setStableClickListener();
    }
    private void init(String searchKeyWord){
        /*tipDialog = new QMUITipDialog.Builder(NoteNoteBookSearchPersonalActivity.this)
                .setTipWord("正在加载笔记...")
                .setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
                .create();
        tipDialog.show();
        if(DataSupport.findLast(User.class)!=null){
            toLoadNote(searchKeyWord);
        }*/
        initAdapter(searchKeyWord);
    }
    private void toLoadNote(String searchKeyWord){
        //Constant.GET_NOTE_URL + "?user_id=" + DataSupport.findLast(User.class).getUserId()
        HttpUtil.sendGetRequest(Constant.GET_NOTEBOOK_BY_TAG+searchKeyWord, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(()-> {

                    tipDialog.dismiss();
                    Toast.makeText(NoteNoteBookSearchPersonalActivity.this,"加载错误，请重试！",Toast.LENGTH_SHORT).show();

                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String body = response.body().string();
                //解析json数据组装RestResponse对象
                RestResponse restResponse =JSON.parseObject(body, RestResponse.class);
                //RestResponse restResponse = new Gson().fromJson(body, RestResponse.class);
                if (restResponse.getCode() == Constant.SUCCESS_CODE) {
                    NoteUiList.clear();
                    NoteUiList=JSON.parseArray(restResponse.getPayload().toString(),ShowNote.class);
                    /*NoteUiList.addAll(new Gson().fromJson(restResponse.getPayload().toString(), new TypeToken<List<ShowNote>>() {
                    }.getType()));*/
                    runOnUiThread(()->{

                        //initAdapter();
                        tipDialog.dismiss();

                    });
                }
                if (restResponse.getCode() != Constant.SUCCESS_CODE) {
                    runOnUiThread(()->{

                        //initAdapter();
                        annbsp_empty_view.setVisibility(View.VISIBLE);
                        tipDialog.dismiss();
                        Toast.makeText(NoteNoteBookSearchPersonalActivity.this,"暂无笔记！",Toast.LENGTH_SHORT).show();

                    });
                }
            }
        });
    }
    private void initAdapter(String searchKeyword) {
        LinearLayoutManager layoutManager2 = new LinearLayoutManager(NoteNoteBookSearchPersonalActivity.this);
        annbsp_recyclerView.setLayoutManager(layoutManager2);
        NoteUiListTemp.clear();
        for(int i=0;i<NoteUiList.size();i++){
            if(NoteUiList.get(i).getTitle().contains(searchKeyword)||NoteUiList.get(i).getContent().contains(searchKeyword)){
                NoteUiListTemp.add(NoteUiList.get(i));
            }
        }
        noteAdapter = new NoteAdapter(NoteUiListTemp,searchKeyword);
        //noteAdapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);
        noteAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent=new Intent(NoteNoteBookSearchPersonalActivity.this,NoteShowDetailActivity.class);
                intent.putExtra("note", NoteUiListTemp.get(position));
                intent.putExtra("avatar", noteAdapter.getAvatar().get(position));
                intent.putExtra("name", noteAdapter.getName().get(position));
                intent.putExtra("teaOrStu", noteAdapter.getStuOrTea().get(position));
                intent.putExtra("commentList", (Serializable) noteAdapter.getNoteCommentList().get(position));
                startActivity(intent);
            }
        });
        annbsp_recyclerView.setAdapter(noteAdapter);
        if(NoteUiList.size()==0){
            annbsp_empty_view.setVisibility(View.VISIBLE);
            Toast.makeText(NoteNoteBookSearchPersonalActivity.this,"暂无您搜索的笔记！",Toast.LENGTH_SHORT).show();
        }else {
            annbsp_empty_view.setVisibility(View.GONE);
        }
    }
    public void setStableClickListener() {
        mstackLabel.setOnLabelClickListener((int index, View view, String s) -> {
            goSearchNow(s);
        });
    }
    private void goSearchNow(String inputText) {
        // 如果全是数字则认为输入的是教师编号
        init(inputText);

    }
    private void detectInput(String searchTagTag) {


        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 11) {
                    mstackLabel = annbsp_search_board_input_layout.getStackbel();
                    setStableClickListener();
                    goSearchNow(annbsp_search_board_input_layout.getFinalSearchString());
                }
                if (msg.what == 12) {
                }
                super.handleMessage(msg);
            }
        };
        timer = new Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                Message message = new Message();
                List<HistoryTag> allData = DataSupport.where("tagTag=?", searchTagTag).find(com.zhaoweihao.architechturesample.database.HistoryTag.class);
                if ((allData.size() != originalNum) && (allData.size() != 0)) {
                    message.what = 11;
                    originalNum = allData.size();
                } else {
                    message.what = 12;
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
