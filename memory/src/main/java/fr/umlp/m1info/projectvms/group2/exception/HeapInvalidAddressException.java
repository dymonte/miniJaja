package fr.umlp.m1info.projectvms.group2.exception;

/**
 * HeapInvalidAddressException
 *
 * This exception is raised when the program is trying to get/set the
 * value with an address already freed or never allocated.
 *
 * This error should never occur while running a user program,
 * as the user cannot allocate and free memory manually.
 * This error could pottentially occur via an issue inside the
 * garbage collector. This exception is mainly used for debug/testing purposes.
 */
public class HeapInvalidAddressException extends RuntimeException {
	public HeapInvalidAddressException(int address) {
		super("address: " + address + " is either already freed or never allocated.");
	}
}
