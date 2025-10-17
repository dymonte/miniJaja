package fr.umlp.m1info.projectvms.group2.exception;

/**
 * HeapOutOfMemory
 */
public class HeapOutOfMemoryException extends RuntimeException {
	public HeapOutOfMemoryException() {
		super("No more space available. Please increase heap size.");
	}
}
