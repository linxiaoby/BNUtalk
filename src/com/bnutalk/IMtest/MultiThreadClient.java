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
                // �����Ϣ�������߳�  
                if (msg.what == 0x234) {  
                    // ����ȡ������׷����ʾ���ı�����  
                    show.append("\n" + msg.obj.toString());  
                }  
            }  
        };  
        //ÿ���û��ڽ����������ʱ���Զ�����һ�����������ĵ㵽��socket
        Socket socket;  
        try {  
            socket = new Socket("172.22.191.137", 9527);  
            // �ͻ�������ClientThread�̲߳��϶�ȡ���Է�����������  
            new Thread(new ClientThread(socket, handler)).start();  
            os = socket.getOutputStream();  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
        sendBtn.setOnClickListener(new OnClickListener() {  
  
            @Override  
            public void onClick(View v) {  
                try {  
                    // ���û����ı��������������д������  
                    os.write((input.getText().toString() + "\r\n").getBytes());  
                    // ���input�ı�������  
                    input.setText("");  
                } catch (Exception e) {  
                    e.printStackTrace();  
                }  
            }  
        });  
    }  
  
}  