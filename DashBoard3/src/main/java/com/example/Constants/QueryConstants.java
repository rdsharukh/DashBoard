package com.example.Constants;

public interface QueryConstants {
	String ADMIN_AUTHENTICAION_QUERY="SELECT * FROM PUMA.USER_MASTER WHERE USER_NAME=? and PASSWORD=?";
	String GET_ALL_SERVERS_QUERY = "SELECT * FROM PUMA.SERVER_M WHERE RECORD_STATUS='1'";
	String ADD_SERVER_QUERY = "INSERT INTO PUMA.SERVER_M VALUES (?,?,?,?,?,?,sysdate,null,?,?,?)";
	String DELETE_SERVER_QUERY = "UPDATE PUMA.SERVER_M SET RECORD_STATUS='0' WHERE SERVER_NAME IN (";
	String UPDATE_SERVER_QUERY = "UPDATE PUMA.SERVER_M SET SERVER_NAME=?,SERVER_IP=?,PORT_NO=?,USER_NAME=?,PASSWORD=?,UPDATE_DT=sysdate,SERVER_TYPE=?,CODE=? WHERE ID=?";
}
