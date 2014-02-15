package com.p2p.sharer;

import java.util.Hashtable;
import java.util.List;

import com.p2p.core.P2PMessage;
import com.p2p.core.PeerInfo;
import com.p2p.core.PeerNode;
import com.p2p.core.util.LoggerUtil;
import com.p2p.sharer.handlers.FileGetHandler;
import com.p2p.sharer.handlers.JoinHandler;
import com.p2p.sharer.handlers.ListHandler;
import com.p2p.sharer.handlers.NameHandler;
import com.p2p.sharer.handlers.QueryHandler;
import com.p2p.sharer.handlers.QueryResponseHandler;
import com.p2p.sharer.handlers.QuitHandler;
import com.p2p.sharer.handlers.Router;
/**
 * Backend Implementation for a Sharer File Node
 * */
public class SharerNode extends PeerNode {
	private Hashtable<String,String> files;
	
	public SharerNode(int maxPeers, PeerInfo myInfo){
		super(maxPeers,myInfo);
		this.files = new Hashtable<String,String>();
		this.addRouter(new Router(this));
		this.addHandlers();
		
	}
	private void addHandlers(){
		this.addHandler(SharerMessage.INSERT_PEER,new JoinHandler(this));
		this.addHandler(SharerMessage.LIST_PEER, new ListHandler(this));
		this.addHandler(SharerMessage.PEER_NAME, new NameHandler(this));
		this.addHandler(SharerMessage.QUERY,new QueryHandler(this));
		this.addHandler(SharerMessage.QUERY_RESPONSE, new QueryResponseHandler(this));
		this.addHandler(SharerMessage.FILE_GET, new FileGetHandler(this));
		this.addHandler(SharerMessage.PEER_QUIT, new QuitHandler(this));
	}
	
	public void addLocalFile(String filename){
		if(this.files.containsKey(filename)){
			this.files.remove(filename);
		}
		this.files.put(filename, this.getId());
	}
	
	public String[] getFileNames(){
		return this.files.keySet().toArray(new String[this.files.size()]);
	}
	public String getFileOwner(String filename){
		return files.get(filename);
	}
	
	public void buildPeers(String host, int port, int hops){
		LoggerUtil.getLogger().fine("build peers");
		if(this.maxPeerReached() || hops <= 0){
			return;
		}
		
		PeerInfo currentPeer = new PeerInfo(host,port);
		List<P2PMessage> responseList = this.connectAndSend(currentPeer, SharerMessage.PEER_NAME, "", true);
		if(responseList == null || responseList.size() == 0){
			return;
		}
		
		String peerId = responseList.get(0).getMsgData();
		LoggerUtil.getLogger().fine("Connected with " + peerId);
		currentPeer.setId(peerId);
		String dataMsg = String.format("%s %s %d",this.getId(),this.getHost(),this.getPort());
		String respMsgType = this.connectAndSend(currentPeer, SharerMessage.INSERT_PEER,dataMsg,true).get(0).getMsgType();
		
		if(!respMsgType.equals(SharerMessage.REPLY) || this.getPeerKeys().contains(peerId)){
			return;
		}
		
		this.addPeer(currentPeer);
		
		//DFS to add mode peers
		
		responseList = this.connectAndSend(currentPeer, SharerMessage.LIST_PEER, "", true);
		if(responseList.size() > 1){
			responseList.remove(0);
			for(P2PMessage msg: responseList){
				String data []  = msg.getMsgData().split("\\s");
				String nextPId = data[0];
				String nextHost = data[1];
				int nextPort = Integer.parseInt(data[2]);
				if(this.getId().equals(nextPId)){
					buildPeers(nextHost, nextPort, hops-1);
				}
			}
		}
		
		
		
	}
	
	public Hashtable<String,String> getTableFiles(){
		return this.files;
	}
	
}
