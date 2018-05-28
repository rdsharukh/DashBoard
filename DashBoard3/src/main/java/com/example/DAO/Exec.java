package com.example.DAO;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;

import com.example.Constants.Pathconstants;
import com.example.Constants.StringConstants;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.ChannelShell;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;
import com.sun.xml.internal.bind.v2.schemagen.xmlschema.List;

public class Exec {


	public  ArrayList getOutput1() throws JSchException   {

    	ArrayList<String> dblist = new ArrayList<>();
    	dblist.add("Pumaqa_CONNECTED_CMD");
    	dblist.add("Pumadev_CONNECTED_CMD");
    	dblist.add("Liveqa_CONNECTED_CMD");


//String cmd=null;
       String line=null;

      ArrayList<StringBuffer>li=new ArrayList<StringBuffer>();

        String uname="db2inst3";
        String ip="172.16.94.15";
        String pwd="brillio@123";


        java.util.Properties config = new java.util.Properties();
        config.put("StrictHostKeyChecking", "no");
        JSch jsch = new JSch();
        for (String cmd : dblist) {
        	switch (cmd) {
			case "Pumaqa_CONNECTED_CMD":
				getDBDetails( li, uname, ip, pwd, config, jsch,StringConstants.Pumaqa_CONNECTED_CMD,Pathconstants.Pumaqa_CONNECTED_PATH);
				break;
			case "Pumadev_CONNECTED_CMD":
				getDBDetails( li, uname, ip, pwd, config, jsch,StringConstants.Pumadev_CONNECTED_CMD,Pathconstants.Pumadev_CONNECTED_PATH);
				break;
			case "Liveqa_CONNECTED_CMD":
				getDBDetails( li, uname, ip, pwd, config, jsch,StringConstants.Liveqa_CONNECTED_CMD,Pathconstants.liveqa_CONNECTED_PATH);
				break;

			default:
				break;
			}
        }
		return li;
    }

    /**
     * Method to get Number of IP's connected to databases
     * @param li
     * @param uname
     * @param ip
     * @param pwd
     * @param config
     * @param jsch
     * @param dbCommand
     * @param path
     */
	private void getDBDetails(ArrayList<StringBuffer> li, String uname, String ip, String pwd,
			java.util.Properties config, JSch jsch, String dbCommand, String path) {
		String line;
		try{
			Session session = jsch.getSession(uname, ip, 22);
			session.setPassword(pwd);
			session.setConfig(config);

			session.connect();
			session.setTimeout(10000);

			Channel channel = session.openChannel("shell");

			channel.setInputStream(null);
			channel.setOutputStream(null);

			InputStream in = channel.getInputStream();
			OutputStream out = channel.getOutputStream();

			((ChannelShell)channel).setPtyType("vt102");
			channel.connect();

			byte[] tmp=new byte[1024];
			out.write(dbCommand.getBytes());


			out.write(("\n").getBytes());
			out.flush();

				while (in.available() > 0)
					{
						int i = in.read(tmp, 0, 1024);

						if (i < 0)
							{
								break;
							}
				String buffer = new String(tmp, 0, i);
					}
				channel.disconnect();
				session.disconnect();

		}
		catch (Exception e) {
							}

		try {
			Session session = jsch.getSession(uname, ip, 22);
			session.setPassword(pwd);
			session.setConfig(config);

			session.connect();
			session.setTimeout(10000);

			Channel channel = session.openChannel("sftp");
			channel.connect();
			ChannelSftp sftpChannel = (ChannelSftp) channel;

			InputStream stream = sftpChannel.get(path);
			StringBuffer buf = new StringBuffer();
			try {
				BufferedReader br = new BufferedReader(new InputStreamReader(stream));

				while ((line = br.readLine()) != null)
					{

						buf.append(line + "\n" );
					}
				li.add(buf);
			}
				catch (IOException io)
			{
					System.out.println("Exception occurred during reading file from SFTP server due to " + io.getMessage());
					io.getMessage();

			}
 catch (Exception e)
			{
		 System.out.println("Exception occurred during reading file from SFTP server due to " + e.getMessage());
		 e.getMessage();

			}

			sftpChannel.exit();
			session.disconnect();
		}
		catch (JSchException e)
		{
			e.printStackTrace();
		}
		catch (SftpException e)
		{
			e.printStackTrace();
		}
	}

}
