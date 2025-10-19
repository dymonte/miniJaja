package fr.umlp.m1info.projectvms.group2.logger;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * A singleton logger for capturing messages from memory
 */
public class MemoryLogger {
	/**
	 * The severity level of a log message
	 */
	public enum MessageLevel {
		INFO, // General parsing info
		DEBUG, // Detailed
		WARNING, // Non-critical problems
		ERROR, // Syntax errors
	}

	/**
	 * Interface for objects that want to receive log messages from the memory
	 */
	public interface MemoryListener {
		/**
		 * Called when a message is logged
		 *
		 * @param message the text of the log message
		 * @param level   the severity level of the message
		 */
		void receiveMessage(String message, MessageLevel level);
	}

	/**
	 * Singleton Holder Pattern
	 */
	private static class Holder {
		private static final MemoryLogger INSTANCE = new MemoryLogger();
	}

	/**
	 * Gets the singleton instance of the memory logger
	 *
	 * @return the shared MemoryLogger instance
	 */
	public static MemoryLogger getInstance() {
		return Holder.INSTANCE;
	}

	private final Set<MemoryListener> listeners = Collections.newSetFromMap(new ConcurrentHashMap<>());

	/**
	 * Private constructor for singleton pattern
	 */
	private MemoryLogger() {
	}

	/**
	 * Adds a listener that will receive log message
	 *
	 * @param listener the listener to add. Ignored if null
	 */
	public void addListener(MemoryListener listener) {
		if (listener != null) {
			listeners.add(listener);
		}
	}

	/**
	 * Removes a previously registered listener
	 *
	 * @param listener the listener to remove
	 */
	public void removeListener(MemoryListener listener) {
		if (listener != null) {
			listeners.remove(listener);
		}
	}

	/**
	 * Removes all registered listeners
	 */
	public void clearListeners() {
		listeners.clear();
	}

	/**
	 * Returns an unmodifiable view of the registered listeners
	 *
	 * @retun a set of registered listeners
	 */
	public Set<MemoryListener> getListeners() {
		return Collections.unmodifiableSet(listeners);
	}

	/**
	 * Logs a message to all registered listeners
	 *
	 * @param message the log message text. Ignored if null
	 * @param level   the severity level of the message. Ignored if null
	 */
	public void log(String message, MessageLevel level) {
		if (message == null || level == null) {
			return;
		}
		for (MemoryListener listener : listeners) {
			listener.receiveMessage(message, level);
		}
	}
}
