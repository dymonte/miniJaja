package fr.umlp.m1info.projectvms.group2.memory.stack;

import fr.umlp.m1info.projectvms.group2.exception.StackOverflowException;
import fr.umlp.m1info.projectvms.group2.memory.symtable.*;

/**
 * Stack
 */
public class Stack {
	private InstNode head;
	private final int MAX_DEPTH;
	private int depth;

	public Stack(int maxDepth) {
		if (maxDepth <= 0) {
			throw new IllegalArgumentException("Cannot create an empty sized stack.");
		}
		this.MAX_DEPTH = maxDepth;
		this.head = null;
		this.depth = 0;
	}

	public int getMaxDepth() {
		return MAX_DEPTH;
	}

	/**
	 * Adds an element on top of the stack.
	 *
	 * @param val    the element's value to add
	 * @param symbol the element's symbol to add
	 */
	public <T> void push(T val, SymNode symbol) {
		if (depth >= MAX_DEPTH) {
			throw new StackOverflowException();
		}
		head = new InstNode(val, symbol, head);
		depth++;
	}

	/**
	 * Removes the element on top of the stack and returns it.
	 *
	 * @returns the InstNode on top of the stack or null if the stack is empty.
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
	 * Swaps the element on top of the stack with the element below it.
	 */
	public void swap() {
		InstNode newHead = head.getNextInStack();
		head.swap();
		head = newHead;
	}
}
