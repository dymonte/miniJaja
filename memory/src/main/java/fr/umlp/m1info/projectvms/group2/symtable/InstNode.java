package fr.umlp.m1info.projectvms.group2.symtable;

/**
 * InstNode
 */
public class InstNode {
	Object val; // TODO: maybe change this to something like a generic type
	SymNode symbol;
	InstNode nextInStack;
	InstNode nextInstance;

	public InstNode(Object val, SymNode symbol, InstNode stackHead) {
		this.val = val;
		this.symbol = symbol;
		this.symbol.addInstance(this);
		this.nextInStack = stackHead;
		this.nextInstance = null;
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

}
