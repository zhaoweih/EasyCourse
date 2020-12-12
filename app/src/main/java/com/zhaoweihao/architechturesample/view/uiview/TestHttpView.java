package com.zhaoweihao.architechturesample.view.uiview;

import java.util.Observable;

public interface TestHttpView extends BaseView {

//    void changeNickNameSuccess(int code, String msg) ;

    /**
     * 查询成功
     * @param code
     * @param msg
     */
    void querySuccess(int code, Object msg);

}
