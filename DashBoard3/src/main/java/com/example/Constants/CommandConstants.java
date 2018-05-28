package com.example.Constants;
public interface CommandConstants {
	String APP_DB_CMD = "cd /opt/PUMA-GIT-JBOSS-PID/jbossinstances/pid/configuration/ && grep -w 'datasource.xa.DatabaseName' jboss.properties";
	String USER_DB_CMD="cd /opt/PUMA-GIT-JBOSS-PID/jbossinstances/pid/configuration/ && grep -w 'datasource.xa.user.DatabaseName' jboss.properties";
	String CODEBASE_CMD="cd /opt/pid/PumaGIT/PID && git status";
	String JBOSS_CMD="cd /opt/PUMA-GIT-JBOSS-PID && git status";
	String USED_MEMORY_CMD=" free -m | grep Mem | awk '{u=$3} END {print u}'";
	String FREE_MEMORY_CMD=" free -m | grep Mem | awk '{u=$4} END {print u}'";
	String TOTAL_MEMORY_CMD=" free -m | grep Mem | awk '{u=$2} END {print u}'";
	String SERVER_CLUSTER_CMD="cd /opt/PUMA-GIT-JBOSS-PID/jbossinstances/pid/configuration/ && grep -w 'secure' standalone.xml";
	String EAR_TIMESTAMP_CMD="cd /opt/PUMA-GIT-JBOSS-PID-8.0/jbossinstances/pid/deployments &&find pid.ear -maxdepth 0 -printf '%TY-%Tm-%Td %TH:%TM/n'";
	String Pumaqa_CONNECTED_CMD="db2 list applications |grep PUMAQA |wc | awk '{u=$1} END {print u}' >/pumadb/db_files/SDashBoard/pumaqa.txt && cat/pumadb/db_files/SDashBoard/pumaqa.txt";
	String Pumadev_CONNECTED_CMD="db2 list applications |grep PUMADEV |wc | awk '{u=$1} END {print u}' > /pumadb/db_files/SDashBoard/pumadev.txt && cat/pumadb/db_files/SDashBoard/pumadev.txt";
	String Liveqa_CONNECTED_CMD="db2 list applications |grep LIVEQA |wc | awk '{u=$1} END {print u}' > /pumadb/db_files/SDashBoard/liveqa.txt && cat/pumadb/db_files/SDashBoard/liveqa.txt";
}