package com.p2p.core;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;

import com.p2p.core.interfaces.HandlerInterface;
import com.p2p.core.interfaces.RouterInterface;
import com.p2p.core.interfaces.SocketInterface;
import com.p2p.core.interfaces.StabilizerInterface;
import com.p2p.core.socket.PeerSocketFactory;
import com.p2p.core.util.LoggerUtil;
/**
 * Main class for the P2P System.
 * It holds the node information (id, host, port), a list of known peers
 * list of messages handlers and a handler for routing data through the p2p netwoek.
 * @author axvelazq
 */

public class PeerNode {
	private PeerInfo myInfo;
	private static final int SOCKET_TIMEOUT = 2000; // 2 seconds
	private int maxPeers;
	private Hashtable<String,HandlerInterface> handlers;
	private Hashtable<String,PeerInfo> peers;
	private RouterInterface router;
	private boolean shutdown;
	
	public PeerNode(int maxPeers, PeerInfo info){
		if(info.getHost() == null){
			info.setHost(getHostname());
		}
		if(info.getId() == null){
			info.setId(info.getHost() + ":" + info.getPort());
		}
		this.myInfo = info;
		this.maxPeers = maxPeers;
		this.peers = new Hashtable<String,PeerInfo>();
		this.handlers = new Hashtable<String,HandlerInterface>();
		this.router = null;
		this.shutdown = false;
	}
	public PeerNode(int port){
		this(0,new PeerInfo(port));
	}
	/*
	 * Attempt to determine the name or IP address of the machine
	 * this node is running on.
	 */
	private String getHostname() {
		String host = "";
		Socket s = null;
		try {
			 s = new Socket("www.google.com", 80);
			host = s.getLocalAddress().getHostAddress();
		
		}
		catch (UnknownHostException e) {
			LoggerUtil.getLogger().warning("Could not determine host: " + e);
		}
		catch (IOException e) {
			LoggerUtil.getLogger().warning(e.toString());
		}finally{
			if(s!= null){
				if(!s.isClosed()){
					try{
					s.close();
					}catch(IOException ex){
						LoggerUtil.getLogger().severe(ex.getMessage());
					}
				}
			}
		}
		
		LoggerUtil.getLogger().config("Determined host: " + host);
		return host;
	}
    public ServerSocket makeServerSocket(int port)throws IOException{
    	return makeServerSocket(port,5);
    }
    public ServerSocket makeServerSocket(int port,int backlog)throws IOException{
    	ServerSocket server = new ServerSocket(port,backlog);
    	server.setReuseAddress(true);
    	return server;
    }
    
    /**
     * Attempts to route and send a message to the specified peer
     * This method using the Node's routing function to decide the next immediate peer to
     * actually send the message to, based on the peer id of the final destination.
     * if no router func ( obj ) has been registered, it will not work.
     * @param peerId the destination peer
     * @param type of message
     * @param data message
     * @param waitReply whether to wait for replies
     * @return
     */
    public List<P2PMessage> sendToPeer(String peerId,String type,String data, boolean waitReply){
    	PeerInfo info = null;
    	if(this.router != null)
    	{
    		info = this.router.route(peerId);
    		
    	}
    	if(info == null){
    		LoggerUtil.getLogger().severe(String.format("Unable to route %s to %s ",type,peerId));
    		return new ArrayList<P2PMessage>();
    	}
    	return connectAndSend(info,type,data,waitReply);
    }
    /**
     * Connects to the specified peer node to send a message, optionally waiting and returning all the replies
     * @param info the peer info
     * @param type of the message being sent
     * @param data the message data
     * @param waitReply whether to wait for reply(ies)
     * @return the list of replies, empty if something went wrong
     */
    public List<P2PMessage> connectAndSend(PeerInfo info, String type, String data, boolean waitReply){
    	List<P2PMessage> msgreply = new ArrayList<P2PMessage>();
    	try{
    		PeerConnection connection = new PeerConnection(info);
    		P2PMessage message = new P2PMessage(type, data);
    		connection.sendData(message);
    		LoggerUtil.getLogger().info("Sent to: " + message + "/" + connection);
    		if(waitReply){
    			P2PMessage oneReply = connection.recvData();
    			while(oneReply != null){
    				msgreply.add(oneReply);
    				LoggerUtil.getLogger().info("Got Reply: " + oneReply);
    				oneReply = connection.recvData();
    			}
    			
    		}
    		connection.close();
    	}catch(IOException ex){
    		LoggerUtil.getLogger().severe("Error: " + ex.getMessage());
    	}
    	return msgreply;
    }
    /**
     * Starts the main loop which is the primary operation of the Peer Node
     * The main loop opens a server Socket, listens for incoming connections
     * and dispatches then to register handlers appropriately
     */
    public void mainLoop(){
    	try{
    		ServerSocket socket = makeServerSocket(myInfo.getPort());
    		socket.setSoTimeout(SOCKET_TIMEOUT);
    		while(!this.shutdown){
    			LoggerUtil.getLogger().fine("Listening...");
    			try{
    				Socket client = socket.accept();
    				client.setSoTimeout(0);
    				
    				PeerHandler handler = new PeerHandler(client);
    				handler.start();
    			}catch(SocketTimeoutException te){
    				LoggerUtil.getLogger().fine("" + te);
    				continue;
    			}
    		}
    		socket.close();
    	}catch(SocketException se){
    		LoggerUtil.getLogger().severe("Stopping main loop (SocketExc): " + se);
    	}catch(IOException ioe){
    		LoggerUtil.getLogger().severe("Stopping main loop (IOExc): " + ioe);
    	}
    	this.shutdown = true;
    	
    }
    public void startStabilizer(StabilizerInterface st, int delay){
    	StabilizerRunner stabilizer = new StabilizerRunner(st,delay);
    	stabilizer.start();
    }
    
    public void addHandler(String type, HandlerInterface handler){
    	handlers.put(type, handler);
    }
    public void addRouter(RouterInterface router){
    	this.router = router;
    }
    public boolean addPeer(PeerInfo pi){
    	return addPeer(pi.getId(),pi);
    }
    public boolean addPeer(String key, PeerInfo peerInfo){
    	if((maxPeers == 0 || peers.size() < maxPeers) && !peers.containsKey(key)){
    		peers.put(key, peerInfo);
    		return true;
    	}
    	return false;
    }
    public PeerInfo getPeer(String key){
    	return peers.get(key);
    }
    public PeerInfo removePeer(String key){
    	return peers.remove(key);
    }
    public Set<String> getPeerKeys(){
    	return peers.keySet();
    }
    public int getNumberPeers(){
    	return peers.size();
    }
    public int getMaxPeers(){
    	return this.maxPeers;
    }
    public boolean maxPeerReached(){
    	return maxPeers > 0 && peers.size() == maxPeers;
    }
    public String getInfo(){
    	return myInfo.getId();
    }
    public String getHost(){
    	return myInfo.getHost();
    }
    public int getPort(){
    	return myInfo.getPort();
    }
    public String getId(){
    	return myInfo.getId();
    }
    
    private class PeerHandler extends Thread{
    	private SocketInterface socket;
    	public PeerHandler(Socket s) throws IOException{
    		socket = PeerSocketFactory.getSocketFactory().makeSocket(s);
    	}
    	public void run(){
    		LoggerUtil.getLogger().fine("New Peer Handler");
    		PeerConnection connection = new PeerConnection(null,socket);
    		P2PMessage msg =connection.recvData();
    		if(!handlers.containsKey(msg.getMsgType())){
    			LoggerUtil.getLogger().fine("Not handled: " + msg);
    		}else{
    			LoggerUtil.getLogger().finer("Handling: " + msg);
    			handlers.get(msg.getMsgType()).handleMessage(connection, msg);
    		}
    		LoggerUtil.getLogger().fine("Disconnecting incoming: " + connection);
    		connection.close();
    		
    	}
    }
    
    private class StabilizerRunner extends Thread{
    	private StabilizerInterface stabilizer;
    	private int delay;
    	public StabilizerRunner(StabilizerInterface st,int delay){
    		this.stabilizer = st;
    		this.delay = delay;
    	}
    	
    	public void run(){
    		while(true){
    			this.stabilizer.stabilize();
    			try{
    				Thread.sleep(delay);
    			}catch(InterruptedException e){
    				LoggerUtil.getLogger().fine(""+e);
    			}
    		}
    	}
    }
}
