package dk.sebsa;

import dk.sebsa.emerald.Logger;
import dk.sebsa.emerald.LoggerFactory;

/**
 * The mother of of Coal programs.
 *
 * @author Sebsa
 * @since 1.0.0-SNAPSHOT
 */
public class Coal {
	public static Coal instance;

	// Coal Settings & Info
	public static final String COAL_VERSION = "1.0.0-SNAPSHOT";
	public static boolean DEBUG = true;
	public static boolean TRACE = true;

	// Sys Info
	public static String graphicsCard = "Unkown";

	// Runtime stuff
	public static Logger logger;
//	private Application application;
//	private TaskManager taskManager;
//	private ThreadManager threadManager;

	public static void fireUp() {
		Emerald.VERBOSE_LOAD = DEBUG;

		// Logging
		LoggerFactory loggerFactory = new LoggerFactory();
		logger = loggerFactory.buildFromFile("/loggers/CoalEngineLogger.xml");
		Emerald.logSystemInfo(logger);
		Emerald.close();
	}
}
