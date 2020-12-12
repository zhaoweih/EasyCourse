package com.zhaoweihao.architechturesample.activity.example;

import android.os.Bundle;
import android.widget.EditText;

import com.zhaoweihao.architechturesample.R;
import com.zhaoweihao.architechturesample.base.BaseActivity;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SocketActivity extends BaseActivity {

    private WebSocketClient cc;

    String address = "ws://192.168.1.43:8887";

    @OnClick(R.id.btn_connect)
    void btn_connect() {
        try {
            // cc = new ChatClient(new URI(uriField.getText()), area, ( Draft ) draft.getSelectedItem() );
            cc = new WebSocketClient( new URI(address), new Draft_6455()) {

                @Override
                public void onMessage( String message ) {
//                    ta.append( "got: " + message + "\n" );
//                    ta.setCaretPosition( ta.getDocument().getLength() );
                }

                @Override
                public void onOpen( ServerHandshake handshake ) {
//                    ta.append( "You are connected to ChatServer: " + getURI() + "\n" );
//                    ta.setCaretPosition( ta.getDocument().getLength() );
                }

                @Override
                public void onClose( int code, String reason, boolean remote ) {
//                    ta.append( "You have been disconnected from: " + getURI() + "; Code: " + code + " " + reason + "\n" );
//                    ta.setCaretPosition( ta.getDocument().getLength() );
                }

                @Override
                public void onError( Exception ex ) {
//                    ta.append( "Exception occured ...\n" + ex + "\n" );
//                    ta.setCaretPosition( ta.getDocument().getLength() );
//                    ex.printStackTrace();

                }
            };

            cc.connect();
        } catch ( URISyntaxException ex ) {
        }
    }

    @OnClick(R.id.btn_disconnect)
    void btn_disconnect() {
        if (cc != null) {
            cc.close();
        }
    }

    @BindView(R.id.et_msg)
    EditText et_msg;

    @OnClick(R.id.btn_send)
    void btn_send() {
        if (cc != null && !cc.isClosed()) {
            cc.send(et_msg.getText().toString());

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_socket);
        ButterKnife.bind(this);


    }
}
