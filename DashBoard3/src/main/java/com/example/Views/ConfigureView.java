package com.example.Views;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.example.Constants.QueryConstants;
import com.example.Constants.StringConstants;
import com.example.DAO.ServerDAO;
import com.example.DAO.ServerDetails;
import com.example.DashBoard3.MyUI;
import com.example.VO.ServerVO;
import com.example.WindowComponents.WindowComponents;
import com.sun.corba.se.pept.protocol.ServerRequestDispatcher;
import com.vaadin.event.MouseEvents.ClickEvent;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.ValueChangeMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Grid.SelectionMode;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.renderers.ButtonRenderer;
import com.vaadin.ui.themes.ValoTheme;

public class ConfigureView extends VerticalLayout implements View {
	MyUI ui;
	Navigator navigator;
	Set<ServerVO> selected = new HashSet<>();
	List<String> options;
	Grid<ServerVO> grid;
	public static final String NAME="ConfigureView";
	public ConfigureView(MyUI ui) throws IOException {
		this.ui=ui;

		grid = new Grid<>();
		grid.setSelectionMode(SelectionMode.MULTI);
		grid.addSelectionListener(e->{
			selected = e.getAllSelectedItems();
		});
		grid.addColumn(ServerVO::getSname).setCaption("Server Name");
		grid.addColumn(ServerVO::getIp).setCaption("Server IP");
		grid.addColumn(editbutton->"Edit", new ButtonRenderer(clickEvent -> {
			ServerVO selectedServer = (ServerVO)clickEvent.getItem();
			editWindow(selectedServer);
	          //Notification.show(server.getSname());
	    })).setCaption("Edit");
		grid.setItems(ServerDAO.getAllServers());
		HorizontalLayout buttons = new HorizontalLayout();
		buttons.setSizeFull();
		grid.setSizeFull();
		Button add = new Button("Add");
		add.addClickListener(e->{
			addWindow();
		});
		add.setDescription("Add New Server");
		add.addStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);
		add.setIcon(FontAwesome.PLUS_CIRCLE);
		Button delete = new Button("Delete Selected");
		delete.addStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);
		delete.setIcon(FontAwesome.TRASH);
		delete.setDescription("Delete Selected Servers");
		delete.addClickListener(e->{
			if(selected.isEmpty())
				Notification.show("Select items to Delete");
			else
			{
				confirmationWindow(ServerDAO.generateDeleteQuery(selected));
			}
		});
		buttons.addComponents(delete,add);
		buttons.setComponentAlignment(delete, Alignment.MIDDLE_LEFT);
		buttons.setComponentAlignment(add, Alignment.MIDDLE_RIGHT);
		addComponents(buttons,grid);
	}

	public void addWindow(){
		VerticalLayout content = new VerticalLayout();
		content.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
		Window window = new Window("Add New Server",content);
		WindowComponents.addStyle(window);
		TextField sname= new TextField("Name");
		TextField ip = new TextField("IP");
		TextField port = new TextField("Port");
		TextField username = new TextField("Username");
		TextField password =new TextField("Password");
		options = new ArrayList<>();
		options.add("Application");
		options.add("Database");
		options.add("Report");
		ComboBox<String> type = new ComboBox<>("Type", options);
		type.setPlaceholder("Select Server Type");
		TextField code  = new TextField("Code");
		Button create = new Button("Add");
		create.addStyleName(ValoTheme.BUTTON_PRIMARY);
		create.addClickListener(e->{
			if(ServerDetails.isReachable(ip.getValue()) && ServerDetails.canConnect(ip.getValue(), username.getValue(), password.getValue()))
			{
				ServerVO server = new ServerVO();
				server.setId("100-"+ServerDetails.getID());
				server.setSname(sname.getValue());
				server.setIp(ip.getValue());
				server.setPort(port.getValue());
				server.setCode(code.getValue());
				server.setType(type.getValue());
				server.setUsername(username.getValue());
				server.setPassword(password.getValue());
				server.setStatus("1");
				ServerDAO.addServer(server);
				try {
					grid.setItems(ServerDAO.getAllServers());
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				ui.removeWindow(window);
			}
			else
			{
				if(ServerDetails.isReachable(ip.getValue()))
				{
					ip.removeStyleName("fail");
					username.addStyleName("fail");
					password.addStyleName("fail");
					Notification.show("WARNING", StringConstants.SERVER_CONNECT_ERROR, Type.ERROR_MESSAGE);
				}
				else
				{
					ip.addStyleName("fail");
					username.removeStyleName("fail");
					password.removeStyleName("fail");
					Notification.show("WARNING", StringConstants.IP_PING_ERROR, Type.ERROR_MESSAGE);
				}
			}
		});
		Button cancel = new Button("Cancel");
		HorizontalLayout buttons = new HorizontalLayout();
		buttons.addComponents(create,cancel);
		content.addComponents(sname,ip,port,code,username,password,type,buttons);
		ui.addWindow(window);
		cancel.addClickListener(e->{
			ui.removeWindow(window);
		});
	}
	public void confirmationWindow(String query) {
		VerticalLayout content = new VerticalLayout();
		Window confirm_window = new Window("Confirm", content);
		WindowComponents.addStyle(confirm_window);
		Label messege = new Label();
		HorizontalLayout buttons = new HorizontalLayout();
		Button yes = new Button("Yes");
		Button no = new Button("No");
		no.addStyleName(ValoTheme.BUTTON_PRIMARY);
		buttons.addComponents(yes, no);
		content.addComponents(messege, buttons);
		messege.setCaption(StringConstants.DELETE_CONFIRM);
		yes.addClickListener(e -> {
			ServerDAO.deleteServer(query);
			try {
				grid.setItems(ServerDAO.getAllServers());
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			ui.removeWindow(confirm_window);
		});
		no.addClickListener(e -> {
			ui.removeWindow(confirm_window);
		});
		ui.addWindow(confirm_window);
	}
	public void editWindow(ServerVO selectedServer){
		VerticalLayout content = new VerticalLayout();
		content.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
		Window window = new Window("Edit Server",content);
		WindowComponents.addStyle(window);
		TextField sname= new TextField("Name");
		sname.setValue(selectedServer.getSname());
		TextField ip = new TextField("IP");
		ip.setValue(selectedServer.getIp());
		ip.setReadOnly(true);
		TextField port = new TextField("Port");
		port.setValue(selectedServer.getPort());
		TextField username = new TextField("Username");
		username.setValue(selectedServer.getUsername());
		TextField password =new TextField("Password");
		password.setValue(selectedServer.getPassword());
		options = new ArrayList<>();
		options.add("Application");
		options.add("Database");
		options.add("Report");
		ComboBox<String> type = new ComboBox<>("Type", options);
		type.setPlaceholder("Select Server Type");
		type.setValue(selectedServer.getType());
		TextField code  = new TextField("Code");
		code.setValue(selectedServer.getCode());
		Button create = new Button("Save");
		create.addStyleName(ValoTheme.BUTTON_PRIMARY);
		create.addClickListener(e->{
			ServerVO server = new ServerVO();
			server.setId(selectedServer.getId());
			server.setSname(sname.getValue());
			server.setIp(ip.getValue());
			server.setPort(port.getValue());
			server.setCode(code.getValue());
			server.setType(type.getValue());
			server.setUsername(username.getValue());
			server.setPassword(password.getValue());
			ServerDAO.editServer(server);
			try {
				grid.setItems(ServerDAO.getAllServers());
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			ui.removeWindow(window);
		});
		Button cancel = new Button("Cancel");
		HorizontalLayout buttons = new HorizontalLayout();
		buttons.addComponents(create,cancel);
		content.addComponents(sname,ip,port,code,username,password,type,buttons);

		ui.addWindow(window);
		cancel.addClickListener(e->{
			ui.removeWindow(window);
		});
	}
}
