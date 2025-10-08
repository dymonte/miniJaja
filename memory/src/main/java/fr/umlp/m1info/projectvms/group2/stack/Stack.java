package fr.umlp.m1info.projectvms.group2.stack;

import fr.umlp.m1info.projectvms.group2.symtable.*;

/**
 * Stack
 */
public class Stack {
	InstNode head;

	/**
	 * TODO: javadoc
	 */
	public <T> void push(T val, SymNode symbol) {
		head = new InstNode(val, symbol, head);
	}

	/**
	 * TODO: javadoc
	 */
	public InstNode pop() {
		InstNode old = head;
		head = old.getNextInStack();
		return old;
	}

	/**
	 * TODO: javadoc
	 */
	public void swap() {
		head.swap();
	}
}
