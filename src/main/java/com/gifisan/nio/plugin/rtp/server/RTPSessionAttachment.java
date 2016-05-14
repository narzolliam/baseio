package com.gifisan.nio.plugin.rtp.server;

import com.gifisan.nio.Attachment;

public class RTPSessionAttachment implements Attachment {

	private RTPContext	context	= null;

	private RTPRoom	rtpRoom	= null;

	public RTPRoom getRtpRoom() {
		return rtpRoom;
	}

	public RTPRoom createRTPRoom() {
		if (rtpRoom == null) {
			
			rtpRoom = new RTPRoom(context);
			
			RTPRoomFactory factory = context.getRTPRoomFactory();
			
			factory.putRTPRoom(rtpRoom);
		}
		return rtpRoom;
	}

	protected RTPSessionAttachment(RTPContext context) {
		this.context = context;
	}
	
	protected void setRTPRoom(RTPRoom room){
		this.rtpRoom = room;
	}

}