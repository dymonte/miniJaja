package fr.umlp.m1info.projectvms.group2.exception;

/**
 * StackOverflowException
 */
public class StackOverflowException extends RuntimeException {
	public StackOverflowException() {
		super("too much recursion or local variables used.");
	}
}
