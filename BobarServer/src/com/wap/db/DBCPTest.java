package com.wap.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

public class DBCPTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			Context initContext = new InitialContext();
			DataSource ds = DBCPManager.getDataSource();
			conn = ds.getConnection();

			String sql = "select * from user";
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				System.out.println("name ==> " + rs.getString(2));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
			} catch (Exception e) {
			}

			try {
				if (pstmt != null)
					pstmt.close();

			} catch (Exception e) {
			}

			try {
				if (conn != null)
					conn.close();

			} catch (Exception e) {
			}
		}
	}
}
