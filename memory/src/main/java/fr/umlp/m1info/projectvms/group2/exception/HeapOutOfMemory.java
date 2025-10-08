package fr.umlp.m1info.projectvms.group2.exception;

/**
 * HeapOutOfMemory
 */
public class HeapOutOfMemory extends RuntimeException {
	public HeapOutOfMemory() {
		super("No more space available. Please increase heap size.");
	}
}
