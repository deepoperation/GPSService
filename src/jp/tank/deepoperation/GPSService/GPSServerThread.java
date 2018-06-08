package jp.tank.deepoperation.GPSService;

import java.io.*;
import java.net.*;
import java.util.*;

//サーバスレッド
public class GPSServerThread extends Thread {
	private static List<GPSServerThread> threads=
		new ArrayList<GPSServerThread>();	//スレッド群
	private Socket socket;	//ソケット


	// コンストラクタ
	public GPSServerThread(Socket socket) {
		super();
		this.socket=socket;
		threads.add(this);
	}

	//処理
	public void run() {
		InputStream in =null;
		String message;
		int size;
		byte[] w=new byte[10240];
		try {
			//ストリーム
			in =socket.getInputStream();
			while(true) {
				try {
					//受信待ち
					size=in.read(w);

					//切断
					if (size<=0) throw new IOException();

					//読み込み
					message=new String(w,0,size,"UTF8");
					//メッセージ送信(現状単純に文字列を返すだけ。将来のためにのこする)
					sendMessage(message);
				} catch (IOException e) {
					socket.close();
					threads.remove(this);
					return;
				}
			}
		} catch (IOException e) {
			System.err.println(e);
		}
	}

	//全員にメッセージ送信
	synchronized public static void sendMessageAll(String message) {
		GPSServerThread thread;
		for (int i=0;i<threads.size();i++) {
			thread=(GPSServerThread)threads.get(i);
			if (thread.isAlive()) thread.sendMessage(message);
		}
	}

	//メッセージ送信
	public void sendMessage(String message){
		try {
			OutputStream out=socket.getOutputStream();
			byte[] w=message.getBytes("Shift_JIS");
			for(int i = 0; i < w.length; i++) {
				if(i > 0) {
					if(w[i] == '?') {
						w[i] = '-';
					}
				}
			}
			out.write(w);
			out.flush();
		} catch (IOException e) {
		}
	}
}