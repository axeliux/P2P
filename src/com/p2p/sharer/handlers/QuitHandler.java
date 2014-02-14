package com.p2p.sharer.handlers;

import com.p2p.core.P2PMessage;
import com.p2p.core.PeerConnection;
import com.p2p.core.PeerHandler;
import com.p2p.core.PeerNode;
import com.p2p.sharer.SharerMessage;

/**
 * HANDLER FOR QUIT MESSAGES
 * QUIT pid: Indicate to a peer that the node identified by pid wishes to be unregistered from the P2P system.
 * @author axvelazq
 *
 */
public class QuitHandler extends PeerHandler{

	public QuitHandler(PeerNode node) {
		super(node);
	}

	@Override
	public void handleMessage(PeerConnection conn, P2PMessage message) {
		PeerNode peer = this.getInstanceNode();
		String pid = message.getMsgData();
		
		if(peer.getPeer(pid) == null){
			conn.sendData(new P2PMessage(SharerMessage.ERROR,"QUIT: PEER NOT FOUND"));
		}else{
			peer.removePeer(pid);
			conn.sendData(new P2PMessage(SharerMessage.REPLY,"QUIT: PEER REMOVED: " + pid));
		}
	}

}
