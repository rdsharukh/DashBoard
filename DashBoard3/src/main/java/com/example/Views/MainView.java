package com.example.Views;

import java.io.IOException;
import java.util.Vector;

import com.example.Constants.StringConstants;
import com.example.DAO.AdminDAO;
import com.example.DashBoard3.MyUI;
import com.example.WindowComponents.WindowComponents;
import com.vaadin.annotations.PreserveOnRefresh;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

public class MainView extends VerticalLayout implements View {
	MyUI ui;
	Button configure,back;
	Button login;
	Navigator navigator;
	Label title=new Label();
	Label welcome = new Label();
	VerticalLayout content = new VerticalLayout();
	public MainView(MyUI ui) throws IOException {
		this.ui = ui;
		back = new Button("Back To DashBoard");
		title.addStyleName("title");
		welcome.addStyleName("welcome");
		back.setIcon(FontAwesome.BACKWARD);
		back.setDescription("Back to DashBoard");
		HorizontalLayout buttons1 = new HorizontalLayout();
		back.addStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);
		HorizontalLayout header = new HorizontalLayout();
		header.setSizeFull();
		welcome.setValue(StringConstants.GUEST);
		VerticalLayout footer = new VerticalLayout();
		footer.addStyleNames(ValoTheme.LAYOUT_WELL, ValoTheme.LAYOUT_CARD,"bgclr");
		VerticalLayout header1 = new VerticalLayout();
		header1.addStyleNames(ValoTheme.LAYOUT_WELL, ValoTheme.LAYOUT_CARD);
		header1.addStyleNames("bgclr");
		header1.setHeight("60px");
		header1.setMargin(false);
		header.setSpacing(true);
		header.setMargin(false);
		title.setValue(StringConstants.DASHBOARD_TITLE);
		back.addClickListener(e->{
			content.removeAllComponents();
			title.setValue(StringConstants.DASHBOARD_TITLE);
			content.addComponent(new ServerView(ui));
			configure.setVisible(true);
			back.setVisible(false);
		});
		content.removeAllComponents();
		content.addComponent(new ServerView(ui));
		content.setMargin(false);

		configure = new Button("Configure");
		configure.setIcon(FontAwesome.COGS);
		configure.setVisible(false);
		back.setVisible(false);
		login = new Button("Login");
		login.setIcon(FontAwesome.SIGN_IN);
		login.addStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);
		login.addClickListener(e -> {
			if (login.getCaption().equals("Login"))
				buildLoginWindow();
			else {
				confirmationWindow("logout");
			}
		});

		header.addComponents(title,welcome);
		header.setComponentAlignment(welcome, Alignment.TOP_RIGHT);
		header1.addComponent(header);
		header1.setComponentAlignment(header, Alignment.TOP_CENTER);
//		header.setComponentAlignment(welcome, Alignment.MIDDLE_RIGHT);
		setMargin(false);
		HorizontalLayout buttons = new HorizontalLayout();
		buttons.setSizeFull();
		buttons1.addComponents(configure,login);
		buttons.addComponents(back,buttons1);
		buttons.setSpacing(false);
		buttons.setComponentAlignment(back, Alignment.MIDDLE_LEFT);
		buttons.setComponentAlignment(buttons1, Alignment.MIDDLE_RIGHT);

//		buttons.setSizeFull();
		configure.addStyleNames(ValoTheme.BUTTON_BORDERLESS_COLORED);
		configure.addClickListener(e->{
			content.removeAllComponents();
			title.setValue(StringConstants.CONFIGURATION_TITLE);
			try {
				content.addComponent(new ConfigureView(ui));
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			configure.setVisible(false);
			back.setVisible(true);
		});
		addComponents(buttons,header1,content);
	}

	public void buildLoginWindow() {
		VerticalLayout content = new VerticalLayout();
		content.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
		Window login_window = new Window("Login", content);
		WindowComponents.addStyle(login_window);
		TextField username = new TextField("Username");
		username.setIcon(FontAwesome.USER);
		PasswordField password = new PasswordField("Password");
		password.setIcon(FontAwesome.USER_SECRET);
		HorizontalLayout buttons = new HorizontalLayout();
		Button login = new Button("Login");
		login.addStyleName(ValoTheme.BUTTON_PRIMARY);
		login.setClickShortcut(KeyCode.ENTER);
		login.addClickListener(e -> {
			if (AdminDAO.adminAuthentication(username, password)) {
				//this.configure.setVisible(true);
				this.title.setValue(StringConstants.CONFIGURATION_TITLE);
				this.back.setVisible(true);
				this.login.setCaption("Logout");
				this.login.setIcon(FontAwesome.SIGN_OUT);
				this.content.removeAllComponents();
				welcome.setValue(StringConstants.ADMIN+username.getValue());
				try {
					this.content.addComponent(new ConfigureView(this.ui));
				} catch (IOException e1) {

					e1.printStackTrace();
				}
				ui.removeWindow(login_window);
			}
			else
			{
				username.clear();
				password.clear();
				Notification.show(StringConstants.LOGIN_ERROR, Type.ERROR_MESSAGE);
			}
		});
		Button cancel = new Button("Cancel");
		cancel.addClickListener(e -> {
			ui.removeWindow(login_window);
		});
		buttons.addComponents(login, cancel);
		buttons.setComponentAlignment(login, Alignment.MIDDLE_LEFT);
		buttons.setComponentAlignment(cancel, Alignment.MIDDLE_RIGHT);
		content.addComponents(username, password, buttons);
//		content.setComponentAlignment(username, Alignment.MIDDLE_CENTER);
//		content.setComponentAlignment(password, Alignment.MIDDLE_CENTER);
//		content.setComponentAlignment(buttons, Alignment.MIDDLE_CENTER);
		ui.addWindow(login_window);
	}

	public void confirmationWindow(String msg) {
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
		messege.setCaption(StringConstants.LOGOUT_CONFIRM);
		yes.addClickListener(e -> {
			title.setValue(StringConstants.DASHBOARD_TITLE);
			login.setCaption("Login");
			welcome.setValue(StringConstants.GUEST);
			login.setIcon(FontAwesome.SIGN_IN);
			configure.setVisible(false);
			back.setVisible(false);
			this.content.removeAllComponents();
			this.content.addComponent(new ServerView(ui));
			this.title.setValue(StringConstants.DASHBOARD_TITLE);
			ui.removeWindow(confirm_window);
		});
		no.addClickListener(e -> {
			ui.removeWindow(confirm_window);
		});
		ui.addWindow(confirm_window);
	}
}
