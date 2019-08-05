package com.wap.client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;

import com.sun.java_cup.internal.runtime.Scanner;

public class Client {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Client cl = new Client();
		System.out.println("request : ");
		String requestData = new java.util.Scanner(System.in).nextLine();
		
		//cl.ClientRun(requestData);
		cl.receiveImg(requestData);
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
			
			//������ ����
			os = socket.getOutputStream();
			osw = new OutputStreamWriter(os);
			bw = new BufferedWriter(osw);
			
			bw.write(data);
			bw.newLine();
			bw.flush();
			
			//�����κ��� ����
			/*is = socket.getInputStream();
			isr = new InputStreamReader(is);
			br = new BufferedReader(isr);
			
			String receiveData = "";
			receiveData = br.readLine();
			
			System.out.println("������ ���� ���� ������: "+receiveData);*/
			
			fos = new FileOutputStream("test.jpg");
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
			
			//������ ����
			os = socket.getOutputStream();
			osw = new OutputStreamWriter(os);
			bw = new BufferedWriter(osw);
			
			bw.write(data);
			bw.newLine();
			bw.flush();
			
			//�����κ��� ����
			is = socket.getInputStream();
			isr = new InputStreamReader(is);
			br = new BufferedReader(isr);
			
			String receiveData = "";
			receiveData = br.readLine();
			
			System.out.println("������ ���� ���� ������: "+receiveData);
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