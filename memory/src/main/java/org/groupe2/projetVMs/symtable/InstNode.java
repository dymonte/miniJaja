package org.groupe2.projetVMs.symtable;

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
		this.nextInStack = stackHead;
		this.nextInstance = null;
	}

	/**
	 * TODO: javadoc
	 */
	public void addInstance(InstNode instNode) {
		nextInstance = instNode;
	}

}
