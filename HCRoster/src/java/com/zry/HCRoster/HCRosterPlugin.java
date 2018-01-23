package com.zry.HCRoster;

import java.io.File;
import java.util.Collection;

import org.jivesoftware.openfire.SharedGroupException;
import org.jivesoftware.openfire.XMPPServer;
import org.jivesoftware.openfire.container.Plugin;
import org.jivesoftware.openfire.container.PluginManager;
import org.jivesoftware.openfire.interceptor.InterceptorManager;
import org.jivesoftware.openfire.interceptor.PacketInterceptor;
import org.jivesoftware.openfire.interceptor.PacketRejectedException;
import org.jivesoftware.openfire.muc.MUCEventDispatcher;
import org.jivesoftware.openfire.muc.MUCRoom;
import org.jivesoftware.openfire.roster.Roster;
import org.jivesoftware.openfire.roster.RosterItem;
import org.jivesoftware.openfire.roster.RosterItem.SubType;
import org.jivesoftware.openfire.session.Session;
import org.jivesoftware.openfire.user.UserAlreadyExistsException;
import org.jivesoftware.openfire.user.UserNotFoundException;
import org.xmpp.packet.JID;
import org.xmpp.packet.Message;
import org.xmpp.packet.Message.Type;
import org.xmpp.packet.Packet;


public class HCRosterPlugin implements Plugin,PacketInterceptor {
	
	private XMPPServer server;
	private final RosterPluginMUCEventListener mMUCEventListener = new RosterPluginMUCEventListener();
	@Override
	public void initializePlugin(PluginManager manager, File pluginDirectory) {
		// TODO Auto-generated method stub
		server = XMPPServer.getInstance();  
        
		MUCEventDispatcher.addListener(mMUCEventListener);
		
		InterceptorManager.getInstance().addInterceptor(this);
	}

	@Override
	public void destroyPlugin() {
		// TODO Auto-generated method stub
		MUCEventDispatcher.removeListener(mMUCEventListener);
		
		InterceptorManager.getInstance().removeInterceptor(this);
	}

	@Override
	public void interceptPacket(Packet packet, Session session, boolean incoming, boolean processed)
			throws PacketRejectedException {
		// TODO Auto-generated method stub
		
		if (packet.getClass().equals(Message.class)) 
		{
			Message msg = (Message)packet;
			
			if (msg.getType() == Type.chat && msg.getBody() != null) 
			{
				this.addJIDToUserItem(msg.getFrom(), msg.getTo());
				
				this.addJIDToUserItem(msg.getTo(), msg.getFrom());
			}
		}
	}
	
	public void addJIDToUserItem(JID user,JID jid)
	{
		Roster roster = null;
		try {
			roster = XMPPServer.getInstance().getRosterManager().getRoster(user.getNode());
		} catch (UserNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		if (roster != null) 
		{
			try {
				RosterItem rosterItem = roster.getRosterItem(jid);
				if (rosterItem.getSubStatus() != SubType.BOTH) 
				{
					rosterItem.setSubStatus(SubType.BOTH);
					roster.updateRosterItem(rosterItem);
				}
			} catch (UserNotFoundException e) {
				RosterItem rosterItem;
				try {
					rosterItem = roster.createRosterItem(jid, false, true);
					rosterItem.setSubStatus(SubType.BOTH);
					try {
						roster.updateRosterItem(rosterItem);
					} catch (UserNotFoundException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				} catch (UserAlreadyExistsException | SharedGroupException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
			}
			
			
		}
	}
}
