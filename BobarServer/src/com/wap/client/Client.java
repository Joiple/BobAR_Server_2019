package com.wap.client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class Client {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Client cl = new Client();
		cl.ClientRun("this is client");
	}
	
	public void ClientRun(String data) {
		String host = "localhost";
		int port = 4000;
		
		Socket socket = null;
		
		OutputStream os = null;
		OutputStreamWriter osw = null;
		BufferedWriter bw = null;
		
		InputStream is = null;
		InputStreamReader isr = null;
		BufferedReader br = null;
		
		try {
			socket = new Socket(host, port);
			
			//서버로 전송
			os = socket.getOutputStream();
			osw = new OutputStreamWriter(os);
			bw = new BufferedWriter(osw);
			
			bw.write(data);
			bw.newLine();
			bw.flush();
			
			//서버로부터 수신
			is = socket.getInputStream();
			isr = new InputStreamReader(is);
			br = new BufferedReader(isr);
			
			String receiveData = "";
			receiveData = br.readLine();
			
			System.out.println("서버로 부터 받은 데이터: "+receiveData);
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}finally {
			try {
				bw.close();
				osw.close();
				os.close();
				
				br.close();
				isr.close();
				is.close();
				
			}catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}
	}
}
