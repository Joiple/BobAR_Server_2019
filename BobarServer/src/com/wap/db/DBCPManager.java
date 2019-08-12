package com.wap.db;

import javax.sql.DataSource;

import org.apache.tomcat.dbcp.dbcp.BasicDataSource;

public class DBCPManager {
	private static BasicDataSource dataSource;

	private DBCPManager() {
	}

	public static DataSource getDataSource() {
		if(dataSource == null) {
			dataSource = new BasicDataSource();
			dataSource.setDriverClassName("com.mysql.jdbc.Driver");
			dataSource.setUrl("jdbc:mysql://127.0.0.1:3306/bobar?useSSL=false");
		    dataSource.setUsername("root");
		    dataSource.setPassword("root");
		    dataSource.setMaxActive(100);
		    dataSource.setMaxWait(10000);
		    dataSource.setMaxIdle(10);
		}
		
		return dataSource;
	}
}
