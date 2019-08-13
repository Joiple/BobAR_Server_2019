package com.wap.client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
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
		
		//cl.initDataRequest(requestData);
		//cl.receiveImg(requestData);
		Socket socket = null;
		try {
			socket = new Socket("localhost",4000);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		//String filename = cl.makeReviewName(requestData, socket);
		//System.out.println(filename);
		cl.sendImg("0000013000013000000142");
	}
	
	public void sendImg(String fileName) {
		String host = "localhost";
		int port = 4000;
		
		Socket socket = null;
		
		byte buffer[] = new byte[2048];
		
		File imgfile = new File("test.jpg");
		String fileLength = String.valueOf(imgfile.length());
		
		FileInputStream fis = null;
		OutputStream os = null;
		OutputStreamWriter osw = null;
		BufferedWriter bw = null;
		
		InputStream is = null;
		InputStreamReader isr = null;
		BufferedReader br = null;
		
		try {
			socket = new Socket(host, port);
			
			fis = new FileInputStream(imgfile);
			os = socket.getOutputStream();
			osw = new OutputStreamWriter(os,"utf-8");
			bw = new BufferedWriter(osw);
			
			//�̹����� ���� �� ���̶�� client �ڵ� ����
			bw.write("8%%"+fileName+"%%"+fileLength+"%%");
			bw.newLine();
			bw.flush();
			
			//������ ���� ������ ��ٸ�
			is = socket.getInputStream();
			isr = new InputStreamReader(is, "utf-8");
			br = new BufferedReader(isr);
			
			String serverResponse = br.readLine();
			
			//�������� �̹����� ���� �غ� ���� ��� ����
			if(serverResponse.equals("1")) {
				//send body
				while(fis.available() > 0) {
					//2048����Ʈ�� ����
					int readSize = fis.read(buffer);
					//System.out.println(readSize);
					os.write(buffer, 0, readSize);
				}
			}
		}catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			try {
				bw.close();
				osw.close();
				os.close();
				
				fis.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void receiveImg(String data) {
		String host = "localhost";
		int port = 4000;
		
		Socket socket = null;
		
		OutputStream os = null;
		OutputStreamWriter osw = null;
		BufferedWriter bw = null;
		
		InputStream is = null;
		
		FileOutputStream fos = null;
		
		
		try {
			socket = new Socket(host, port);
			
			//������ �̹��� ��û
			os = socket.getOutputStream();
			osw = new OutputStreamWriter(os,"utf-8");
			bw = new BufferedWriter(osw);
			
			bw.write(data);
			bw.newLine();
			bw.flush();
			
			//�̹��� ����
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
			
			//������ ����
			os = socket.getOutputStream();
			osw = new OutputStreamWriter(os,"utf-8");
			bw = new BufferedWriter(osw);
			
			bw.write(data);
			bw.newLine();
			bw.flush();
			
			//�����κ��� ����
			is = socket.getInputStream();
			isr = new InputStreamReader(is,"utf-8");
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
	
	
	public String makeReviewName(String data, Socket socket) {
		String host = "localhost";
		int port = 4000;
		
		//Socket socket = null;
		
		OutputStream os = null;
		OutputStreamWriter osw = null;
		BufferedWriter bw = null;
		
		InputStream is = null;
		InputStreamReader isr = null;
		BufferedReader br = null;
		
		String receiveData = "";
		
		try {
			//socket = new Socket(host, port);
			
			//������ �̹��� ��û
			os = socket.getOutputStream();
			osw = new OutputStreamWriter(os,"utf-8");
			bw = new BufferedWriter(osw);
			
			bw.write(data);
			bw.newLine();
			bw.flush();
			
			//�����κ��� ����
			is = socket.getInputStream();
			isr = new InputStreamReader(is,"utf-8");
			br = new BufferedReader(isr);
			
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
		return receiveData;
	}
}
