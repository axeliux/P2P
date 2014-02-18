Peet to Peer Framework & Sharer App
=======================================
@author Axel Velazquez

Expected finish time : March 15th

com.p2p.core
============
The clasess contained at this level contains the base implementation for a Peer 2 Peer Netowork System.
It defines some of the most importat  classes as:
  - Peer Socket, encapsulate the base form of a Socket Object
  - PeerNode, defines a peer machine
  - P2PMessages, defines the ,message packet that will be sent back and forth between PeerNodes
  - PeerHandler, is the base implementation for each handler for each Message supported
  - PeerInfo, holds the basic information about each Peer
  
com.p2p.sharer
==============
The clases contained at this level contains the backend implementation for a File Sharer P2P implementation. 
Which uses the p2p.core as the base basic form of communication.

com.p2p.handlers
=================

Implements all the handlers where It defines how each message will be handle.  There will be One Handler class for each
message sopported.


Communication Protocol
=========================

The following lines explain the message that will be supported for the P2P System.  

REPL ...: Used to indicate an ACK of the other message types above or to send back results of a successful request.
ERRO msg: Used to notify an erroneous or unsuccessful request.

NAME: Requests a peer to reply with its official peer id.
LIST: Requests a peer to reply with the list of peers that it knows about.
JOIN pid host port: Requests a peer to add the supplied host/port combination, associated with the node identified by pid, to its list of known peers.
QUER return-pid key ttl: Queries a peer to see if the peer has any record of a file name matching key. If so, send a RESP message back to the node identified by return-pid; if not, propagate the query to all known peers with a decreased ttl (time-to-live) value, unless ttl is already 0.
RESP file-name pid: Notifies a peer that the node specified by pid has a file with the given name.
FGET file-name: Request a peer to reply with the contents of the specified file.
QUIT pid: Indicate to a peer that the node identified by pid wishes to be unregistered from the P2P system.
