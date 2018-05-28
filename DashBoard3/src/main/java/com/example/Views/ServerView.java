package com.example.Views;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.example.Constants.IntegerConstants;
import com.example.Constants.StringConstants;
import com.example.DAO.Exec;
import com.example.DAO.ServerDAO;
import com.example.DAO.ServerDetails;
import com.example.DashBoard3.MyUI;
import com.example.VO.ServerVO;
import com.jcraft.jsch.JSchException;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.Label;
import com.vaadin.ui.Link;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.Timer;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

public class ServerView extends VerticalLayout implements View {
	public static final String NAME = "ServerView";
	HorizontalSplitPanel panel;
	List<ServerVO> server_list;
	int index = 0, last_index;
	List<VerticalLayout> layout_list = new ArrayList<>();
	Timer timer;
	MyUI ui;
	Navigator navigator;

	public ServerView(MyUI ui) {
		// this.navigator=navigator;
		this.ui = ui;
		this.timer = ui.timer;
		try {
			init();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Notification.show("Error", e.getMessage(), Type.ERROR_MESSAGE);
		}
	}

	private void init() throws IOException {
		server_list = ServerDAO.getAllServers();
		panel = new HorizontalSplitPanel();
		panel.setSplitPosition(65, Unit.PERCENTAGE);
		if (server_list.isEmpty()) {
			panel.setSecondComponent(noServerSecondComponent());
			panel.setFirstComponent(noServerFirstComponent());
		} else {
			panel.setSecondComponent(buildSecondComponent(server_list));
			panel.setFirstComponent(firstComponent(server_list.get(index)));
			timer.scheduleRepeatable(IntegerConstants.time);
			timer.run(() -> {
				layout_list.get(last_index).removeStyleName("delay");
				layout_list.get(index).addStyleName("delay");
				try {
					panel.setFirstComponent(buildFirstComponent(server_list.get(index)));
				} catch (IOException e) {
					e.printStackTrace();
				}
				last_index = index;
				index++;
				if (index == server_list.size()) {
					index = 0;
				}
			});
		}
		addComponent(panel);
		setMargin(false);
	}

	public VerticalLayout buildFirstComponent(ServerVO server) throws IOException {
		VerticalLayout layoutComponent = new VerticalLayout();
		HorizontalLayout firstpanel = new HorizontalLayout();
		VerticalLayout heading = new VerticalLayout();
		heading.setMargin(false);
		heading.setSpacing(false);
		VerticalLayout details = new VerticalLayout();
		details.setMargin(false);
		details.setSpacing(false);
		VerticalLayout charts = new VerticalLayout();
		charts.setMargin(false);
		// charts.setDefaultComponentAlignment(Alignment.TOP_CENTER);
		charts.setSpacing(false);
		layoutComponent.setSpacing(false);
		layoutComponent.setHeight("470");
		layoutComponent.addStyleNames(ValoTheme.LAYOUT_WELL, ValoTheme.LAYOUT_CARD, "first");
		String url = StringConstants.URL + server.getIp() + ":" + server.getPort();
		Label sName = new Label("Server Name");
		heading.addComponent(sName);
		details.addComponent(new Label(": " + server.getSname()));
		Label sType = new Label("Server Type");
		heading.addComponent(sType);
		details.addComponent(new Label(": " + server.getType()));
		if (server.getType().equals("Application")) {
			List<String> detailsList = ServerDetails.getAllDetails(server.getIp(), server.getUsername(),
					server.getPassword());
			Link link = new Link(url, new ExternalResource(url));
			Label sUrl = new Label("Server URL");
			heading.addComponent(sUrl);
			HorizontalLayout hl = new HorizontalLayout();
			hl.addComponents(new Label(":"), link);
			details.addComponents(hl);
			Label status = new Label("Server Status");
			heading.addComponent(status);
			String statusDetail = ServerDetails.getStatus(url);
			Label statusLabel = new Label(": " + statusDetail);
			if (statusDetail.equals("UP")) {
				statusLabel.addStyleName("up");
			} else {
				statusLabel.addStyleName("down");
			}
			details.addComponent(statusLabel);
			Label appDB = new Label("Application Database");
			heading.addComponent(appDB);
			details.addComponent(new Label(": " + detailsList.get(0)));
			Label usrDB = new Label("User Database");
			heading.addComponent(usrDB);
			details.addComponent(new Label(": " + detailsList.get(1)));
			Label codebase = new Label("Codebase");
			heading.addComponent(codebase);
			details.addComponent(new Label(": " + detailsList.get(2)));
			Label jboss = new Label("JBoss");
			heading.addComponent(jboss);
			details.addComponent(new Label(": " + detailsList.get(3)));
			Label usedMemory = new Label("Used Memory(MB)");
			heading.addComponent(usedMemory);
			details.addComponent(new Label(": " + detailsList.get(4)));
			Label freeMemory = new Label("Free Memory(MB)");
			heading.addComponent(freeMemory);
			details.addComponent(new Label(": " + detailsList.get(5)));
			Label totalMemory = new Label("Total Memory(MB)");
			heading.addComponent(totalMemory);
			details.addComponent(new Label(": " + detailsList.get(6)));
			Label cluster = new Label("Server in cluster");
			heading.addComponent(cluster);
			details.addComponent(new Label(": " + "Yes"));
			Label timestamp = new Label("Last Deployment");
			heading.addComponent(timestamp);
			details.addComponent(new Label(": " + detailsList.get(7)));
			Label lastMerge = new Label("Last Merge");
			heading.addComponent(lastMerge);
			details.addComponent(new Label(": " + ServerDetails.getLastMerge(detailsList.get(2))));
			Label lastBuild = new Label("Last Auto Build");
			heading.addComponent(lastBuild);
			details.addComponent(new Label(": " + detailsList.get(8)));
			charts.addComponent(
					ServerDetails.getMemoryChart(detailsList.get(4), detailsList.get(5), detailsList.get(6)));
			// charts.addComponent(ServerDetails.getMemoryChart("45", "55",
			// "100"));
			link.setTargetName("_blank");
			firstpanel.addComponents(heading, details, charts);
			layoutComponent.addComponent(firstpanel);
		} else if (server.getType().equals("Database")) {
			List<String> detailsList = ServerDetails.getDBDetails(server.getIp(), server.getUsername(),
					server.getPassword());
			Exec ex1 = new Exec();
			ArrayList<StringBuffer> dblist;
			try {
				dblist = ex1.getOutput1();
				String s = dblist.toString();
				int i = s.length();
				String s3 = s.substring(1, i - 1);
//				System.out.println(s3);
				String[] s2 = s3.split(",");
				Label dbqa = new Label("IP's connected to PUMAQA DB");
				heading.addComponent(dbqa);
				if (!" ".equals(s2[0]) && s2[0] != null)
				{
					details.addComponent(new Label(": " + dblist.get(0)));
				}
				else
				{
					details.addComponent(new Label(": Unknown"));
				}
				Label dbdev = new Label("IP's connected to PUMAdev DB");
				heading.addComponent(dbdev);
				if (!" ".equals(s2[1]) && s2[1] != null) {

					details.addComponent(new Label(": " + dblist.get(1)));

				} else {
					details.addComponent(new Label(":Unknown"));

				}
				Label dblive = new Label("IP's connected to LiveQA DB");
				heading.addComponent(dblive);
				if (!" ".equals(s2[2]) && s2[2] != null) {

					details.addComponent(new Label(": " + dblist.get(2)));

				} else {
					details.addComponent(new Label(":Unknown"));
				}

			} catch (JSchException e) {
				e.printStackTrace();
			}

			Label usedMemory = new Label("Used Memory(MB)");
			heading.addComponent(usedMemory);
			details.addComponent(new Label(": " + detailsList.get(0)));
			Label freeMemory = new Label("Free Memory(MB)");
			heading.addComponent(freeMemory);
			details.addComponent(new Label(": " + detailsList.get(1)));
			Label totalMemory = new Label("Total Memory(MB)");
			heading.addComponent(totalMemory);
			details.addComponent(new Label(": " + detailsList.get(2)));
			Label dbCount = new Label("Number of Database");
			heading.addComponent(dbCount);
			details.addComponent(new Label(": " + detailsList.get(3)));
			charts.addComponent(
					ServerDetails.getMemoryChart(detailsList.get(0), detailsList.get(1), detailsList.get(2)));
			firstpanel.addComponents(heading, details, charts);
			layoutComponent.addComponent(firstpanel);
		} else {
			Link link = new Link(url, new ExternalResource(url + "/jasperserver"));
			Label sUrl = new Label("Server URL");
			heading.addComponent(sUrl);
			HorizontalLayout hl = new HorizontalLayout();
			hl.addComponents(new Label(":"), link);
			details.addComponents(hl);
			Label status = new Label("Server Status");
			heading.addComponent(status);
			String statusDetail = ServerDetails.getStatus(url);
			Label statusLabel = new Label(": " + statusDetail);
			if (statusDetail.equals("UP")) {
				statusLabel.addStyleName("up");
			} else {
				statusLabel.addStyleName("down");
			}
			details.addComponent(statusLabel);
			List<String> detailsList = ServerDetails.getDBDetails(server.getIp(), server.getUsername(),
					server.getPassword());
			Label usedMemory = new Label("Used Memory(MB)");
			heading.addComponent(usedMemory);
			details.addComponent(new Label(": " + detailsList.get(0)));
			Label freeMemory = new Label("Free Memory(MB)");
			heading.addComponent(freeMemory);
			details.addComponent(new Label(": " + detailsList.get(1)));
			Label totalMemory = new Label("Total Memory(MB)");
			heading.addComponent(totalMemory);
			details.addComponent(new Label(": " + detailsList.get(2)));
			link.setTargetName("_blank");
			charts.addComponent(
					ServerDetails.getMemoryChart(detailsList.get(0), detailsList.get(1), detailsList.get(2)));
			firstpanel.addComponents(heading, details, charts);
			layoutComponent.addComponent(firstpanel);
		}
		return layoutComponent;
	}

	public VerticalLayout firstComponent(ServerVO server) throws IOException {
		VerticalLayout layoutComponent = new VerticalLayout();
		HorizontalLayout firstpanel = new HorizontalLayout();
		VerticalLayout heading = new VerticalLayout();
		heading.setMargin(false);
		heading.setSpacing(false);
		VerticalLayout details = new VerticalLayout();
		details.setMargin(false);
		details.setSpacing(false);
		VerticalLayout charts = new VerticalLayout();
		charts.setMargin(false);
		// charts.setDefaultComponentAlignment(Alignment.TOP_CENTER);
		charts.setSpacing(false);
		layoutComponent.setSpacing(false);
		layoutComponent.setHeight("470");
		layoutComponent.addStyleNames(ValoTheme.LAYOUT_WELL, ValoTheme.LAYOUT_CARD, "first");
		String url = StringConstants.URL + server.getIp() + ":" + server.getPort();
		Label sName = new Label("Server Name");
		heading.addComponent(sName);
		details.addComponent(new Label(": " + server.getSname()));
		Label sType = new Label("Server Type");
		heading.addComponent(sType);
		details.addComponent(new Label(": " + server.getType()));
		if (server.getType().equals("Application")) {
			List<String> detailsList = ServerDetails.getAllDetails(server.getIp(), server.getUsername(),
					server.getPassword());
			Link link = new Link(url, new ExternalResource(url));
			Label sUrl = new Label("Server URL");
			heading.addComponent(sUrl);
			HorizontalLayout hl = new HorizontalLayout();
			hl.addComponents(new Label(":"), link);
			details.addComponents(hl);
			Label status = new Label("Server Status");
			heading.addComponent(status);
			String statusDetail = ServerDetails.getStatus(url);
			Label statusLabel = new Label(": " + statusDetail);
			if (statusDetail.equals("UP")) {
				statusLabel.addStyleName("up");
			} else {
				statusLabel.addStyleName("down");
			}
			details.addComponent(statusLabel);
			Label appDB = new Label("Application Database");
			heading.addComponent(appDB);
			details.addComponent(new Label(": " + detailsList.get(0)));
			Label usrDB = new Label("User Database");
			heading.addComponent(usrDB);
			details.addComponent(new Label(": " + detailsList.get(1)));
			Label codebase = new Label("Codebase");
			heading.addComponent(codebase);
			details.addComponent(new Label(": " + detailsList.get(2)));
			Label jboss = new Label("JBoss");
			heading.addComponent(jboss);
			details.addComponent(new Label(": " + detailsList.get(3)));
			Label usedMemory = new Label("Used Memory(MB)");
			heading.addComponent(usedMemory);
			details.addComponent(new Label(": " + detailsList.get(4)));
			Label freeMemory = new Label("Free Memory(MB)");
			heading.addComponent(freeMemory);
			details.addComponent(new Label(": " + detailsList.get(5)));
			Label totalMemory = new Label("Total Memory(MB)");
			heading.addComponent(totalMemory);
			details.addComponent(new Label(": " + detailsList.get(6)));
			Label cluster = new Label("Server in cluster");
			heading.addComponent(cluster);
			details.addComponent(new Label(": " + "Yes"));
			Label timestamp = new Label("Last Deployment");
			heading.addComponent(timestamp);
			details.addComponent(new Label(": " + detailsList.get(7)));
			Label lastMerge = new Label("Last Merge");
			heading.addComponent(lastMerge);
			details.addComponent(new Label(": " + ServerDetails.getLastMerge(detailsList.get(2))));
			Label lastBuild = new Label("Last Auto Build");
			heading.addComponent(lastBuild);
			details.addComponent(new Label(": " + detailsList.get(8)));
			charts.addComponent(
					ServerDetails.getMemoryChart(detailsList.get(4), detailsList.get(5), detailsList.get(6)));
			// charts.addComponent(ServerDetails.getMemoryChart("45", "55",
			// "100"));
			link.setTargetName("_blank");
			firstpanel.addComponents(heading, details, charts);
			layoutComponent.addComponent(firstpanel);
		} else if (server.getType().equals("Database")) {
			List<String> detailsList = ServerDetails.getDBDetails(server.getIp(), server.getUsername(),
					server.getPassword());
			Exec ex1 = new Exec();
			ArrayList<StringBuffer> dblist;
			try {
				dblist = ex1.getOutput1();
				String s = dblist.toString();
				int i = s.length();
				String s3 = s.substring(1, i - 1);
//				System.out.println(s3);
				String[] s2 = s3.split(",");
				Label dbqa = new Label("IP's connected to PUMAQA DB");
				heading.addComponent(dbqa);
				if (!" ".equals(s2[0]) && s2[0] != null) {

					details.addComponent(new Label(": " + dblist.get(0)));
				} else {
					details.addComponent(new Label(": Unknown"));
				}
				Label dbdev = new Label("IP's connected to PUMAdev DB");
				heading.addComponent(dbdev);
				if (!" ".equals(s2[1]) && s2[1] != null) {

					details.addComponent(new Label(": " + dblist.get(1)));

				} else {
					details.addComponent(new Label(":Unknown"));

				}
				Label dblive = new Label("IP's connected to LiveQA DB");
				heading.addComponent(dblive);
				if (!" ".equals(s2[2]) && s2[2] != null) {

					details.addComponent(new Label(": " + dblist.get(2)));

				} else {
					details.addComponent(new Label(":Unknown"));
				}

			} catch (JSchException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			Label usedMemory = new Label("Used Memory(MB)");
			heading.addComponent(usedMemory);
			details.addComponent(new Label(": " + detailsList.get(0)));
			Label freeMemory = new Label("Free Memory(MB)");
			heading.addComponent(freeMemory);
			details.addComponent(new Label(": " + detailsList.get(1)));
			Label totalMemory = new Label("Total Memory(MB)");
			heading.addComponent(totalMemory);
			details.addComponent(new Label(": " + detailsList.get(2)));
			Label dbCount = new Label("Number of Database");
			heading.addComponent(dbCount);
			details.addComponent(new Label(": " + detailsList.get(3)));
			charts.addComponent(
					ServerDetails.getMemoryChart(detailsList.get(0), detailsList.get(1), detailsList.get(2)));
			firstpanel.addComponents(heading, details, charts);
			layoutComponent.addComponent(firstpanel);
		} else {
			Link link = new Link(url, new ExternalResource(url + "/jasperserver"));
			Label sUrl = new Label("Server URL");
			heading.addComponent(sUrl);
			HorizontalLayout hl = new HorizontalLayout();
			hl.addComponents(new Label(":"), link);
			details.addComponents(hl);
			Label status = new Label("Server Status");
			heading.addComponent(status);
			String statusDetail = ServerDetails.getStatus(url);
			Label statusLabel = new Label(": " + statusDetail);
			if (statusDetail.equals("UP")) {
				statusLabel.addStyleName("up");
			} else {
				statusLabel.addStyleName("down");
			}
			details.addComponent(statusLabel);
			List<String> detailsList = ServerDetails.getDBDetails(server.getIp(), server.getUsername(),
					server.getPassword());
			Label usedMemory = new Label("Used Memory(MB)");
			heading.addComponent(usedMemory);
			details.addComponent(new Label(": " + detailsList.get(0)));
			Label freeMemory = new Label("Free Memory(MB)");
			heading.addComponent(freeMemory);
			details.addComponent(new Label(": " + detailsList.get(1)));
			Label totalMemory = new Label("Total Memory(MB)");
			heading.addComponent(totalMemory);
			details.addComponent(new Label(": " + detailsList.get(2)));
			link.setTargetName("_blank");
			charts.addComponent(
					ServerDetails.getMemoryChart(detailsList.get(0), detailsList.get(1), detailsList.get(2)));
			firstpanel.addComponents(heading, details, charts);
			layoutComponent.addComponent(firstpanel);
		}
		layout_list.get(index).addStyleName("delay");
		last_index = index;
		index++;
		if (index == server_list.size()) {
			index = 0;
		}
		return layoutComponent;
	}

	public VerticalLayout buildSecondComponent(List<ServerVO> list) {
		VerticalLayout vl = new VerticalLayout();
		CssLayout secondPanel = new CssLayout();
		for (ServerVO server : list) {
			VerticalLayout child = new VerticalLayout();
			child.addStyleNames(ValoTheme.LAYOUT_WELL, ValoTheme.LAYOUT_CARD, "second");
			layout_list.add(child);
			child.setWidth("200");
			child.addLayoutClickListener(e -> {
				try {

					panel.setFirstComponent(buildFirstComponent(server));
					layout_list.get(last_index).removeStyleName("delay");
					index = list.indexOf(server);
					last_index = index;
					index++;
					if (index == server_list.size()) {
						index = 0;
					}
					child.addStyleName("delay");
					timer.cancel();
					timer.scheduleRepeatable(IntegerConstants.time);

				} catch (IOException e1) {
					e1.printStackTrace();
				}
			});
			Label sCode = new Label(server.getCode());
			sCode.addStyleNames("label");
			Label ip = new Label("Server");
			child.setMargin(false);
			child.setSpacing(false);
			child.addComponents(sCode, ip);
			child.setComponentAlignment(sCode, Alignment.TOP_CENTER);
			child.setComponentAlignment(ip, Alignment.TOP_CENTER);
			secondPanel.addComponents(child);
		}
		vl.addComponents(secondPanel);
		return vl;
	}

	public VerticalLayout noServerSecondComponent() {
		VerticalLayout firstpanel = new VerticalLayout();
		Label msg = new Label();
		msg.setValue("No Servers Configured");
		firstpanel.addComponent(msg);
		return firstpanel;
	}

	public VerticalLayout noServerFirstComponent() {
		VerticalLayout firstpanel = new VerticalLayout();
		firstpanel.setSpacing(true);
		firstpanel.setHeight("470");
		firstpanel.addStyleNames(ValoTheme.LAYOUT_WELL, ValoTheme.LAYOUT_CARD, "first");
		Label msg = new Label();
		msg.setValue("Configure Server");
		firstpanel.addComponent(msg);
		return firstpanel;
	}
}
