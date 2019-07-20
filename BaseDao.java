package com.kgc.exam.utils;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

public class BaseDao {//工具类
	Connection conn=null;//创建连接对象
	PreparedStatement pstmt=null;//创建sql语句解析对象
	ResultSet rs=null;//创建容器对象接收查询内容
	int count=0;
	private static String driver;
	private static String url;
	private static String user;
	private static String password;
	//静态代码块
	static{
		init();
	}
	private static void init(){
		Properties pro=new Properties();
		InputStream is=BaseDao.class.getClassLoader().getResourceAsStream("database.properties");
		try {
			pro.load(is);
		} catch (IOException e) {
			e.printStackTrace();
		}
		driver=pro.getProperty("driver");
		url=pro.getProperty("url");
		user=pro.getProperty("user");
		password=pro.getProperty("password");
		
	}
	//加载驱动
	public Connection getConnection(){
		try {
			Class.forName(driver);
			conn=DriverManager.getConnection(url,user,password);
		}catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return conn;
	}
	//关闭资源
	public void closeAll(ResultSet rs,PreparedStatement pre,Connection conn){
		try{
			if(rs!=null){
				rs.close();
			}
		}catch (SQLException e) {
			e.printStackTrace();
		}finally{
			try {
				if(conn!=null){
				conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}finally{
				try {
					if(pre!=null){
					pre.close();
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}
	//增删改通用方法
	public int exeUpdate(String sql,Object [] params ){
		conn=getConnection();
		try{
			pstmt=conn.prepareStatement(sql);
			for (int i = 0; i < params.length; i++) {
				pstmt.setObject(i+1, params[i]);
			}
			count=pstmt.executeUpdate();
		}catch (SQLException e) {
			e.printStackTrace();
		}finally{
			closeAll(null,pstmt,conn);
		}
		return count;
	}
}
