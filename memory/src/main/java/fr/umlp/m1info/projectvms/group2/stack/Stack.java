package fr.umlp.m1info.projectvms.group2.stack;

import fr.umlp.m1info.projectvms.group2.exception.StackOverflow;
import fr.umlp.m1info.projectvms.group2.symtable.*;

/**
 * Stack
 */
public class Stack {
	InstNode head;
	// TODO: maybe parameterize this via GUI/CLI args
	public static int MAX_DEPTH = 32000;
	int depth = 0;

	/**
	 * TODO: javadoc
	 */
	public <T> void push(T val, SymNode symbol) {
		if (depth >= MAX_DEPTH) {
			throw new StackOverflow();
		}
		head = new InstNode(val, symbol, head);
		depth++;
	}

	/**
	 * TODO: javadoc
	 */
	public InstNode pop() {
		if (head == null) {
			return null;
		}
		InstNode old = head;
		head = old.getNextInStack();
		depth--;
		return old;
	}

	/**
	 * TODO: javadoc
	 */
	public void swap() {
		InstNode newHead = head.getNextInStack();
		head.swap();
		head = newHead;
	}
}
