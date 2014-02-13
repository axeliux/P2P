package com.p2p.core.socket;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import com.p2p.core.interfaces.SocketInterface;
/**
 * Factory for socket implementations. Used by classes of the P2P system to create sockets appropriate for regular use.
 * @author axvelazq
 *
 */
public abstract class SocketFactory {
	protected SocketFactory factory;
	
	//public abstract SocketFactory getSocketFactory();
	public abstract SocketInterface makeSocket(Socket socket)throws IOException;
	public abstract SocketInterface makeSocket(String host, int port)throws IOException, UnknownHostException;
	public static void setSocketFactory(SocketFactory factory){
		
	}
}
