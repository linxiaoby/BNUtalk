package com.bnutalk.socket;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.PublicKey;

import com.bnutalk.http.GetServerIp;

public class SocketTread extends Thread {
	
	private Socket socket;

	public SocketTread() {
		// TODO Auto-generated constructor stub
		this.socket = null;
	}

	public void option() {
		try {
			System.out.println("���ڳ�������");
			// �����ͻ���socket��ָ����������ַ�Ͷ˿ں�
			String servIp=new GetServerIp().getServerIp();
			int servPort=new GetServerIp().getServScoketPrt();
			socket = new Socket(servIp, servPort);
			// ��ȡ��������������������Ϣ
			OutputStream os = socket.getOutputStream();// �ֽ������
			PrintWriter pw = new PrintWriter(os);// ���������װ�ɴ�ӡ��
			pw.write("How are you?");
			pw.flush();
			socket.shutdownInput();// �ر������
			pw.close();
			os.close();
			socket.close();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		option();
	}
}
