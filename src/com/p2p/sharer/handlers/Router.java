package com.p2p.sharer.handlers;

import com.p2p.core.PeerInfo;
import com.p2p.core.PeerNode;
import com.p2p.core.interfaces.RouterInterface;
/**
 * Router
 * @author axvelazq
 *
 */
public class Router implements RouterInterface {
	private PeerNode node;
	public Router(PeerNode node){
		this.node = node;
	}
	@Override
	public PeerInfo route(String peerId) {
		if(node.getPeerKeys().contains(peerId)){
			return node.getPeer(peerId);
		}else{
			return null;
		}
		
	}

}
