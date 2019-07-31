package com.wap.db;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConnectionFactory {
    final static String server = "localhost";
    final static String database = "bobar1";
    final static String url = "jdbc:mysql://"+server+"/"+database+"?useSSL=false";

    final static String usr = "root";
    final static String password = "root";

    //디폴트 생성자
    public ConnectionFactory(){}

    public Connection getConnection() {
        Connection conn = null;

        try{
            Class.forName("com.mysql.jdbc.Driver");

            conn = DriverManager.getConnection(url, usr, password);
        }catch (Exception e){
            if(e instanceof ClassNotFoundException){
                System.out.println("driver loading failed : "+e.toString());
                return conn;
            }else{
                System.out.println("db connection error : "+e.toString());
                return conn;
            }
        }
        return conn;
    }
}
