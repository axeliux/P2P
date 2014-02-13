package com.p2p.core;

import java.io.IOException;
import java.net.UnknownHostException;

import com.p2p.core.interfaces.SocketInterface;
import com.p2p.core.socket.PeerSocketFactory;
import com.p2p.core.socket.SocketFactory;
import com.p2p.core.util.LoggerUtil;

/**
 * Encapsulates a socket connection to a peer node, providing simple, reliable send and receive functionality
 * All data sent to a peer through this class must be 
 * formatted as a P2PMessage object
 * @author axvelazq
 *
 */
public class PeerConnection {
	private PeerInfo peerInfo;
	private SocketInterface socket;
	
	 public PeerConnection(PeerInfo info) throws IOException,UnknownHostException{
		 this.peerInfo = info;
		 this.socket = PeerSocketFactory.getSocketFactory().makeSocket(info.getHost(),info.getPort());
	 }
	 
	 public PeerConnection(PeerInfo info, SocketInterface socket){
		 this.peerInfo = info;
		 this.socket = socket;
	 }
	 
	 public void sendData(P2PMessage message){
		 try{
			 socket.write(message.toBytes());
		 }catch(IOException ex){
			 LoggerUtil.getLogger().warning("Error sending data: " + ex.getMessage());
		 }
	 }
	 public P2PMessage recvData(){
		 try{
			 P2PMessage message = new P2PMessage(socket);
			 return message;
		 }catch(IOException ex){
			 LoggerUtil.getLogger().warning("Error to recv Data: " + ex.getMessage());
			 return null;
		 }
	 }
	 /**
	  * Close the peer Connection
	  */
	 public void close(){
		 if(socket != null){
			 try{
				 socket.close();
			 }catch(IOException ex){
				 LoggerUtil.getLogger().warning("Error Closing: " + ex.getMessage());
			 }
			 socket = null;
		 }
	 }
	 
	 public PeerInfo getPeerInfo(){
		 return this.peerInfo;
	 }
	 
	 public String toString(){
		 return "PeerConnection[" + this.peerInfo + "]";
	 }
	 
}
