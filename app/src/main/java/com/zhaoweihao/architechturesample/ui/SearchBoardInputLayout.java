package com.zhaoweihao.architechturesample.ui;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.Nullable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kongzue.stacklabelview.StackLabel;
import com.kongzue.stacklabelview.interfaces.OnLabelClickListener;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;
import com.zhaoweihao.architechturesample.R;
import com.zhaoweihao.architechturesample.database.HistoryTag;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 *@description
 * 搜索输入界面的模板
 * 先设置editext的hintText,然后是限制的历史记录数量，然后是搜索的页面的标签，（标签设置完，在HistoryTag备注一下:例如：note_search_all）确保标签不重复
 * 使用时先调用initWithArgs(String initHintTextString,int initlimitedNumber,String initSearchTagTag);
 * @author tanxinkui
 * @date 2019/1/8
 */


public class SearchBoardInputLayout extends LinearLayout {
    @BindView(R.id.lsbi_bt_back)
    Button lsbi_bt_back;
    @BindView(R.id.lsbi_et_input)
    EditText lsbi_et_input;
    @BindView(R.id.lsbi_ib_clear_history)
    ImageButton lsbi_ib_clear_history;
    @BindView(R.id.lsbi_st_search_histroy)
    StackLabel lsbi_st_search_histroy;
    @BindView(R.id.lsbi_tv_default)
    TextView lsbi_tv_default;

    ArrayList<String> labels;
    String searchTagTag;
    int litimitedNumber = -1;
    String SearchBoardInputLayout = "SearchBoardInputLayout.class";
    String finalSearchString;

    public SearchBoardInputLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.layout_search_board_input, this);
        ButterKnife.bind(this);
        //返回按钮
        lsbi_bt_back.setOnClickListener(view -> ((Activity) getContext()).finish());

        labels = new ArrayList<>();

        //清除历史记录
        lsbi_ib_clear_history.setOnClickListener(view -> {
            if (checkIsLebelsNull()) {
                Toast.makeText(getContext(), "暂无历史记录", Toast.LENGTH_SHORT).show();
            } else {
                new QMUIDialog.MessageDialogBuilder(getContext())
                        .setTitle("清空历史搜索")
                        .setMessage("确定要删除所有历史记录吗？")
                        .addAction("取消", new QMUIDialogAction.ActionListener() {
                            @Override
                            public void onClick(QMUIDialog dialog, int index) {
                                dialog.dismiss();
                            }
                        })
                        .addAction(0, "删除", QMUIDialogAction.ACTION_PROP_NEGATIVE, new QMUIDialogAction.ActionListener() {
                            @Override
                            public void onClick(QMUIDialog dialog, int index) {
                                removeAllLabelsArray();
                                Toast.makeText(getContext(), "删除成功", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }
                        })
                        .show();
            }

        });
        //单击标签事件
        openClickSearch();
        //键盘回车或完成，则搜索
        goKeyBoardSearch();
        //检查是否有搜索历史记录，有则显示
        //禁止输入特殊字符和空格
        lsbi_et_input.setFilters(new InputFilter[]{setEditTextInputSpace(),setEditTextInputSpeChat()});

    }

    public void initWithArgs(String initHintTextString,int initlimitedNumber,String initSearchTagTag){
        setHintText(initHintTextString);
        setLitimitedNumber(initlimitedNumber);
        setSearchTagTag(initSearchTagTag);
        if(!checkIsLebelsNull()){
            refreshLabels();
        }
    }


    /**
     * 返回输入的editext
     */

    public EditText getLsbi_et_input() {
        return lsbi_et_input;
    }

    /**
    以下方法是设置哪一类型的搜索
    */

    public void setSearchTagTag(String searchTagTag) {
        this.searchTagTag = searchTagTag;
    }

    /**
    以下方法是设置限制的历史记录的最多数量
    */

    public void setLitimitedNumber(int litimitedNumber) {
        this.litimitedNumber = litimitedNumber;
    }

    /**
    以下方法检查是否设置搜索类型
    */

    public Boolean checkSearchTagTagIsSet() {
        if (TextUtils.isEmpty(searchTagTag)) {
            Log.d(SearchBoardInputLayout, "没有设置哪一类搜索的类型");
            return false;
        } else {
            return true;
        }
    }

    /**
    以下方法检查是否设置限制的历史记录的数量
    */

    public Boolean checkLimitedNumberIsSet() {
        if (litimitedNumber == -1) {
            Log.d(SearchBoardInputLayout, "限制的数量未设置");
            return false;
        } else {
            return true;
        }
    }

    /**
    以下方法是否设置输入框提示的文字
    */

    public void setHintText(String hintText) {
        lsbi_et_input.setHint(hintText);
    }

    /**
    以下方法检查是点击标签的事件
    */

    public void openClickSearch() {
        lsbi_st_search_histroy.setOnLabelClickListener((int index, View view, String s) -> {
           /* if (lsbi_st_search_histroy.isDeleteButton()) {
                // removeSelectedLable(searchTagTag, s);
                //setLabelsArray(searchTagString,);
            } else {
                //Toast.makeText(getContext(), "点击了：" + s, Toast.LENGTH_SHORT).show();
                //setFinalSearchString(s);
                //searchAction();
            }*/
        });
    }

    /**
     以下方法是为了要不要开启单独制定删除某个历史标签
     *暂时不需要开启删除指定的标签
     */

    private void openDeletesetting() {
        lsbi_st_search_histroy.setDeleteButton(true);
    }

   /**
   用于检测键盘输入，如果键盘回车或完成，则开始搜索
   */

    public void goKeyBoardSearch() {
        lsbi_et_input.setOnEditorActionListener((TextView v, int actionId, KeyEvent event) -> {
            //当actionId == XX_SEND 或者 XX_DONE时都触发
            //或者event.getKeyCode == ENTER 且 event.getAction == ACTION_DOWN时也触发
            //注意，这是一定要判断event != null。因为在某些输入法上会返回null。
            if (actionId == EditorInfo.IME_ACTION_SEND
                    || actionId == EditorInfo.IME_ACTION_DONE
                    || (event != null && KeyEvent.KEYCODE_ENTER == event.getKeyCode() && KeyEvent.ACTION_DOWN == event.getAction())) {
                if (TextUtils.isEmpty(lsbi_et_input.getText())) {
                    Toast.makeText(getContext(), "请输入搜索词", Toast.LENGTH_SHORT).show();
                } else {
                    //判断是否含有回车符
                    if (lsbi_et_input.getText().toString().contains("\n")) {
                        checkAndEnter(processEnterAsString(lsbi_et_input.getText().toString(),"\n"));
                    } else if (lsbi_et_input.getText().toString().contains("\r\n")) {
                        checkAndEnter(processEnterAsString(lsbi_et_input.getText().toString(),"\r\n"));
                    } else {
                        enterAndSearch(lsbi_et_input.getText().toString());
                    }
                }
            }
            return false;
        });
    }
    /**
    *1.处理换行作为string的一部分
    */

    public String processEnterAsString(String needProcessed,String enterType){
        while(needProcessed.contains(enterType)){
            needProcessed=needProcessed.substring(enterType.length());
            Log.d(SearchBoardInputLayout, "注意这里有换行符:"+enterType+"需要处理的是：" +needProcessed);
        }
        return needProcessed;

    }

    /**
   2. 检查处理过的字符串是否为空值
    */

    public void checkAndEnter(String processedString){
        if(processedString.equals("")){
            Toast.makeText(getContext(), "请输入搜索词", Toast.LENGTH_SHORT).show();
        }else {
            enterAndSearch(processedString);
        }
    }

    /**
    3.执行换行搜索，并且设置当前的搜索词 setFinalSearchString
    */

    public void enterAndSearch(String currentString) {
        try {
            setLabelsArray(currentString);
            setFinalSearchString(currentString);
            lsbi_et_input.setText("");
        } catch (Exception e) {
            Log.v(SearchBoardInputLayout, "输入出错" + e.toString());
            e.printStackTrace();
        }
        searchAction();
    }

    /**
    返回及设置最终要搜的关键字
     */

    public String getFinalSearchString() {
        return finalSearchString;
    }

    public void setFinalSearchString(String finalSearchString) {
        this.finalSearchString = finalSearchString;
    }

    /**
    最终搜索的事件
    */

    public void searchAction() {

    }

    /**
    SearchTagTag:例如：首页的搜索(main)，信息页的搜索(info)
    每类标签总量（allData）最多限制的数量（limitedNumber）10个，超过10个则自动覆盖（删除该类多余的最旧的历史标签：historyExtra）
    SearTagString:例如：“大学英语”

    */

    public void setLabelsArray(String searchTagString) {
        if (checkSearchTagTagIsSet()) {
            //先添加标签
            com.zhaoweihao.architechturesample.database.HistoryTag addHistoryTag = new com.zhaoweihao.architechturesample.database.HistoryTag();
            addHistoryTag.setTagTag(searchTagTag);
            addHistoryTag.setTagContent(searchTagString);
            addHistoryTag.save();
            //判断该类型的标签有没有超过10个，超过则删除掉最旧的那个
            List<HistoryTag> allData = DataSupport.where("tagTag=?", searchTagTag).find(com.zhaoweihao.architechturesample.database.HistoryTag.class);
            if (checkLimitedNumberIsSet()) {
                if (allData.size() > litimitedNumber) {
                    com.zhaoweihao.architechturesample.database.HistoryTag historyExtra = DataSupport.where("tagTag=?", searchTagTag).findFirst(com.zhaoweihao.architechturesample.database.HistoryTag.class);
                    historyExtra.delete();
                }
                refreshLabels();
            }
        }
    }
    public  StackLabel getStackbel(){
        return lsbi_st_search_histroy;
    }

    public void refreshLabels() {
        //传给labelArray
        labels = new ArrayList<>();
        List<HistoryTag> allDataSorted = DataSupport.where("tagTag=?", searchTagTag).find(com.zhaoweihao.architechturesample.database.HistoryTag.class);
        for (com.zhaoweihao.architechturesample.database.HistoryTag forLabels : allDataSorted) {
            labels.add(forLabels.getTagContent());
        }
        lsbi_st_search_histroy.setLabels(labels);
        if (labels.size() == 0) {
            lsbi_tv_default.setText("暂无搜索记录");
        } else {
            lsbi_tv_default.setText("");
        }
    }

    /**
     * 删除数据库所有标签
     *
     * */

    public void removeAllLabelsArray() {
        if (checkSearchTagTagIsSet()) {
            DataSupport.deleteAll(com.zhaoweihao.architechturesample.database.HistoryTag.class, "tagTag=?", searchTagTag);
            refreshLabels();
        }
    }

    public Boolean checkIsLebelsNull() {
        List<HistoryTag> allData = DataSupport.where("tagTag=?", searchTagTag).find(com.zhaoweihao.architechturesample.database.HistoryTag.class);
        if (allData.size() > 0) {
            return false;
        } else {
            return true;
        }
    }
    /**
     * 如果需要可以删除指定的标签
     * */

    public void removeSelectedLable(String searchTagTag, String searchTagString) {
        DataSupport.deleteAll(com.zhaoweihao.architechturesample.database.HistoryTag.class, "tagTag=? and searchTagString=?", searchTagTag, searchTagString);
        refreshLabels();
    }

    /**
     * 禁止EditText输入特殊字符
     *
     */
    private InputFilter setEditTextInputSpeChat() {
        InputFilter filter = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                String speChat = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？-]";
                Pattern pattern = Pattern.compile(speChat);
                Matcher matcher = pattern.matcher(source.toString());
                if (matcher.find()) {
                    return "";
                } else {
                    return null;
                }
            }
        };
        return filter;
    }
    /**
     * 禁止EditText输入空格
     */
    private InputFilter setEditTextInputSpace() {
        InputFilter filter = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                if (source.equals(" ")) {
                    return "";
                } else {
                    return null;
                }
            }
        };
        return filter;
    }

}
