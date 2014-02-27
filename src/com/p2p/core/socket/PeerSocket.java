package com.p2p.core.socket;

import java.net.Socket;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;
import java.net.UnknownHostException;

import com.p2p.core.interfaces.SocketInterface;

/**
 * PeerSocket
 * Encapsulates the standard Socket object of the Java Library 
 * to fit the Socket Interface of the P2P system.
 * @author axvelazq
 *
 */
public class PeerSocket implements SocketInterface {
	private Socket socket;
	private InputStream input;
	private OutputStream output;
	/**
	 * Encapsulates the socket object
	 * @param socket an already open socket
	 * @throws IOException
	 */
	public PeerSocket(Socket socket) throws IOException{
		this.socket = socket;
		this.input = socket.getInputStream();
		this.output = socket.getOutputStream();
	}
	public PeerSocket(String host,int port) throws IOException, UnknownHostException{
		this(new Socket(host,port));
	}
	/**
	 * Close the connection and releases any system resource associeted with the socket.
	 */
	@Override
	public void close() throws IOException {
		 
		this.socket.close();
		this.input.close();
		this.output.close();
	}
    /**
     * Reads the next byte of data from the socket connection.
     */
	@Override
	public int read() throws IOException {
		return this.input.read();
	}
   /**
    * Reads some number of bytes from the socket connection and store them into the buffer array.
    */
	@Override
	public int read(byte[] array)throws IOException {
		return this.input.read(array);
	}
	/**
	 * Writes array.length bytes from the specified byte array to this socket connection
	 */
	@Override
	public void write(byte[] array)throws IOException {
		this.output.write(array);
		this.output.flush();
	}

}
