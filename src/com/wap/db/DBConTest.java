package com.wap.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class DBConTest {
    private Connection con;
    private PreparedStatement pstmt;
    private ResultSet rs;

    public DBConTest(){

        try{
            //Connection 객체 얻어오기
            con = DBCon.getConnection();

            //insert 쿼리
            String sql = "insert  into user(userID, userPassword) values (?, ?)";
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1,"tset1");
            pstmt.setString(2,"ptset1");
            int r = pstmt.executeUpdate();
            System.out.println("processed num : "+r);

            //select 쿼리
            /*String sql = "select * from user";
            pstmt = con.prepareStatement(sql);
            rs = pstmt.executeQuery();

            while(rs.next()){
                System.out.println(rs.getInt("uid")+rs.getString("userID") + " : " + rs.getString("userPassword"));
            }*/

            rs.close();
            pstmt.close();
            con.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /*public static void main(String[] args){
        new DBConTest();
    }*/
}
