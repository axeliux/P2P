package com.p2p.sharer.handlers;

import java.util.Hashtable;

import com.p2p.core.P2PMessage;
import com.p2p.core.PeerConnection;
import com.p2p.core.PeerHandler;
import com.p2p.core.PeerNode;
import com.p2p.sharer.SharerMessage;
import com.p2p.sharer.SharerNode;
/**
 * Handler for the RESP
 * RESP file-name pid: Notifies a peer that the node specified by pid has a file with the given name.
 * @author axvelazq
 *
 */
public class QueryResponseHandler extends PeerHandler{

	public QueryResponseHandler(PeerNode node) {
		super(node);
	}

	@Override
	public void handleMessage(PeerConnection conn, P2PMessage message) {
		SharerNode peer = (SharerNode)this.getInstanceNode();
		Hashtable<String,String> files = peer.getTableFiles();
		
		String data [] = message.getMsgData().split("\\s");
		if(data.length != 2){
			conn.sendData(new P2PMessage(SharerMessage.ERROR,"RESP: INCORRECT ARGUMENTS"));
			return;
		}
		String fileName = data[0];
		String pid = data[1];
		
		if(files.containsKey(fileName)){
			conn.sendData(new P2PMessage(SharerMessage.ERROR,"RESP: CANNOT ADD DUPLICATE FILE " + fileName));
			return;
		}
		files.put(fileName, pid);
		conn.sendData(new P2PMessage(SharerMessage.REPLY, "RESP: FILE INFO ADDED" + fileName));
		
		
		
		
	}

}
