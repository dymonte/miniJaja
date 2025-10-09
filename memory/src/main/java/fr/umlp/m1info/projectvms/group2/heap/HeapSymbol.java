package fr.umlp.m1info.projectvms.group2.heap;

import fr.umlp.m1info.projectvms.group2.symtable.Type;

/**
 * HeapSymbol
 */
class HeapSymbol {
	int address;
	int size;
	int ref;
	Type type;

	HeapSymbol(int address, int size, Type type) {
		this.address = address;
		this.size = size;
		this.ref = 0;
		this.type = type;
	}

	public int getAddress() {
		return address;
	}

	public int getSize() {
		return size;
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
		ref++;
	}
}
