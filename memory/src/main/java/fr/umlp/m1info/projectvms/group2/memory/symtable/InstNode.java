package fr.umlp.m1info.projectvms.group2.memory.symtable;

/**
 * InstNode
 */
public class InstNode {
	private Object val;
	private SymNode symbol;
	private InstNode nextInStack;
	private InstNode nextInstance;

	public InstNode(Object val, SymNode symbol, InstNode stackHead) {
		this.val = val;
		this.symbol = symbol;
		this.symbol.addInstance(this);
		this.nextInStack = stackHead;
	}

	/**
	 * Sets the next element in the linked list
	 *
	 * @param instNode the instance node to become the next of this one in the list
	 */
	public void setNextInstance(InstNode instNode) {
		nextInstance = instNode;
	}

	/**
	 * @return the instance below this one in the stack
	 */
	public InstNode getNextInStack() {
		return nextInStack;
	}

	/**
	 * @return the next instance in the instance linked list.
	 */
	public InstNode getNextInstance() {
		return nextInstance;
	}

	/**
	 * removes an instance node in the instance linked list.
	 *
	 * @param instNode the instance node to remove
	 */
	public boolean removeInstance(InstNode instNode) {
		if (instNode == null || nextInstance == null) {
			return false;
		}

		if (nextInstance == instNode) {
			nextInstance = nextInstance.nextInstance;
			return true;
		}
		return nextInstance.removeInstance(instNode);
	}

	/**
	 * Swaps this instance node with the next one in the stack
	 */
	public void swap() {
		InstNode nextNext = nextInStack.nextInStack;
		nextInStack.nextInStack = this;
		nextInStack = nextNext;
	}

	/**
	 * @return the instance's value
	 */
	public Object getValue() {
		return val;
	}

	/**
	 * @return the instance's symbol
	 */
	public SymNode getSymbol() {
		return symbol;
	}

}
