package com.p2p.sharer;
/**
 * Defines the messages supported by th2 P2P Sharer Application
 * @author axvelazq
 *
 */
public class SharerMessage {

	/**
	 * JOIN pid host port: Requests a peer to add the supplied host/port combination, associated with the node identified by pid, to its list of known peers.
	 */
	public static final String INSERT_PEER = "JOIN";
	/**
	 * LIST: Requests a peer to reply with the list of peers that it knows about.
	 */
	public static final String LIST_PEER = "LIST";
	/**
	 * NAME: Requests a peer to reply with its "official" peer id.
	 */
	public static final String PEER_NAME = "NAME";
	/**
	 * QUER return-pid key ttl: Queries a peer to see if the peer has any record of a file name matching key. 
	 * If so, send a RESP message back to the node identified by return-pid; 
	 * if not, propagate the query to all known peers with a decreased ttl (time-to-live) value, unless ttl is already 0.
	 */
	public static final String QUERY = "QUER";
	/**
	 * RESP file-name pid: Notifies a peer that the node specified by pid has a file with the given name.
	 */
	public static final String QUERY_RESPONSE = "RESP";
	/**
	 * FGET file-name: Request a peer to reply with the contents of the specified file.
	 */
	public static final String FILE_GET = "FGET";
	/**
	 * QUIT pid: Indicate to a peer that the node identified by pid wishes to be unregistered from the P2P system.
	 */
	public static final String PEER_QUIT = "QUIT";
	/**
	 * REPL ...: Used to indicate an acknowledgment of the other message types above or to send back results of a successful request.
	 */
	public static final String REPLY = "REPL";
	/**
	 * ERRO msg: Used to indicate an erroneous or unsuccessful request.
	 */
	public static final String ERROR = "ERRO";
}
