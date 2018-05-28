package com.example.Constants;

import org.atmosphere.cpr.MetaBroadcaster.ThirtySecondsCache;

public interface StringConstants {
	String CONFIG_FILE = "config.properties";
	String DB2_DRIVER = "com.ibm.db2.jcc.DB2Driver";
	String DB2_USERNAME = "devusr";
	String DB2_PASSWORD = "devusr";
	String DB2_Url="jdbc:db2://";
	String ERROR = "Error";
	String LOGOUT_CONFIRM="Are you sure you want to Logout?";
	String LOGIN_ERROR = "Check credentials once and Try again...";
	String DASHBOARD_TITLE ="Server DashBoard";
	String CONFIGURATION_TITLE ="Configure Servers";
	String URL = "http://";
	String DELETE_CONFIRM = "Are you sure you want Delete Selected Servers?";
	String APP_DB_CMD = "cd /opt/PUMA-GIT-JBOSS-PID/jbossinstances/pid/configuration/ && grep -w 'datasource.xa.DatabaseName' jboss.properties | awk '{split($0,a,\"=\"); print a[2]}'";
	String USER_DB_CMD="cd /opt/PUMA-GIT-JBOSS-PID/jbossinstances/pid/configuration/ && grep -w 'datasource.xa.user.DatabaseName' jboss.properties | awk '{split($0,a,\"=\"); print a[2]}'";
	String CODEBASE_CMD="cd /opt/pid/PumaGIT/PID && git status | grep branch | awk '{usage=$4} END {print usage }'";
	String JBOSS_CMD="cd /opt/PUMA-GIT-JBOSS-PID && git status | grep branch | awk '{usage=$4} END {print usage }'";
	String USED_MEMORY_CMD="free -m | grep Mem | awk '{usage=$3} END {print usage }'";
	String FREE_MEMORY_CMD=" free -m | grep Mem | awk '{usage=$4} END {print usage }'";
	String TOTAL_MEMORY_CMD=" free -m | grep Mem | awk '{usage=$2} END {print usage }'";
	String SERVER_CONNECT_ERROR = "Cannot connect to Server check the credentials...";
	String IP_PING_ERROR = "IP is Not Reachable Enter avalid IP";
	String SERVER_CLUSTER_CMD="cd /opt/PUMA-GIT-JBOSS-PID/jbossinstances/pid/configuration/ && grep -w 'secure' standalone.xml";
	String EAR_TIMESTAMP_CMD="cd /opt/PUMA-GIT-JBOSS-PID-8.0/jbossinstances/pid/deployments &&find pid.ear -maxdepth 0 -printf '%TY-%Tm-%Td %TH:%TM\n'";
	String MERGE_TIMESTAMP_CMD="cd /opt/pid/autolog &&find ";
	String TIMESTAMP_CMD=" -maxdepth 0 -printf '%TY-%Tm-%Td %TH:%TM\n'";
	String LAST_BUILD_CMD = "cd /opt/pid/autolog && find BUILD-*  -maxdepth 0 -printf '%TY-%Tm-%Td %TH:%TM\n'";
	String DBS_COUNT="list db directory | grep alias | wc | awk '{usage=$1} END {print usage }' >> /tmp/tmp/out.txt";
	String CPU_UTIL_CMD = "top -n 1 -u ";
	String DB_CPU_UTIL_CMD = " |grep db2sysc| awk '{print $9}'";
	String APP_CPU_UTIL_CMD = " |grep java| awk '{print $9}'";
	String GUEST = "Welcome Guest";
	String ADMIN = "Welcome ";
	String USER_IMG = "user.png";
	String Pumaqa_CONNECTED_CMD="db2 list applications |grep PUMAQA |wc | awk '{u=$1} END {print u}' >/pumadb/db_files/SDashBoard/pumaqa.txt && cat/pumadb/db_files/SDashBoard/pumaqa.txt";
	String Pumadev_CONNECTED_CMD="db2 list applications |grep PUMADEV |wc | awk '{u=$1} END {print u}' > /pumadb/db_files/SDashBoard/pumadev.txt && cat/pumadb/db_files/SDashBoard/pumadev.txt";
	String Liveqa_CONNECTED_CMD="db2 list applications |grep LIVEQA |wc | awk '{u=$1} END {print u}' > /pumadb/db_files/SDashBoard/liveqa.txt && cat/pumadb/db_files/SDashBoard/liveqa.txt";
}
