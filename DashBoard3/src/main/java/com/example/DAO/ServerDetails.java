package com.example.DAO;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.example.Constants.StringConstants;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.vaadin.addon.charts.Chart;
import com.vaadin.addon.charts.model.ChartType;
import com.vaadin.addon.charts.model.Configuration;
import com.vaadin.addon.charts.model.Cursor;
import com.vaadin.addon.charts.model.DataLabels;
import com.vaadin.addon.charts.model.DataSeries;
import com.vaadin.addon.charts.model.DataSeriesItem;
import com.vaadin.addon.charts.model.PlotOptionsPie;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.ui.Component;
import com.vaadin.ui.themes.ValoTheme;

public class ServerDetails {

	public static String getStatus(String url) {
		String status = "";
		try {
			URL url1 = new URL(url);
			HttpURLConnection connection = (HttpURLConnection) url1.openConnection();
			connection.connect();
			int code = connection.getResponseCode();
			if (code == 200) {
				status = "UP";
			} else {
				status = "DOWN";
			}
		} catch (Exception e) {
			status = "DOWN";
		}
		return status;
	}

	public static int getID() {
		Random random = new Random();
		int value = random.nextInt(100);
		return value;
	}

	public static List<String> getAllDetails(String ip, String uname, String pwd) {
		List<String> list = new ArrayList<>();
		List<String> details = new ArrayList<>();
		list.add(StringConstants.APP_DB_CMD);
		list.add(StringConstants.USER_DB_CMD);
		list.add(StringConstants.CODEBASE_CMD);
		list.add(StringConstants.JBOSS_CMD);
		list.add(StringConstants.USED_MEMORY_CMD);
		list.add(StringConstants.FREE_MEMORY_CMD);
		list.add(StringConstants.TOTAL_MEMORY_CMD);
//		list.add("SERVER_CLUSTER_CMD");
		list.add(StringConstants.EAR_TIMESTAMP_CMD);
		list.add(StringConstants.LAST_BUILD_CMD);
		try {
			java.util.Properties config = new java.util.Properties();
			config.put("StrictHostKeyChecking", "no");
			JSch jsch = new JSch();
			Session session = jsch.getSession(uname, ip, 22);
			session.setPassword(pwd);
			session.setConfig(config);
			session.connect();
			for (String cmd : list) {
				String s = null;
				Channel channel = session.openChannel("exec");
				((ChannelExec) channel).setCommand(cmd);
				channel.setInputStream(null);
				((ChannelExec) channel).setErrStream(System.err);

				InputStream in = channel.getInputStream();
				channel.connect();
				byte[] tmp = new byte[1024];
				while (true) {
					while (in.available() > 0) {
						int i = in.read(tmp, 0, 1024);
						if (i < 0)
							break;
						s = new String(tmp, 0, i);
					}
					if (channel.isClosed()) {
						if (channel.getExitStatus() == 1) {
							details.add("Unknown");
						} else
							details.add(s.trim());
						break;
					}
				}
				channel.disconnect();
			}
			session.disconnect();
		} catch (Exception e) {
			// TODO: handle exception
		}
		return details;
	}

	public static boolean isReachable(String ip) {
		boolean result = false;
		try {
			InetAddress geek = InetAddress.getByName(ip);
			if (geek.isReachable(5000))
				result = true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
	public static boolean canConnect(String ip, String username, String password) {
		boolean result = false;
		try {
			java.util.Properties config = new java.util.Properties();
			config.put("StrictHostKeyChecking", "no");
			JSch jsch = new JSch();
			Session session = jsch.getSession(username, ip, 22);
			session.setPassword(password);
			session.setConfig(config);
			session.connect();
			if (session.isConnected())
				result = true;
		} catch (Exception e) {
			// TODO: handle exception
		}
		return result;
	}

	public static Component getMemoryChart(String used, String free, String total) {
		Component ret = createChart(used, free, total);
//		ret.setWidth("100px");
//		ret.setHeight("100px");
		return ret;
	}

	public static Chart createChart(String used, String free, String total) {
		double usd = Double.parseDouble(used.trim());
		double fre = Double.parseDouble(free.trim());
		double tot = Double.parseDouble(total.trim());
		double aa = usd * 100.0 / tot;
		DecimalFormat df = new DecimalFormat("##.00");

		double bb = Double.parseDouble(df.format(aa));
		double cc = Double.parseDouble(df.format(100-bb));
		Chart chart = new Chart(ChartType.PIE);
		chart.addStyleName("chart");
		Configuration conf = chart.getConfiguration();
		conf.setTitle("Memory Usage");
		PlotOptionsPie plotOptions = new PlotOptionsPie();
		plotOptions.setInnerSize(15, Unit.PERCENTAGE);
		plotOptions.setCursor(Cursor.POINTER);
		//plotOptions.setCenter("30%", "0%");
		plotOptions.setDepth(100);
//		plotOptions.setShowInLegend(true);
//		DataLabels dataLabels = new DataLabels();
//		dataLabels.setEnabled(true);
//		dataLabels.setFormatter("'<b>'+ this.point.name +'</b>: '+ this.percentage +' %'");
//		plotOptions.setDataLabels(dataLabels);
		conf.setPlotOptions(plotOptions);

		final DataSeries series = new DataSeries();
		DataSeriesItem usedData = new DataSeriesItem("Used",bb);
		series.add(usedData);
		series.add(new DataSeriesItem("Free", cc));
		conf.setSeries(series);
		chart.drawChart(conf);
		return chart;
	}
	public static String getLastMerge(String codebase) {
		String s = null;
		String a = "Unknown";
		if(codebase.contains("_"))
		{
			String arr [] = codebase.split("_");
			String fileName = arr[1]+"_autoMerge.log";
			try
			{
				java.util.Properties config = new java.util.Properties();
				config.put("StrictHostKeyChecking", "no");
				JSch jsch = new JSch();
				Session session = jsch.getSession("srinivasm", "172.16.94.11", 22);
				session.setPassword("srinivasm@puma");
				session.setConfig(config);
				session.connect();

				Channel channel = session.openChannel("exec");
				((ChannelExec) channel).setCommand(StringConstants.MERGE_TIMESTAMP_CMD+fileName+StringConstants.TIMESTAMP_CMD);
				channel.setInputStream(null);
				((ChannelExec) channel).setErrStream(System.err);

				InputStream in = channel.getInputStream();
				channel.connect();
				byte[] tmp = new byte[1024];
				while (true) {
					while (in.available() > 0) {
						int i = in.read(tmp, 0, 1024);
						if (i < 0)
							break;
						a = new String(tmp, 0, i);
					}
					if (channel.isClosed()) {
						break;
					}
				}
				channel.disconnect();
				session.disconnect();
			}
			catch (Exception e) {
				// TODO: handle exception
			}
		}
		return a;
	}

	public static List<String> getDBDetails(String ip, String uname, String pwd) {
		List<String> list = new ArrayList<>();
		List<String> details = new ArrayList<>();
		list.add("USED_MEMORY_CMD");
		list.add("FREE_MEMORY_CMD");
		list.add("TOTAL_MEMORY_CMD");
		list.add("DBS_COUNT");
		try {
			java.util.Properties config = new java.util.Properties();
			config.put("StrictHostKeyChecking", "no");
			JSch jsch = new JSch();
			Session session = jsch.getSession(uname, ip, 22);
			session.setPassword(pwd);
			session.setConfig(config);
			session.connect();
			for (String cmd : list) {
				String s = null;
				String a = "Unknown";
				Channel channel = session.openChannel("exec");
				switch (cmd) {

				case "USED_MEMORY_CMD": {
					((ChannelExec) channel).setCommand(StringConstants.USED_MEMORY_CMD);
					break;
				}
				case "FREE_MEMORY_CMD": {
					((ChannelExec) channel).setCommand(StringConstants.FREE_MEMORY_CMD);
					break;
				}
				case "TOTAL_MEMORY_CMD": {
					((ChannelExec) channel).setCommand(StringConstants.TOTAL_MEMORY_CMD);
					break;
				}
				case "DBS_COUNT": {
					((ChannelExec) channel).setCommand(StringConstants.DBS_COUNT);
					break;
				}
				default:
					((ChannelExec) channel).setCommand("echo WRONG_OPTION ");
					break;
				}
				channel.setInputStream(null);
				((ChannelExec) channel).setErrStream(System.err);

				InputStream in = channel.getInputStream();
				channel.connect();
				byte[] tmp = new byte[8024];
				while (true) {
					while (in.available() > 0) {
						int i = in.read(tmp, 0, 8024);
						if (i < 0)
							break;
						s = new String(tmp, 0, i);
						String array1[] = s.split("\\s");
						a = array1[0];

					}
					if (channel.isClosed()) {
						break;
					}
				}
				channel.disconnect();
				details.add(a);

			}
			session.disconnect();
		} catch (Exception e) {
			System.out.println("Exception" + e.getStackTrace());
		}
		return details;
	}

}
