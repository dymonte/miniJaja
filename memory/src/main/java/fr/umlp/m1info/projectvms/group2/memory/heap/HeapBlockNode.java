package fr.umlp.m1info.projectvms.group2.memory.heap;

/**
 * HeapBlockNode
 */
class HeapBlockNode {
	int address;
	HeapBlockNode next;

	HeapBlockNode(int address) {
		this.address = address;
		this.next = null;
	}

	/**
	 * @return the address of the heap block in the heap
	 */
	public int getAddress() {
		return address;
	}
}
