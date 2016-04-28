package com.bnutalk.IMtest;  
  
import java.io.OutputStream;  
import java.net.Socket;  
  
import android.app.Activity;  
import android.os.Bundle;  
import android.os.Handler;  
import android.os.Message;  
import android.view.View;  
import android.view.View.OnClickListener;  
import android.widget.Button;  
import android.widget.EditText;  
import com.bnutalk.ui.R;
import com.bnutalk.IMtest.ClientThread;  
public class MultiThreadClient extends Activity {  
    private EditText input, show;  
    private Button sendBtn;  
    private OutputStream os;  
    private Handler handler;  
  
    @Override  
    protected void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
        setContentView(R.layout.activity_imtest);  
        input = (EditText) findViewById(R.id.main_et_input);  
        show = (EditText) findViewById(R.id.main_et_show);  
        sendBtn = (Button) findViewById(R.id.main_btn_send);  
        handler = new Handler() {  
            @Override  
            public void handleMessage(Message msg) {  
                // 如果消息来自子线程  
                if (msg.what == 0x234) {  
                    // 将读取的内容追加显示在文本框中  
                    show.append("\n" + msg.obj.toString());  
                }  
            }  
        };  
        //每个用户在进入聊天界面时就自动创建一个到服务器的点到点socket
        Socket socket;  
        try {  
            socket = new Socket("172.22.191.137", 9527);  
            // 客户端启动ClientThread线程不断读取来自服务器的数据  
            new Thread(new ClientThread(socket, handler)).start();  
            os = socket.getOutputStream();  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
        sendBtn.setOnClickListener(new OnClickListener() {  
  
            @Override  
            public void onClick(View v) {  
                try {  
                    // 将用户在文本框内输入的内容写入网络  
                    os.write((input.getText().toString() + "\r\n").getBytes());  
                    // 清空input文本框数据  
                    input.setText("");  
                } catch (Exception e) {  
                    e.printStackTrace();  
                }  
            }  
        });  
    }  
  
}  