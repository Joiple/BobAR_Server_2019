package com.wap.server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import com.wap.db.*;

public class Server {
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

        //BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
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
                socket = server.accept();	//클라이언트가 접속하면 통신할 수 있는 소켓 반환
                System.out.println(socket.getInetAddress() +" 로 부터 연결오쳥");

                //클라이언트로부터 데이터를 받기 위한 InputStream 선언
                is = socket.getInputStream();
                isr = new InputStreamReader(is);
                br = new BufferedReader(isr);

                String data = null;
                data = br.readLine();
                System.out.println("클라이언트로 부터 받은 데이터 : "+data);

                //받은 데이터 그대로  다시 보내기
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

    public void sendData(String data, Socket socket) {
        OutputStream os = null;
        OutputStreamWriter osw = null;
        BufferedWriter bw = null;

        try {
            //클라이언트로 데이터를 보내기 위한 OutputStream 선언
            os = socket.getOutputStream();
            osw = new OutputStreamWriter(os);
            bw = new BufferedWriter(osw);

            bw.write("서버로 부터 받은 데이터 : "+data);
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

