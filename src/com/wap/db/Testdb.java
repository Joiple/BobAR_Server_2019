package com.wap.db;

import java.sql.*;

public class Testdb {
    public static void main(String[] args) {
        Connection con = null;
        String server = "localhost";
        String database = "bobar1";
        String user_name = "root";
        String password = "root";

        // 1. 드라이버 로딩
        try {
            Class.forName("com.mysql.jdbc.Driver");
        }catch (ClassNotFoundException e){
            System.err.println("<jdbc 오류> driver load 오류 : "+ e.getMessage());
            e.printStackTrace();
        }

        // 2. 연결
        try {
            con = DriverManager.getConnection("jdbc:mysql://"+server+"/"+database+"?useSSL=false", user_name, password);
            System.out.println("정상적으로 연결되었습니다.");

            String sql = "select * from restaurant";
            //String sql = "desc user";
            Statement stmt = null;
            stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            rs.next();
            System.out.println(rs.getString(2));


        } catch (SQLException e){
            System.err.println("con 요류 : "+e.getMessage());
            e.printStackTrace();
        }

        // 3.해제
        try{
            if(con != null)
                con.close();
        }catch (SQLException e){

        }
    }
}
