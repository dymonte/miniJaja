package fr.umlp.m1info.projectvms.group2.exception;

/**
 * StackOverflow
 */
public class StackOverflow extends RuntimeException {
	public StackOverflow() {
		super("too much recursion or local variables used.");
	}
}
