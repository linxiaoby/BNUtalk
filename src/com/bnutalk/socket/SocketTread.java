package com.bnutalk.socket;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.PublicKey;

public class SocketTread extends Thread {
	
	private Socket socket;

	public SocketTread() {
		// TODO Auto-generated constructor stub
		this.socket = null;
	}

	public void option() {
		try {
			System.out.println("正在尝试连接");
			// 创建客户端socket，指定服务器地址和端口号
			socket = new Socket("172.16.115.85", 8211);
			// 获取输出流，向服务器发送信息
			OutputStream os = socket.getOutputStream();// 字节输出流
			PrintWriter pw = new PrintWriter(os);// 将输出流包装成打印流
			pw.write("How are you?");
			pw.flush();
			socket.shutdownInput();// 关闭输出流
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
