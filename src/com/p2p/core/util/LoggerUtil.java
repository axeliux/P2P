package com.p2p.core.util;

import java.util.logging.*;
/**
 * Logging Util
 * @author axvelazq
 *
 */
public class LoggerUtil {
	private static final String LOGGER = "p2p.logging";
	static{
		Logger.getLogger(LOGGER).setUseParentHandlers(false);
		Handler handler = new ConsoleHandler();
		handler.setLevel(Level.INFO);
		Logger.getLogger(LOGGER).addHandler(handler);
	}
	public static void setHandlerLevel(Level level){
		Handler[] handlers = Logger.getLogger(LOGGER).getHandlers();
		for(Handler h: handlers){
			h.setLevel(level);
		}
		Logger.getLogger(LOGGER).setLevel(level);
	}
	
	public static Logger getLogger(){
		return Logger.getLogger(LOGGER);
	}
	
}
