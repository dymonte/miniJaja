package fr.umlp.m1info.projectvms.group2.memory.heap;

import fr.umlp.m1info.projectvms.group2.memory.symtable.Type;

/**
 * HeapSymbol
 */
class HeapSymbol {
	int address;
	int size;
	int blockSize;
	int ref;
	Type type;

	HeapSymbol(int address, int size, int blockSize, Type type) {
		this.address = address;
		this.size = size;
		this.blockSize = blockSize;
		this.ref = 1;
		this.type = type;
	}

	public int getAddress() {
		return address;
	}

	public int getSize() {
		return size;
	}

	public int getBlockSize() {
		return blockSize;
	}

	public int getRef() {
		return ref;
	}

	public Type getType() {
		return type;
	}

	public void incRef() {
		ref++;
	}

	public void decRef() {
		ref--;
	}
}
