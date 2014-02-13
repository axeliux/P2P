package com.p2p.core.interfaces;

import com.p2p.core.P2PMessage;
import com.p2p.core.PeerConnection;

/**
 * Handles a new incomming connection from a peer.
 * @author axvelazq
 *
 */
public interface HandlerInterface {
	public void handleMessage(PeerConnection conn,P2PMessage message);
}
