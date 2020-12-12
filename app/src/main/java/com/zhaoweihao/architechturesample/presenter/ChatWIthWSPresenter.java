package com.zhaoweihao.architechturesample.presenter;

import android.content.Context;
import android.content.Intent;

import com.zhaoweihao.architechturesample.bean.ChatBean;
import com.zhaoweihao.architechturesample.bean.ChatRequestBean;
import com.zhaoweihao.architechturesample.https.ApiCallback;
import com.zhaoweihao.architechturesample.util.GsonUtil;
import com.zhaoweihao.architechturesample.util.Logger;
import com.zhaoweihao.architechturesample.view.uiview.ChatSystemView;
import com.zhaoweihao.architechturesample.view.uiview.ChatWIthWSView;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * @author zhaoweihao
 * @date 2019/1/10
 */

public class ChatWIthWSPresenter extends BasePresenter<ChatWIthWSView> {

    private WebSocketClient cc;

    public ChatWIthWSPresenter(ChatWIthWSView mvpView, Context context) {
        super(context.getApplicationContext(), mvpView);
    }

    /**
     * 连接到WS服务器
     * @param address
     */
    public void connect2WS(String address, Integer courseId, Integer userId, Integer friendId, boolean isFriendTalk) {
        try {
            cc = new WebSocketClient(new URI(address), new Draft_6455()) {

                @Override
                public void onMessage(String message) {
//                    ta.append( "got: " + message + "\n" );
//                    ta.setCaretPosition( ta.getDocument().getLength() );
                    Logger.d("onMessage message === " + message);
                    mvpView.onWSMessage(message);

                }

                @Override
                public void onOpen(ServerHandshake handshake) {
//                    ta.append( "You are connected to ChatServer: " + getURI() + "\n" );
//                    ta.setCaretPosition( ta.getDocument().getLength() );
                    mvpView.onWSOpen(handshake);
                    ChatRequestBean chatRequestBean  = new ChatRequestBean();
                    if (isFriendTalk) {
                        chatRequestBean.setInfoType(2);
                        chatRequestBean.setUserId(userId);
                        chatRequestBean.setFriendId(friendId);
                    } else {
                        chatRequestBean.setInfoType(0);
                        chatRequestBean.setClassId(courseId);
                    }
                    cc.send(GsonUtil.GsonString(chatRequestBean));


                }

                @Override
                public void onClose(int code, String reason, boolean remote) {
//                    ta.append( "You have been disconnected from: " + getURI() + "; Code: " + code + " " + reason + "\n" );
//                    ta.setCaretPosition( ta.getDocument().getLength() );
                    if (mvpView != null) {
                        mvpView.onWSClose(code, reason, remote);
                    }
                }

                @Override
                public void onError(Exception ex) {
//                    ta.append( "Exception occured ...\n" + ex + "\n" );
//                    ta.setCaretPosition( ta.getDocument().getLength() );
//                    ex.printStackTrace();
                    if (mvpView != null) {
                        mvpView.onWSError(ex);
                    }

                }
            };
            cc.connect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 关闭WS服务器
     */
    public void closeWS() {
        if (cc != null) {
            cc.close();
        }
    }

    /**
     * 发送信息到WS服务器
     * @param msg
     */
    public void sendMsg(String msg) {
        if (cc != null && !cc.isClosed()) {
            cc.send(msg);

        }
    }

}
