package com.wap.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBCon {
    private static Connection con;  //프로그램에서 공동으로 사용할수있게 static으로 선언

    private DBCon(){
        //외부에서 생성하지 못하게 생성자 private으로 지정
    }

    public static Connection getConnection(){
        if(con != null){    //con이 null이 아니면 con이 참조하고있는 connection 인스턴스 리턴
            return con;
        }

        try{
            String server = "localhost";
            String database = "bobar1";
            String url = "jdbc:mysql://"+server+"/"+database+"?useSSL=false";

            String usr = "root";
            String password = "root";

            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection(url, usr, password);

            System.out.println("연결 성공");
        }catch (ClassNotFoundException e){
            System.out.println("driver not founded :" + e);
        }catch (SQLException e){
            System.out.println("exception : " + e );
        }
        return con;
    }
}
