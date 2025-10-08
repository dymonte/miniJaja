package fr.umlp.m1info.projectvms.group2.heap;

import java.nio.ByteBuffer;

import fr.umlp.m1info.projectvms.group2.exception.HeapInvalidAddress;
import fr.umlp.m1info.projectvms.group2.exception.HeapOutOfBounds;
import fr.umlp.m1info.projectvms.group2.symtable.Type;

/**
 * Heap
 */
public class Heap {
	public static int HEAP_SIZE = 1024; // TODO: maybe this should be passed as CLI/GUI param
	ByteBuffer heap = ByteBuffer.allocate(HEAP_SIZE);

	/**
	 * This array is of size log2(HEAP_SIZE).
	 * each index i corresponds to blocks of size 2^(i-1)
	 * for instance, addresses of blocks of size 256 are stored at index 7.
	 */
	HeapBlockNode[] blocks = new HeapBlockNode[(int) (Math.log(HEAP_SIZE) / Math.log(2))];

	static int LOCAL_TABLE_LENGTH = 32;
	HeapSymbol[] localTable = new HeapSymbol[LOCAL_TABLE_LENGTH];

	/**
	 * TODO: javadoc
	 * 
	 * @return The address in the heap local table of the newly allocated data
	 *         (note: this should then be stored as the variable value in the
	 *         stack/symtable)
	 */
	public int allocate(int size, Type type) {
		// TODO: implement this
		throw new UnsupportedOperationException();
	}

	// TODO: implement garbage collector

	/**
	 * TODO: javadoc
	 */
	public boolean free(int address) throws HeapInvalidAddress {
		// TODO: implement this
		throw new UnsupportedOperationException();
	}

	/**
	 * TODO: javadoc
	 */
	public <T> T search(int address, int index) throws HeapOutOfBounds, HeapInvalidAddress {
		// TODO: implement this
		// Note: if not found, throw exception (segfault)
		throw new UnsupportedOperationException();
	}

	/**
	 * TODO: javadoc
	 */
	public <T> boolean setValue(int address, int index, T value) throws HeapOutOfBounds, HeapInvalidAddress {
		// TODO: implement this
		throw new UnsupportedOperationException();
	}

}
