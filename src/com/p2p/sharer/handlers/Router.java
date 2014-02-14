package com.p2p.sharer.handlers;

import com.p2p.core.PeerInfo;
import com.p2p.core.PeerNode;
import com.p2p.core.interfaces.RouterInterface;

public class Router implements RouterInterface {
	private PeerNode node;
	public Router(PeerNode node){
		this.node = node;
	}
	@Override
	public PeerInfo route(String peerId) {
		// TODO Auto-generated method stub
		return null;
	}

}
