package com.example.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.example.Constants.QueryConstants;
import com.example.Constants.StringConstants;
import com.example.Helpers.ConnectionUtils;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Notification.Type;

public class AdminDAO {

	public static boolean adminAuthentication(TextField username,TextField password){
		try {
			Connection conn =ConnectionUtils.getConnection();
			PreparedStatement statement = conn.prepareStatement(QueryConstants.ADMIN_AUTHENTICAION_QUERY);
			statement.setString(1,username.getValue());
			statement.setString(2, password.getValue());
			ResultSet rs = statement.executeQuery();
			if(rs.next())
			{
				return true;
			}
			ConnectionUtils.closeConnection(conn);
		} catch (Exception e) {
			Notification.show(StringConstants.ERROR, e.getMessage(), Type.ERROR_MESSAGE);
			e.printStackTrace();
		}
		return false;
	}
}
