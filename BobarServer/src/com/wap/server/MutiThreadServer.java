package com.wap.server;

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
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

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
			System.out.println("******************waiting******************");
			System.out.println(socket.getInetAddress() + " 로 부터 연결 요청");

			// 클라이언트로 부터 데이터를 받기 위한 InputStream 선언
			InputStream is = socket.getInputStream();
			InputStreamReader isr = new InputStreamReader(is, "utf-8");
			BufferedReader br = new BufferedReader(isr);

			String data = null;
			data = br.readLine();
			System.out.println("클라이언트로 부터 받은 데이터 : " + data);

			List<String> dataArray = new ArrayList<>();
			StringTokenizer st = new StringTokenizer(data, delimiter);
			while (st.hasMoreTokens()) {
				dataArray.add(st.nextToken());
			}

			String requestCode = dataArray.get(0);
			if (requestCode.equals("4")) {
				sendImg(dataArray.get(1), socket);
			} else if (requestCode.equals("0")) {
				responseData = myPage(dataArray.get(1));
				sendData(responseData, socket);
			} else if (requestCode.equals("1")) {
				responseData = initViewData();
				sendData(responseData, socket);
			} else if (requestCode.equals("3")) {
				responseData = reviewList(dataArray.get(1));
				sendData(responseData, socket);
			} else if (requestCode.equals("5")) {
				responseData = detailReview(dataArray.get(1));
				sendData(responseData, socket);
			} else if (requestCode.equals("6")) {
				responseData = search(dataArray.get(1));
				sendData(responseData, socket);
			} else if (requestCode.equals("7")) {
				responseData = makeImgName(dataArray.get(1), dataArray.get(2), dataArray.get(3));
				sendData(responseData, socket);
			} else if (requestCode.equals("8")) {
				recieveImg(socket, dataArray.get(1), dataArray.get(2));
			} else if (requestCode.equals("9")) {
				makeReview(dataArray.get(1), dataArray.get(2), dataArray.get(3), dataArray.get(4), dataArray.get(5),
						dataArray.get(6), dataArray.get(7), dataArray.get(8));
			} else if (requestCode.equals("10")) {
				responseData = follower(dataArray.get(1));
				sendData(responseData, socket);
			} else if (requestCode.equals("11")) {
				responseData = following(dataArray.get(1));
				sendData(responseData, socket);
			} else {
				responseData = "wrong code";
				sendData(responseData, socket);
			}
			System.out.println("****************send success***************");

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	public void sendData(String data, Socket socket) {
		OutputStream os = null;
		OutputStreamWriter osw = null;
		BufferedWriter bw = null;

		try {
			// 클라이언트로 데이터를 보내기 위한 OutputStream 선언
			os = socket.getOutputStream();
			osw = new OutputStreamWriter(os, "utf-8");
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
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

	public void dbinit() {
		try {
			// Context initContext = new InitialContext();
			DataSource ds = DBCPManager.getDataSource();
			con = ds.getConnection();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	// code 0 - mypage
	public String myPage(String userId) {
		String responseData = "";

		String nickname = null;
		String profileFileName = null;
		int userNum = 0;
		int followerNum = 0;
		int followingNum = 0;
		int reviewNum = 0;

		List<Integer> reviewId = new ArrayList<Integer>();
		List<Integer> restaurantId = new ArrayList<Integer>();
		List<String> restaurantName = new ArrayList<String>();
		List<String> imgFileName = new ArrayList<String>();

		String sql = "select * from user where userId = ?";
		try {
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, userId);
			rs = pstmt.executeQuery();

			if (rs.next()) {
				userNum = rs.getInt("userNum");
				nickname = rs.getString("nickname");
				profileFileName = rs.getString("profilePicture");

			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		sql = "select * from review where userNum = ?";
		try {
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, userNum);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				reviewId.add(rs.getInt("reviewId"));
				restaurantId.add(rs.getInt("restaurantId"));
				imgFileName.add(rs.getString("reviewPicture"));
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		reviewNum = reviewId.size();

		for (int i = 0; i < restaurantId.size(); i++) {
			sql = "select rName from restaurant where restaurantId = ?";
			try {
				pstmt = con.prepareStatement(sql);
				pstmt.setInt(1, restaurantId.get(i));
				rs = pstmt.executeQuery();

				if (rs.next()) {
					restaurantName.add(rs.getString("rName"));
				}
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}

		sql = "select count(*) from follow where userId = ?";
		try {
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, userId);
			rs = pstmt.executeQuery();

			if (rs.next()) {
				followingNum = rs.getInt(1);
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		sql = "select count(*) from follow where followingId = ?";
		try {
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, userId);
			rs = pstmt.executeQuery();

			if (rs.next()) {
				followerNum = rs.getInt(1);
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		responseData += nickname;
		responseData += delimiter;

		responseData += profileFileName;
		responseData += delimiter;

		responseData += String.valueOf(followerNum);
		responseData += delimiter;

		responseData += String.valueOf(followingNum);
		responseData += delimiter;

		responseData += String.valueOf(reviewNum);
		responseData += delimiter;

		for (int i = 0; i < reviewNum; i++) {
			responseData += reviewId.get(i);
			responseData += delimiter;

			responseData += restaurantName.get(i);
			responseData += delimiter;

			responseData += imgFileName.get(i);
			responseData += delimiter;
		}

		return responseData;
	}

	// code 1 - Initial view
	public String initViewData() {
		String responseData = "";

		List<Integer> restaurantId = new ArrayList<Integer>();
		List<String> rLongitude = new ArrayList<String>();
		List<String> rLatitude = new ArrayList<String>();
		List<String> rAltitude = new ArrayList<String>();
		List<String> imgFileName = new ArrayList<String>();

		int restaurantNum = 0;

		String sql = "select count(*) from restaurant";
		try {
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();

			if (rs.next()) {
				restaurantNum = rs.getInt(1);
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		responseData += String.valueOf(restaurantNum) + delimiter;

		sql = "select restaurantId from restaurant";
		try {
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				restaurantId.add(rs.getInt(1));
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		sql = "select reviewPicture from review where restaurantId = ? order by date desc limit 1";
		for (int i = 0; i < restaurantId.size(); i++) {
			try {
				pstmt = con.prepareStatement(sql);
				pstmt.setInt(1, restaurantId.get(i));

				rs = pstmt.executeQuery();

				while (rs.next()) {
					imgFileName.add(rs.getString(1));
				}
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}

		sql = "select rLongitude, rLatitude, rAltitude from restaurant where restaurantId = ?";
		for (int i = 0; i < restaurantId.size(); i++) {
			try {
				pstmt = con.prepareStatement(sql);
				pstmt.setInt(1, restaurantId.get(i));

				rs = pstmt.executeQuery();

				while (rs.next()) {
					rLongitude.add(rs.getString("rLongitude"));
					rLatitude.add(rs.getString("rLatitude"));
					rAltitude.add(rs.getString("rAltitude"));
				}
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}

		for (int i = 0; i < restaurantId.size(); i++) {
			responseData += String.valueOf(restaurantId.get(i));
			responseData += delimiter;

			responseData += rLongitude.get(i);
			responseData += delimiter;

			responseData += rLatitude.get(i);
			responseData += delimiter;

			responseData += rAltitude.get(i);
			responseData += delimiter;

			responseData += imgFileName.get(i);
			responseData += delimiter;
		}

		return responseData;
	}

	// code 3 review list
	public String reviewList(String restaurantId) {
		int rId = Integer.parseInt(restaurantId);
		String responseData = "";

		String restaurantName = "";
		double avgPoint = 0; // 모든 점수 다 더하고 리뷰개수*5 로 나눔
		String address = "";
		String phoneNum = "";
		int reviewNum = 0;

		List<Integer> reviewId = new ArrayList<Integer>();
		List<String> text = new ArrayList<String>();
		List<String> imgFileName = new ArrayList<String>();
		List<Integer> likes = new ArrayList<Integer>();
		List<String> date = new ArrayList<String>();

		String sql = "select * from restaurant where restaurantId = ?";
		try {
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, rId);
			rs = pstmt.executeQuery();

			if (rs.next()) {
				restaurantName = rs.getString("rName");
				address = rs.getString("rAddr");
				phoneNum = rs.getString("rCall");

			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		sql = "select count(*) from review where restaurantId = ?";
		try {
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, rId);
			rs = pstmt.executeQuery();

			if (rs.next()) {
				reviewNum = rs.getInt(1);
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		sql = "select * from review where restaurantId = ?";
		try {
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, rId);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				reviewId.add(rs.getInt("reviewId"));
				text.add(rs.getString("text"));
				imgFileName.add(rs.getString("reviewPicture"));
				likes.add(rs.getInt("likes"));
				date.add(rs.getString("date"));

				avgPoint += rs.getInt("tastePoint");
				avgPoint += rs.getInt("cleanPoint");
				avgPoint += rs.getInt("kindnessPoint");
				avgPoint += rs.getInt("moodPoint");
				avgPoint += rs.getInt("costPoint");
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		responseData += restaurantName;
		responseData += delimiter;

		avgPoint /= reviewNum * 5;
		responseData += String.format("%.1f", avgPoint);
		responseData += delimiter;

		responseData += address;
		responseData += delimiter;

		responseData += phoneNum;
		responseData += delimiter;

		responseData += String.valueOf(reviewNum);
		responseData += delimiter;

		for (int i = 0; i < reviewNum; i++) {
			responseData += String.valueOf(reviewId.get(i));
			responseData += delimiter;

			responseData += text.get(i);
			responseData += delimiter;

			responseData += imgFileName.get(i);
			responseData += delimiter;

			responseData += String.valueOf(likes.get(i));
			responseData += delimiter;

			responseData += date.get(i);
			responseData += delimiter;
		}

		return responseData;
	}

	// code 4 send image file
	public void sendImg(String fileName, Socket socket) {
		byte buffer[] = new byte[2048];

		File imgfile = new File("./image/" + fileName + ".JPG");
		String fileLength = String.valueOf(imgfile.length());

		// 파일의 크기를 10바이트의 고정 크기로 전송
		// ex) "1234" to "0000001234"
		String header = "0000000000".substring(0, 10 - fileLength.length()) + fileLength;

		FileInputStream fis = null;
		OutputStream os = null;

		try {
			fis = new FileInputStream(imgfile);
			os = socket.getOutputStream();

			// send header
			os.write(header.getBytes());

			// send body
			while (fis.available() > 0) {
				// 2048바이트씩 전송
				int readSize = fis.read(buffer);
				System.out.println(readSize);
				os.write(buffer, 0, readSize);
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				os.close();
				fis.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	// code 5 detailed review
	public String detailReview(String reviewId) {
		String responseData = "";

		int rvId = Integer.parseInt(reviewId);

		String nickName = "";
		int tastePoint = 0;
		int cleanPoint = 0;
		int kindnessPoint = 0;
		int moodePoint = 0;
		int costPoint = 0;

		int userNum = -1;
		
		String picture = "";
		int pictureNum = 0;

		String sql = "select * from review where reviewId = ?";
		try {
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, rvId);
			rs = pstmt.executeQuery();

			if (rs.next()) {
				tastePoint = rs.getInt("tastePoint");
				cleanPoint = rs.getInt("cleanPoint");
				kindnessPoint = rs.getInt("kindnessPoint");
				moodePoint = rs.getInt("moodPoint");
				costPoint = rs.getInt("costPoint");
				
				picture = rs.getString("reviewPicture");
				pictureNum = rs.getInt("pictureNum");

				userNum = rs.getInt("userNum");
				System.out.println(userNum);
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		sql = "select nickname from user where userNum = ?";
		try {
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, userNum);
			rs = pstmt.executeQuery();

			if (rs.next()) {
				nickName = rs.getString("nickname");
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		picture = picture.substring(0, picture.length()-1);
		
		responseData += nickName;
		responseData += delimiter;

		responseData += String.valueOf(tastePoint);
		responseData += delimiter;

		responseData += String.valueOf(cleanPoint);
		responseData += delimiter;

		responseData += String.valueOf(kindnessPoint);
		responseData += delimiter;

		responseData += String.valueOf(moodePoint);
		responseData += delimiter;

		responseData += String.valueOf(costPoint);
		responseData += delimiter;

		for(int i = 1; i <= pictureNum; i++) {
			responseData += picture + String.valueOf(i);
			responseData += delimiter;
		}
		
		return responseData;
	}

	// code 6 search
	public String search(String keyword) {
		keyword = "%" + keyword + "%";

		String responseData = "";

		List<Integer> restaurantId = new ArrayList<Integer>();
		List<String> rLongitude = new ArrayList<String>();
		List<String> rLatitude = new ArrayList<String>();
		List<String> rAltitude = new ArrayList<String>();
		List<String> imgFileName = new ArrayList<String>();

		int restaurantNum = 0;

		String sql = "select restaurantId from review where text like ?";
		try {
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, keyword);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				if (!restaurantId.contains(rs.getInt("restaurantId"))) {
					restaurantId.add(rs.getInt("restaurantId"));
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		restaurantNum = restaurantId.size();

		responseData += String.valueOf(restaurantNum) + delimiter;

		sql = "select reviewPicture from review where restaurantId = ? order by date desc limit 1";
		for (int i = 0; i < restaurantId.size(); i++) {
			try {
				pstmt = con.prepareStatement(sql);
				pstmt.setInt(1, restaurantId.get(i));

				rs = pstmt.executeQuery();

				while (rs.next()) {
					imgFileName.add(rs.getString(1));
				}
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}

		sql = "select rLongitude, rLatitude, rAltitude from restaurant where restaurantId = ?";
		for (int i = 0; i < restaurantId.size(); i++) {
			try {
				pstmt = con.prepareStatement(sql);
				pstmt.setInt(1, restaurantId.get(i));

				rs = pstmt.executeQuery();

				while (rs.next()) {
					rLongitude.add(rs.getString("rLongitude"));
					rLatitude.add(rs.getString("rLatitude"));
					rAltitude.add(rs.getString("rAltitude"));
				}
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}

		for (int i = 0; i < restaurantId.size(); i++) {
			responseData += String.valueOf(restaurantId.get(i));
			responseData += delimiter;

			responseData += rLongitude.get(i);
			responseData += delimiter;

			responseData += rLatitude.get(i);
			responseData += delimiter;

			responseData += rAltitude.get(i);
			responseData += delimiter;

			responseData += imgFileName.get(i);
			responseData += delimiter;
		}

		return responseData;
	}

	// code 7 make image file name(write review)
	public String makeImgName(String userId, String restaurantId, String fileNum) {
		String sql = "select auto_increment from information_schema.tables where table_name = ? and table_schema = database()";
		String review = "review";
		String responseData = "";

		String reviewId = "";
		int newReviewNum = 0;
		try {
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, review);

			rs = pstmt.executeQuery();
			if (rs.next()) {
				newReviewNum = rs.getInt(1);
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		reviewId = String.valueOf(newReviewNum);

		responseData += "0";
		responseData += "000000".substring(restaurantId.length()) + restaurantId;
		responseData += "000000".substring(userId.length()) + userId;
		responseData += "00000000".substring(reviewId.length()) + reviewId;
		// responseData += delimiter;
		
		String result = "";
		for(int i = 1; i <= Integer.parseInt(fileNum); i++) {
			result += responseData + String.valueOf(i);
			result += delimiter;
		}

		return result;
	}

	// code 8 receive image file(write review)
	public void recieveImg(Socket socket, String fileName, String fileLen) {
		FileOutputStream fos = null;
		InputStream is = null;

		OutputStream os = null;
		OutputStreamWriter osw = null;
		BufferedWriter bw = null;

		int fileLength = Integer.parseInt(fileLen);

		try {
			fos = new FileOutputStream("./image/" + fileName + ".jpg");
			is = socket.getInputStream();
			byte buffer[] = new byte[2048];

			int readSize = 0;

			// read body
			os = socket.getOutputStream();
			osw = new OutputStreamWriter(os, "utf-8");
			bw = new BufferedWriter(osw);

			// 1을 client로 전송해서 이미지를 받을 준비가 됐다는 것을 알림
			bw.write("1");
			bw.newLine();
			bw.flush();

			while (readSize < fileLength) {
				int rsize = is.read(buffer);
				fos.write(buffer, 0, rsize);
				readSize += rsize;
			}
			System.out.println("readSize :" + readSize);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			try {
				is.close();
				fos.close();
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}
	}

	// code 9 make review(write review)
	public void makeReview(String imgFileName, String text, String tastePoint, String cleanPoint, String kindnessPoint,
			String moodPoint, String costPoint, String imgFileNum) {
		OutputStream os = null;
		OutputStreamWriter osw = null;
		BufferedWriter bw = null;

		String reviewId = "";
		String userNum = "";
		String restaurantId = "";
		String date = null;

		int tastePointToInt = Integer.parseInt(tastePoint);
		int cleanPointToInt = Integer.parseInt(cleanPoint);
		int kindnessPointToInt = Integer.parseInt(kindnessPoint);
		int moodPointToInt = Integer.parseInt(moodPoint);
		int costPointToInt = Integer.parseInt(costPoint);

		for (int i = 1; i <= 6; i++) {
			restaurantId += imgFileName.charAt(i);
		}

		for (int i = 7; i <= 12; i++) {
			userNum += imgFileName.charAt(i);
		}

		for (int i = 13; i <= 20; i++) {
			reviewId += imgFileName.charAt(i);
		}

		Date d = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		date = sdf.format(d);

		String sql = "insert into review (userNum, restaurantId, date, text, tastePoint, cleanPoint, kindnessPoint, moodPoint, costPoint, reviewPicture, pictureNum)"
				+ "values(?,?,?,?,?,?,?,?,?,?)";
		try {
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, Integer.parseInt(userNum));
			pstmt.setInt(2, Integer.parseInt(restaurantId));
			pstmt.setString(3, date);
			pstmt.setString(4, text);
			pstmt.setInt(5, tastePointToInt);
			pstmt.setInt(6, cleanPointToInt);
			pstmt.setInt(7, kindnessPointToInt);
			pstmt.setInt(8, moodPointToInt);
			pstmt.setInt(9, costPointToInt);
			pstmt.setString(10, imgFileName);
			pstmt.setInt(11, Integer.parseInt(imgFileNum));
			pstmt.executeUpdate();

			os = socket.getOutputStream();
			osw = new OutputStreamWriter(os, "utf-8");
			bw = new BufferedWriter(osw);

			bw.write("1");
			bw.newLine();
			bw.flush();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			try {
				bw.close();
				osw.close();
				os.close();
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}
	}

	// code 10 follower
	public String follower(String userId) {
		String responseData = "";
		List<String> followerId = new ArrayList<String>();
		List<Integer> followerUserNum = new ArrayList<Integer>();
		List<String> imgFileName = new ArrayList<String>();
		List<String> nickname = new ArrayList<String>();
		List<Integer> reviewNum = new ArrayList<Integer>();
		List<Boolean> isFollow = new ArrayList<Boolean>();

		String sql = "select userId from follow where followingId = ?";
		try {
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, userId);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				followerId.add(rs.getString("userId"));
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		sql = "select * from user where userId = ?";
		try {
			pstmt = con.prepareStatement(sql);
			for (int i = 0; i < followerId.size(); i++) {
				pstmt.setString(1, followerId.get(i));
				rs = pstmt.executeQuery();

				if (rs.next()) {
					nickname.add(rs.getString("nickname"));
					imgFileName.add(rs.getString("profilePicture"));
					followerUserNum.add(rs.getInt("userNum"));
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		sql = "select count(*) from review where userNum = ?";
		try {
			pstmt = con.prepareStatement(sql);
			for (int i = 0; i < followerId.size(); i++) {
				pstmt.setInt(1, followerUserNum.get(i));
				rs = pstmt.executeQuery();

				if (rs.next()) {
					reviewNum.add(rs.getInt(1));
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		sql = "select * from follow where userId = ? and followingId = ?";
		try {
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, userId);
			for (int i = 0; i < followerId.size(); i++) {
				pstmt.setString(2, followerId.get(i));
				rs = pstmt.executeQuery();

				if (rs.next()) {
					isFollow.add(true);
				} else {
					isFollow.add(false);
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		responseData += String.valueOf(followerId.size());
		responseData += delimiter;

		for (int i = 0; i < followerId.size(); i++) {
			responseData += nickname.get(i);
			responseData += delimiter;

			responseData += imgFileName.get(i);
			responseData += delimiter;

			responseData += String.valueOf(reviewNum.get(i));
			responseData += delimiter;

			responseData += String.valueOf(isFollow.get(i));
			responseData += delimiter;
		}

		return responseData;
	}

	// code 11 following
	public String following(String userId) {
		String responseData = "";
		List<String> followingId = new ArrayList<String>();
		List<Integer> followingUserNum = new ArrayList<Integer>();
		List<String> imgFileName = new ArrayList<String>();
		List<String> nickname = new ArrayList<String>();
		List<Integer> reviewNum = new ArrayList<Integer>();

		String sql = "select followingId from follow where userId = ?";
		try {
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, userId);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				followingId.add(rs.getString("followingId"));
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		sql = "select * from user where userId = ?";
		try {
			pstmt = con.prepareStatement(sql);
			for (int i = 0; i < followingId.size(); i++) {
				pstmt.setString(1, followingId.get(i));
				rs = pstmt.executeQuery();

				if (rs.next()) {
					nickname.add(rs.getString("nickname"));
					imgFileName.add(rs.getString("profilePicture"));
					followingUserNum.add(rs.getInt("userNum"));
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		sql = "select count(*) from review where userNum = ?";
		try {
			pstmt = con.prepareStatement(sql);
			for (int i = 0; i < followingId.size(); i++) {
				pstmt.setInt(1, followingUserNum.get(i));
				rs = pstmt.executeQuery();

				if (rs.next()) {
					reviewNum.add(rs.getInt(1));
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		responseData += String.valueOf(followingId.size());
		responseData += delimiter;

		for (int i = 0; i < followingId.size(); i++) {
			responseData += nickname.get(i);
			responseData += delimiter;

			responseData += imgFileName.get(i);
			responseData += delimiter;

			responseData += String.valueOf(reviewNum.get(i));
			responseData += delimiter;
		}

		return responseData;
	}
}