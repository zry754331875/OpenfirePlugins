package com.zry.userRoom;

import java.io.File;
import java.util.List;

import org.jivesoftware.openfire.XMPPServer;
import org.jivesoftware.openfire.container.Plugin;
import org.jivesoftware.openfire.container.PluginManager;




public class UserRoomPlugin implements Plugin {
	
	private XMPPServer server;
	private UserRoomIQHandler iqHandler;
	private UserRoomIQAdminHandler iqAdminHandler;
	@Override
	public void initializePlugin(PluginManager manager, File pluginDirectory) {
		// TODO Auto-generated method stub
		System.out.println("userRoom插件启动成功");
		server = XMPPServer.getInstance();
		iqHandler = new UserRoomIQHandler();
		iqAdminHandler = new UserRoomIQAdminHandler();
		server.getIQRouter().addHandler(iqHandler); 
		server.getIQRouter().addHandler(iqAdminHandler); 
	}

	@Override
	public void destroyPlugin() {
		// TODO Auto-generated method stub
		server.getIQRouter().removeHandler(iqHandler);
		server.getIQRouter().removeHandler(iqAdminHandler);
	}

}
