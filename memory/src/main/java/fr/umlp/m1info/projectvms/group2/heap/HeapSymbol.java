package fr.umlp.m1info.projectvms.group2.heap;

/**
 * HeapSymbol
 */
class HeapSymbol {
	int address;
	int size;
	int ref;

	HeapSymbol(int address, int size) {
		this.address = address;
		this.size = size;
		this.ref = 0;
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

	public void incRef() {
		ref++;
	}

	public void decRef() {
		ref++;
	}

}
