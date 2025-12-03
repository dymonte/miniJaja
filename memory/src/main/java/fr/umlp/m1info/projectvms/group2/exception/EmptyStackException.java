package fr.umlp.m1info.projectvms.group2.exception;

/**
 * EmptyStackException
 *
 * This exception is thrown when trying to pop
 * when the stack is empty in jajacode interpretation.
 */
public class EmptyStackException extends RuntimeException {

	/**
	 * Constructs an Empty Stack Exception
	 */
	public EmptyStackException() {
		super("Stack is empty.");
	}
}
