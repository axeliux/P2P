package com.p2p.sharer.handlers;

import com.p2p.core.P2PMessage;
import com.p2p.core.PeerConnection;
import com.p2p.core.PeerHandler;
import com.p2p.core.PeerInfo;
import com.p2p.core.PeerNode;
import com.p2p.core.util.LoggerUtil;
import com.p2p.sharer.SharerMessage;

/**
 * Handler for JOIN Messages
 * JOIN pid host port: Requests a peer to add the supplied host/port combination, associated with the node identified by pid, 
 * to its list of known peers.
 * @author axvelazq
 */
public class JoinHandler extends PeerHandler {
	
	public JoinHandler(PeerNode node){
		super(node);
	}
	@Override
	public void handleMessage(PeerConnection conn, P2PMessage message) {
		PeerNode peer = this.getInstanceNode();
		
		if(peer.maxPeerReached()){
			LoggerUtil.getLogger().fine("MAX PEERS REACHED: " + peer.getMaxPeers());
			conn.sendData(new P2PMessage(SharerMessage.ERROR, "JOIN ERROR: TOO MANY ARGUMENTS"));
		}
		
		String data [] =  message.getMsgData().split("\\s");
		
		if(data.length != 3){
			conn.sendData(new P2PMessage(SharerMessage.ERROR,"JOIN ERROR : INCORRECT ARGUMENTS"));
		}
		
		PeerInfo info = new PeerInfo(data[0],data[1],Integer.parseInt(data[2]));
		
		if(peer.getPeer(info.getId()) != null){
			conn.sendData(new P2PMessage(SharerMessage.ERROR,"JOIN ERROR: PEER ALREADY INSERTED"));
		}else if( peer.getId().equals(info.getId())){
			conn.sendData(new P2PMessage(SharerMessage.ERROR, "JOIN ERROR: ATTEMPT TO INSERT SELF"));
		}else{
			peer.addPeer(info);
			conn.sendData(new P2PMessage(SharerMessage.REPLY,"JOIN: PEER ADDED: " + info.getId()));
		}
	}

}
