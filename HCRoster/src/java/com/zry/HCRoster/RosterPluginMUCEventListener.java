package com.zry.HCRoster;

import java.util.Collection;
import java.util.List;

import org.jivesoftware.openfire.SharedGroupException;
import org.jivesoftware.openfire.XMPPServer;
import org.jivesoftware.openfire.muc.MUCEventListener;
import org.jivesoftware.openfire.muc.MUCRoom;
import org.jivesoftware.openfire.muc.MultiUserChatService;
import org.jivesoftware.openfire.muc.NotAllowedException;
import org.jivesoftware.openfire.roster.Roster;
import org.jivesoftware.openfire.roster.RosterItem;
import org.jivesoftware.openfire.roster.RosterItem.AskType;
import org.jivesoftware.openfire.roster.RosterItem.SubType;
import org.jivesoftware.openfire.roster.RosterItemProvider;
import org.jivesoftware.openfire.roster.RosterManager;
import org.jivesoftware.openfire.user.UserAlreadyExistsException;
import org.jivesoftware.openfire.user.UserNotFoundException;
import org.jivesoftware.openfire.muc.MUCRole.Affiliation;
import org.xmpp.packet.JID;
import org.xmpp.packet.Message;


public class RosterPluginMUCEventListener implements MUCEventListener {

	@Override
	public void roomCreated(JID roomJID) {
		// TODO Auto-generated method stub

	}

	@Override
	public void roomDestroyed(JID roomJID) {
		
	}

	@Override
	public void occupantJoined(JID roomJID, JID user, String nickname) {
		// TODO Auto-generated method stub

	}

	@Override
	public void occupantLeft(JID roomJID, JID user) {
		// TODO Auto-generated method stub

	}

	@Override
	public void nicknameChanged(JID roomJID, JID user, String oldNickname, String newNickname) {
		// TODO Auto-generated method stub

	}

	@Override
	public void messageReceived(JID roomJID, JID user, String nickname, Message message) {
		
		List<MultiUserChatService> servers = XMPPServer.getInstance().getMultiUserChatManager()
				.getMultiUserChatServices();
		
		MUCRoom room = null;
		for (MultiUserChatService multiUserChatService : servers) {
			List<MUCRoom> rooms = multiUserChatService.getChatRooms();
			
			for (MUCRoom mucRoom : rooms) {
				if (mucRoom.getJID().compareTo(roomJID) == 0) 
				{
					room = mucRoom;
					break;
				}
			}
		}
		
		if (room != null) 
		{
			this.addRoomJIDToUserItem(room.getMembers(), room);
			this.addRoomJIDToUserItem(room.getOwners(), room);
			this.addRoomJIDToUserItem(room.getAdmins(), room);
		}

	}
	public void addRoomJIDToUserItem(Collection<JID> jids,MUCRoom room)
	{
		for (JID jid : jids) {
			Roster roster = null;
			try {
				roster = XMPPServer.getInstance().getRosterManager().getRoster(jid.getNode());
			} catch (UserNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			if (roster != null) 
			{
				try {
					RosterItem rosterItem = roster.getRosterItem(room.getJID());
					if (rosterItem.getSubStatus() != SubType.BOTH) 
					{
						rosterItem.setSubStatus(SubType.BOTH);
						roster.updateRosterItem(rosterItem);
					}
				} catch (UserNotFoundException e) {
					RosterItem rosterItem;
					try {
						rosterItem = roster.createRosterItem(room.getJID(), false, true);
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
	public void deleteRoomJIDToUserItem(Collection<JID> jids,MUCRoom room)
	{
		for (JID jid : jids) {
			try {
				Roster roster = XMPPServer.getInstance().getRosterManager().getRoster(jid.getNode());
				try {
					roster.deleteRosterItem(room.getJID(), false);
				} catch (SharedGroupException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			} catch (UserNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}
	@Override
	public void privateMessageRecieved(JID toJID, JID fromJID, Message message) {
		// TODO Auto-generated method stub

	}

	@Override
	public void roomSubjectChanged(JID roomJID, JID user, String newSubject) {
		// TODO Auto-generated method stub

	}

}
