package fr.umlp.m1info.projectvms.group2.exception;

/**
 * NoInstanceException
 *
 * This exception is thrown when wanting to retrieve an instance
 * from a symbol, but none are found.
 */
public class NoInstanceException extends RuntimeException {

	/**
	 * Constructs a NoInstanceException.
	 *
	 * @param identifier the symbol identifier
	 */
	public NoInstanceException(String identifier) {
		super(identifier + " has no instances.");
	}

}
