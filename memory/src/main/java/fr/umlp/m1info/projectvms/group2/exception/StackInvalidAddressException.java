package fr.umlp.m1info.projectvms.group2.exception;

/**
 * StackInvalidAddressException
 */
public class StackInvalidAddressException extends RuntimeException {

	/**
	 * Constructs a new StackInvalidAddressException
	 */
	public StackInvalidAddressException(int address) {
		super("address : " + address + "was not found in the stack.");
	}
}
