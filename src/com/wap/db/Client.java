package com.wap.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Client {
    public static void main(String[] args) throws SQLException {
        Connection conn = new ConnectionFactory().getConnection();

        if(conn!=null){
            Statement stmt = conn.createStatement();
            String sql = "select * from restaurant";
            ResultSet rs = stmt.executeQuery(sql);

            while(rs.next()){
                System.out.println(rs.getInt(1) + " " + rs.getString(2) + " "+
                        rs.getString(3) + " "+ rs.getString(4) + " ");
            }

            rs.close();
            stmt.close();
            conn.close();
        }
    }
}
