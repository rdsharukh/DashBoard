package com.example.DAO;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.example.Constants.QueryConstants;
import com.example.Helpers.ConnectionUtils;
import com.example.VO.ServerVO;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;

public class ServerDAO {
	public static List<ServerVO> getAllServers() throws IOException
	{
		List<ServerVO> list = new ArrayList<>();
		try {
			Connection conn = ConnectionUtils.getConnection();
			PreparedStatement statement = conn.prepareStatement(QueryConstants.GET_ALL_SERVERS_QUERY);
			ResultSet rs = statement.executeQuery();
			while(rs.next())
			{
				ServerVO server = new ServerVO();
				server.setId(rs.getString("ID"));
				server.setSname(rs.getString("SERVER_NAME"));
				server.setIp(rs.getString("SERVER_IP"));
				server.setCode(rs.getString("CODE"));
				server.setPort(rs.getString("PORT_NO"));
				server.setUsername(rs.getString("USER_NAME"));
				server.setPassword(rs.getString("PASSWORD"));
				server.setType(rs.getString("SERVER_TYPE"));
				list.add(server);
			}
			ConnectionUtils.closeConnection(conn);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return list;
	}
	public static void addServer(ServerVO server)
	{
		try
		{
			Connection conn = ConnectionUtils.getConnection();
			PreparedStatement statement = conn.prepareStatement(QueryConstants.ADD_SERVER_QUERY);
			statement.setString(1, server.getId());
			statement.setString(2, server.getSname());
			statement.setString(3, server.getIp());
			statement.setString(4, server.getPort());
			statement.setString(5, server.getUsername());
			statement.setString(6, server.getPassword());
			statement.setString(7, server.getType());
			statement.setString(8, server.getStatus());
			statement.setString(9, server.getCode());
			statement.executeUpdate();
			ConnectionUtils.closeConnection(conn);
		}
		catch (Exception e) {
			Notification.show("ERROR", e.getMessage(), Type.ERROR_MESSAGE);
		}
	}
	public static void deleteServer(String query)
	{
		try
		{
			Connection conn = ConnectionUtils.getConnection();
			PreparedStatement statement = conn.prepareStatement(query);
			statement.executeUpdate();
			ConnectionUtils.closeConnection(conn);
		}
		catch (Exception e) {
			Notification.show("ERROR", e.getMessage(), Type.ERROR_MESSAGE);
		}
	}
	public static String generateDeleteQuery(Set<ServerVO> selected)
	{
		String query = QueryConstants.DELETE_SERVER_QUERY;
		int count=1;
		for(ServerVO server:selected)
		{
			if(selected.size()==1)
			{
				query+="'"+server.getSname()+"'";
			}
			else {
				if(count == selected.size())
				{
					query+="'"+server.getSname()+"'";
				}
				else
				{
					query+="'"+server.getSname()+"',";
					count++;
				}
			}
		}
		query+=")";
		return query;
	}
	public static void editServer(ServerVO server)
	{
		try
		{
			Connection conn = ConnectionUtils.getConnection();
			PreparedStatement statement = conn.prepareStatement(QueryConstants.UPDATE_SERVER_QUERY);
			statement.setString(1, server.getSname());
			statement.setString(2, server.getIp());
			statement.setString(3, server.getPort());
			statement.setString(4, server.getUsername());
			statement.setString(5, server.getPassword());
			statement.setString(6, server.getType());
			statement.setString(7, server.getCode());
			statement.setString(8, server.getId());
			statement.executeUpdate();
			ConnectionUtils.closeConnection(conn);
		}
		catch (Exception e) {
			Notification.show("ERROR", e.getMessage(), Type.ERROR_MESSAGE);
		}
	}
}
