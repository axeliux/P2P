package com.p2p.core;

import com.p2p.core.interfaces.HandlerInterface;

public abstract class PeerHandler implements HandlerInterface {

	private PeerNode node;
	public PeerHandler(PeerNode node){
		this.node = node;
	}
	protected PeerNode getInstanceNode(){
		return this.node;
	}
	public abstract void handleMessage(PeerConnection conn, P2PMessage message);

}
