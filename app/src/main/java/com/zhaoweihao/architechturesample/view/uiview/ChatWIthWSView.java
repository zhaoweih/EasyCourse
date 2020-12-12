package com.zhaoweihao.architechturesample.view.uiview;

import org.java_websocket.handshake.ServerHandshake;

public interface ChatWIthWSView extends BaseView {

//    void getMsgsSucceed(int code, Object msg);

    void onWSMessage(String message);

    void onWSOpen(ServerHandshake handshake);

    void onWSClose(int code, String reason, boolean remote);

    void onWSError(Exception ex);

}
