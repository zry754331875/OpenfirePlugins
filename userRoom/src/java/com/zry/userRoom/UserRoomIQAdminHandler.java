package com.zry.userRoom;

import org.jivesoftware.openfire.IQHandlerInfo;
import org.jivesoftware.openfire.auth.UnauthorizedException;
import org.jivesoftware.openfire.handler.IQHandler;
import org.xmpp.packet.IQ;

public class UserRoomIQAdminHandler extends IQHandler {

	private static final String MODULE_NAME = "UserRoom plugin";  
    private static final String NAME_SPACE = "http://jabber.org/protocol/muc#admin";  
    private IQHandlerInfo info;
    
	public UserRoomIQAdminHandler() {
		super(MODULE_NAME);  
        info = new IQHandlerInfo("query", NAME_SPACE);
	}

	@Override
	public IQ handleIQ(IQ packet) throws UnauthorizedException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IQHandlerInfo getInfo() {
		// TODO Auto-generated method stub
		return info;
	}

}
