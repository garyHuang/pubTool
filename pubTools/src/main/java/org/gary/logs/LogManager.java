package org.gary.logs;

import org.apache.log4j.Logger;
/**
 * 写日志接口
 * */
public class LogManager {
	static Logger logger = Logger.getLogger(LogManager.class);
	public static void info(Object msg) {
		logger.info(msg);
	}
	public static void err(String msg, Throwable e) {
		
		logger.error(msg, e);
	}

	public static void err(Object msg) {
		logger.info(msg);
		if (msg == null) {
			return;
		}
		if (msg instanceof Throwable) {
			Throwable t = (Throwable) msg;
			logger.error("", t);
		}
	}

}
