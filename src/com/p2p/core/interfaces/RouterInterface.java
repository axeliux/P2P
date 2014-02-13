package com.p2p.core.interfaces;

import com.p2p.core.PeerInfo;

/**
 * Used to determine the next hoop for a message to be routed to in the p2p network
 * @author axvelazq
 *
 */
public interface RouterInterface {
	public  PeerInfo route(String peerId);
}
