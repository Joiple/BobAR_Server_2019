package com.wap.client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner scan = new Scanner(System.in);
		Client cl = new Client();
		System.out.println("request : ");
		String requestData = scan.nextLine();
		
		cl.initDataRequest(requestData);
		//cl.receiveImg(requestData);
	}
	
	
	public void receiveImg(String data) {
		String host = "localhost";
		int port = 4000;
		
		Socket socket = null;
		
		OutputStream os = null;
		OutputStreamWriter osw = null;
		BufferedWriter bw = null;
		
		InputStream is = null;
		InputStreamReader isr = null;
		BufferedReader br = null;
		
		FileOutputStream fos = null;
		
		
		try {
			socket = new Socket(host, port);
			
			//서버로 전송
			os = socket.getOutputStream();
			osw = new OutputStreamWriter(os);
			bw = new BufferedWriter(osw);
			
			bw.write(data);
			bw.newLine();
			bw.flush();
			
			fos = new FileOutputStream("test5.jpg");
			is = socket.getInputStream();
			byte buffer[] = new byte[2048];
			
			//read header(10byte)
			is.read(buffer, 0, 10);
			String header = new String(buffer, 0, 10);
			int bodysize = Integer.parseInt(header);
			
			int readsize = 0;
			
			//read body
			while(readsize < bodysize) {
				int rsize = is.read(buffer);
				fos.write(buffer, 0, rsize);
				readsize += rsize;
			}
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}finally {
			try {
				bw.close();
				osw.close();
				os.close();
				
				
				is.close();
				fos.close();
				
			}catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}
	}
	
	public void initDataRequest(String data) {
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
