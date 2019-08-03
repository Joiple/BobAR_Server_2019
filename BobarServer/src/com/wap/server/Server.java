package com.wap.server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import com.wap.db.*;

public class Server {
	static String delimiter = "%%";
	static Connection con = null;
	static PreparedStatement pstmt = null;
	static ResultSet rs = null;

    public static void main(String[] args) throws IOException {
        // TODO Auto-generated method stub
        Server sv = new Server();
        sv.dbinit();
        sv.serverRun();

    }
    
    public void dbinit() {
    	try {
    		Context initContext = new InitialContext();
    		DataSource ds = DBCPManager.getDataSource();
    		con = ds.getConnection();
    	}catch (Exception e) {
			// TODO: handle exception
    		e.printStackTrace();
		}
    }
    
    public void serverRun() throws IOException {
    	
        ServerSocket server = null;
        int port = 4000;
        Socket socket = null;

        
        InputStream is = null;
        InputStreamReader isr = null;
        BufferedReader br = null;


        try {
        	String sql = "select * from user";
        	pstmt = con.prepareStatement(sql);
        	rs = pstmt.executeQuery();
        	
        	rs.next();
        	
        	String responseData = rs.getString("userId");
        	
            server = new ServerSocket(port);
            while (true) {
                System.out.println("----------waiting----------");
                socket = server.accept();	//Ŭ���̾�Ʈ�� �����ϸ� ����� �� �ִ� ���� ��ȯ
                System.out.println(socket.getInetAddress() +" �� ���� �������");

                //Ŭ���̾�Ʈ�κ��� �����͸� �ޱ� ���� InputStream ����
                is = socket.getInputStream();
                isr = new InputStreamReader(is);
                br = new BufferedReader(isr);

                String data = null;
                data = br.readLine();
                System.out.println("Ŭ���̾�Ʈ�� ���� ���� ������ : "+data);
                
                List<String> dataArray = new ArrayList<String>();
                StringTokenizer st = new StringTokenizer(data,delimiter);
                while(st.hasMoreTokens()) {
                	dataArray.add(st.nextToken());
                }
                
                for(int i = 0; i < dataArray.size(); i++) {
                	System.out.println(dataArray.get(i));
                }
                
                responseData = processData(dataArray);

                //���� ������ �״��  �ٽ� ������
                sendData(responseData, socket);
                System.out.println("--------send success--------");
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        } finally {
            br.close();
            isr.close();
            is.close();
            server.close();
        }

    }
    
    public String processData(List<String> dataArray) {
    	String requestCode = dataArray.get(0);
    	System.out.println("request code : "+ requestCode);
    	String responseData = "";
    	
    	List<Integer> restaurantId = new ArrayList<Integer>();
    	List<String> imgList = new ArrayList<String>();
    	
    	if(requestCode.equals("1")) {
    		String sql = "select rid from restaurant";
    		try {
				pstmt = con.prepareStatement(sql);
				rs = pstmt.executeQuery();
				
				while(rs.next()) {
					restaurantId.add(rs.getInt("rid"));
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
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
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
    		}
    		
    		for(int i = 0; i < restaurantId.size(); i++) {
    			responseData += String.valueOf(restaurantId.get(i));
    			responseData += delimiter;
    			
    			responseData += imgList.get(i);
    			responseData += delimiter;
    		}
    		
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
            //Ŭ���̾�Ʈ�� �����͸� ������ ���� OutputStream ����
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

}

