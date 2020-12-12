package com.zhaoweihao.architechturesample.ui;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhaoweihao.architechturesample.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author tanxinkui
 * @description: 自定义Editext模板
 * @time 2019/1/9 23:47
 */

public class EditTextLayout extends LinearLayout {
    @BindView(R.id.le_tv_tip)
    TextView le_tv_tip;
    @BindView(R.id.le_et)
    EditText le_et;
    @BindView(R.id.let_ib_visibility_switch)
    ImageButton let_ib_visibility_switch;
    Boolean initIsTextVisible;

    public EditTextLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.layout_edit_text, this);
        ButterKnife.bind(this);
    }
   public void initWithArgs(String textviewString,String edittexthintString,int inputType,int maxLength,Boolean ifBanInputSpace,Boolean ifBanSpecialChar){
        setTextViewString(textviewString);
        setEditTextHintString(edittexthintString);
        setInputType(inputType);
       //le_et.setTransformationMethod(PasswordTransformationMethod.getInstance());
        if(ifBanInputSpace){
            if(ifBanSpecialChar){
                le_et.setFilters(new InputFilter[]{setMaxLength(maxLength),setEditTextInputSpace(),setEditTextInputSpeChat()});
            }else{
                le_et.setFilters(new InputFilter[]{setMaxLength(maxLength),setEditTextInputSpace()});
            }
        }else{
            if(ifBanSpecialChar){
                le_et.setFilters(new InputFilter[]{setMaxLength(maxLength),setEditTextInputSpeChat()});
            }else{
                le_et.setFilters(new InputFilter[]{setMaxLength(maxLength)});
            }
        }
        let_ib_visibility_switch.setVisibility(INVISIBLE);

   }

    public void initWithArgs(String textviewString,String edittexthintString,int inputType,int maxLength,Boolean ifBanInputSpace,Boolean ifBanSpecialChar,Boolean isTextNowVisible){
        setTextViewString(textviewString);
        setEditTextHintString(edittexthintString);
        setInputType(inputType);
        if(ifBanInputSpace){
            if(ifBanSpecialChar){
                le_et.setFilters(new InputFilter[]{setMaxLength(maxLength),setEditTextInputSpace(),setEditTextInputSpeChat()});
            }else{
                le_et.setFilters(new InputFilter[]{setMaxLength(maxLength),setEditTextInputSpace()});
            }
        }else{
            if(ifBanSpecialChar){
                le_et.setFilters(new InputFilter[]{setMaxLength(maxLength),setEditTextInputSpeChat()});
            }else{
                le_et.setFilters(new InputFilter[]{setMaxLength(maxLength)});
            }
        }
        initIsTextVisible=isTextNowVisible;
        initTextVisibility(isTextNowVisible);
        let_ib_visibility_switch.setOnClickListener(view -> switchTextVisibility());
    }

   private void initTextVisibility(Boolean isTextNowVisible){
       if(isTextNowVisible){
           hideText();
       }else {
          showText();
       }
   }
   private void switchTextVisibility(){
        if(initIsTextVisible){
            showText();
            initIsTextVisible=false;
        }else {
            hideText();
            initIsTextVisible=true;
        }
   }

   private void hideText(){
       Resources resources = this.getResources();
       Drawable btnDrawable1 = resources.getDrawable(R.drawable.showpassword2);
       let_ib_visibility_switch.setBackground(btnDrawable1);
       le_et.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
   }
    private void showText(){
        Resources resources = this.getResources();
        Drawable btnDrawable1 = resources.getDrawable(R.drawable.showpassword1);
        let_ib_visibility_switch.setBackground(btnDrawable1);
        le_et.setTransformationMethod(PasswordTransformationMethod.getInstance());
    }


    private void setTextViewString(String tvString) {
        le_tv_tip.setText(tvString);
    }

    /**
     * 以下方法用于:设置默认的提示的字符串
     * by txk
     */
    private void setEditTextHintString(String etString) {
        le_et.setHint(etString);
    }

    /**
     * 以下方法：用于:返回Editext获得的字符串
     * by txk
     */
    public String getEditTextString() {
        return le_et.getText().toString();
    }

    /**
     * 以下方法用于:设置输入的类型
     * by txk
     */
    private void setInputType(int inputType) {
        le_et.setInputType(inputType);
    }

    /**
     * 以下方法用于:设置最大字符串
     * by txk
     */
    private InputFilter setMaxLength(int maxLength) {
        return new InputFilter.LengthFilter(maxLength);
    }

    /**
     * 禁止EditText输入空格和换行符
     */
    private InputFilter setEditTextInputSpace() {
        InputFilter filter = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                if (source.equals(" ") || source.toString().contentEquals("\n")) {
                    return "";
                } else {
                    return null;
                }
            }
        };
        return filter;
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
}
