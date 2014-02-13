package com.p2p.core;
/**
 * Contains info related to the location of a peer node in the p2p network
 * along with the peers id
 * @author axvelazq
 *
 */
public class PeerInfo {
	private String id;
	private String host;
	private int port;
	
	public PeerInfo(String id, String host, int port){
		this.id = id;
		this.host = host;
		this.port = port;
	}
	
	public PeerInfo(String host, int port){
		this(host + ":" +port,host,port);
	}
	
	public PeerInfo(int port){
		this(null,port);
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String toString(){
		return id + "(" + host + ":" + port + ")";
	}
	
	
}
