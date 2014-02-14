package com.p2p.sharer.handlers;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Hashtable;

import com.p2p.core.P2PMessage;
import com.p2p.core.PeerConnection;
import com.p2p.core.PeerHandler;
import com.p2p.core.PeerNode;
import com.p2p.core.util.LoggerUtil;
import com.p2p.sharer.SharerMessage;
import com.p2p.sharer.SharerNode;

/**
 * Handler for the  FGET Messages
 * FGET file-name: Request a peer to reply with the contents of the specified file.
 * @author axvelazq
 *
 */
public class FileGetHandler extends PeerHandler{

	public FileGetHandler(PeerNode node) {
		super(node);
		
	}

	@Override
	public void handleMessage(PeerConnection conn, P2PMessage message) {
		 String filename  = message.getMsgData();
		 SharerNode peer = (SharerNode)this.getInstanceNode();
		 Hashtable<String, String> files = peer.getTableFiles();
		 if(!files.containsKey(filename)){
			 conn.sendData(new P2PMessage(SharerMessage.ERROR, "FGET: FILE NOT FOUND" + filename));
		 }
		 
		 byte [] filedata = null;
		 try{
			FileInputStream inFile = new FileInputStream(filename);
			int length = inFile.available();
			filedata = new byte[length];
			inFile.read(filedata);
			inFile.close();
		 }catch(IOException ex){
			 LoggerUtil.getLogger().severe("FGET: ERROR READING FILE: " + ex);
			 conn.sendData(new P2PMessage(SharerMessage.ERROR,"FGET: ERROR READING FILE " + filename));
			 return;
		 }
		 conn.sendData(new P2PMessage(SharerMessage.REPLY,filedata));
		 

	}

}
