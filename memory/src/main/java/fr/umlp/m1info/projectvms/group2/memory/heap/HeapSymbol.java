package fr.umlp.m1info.projectvms.group2.memory.heap;

import fr.umlp.m1info.projectvms.group2.memory.symtable.Type;

/**
 * HeapSymbol
 *
 * A symbol to be used inside the heap local symbol table.
 */
class HeapSymbol {
	private int address;
	private int size;
	private int blockSize;
	private int ref;
	private Type type;

	/**
	 * Constructs a new heap symbol
	 *
	 * @param address the address of the data stored in the heap
	 * @param size the size of the data stored
	 * @param blockSize the block size (the smallest 2^n such that 2^n > size)
	 * @param type the data type stored
	 */
	HeapSymbol(int address, int size, int blockSize, Type type) {
		this.address = address;
		this.size = size;
		this.blockSize = blockSize;
		this.ref = 1;
		this.type = type;
	}

	/**
	 * @return the address of where the data is stored in the heap
	 */
	public int getAddress() {
		return address;
	}

	/**
	 * @return the stored data size
	 */
	public int getSize() {
		return size;
	}

	/**
	 * @return the size of the block that contains the data
	 * (it is usually the smallest 2^n such that 2^n > data size)
	 */
	public int getBlockSize() {
		return blockSize;
	}

	/**
	 * @return the number of references of the data stored in the heap referenced by this symbol
	 */
	public int getRef() {
		return ref;
	}

	/**
	 * @return the stored data type
	 */
	public Type getType() {
		return type;
	}

	/**
	 * Increments the amount of references of this data.
	 * This usually happens when a data stored in the heap with symbol h1
	 * gets a new reference symbol.
	 * i.e. h2 = h1; in the code
	 */
	public void incRef() {
		ref++;
	}

	/**
	 * Decrements the amount of references of this data.
	 * This usually happens when a local reference is removed at the end of a scope.
	 */
	public void decRef() {
		ref--;
	}
}
