package com.wap.server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import com.wap.db.DBCPManager;

public class MutiThreadServer {
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		try {
			ServerSocket server = null;
			int port = 4000;

			server = new ServerSocket(port);
			while (true) {
				Socket socket = server.accept();
				Multithread multithread = new Multithread(socket);
				new Thread(multithread).start();
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

	}
}

class Multithread implements Runnable {

	static String delimiter = "%%";
	static Connection con = null;
	static PreparedStatement pstmt = null;
	static ResultSet rs = null;

	/* ServerSocket server; */
	Socket socket;

	String responseData = "";

	public Multithread(Socket socket) {
		// TODO Auto-generated constructor stub
		this.socket = socket;
		dbinit();
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			System.out.println("*************waiting*************");
			/* socket = server.accept(); */
			System.out.println(socket.getInetAddress() + " 로 부터 연결 요청");

			// 클라이언트로 부터 데이터를 받기 위한 InputStream 선언
			InputStream is = socket.getInputStream();
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(isr);

			String data = null;
			data = br.readLine();
			System.out.println("클라이언트로 부터 받은 데이터 : " + data);

			List<String> dataArray = new ArrayList<>();
			StringTokenizer st = new StringTokenizer(data, "%%");
			while (st.hasMoreTokens()) {
				dataArray.add(st.nextToken());
			}

			responseData = processData(dataArray);
			sendData(responseData, socket);
			System.out.println("*************send success*************");

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	public String initViewData() {
		String responseData = "";
		
		List<Integer> restaurantId = new ArrayList<Integer>();
		List<String> imgList = new ArrayList<String>();
		
		String sql = "select rid from restaurant";
		try {
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				restaurantId.add(rs.getInt("rid"));
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		sql = "select picture from review where rid = ? order by date desc limit 1";
		for(int i = 0; i < restaurantId.size(); i++) {
			try {
				pstmt = con.prepareStatement(sql);
				pstmt.setInt(1, restaurantId.get(i));
				
				rs = pstmt.executeQuery();
				
				while(rs.next()) {
					imgList.add(rs.getString("picture"));
				}
			}catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}
		
		for(int i = 0; i < restaurantId.size(); i++) {
			responseData += String.valueOf(restaurantId.get(i));
			responseData += delimiter;
			
			responseData += imgList.get(i);
			responseData += delimiter;
		}
		
		return responseData;
	}

	public String processData(List<String> dataArray) {
		String requestCode = dataArray.get(0);
		System.out.println("request code: "+ requestCode);
		String responseData = "";
		
		List<Integer> restaurantId = new ArrayList<Integer>();
		List<String> imgList = new ArrayList<String>();
		
		if(requestCode.equals("1")) {
			responseData = initViewData();
		}else {
			responseData = "nothing";
		}
		
		return responseData;
	}

	public void sendData(String data, Socket socket) {
		OutputStream os = null;
		OutputStreamWriter osw = null;
		BufferedWriter bw = null;

		try {
			// 클라이언트로 데이터를 보내기 위한 OutputStream 선언
			os = socket.getOutputStream();
			osw = new OutputStreamWriter(os);
			bw = new BufferedWriter(osw);

			bw.write(data);
			bw.flush();
		} catch (Exception e) {
			// TODO: handle exception

			e.printStackTrace();
		} finally {
			try {
				bw.close();
				osw.close();
				os.close();
				socket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

	public void dbinit() {
		try {
			Context initContext = new InitialContext();
			DataSource ds = DBCPManager.getDataSource();
			con = ds.getConnection();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

}