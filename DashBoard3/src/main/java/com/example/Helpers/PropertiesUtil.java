package com.example.Helpers;

import java.io.InputStream;
import java.util.Properties;

import com.example.Constants.StringConstants;

public class PropertiesUtil {

	private static Properties configProperties = new Properties();
	private String filename = StringConstants.CONFIG_FILE;

	public Properties getConfigProperties(){
		if(configProperties.size()==0)
			loadProperties();
		return configProperties;
	}

	private void loadProperties(){
		InputStream instream = getClass().getClassLoader().getResourceAsStream(filename);
		try{
			configProperties.load(instream);
		}
		catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	public String getProperty(String name){
		if(configProperties.isEmpty())
			loadProperties();
		return configProperties.getProperty(name);
	}


}
