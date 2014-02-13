package com.p2p.core;


import java.io.IOException;

import com.p2p.core.interfaces.SocketInterface;

/**
 * Represents a message for the P2P system.  composed of type and data field.
 * @author axvelazq
 *
 */
public class P2PMessage {
	private byte[] type;
	private byte[] data;
	
	public P2PMessage(byte type [], byte data []){
		this.type  = (byte []) type.clone();
		this.data = (byte []) data.clone();
	}
	
	public P2PMessage(String type, String data){
		this(type.getBytes(),data.getBytes());
	}
	
	public P2PMessage(String type, byte data []){
		this(type.getBytes(),data);
	}
	
	public P2PMessage(SocketInterface socket)throws IOException{
		this.type = new byte[4];
		byte [] len = new byte[4];
		if(socket.read(type) != 4){
			throw new IOException("Type Exception");
		}
		if(socket.read(len) != 4){
			throw new IOException("Length Exception");
		}
		int length =  byteArrayToInt(len);
		this.data = new byte[length];
		
		if(socket.read(data) != length){
			throw new IOException("Data Exception");
		}
	}
	
	public byte[] getMsgTypeBytes(){
		return (byte[])type.clone();
	}
	public byte[] getMsgDataBytes(){
		return (byte [])data.clone();
	}
	public String getMsgData(){
		return new String(data);
	}
	public String getMsgType(){
		return new String(type);
	}
	
	/**
	 * Returns a packed representation of this message as an
	 * array of bytes.
	 * @return byte array of message data
	 */
	public byte[] toBytes() {
		byte[] bytes = new byte[4 + 4 + data.length];
		byte[] lenbytes = intToByteArray(data.length);
		
		for (int i=0; i<4; i++) bytes[i] = type[i];
		for (int i=0; i<4; i++) bytes[i+4] = lenbytes[i];
		for (int i=0; i<data.length; i++) bytes[i+8] = data[i];
		
		return bytes;
	}
	
	
	public String toString() {
		return "P2PMessage[" + getMsgType() + ":" + getMsgData() + "]";
	}
	/**
	 * Returns a byte array containing the two's-complement representation of the integer.<br>
	 * The byte array will be in big-endian byte-order with a fixes length of 4
	 * (the least significant byte is in the 4th element).<br>
	 * <br>
	 * <b>Example:</b><br>
	 * <code>intToByteArray(258)</code> will return { 0, 0, 1, 2 },<br>
	 * <code>BigInteger.valueOf(258).toByteArray()</code> returns { 1, 2 }. 
	 * @param integer The integer to be converted.
	 * @return The byte array of length 4.
	 */
	public static byte[] intToByteArray (final int integer) {
		int byteNum = (40 - Integer.numberOfLeadingZeros (integer < 0 ? ~integer : integer)) / 8;
		byte[] byteArray = new byte[4];
		
		for (int n = 0; n < byteNum; n++)
			byteArray[3 - n] = (byte) (integer >>> (n * 8));
		
		return (byteArray);
	}
	

	public static int byteArrayToInt(byte[] byteArray) {
		int integer = 0;
		for (int n = 0; n < 4; n++) {
			integer = (integer << 8) | ( ((int)byteArray[n]) & 0xff );
		}
						  
		return integer;
	}
}
