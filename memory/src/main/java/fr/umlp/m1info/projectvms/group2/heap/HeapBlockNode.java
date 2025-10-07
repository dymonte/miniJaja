package fr.umlp.m1info.projectvms.group2.heap;

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
		}
		next.addNode(node);
	}

	// TODO: maybe add a remove method

	public int getAddress() {
		return address;
	}

}
