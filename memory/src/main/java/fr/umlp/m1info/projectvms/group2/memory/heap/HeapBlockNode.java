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
	 * adds a block node at the end of the linked list
	 * 
	 * @param node the node to add
	 */
	public void addNode(HeapBlockNode node) {
		if (next == null) {
			next = node;
		} else {
			next.addNode(node);
		}
	}

	/**
	 * @return the address of the heap block in the heap
	 */
	public int getAddress() {
		return address;
	}
}
