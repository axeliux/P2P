package com.p2p.core.interfaces;

import java.io.IOException;

/**
 * The socket interface fot the P2P system.
 * @author axvelazq
 *
 */
public interface SocketInterface {
	public void close()throws IOException;
	public int read() throws IOException;
	public int read(byte array[]) throws IOException;
	public void write(byte array[]) throws IOException;
}
