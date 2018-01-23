package com.zry.userRoom;



import java.util.ArrayList;
import java.util.List;

import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.jivesoftware.openfire.IQHandlerInfo;
import org.jivesoftware.openfire.XMPPServer;
import org.jivesoftware.openfire.auth.UnauthorizedException;
import org.jivesoftware.openfire.handler.IQHandler;
import org.jivesoftware.openfire.muc.MUCRoom;
import org.jivesoftware.openfire.muc.MultiUserChatService;
import org.jivesoftware.openfire.muc.MUCRole.Affiliation;
import org.xmpp.packet.IQ;
import org.xmpp.packet.IQ.Type;
import org.xmpp.packet.JID;
import org.xmpp.packet.PacketError;


public class UserRoomIQHandler extends IQHandler {

	private static final String MODULE_NAME = "UserRoom plugin";  
    private static final String NAME_SPACE = "com:zry:UserRoomPlugin";  
    private IQHandlerInfo info;
    
    public UserRoomIQHandler(){
    		super(MODULE_NAME);  
        info = new IQHandlerInfo("query", NAME_SPACE);
    }

	@Override
	public IQ handleIQ(IQ packet) throws UnauthorizedException {
		IQ reply = IQ.createResultIQ(packet);
		
		if (!packet.getType().equals(Type.get)) 
		{
			reply.setChildElement(packet.getChildElement().createCopy());
			reply.setError(PacketError.Condition.bad_request);
			
            return reply;
		}
		
		List<JID> jidList = this.getUserRooms(packet.getFrom());
		Element query = packet.getChildElement().createCopy();
		for (JID jid : jidList) {
			Element item = DocumentHelper.createDocument().addElement("item");
			item.setText(jid.toString());
			query.add(item);
		}
		
		reply.setChildElement(query);

		return reply;
	}

	@Override
	public IQHandlerInfo getInfo() {
		// TODO Auto-generated method stub
		return info;
	}
	
	public List<JID> getUserRooms(JID user) {
		List<JID> jidList = new ArrayList<JID>();
		
		List<MultiUserChatService> servers = XMPPServer.getInstance().getMultiUserChatManager()
				.getMultiUserChatServices();

		for (MultiUserChatService multiUserChatService : servers) {
			List<MUCRoom> rooms = multiUserChatService.getChatRooms();

			for (MUCRoom mucRoom : rooms) {
				JID bareJID = user.asBareJID();
				
				Affiliation affiliation = mucRoom.getAffiliation(bareJID);
				
				if (affiliation != Affiliation.none && affiliation != Affiliation.outcast) {
					jidList.add(mucRoom.getJID());
				}
			}
		}
		
		return jidList;
	}
}
