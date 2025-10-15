package fr.umlp.m1info.projectvms.group2.memory.symtable;

/**
 * InstNode
 */
public class InstNode {
	private Object val; // TODO: maybe change this to something like a generic type
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
	 * TODO: javadoc
	 */
	public void addInstance(InstNode instNode) {
		nextInstance = instNode;
	}

	/**
	 * TODO: javadoc
	 */
	public InstNode getNextInStack() {
		return nextInStack;
	}

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
	 * TODO: javadoc
	 */
	public void swap() {
		InstNode nextNext = nextInStack.nextInStack;
		nextInStack.nextInStack = this;
		nextInStack = nextNext;
	}

	/**
	 * TODO: javadoc
	 * NOTE: maybe replace Object with <T>
	 */
	public Object getValue() {
		return val;
	}

	public SymNode getSymbol() {
		return symbol;
	}

}
