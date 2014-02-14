package com.p2p.sharer.handlers;

import com.p2p.core.P2PMessage;
import com.p2p.core.PeerConnection;
import com.p2p.core.PeerHandler;
import com.p2p.core.PeerNode;
import com.p2p.sharer.SharerMessage;

/**
 * Handler for LIST Messages
 * LIST: Requests a peer to reply with the list of peers that it knows about.
 * @author axvelazq
 */
public class ListHandler extends PeerHandler {

	public ListHandler(PeerNode node) {
		super(node);
	}

	@Override
	public void handleMessage(PeerConnection conn, P2PMessage message) {
		PeerNode peer = this.getInstanceNode();
		conn.sendData(new P2PMessage(SharerMessage.REPLY,String.format("%d",peer.getNumberPeers())));
		for(String pid : peer.getPeerKeys()){
			String host = peer.getPeer(pid).getHost();
			int port = peer.getPeer(pid).getPort();
			conn.sendData(new P2PMessage(SharerMessage.REPLY,String.format("%s %s %d",pid,host,port)));
		}
		
	}

}
