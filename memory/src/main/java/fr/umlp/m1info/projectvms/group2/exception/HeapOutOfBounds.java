package fr.umlp.m1info.projectvms.group2.exception;

/**
 * HeapOutOfBounds
 *
 * This exception should possibly be occured while
 * interpreting/running a miniJaJa/jajacode program.
 *
 * It is occured when the user tries to access an element in an array
 * at an invalid index.
 */
public class HeapOutOfBounds extends RuntimeException {

	public HeapOutOfBounds(int address, int index) {
		super("" + address + "[" + index + "]" + " out of bounds.");
	}

}
