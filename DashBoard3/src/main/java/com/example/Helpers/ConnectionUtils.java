package com.example.Helpers;

import java.sql.Connection;
import java.sql.DriverManager;

import com.example.Constants.StringConstants;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;

public class ConnectionUtils {
/**
 * get Database Connection
 */
	public static Connection getConnection(){
		Connection connection=null;
		PropertiesUtil properites=new PropertiesUtil();
		try {
			if(connection==null)
			{

				Class.forName(StringConstants.DB2_DRIVER);
				String ip =properites.getProperty("DatabaseIP");
				String port =properites.getProperty("DatabasePort");
				String dbname =properites.getProperty("DatabaseName");
				String url = StringConstants.DB2_Url+ip+":"+port+"/"+dbname;
				connection=DriverManager.getConnection(url,StringConstants.DB2_USERNAME,StringConstants.DB2_PASSWORD);
			}
		} catch (Exception e) {
			Notification.show(StringConstants.ERROR, e.getMessage(), Type.ERROR_MESSAGE);
		}
		return connection;
	}
	/**
	 * Close Connection
	 */
	public static void closeConnection(Connection connection)
	{
		try {
			connection.close();
		} catch (Exception e) {
			// TODO: handle exception
			Notification.show(StringConstants.ERROR,e.getMessage(), Type.ERROR_MESSAGE);
		}
	}
}
