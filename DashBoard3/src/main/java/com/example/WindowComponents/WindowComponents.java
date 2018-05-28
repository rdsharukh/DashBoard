package com.example.WindowComponents;

import java.util.ArrayList;
import java.util.List;

import com.example.DashBoard3.MyUI;
import com.example.VO.ServerVO;
import com.vaadin.shared.ui.ValueChangeMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

public class WindowComponents {

	public static void addStyle(Window window)
	{
		window.setWidth("500");
		window.setClosable(false);
		window.setResizable(false);
		window.center();
		window.addStyleName(ValoTheme.WINDOW_TOP_TOOLBAR);
		window.setModal(true);
	}
}
