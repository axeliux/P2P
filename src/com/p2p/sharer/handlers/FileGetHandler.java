package com.p2p.sharer.handlers;

import com.p2p.core.P2PMessage;
import com.p2p.core.PeerConnection;
import com.p2p.core.PeerHandler;
import com.p2p.core.PeerNode;


public class FileGetHandler extends PeerHandler{

	public FileGetHandler(PeerNode node) {
		super(node);
		
	}

	@Override
	public void handleMessage(PeerConnection conn, P2PMessage message) {
		// TODO Auto-generated method stub

	}

}
