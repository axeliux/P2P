package com.p2p.sharer.handlers;

import com.p2p.core.P2PMessage;
import com.p2p.core.PeerConnection;
import com.p2p.core.PeerHandler;
import com.p2p.core.PeerNode;
import com.p2p.sharer.QueryEngine;
import com.p2p.sharer.SharerMessage;
import com.p2p.sharer.SharerNode;

public class QueryHandler extends PeerHandler {

	public QueryHandler(PeerNode node) {
		super(node);
	}

	@Override
	public void handleMessage(PeerConnection conn, P2PMessage message) {
		PeerNode peer = this.getInstanceNode();
		String data[] =  message.getMsgData().split("\\s");
		if(data.length != 3){
			conn.sendData(new P2PMessage(SharerMessage.ERROR,"QUERY: INCORRECT ARGUMENTS"));
			return;
		}
		
		String ret_id = data[0];
		String key = data[1];
		int ttl = Integer.parseInt(data[2]);
		// SEND ACK AND CLOSE CONNECTION.  AFTER THAT A NEW THREAD WILL TAKE CARE OF THE QUERY
		conn.sendData(new P2PMessage(SharerMessage.REPLY, "QUERY: ACK"));
		
		QueryEngine query = new QueryEngine((SharerNode)peer, ret_id, key, ttl);
		query.start();
		
	}

}
