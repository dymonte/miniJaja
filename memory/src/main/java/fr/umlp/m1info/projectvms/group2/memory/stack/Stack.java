package fr.umlp.m1info.projectvms.group2.memory.stack;

import fr.umlp.m1info.projectvms.group2.exception.StackOverflowException;
import fr.umlp.m1info.projectvms.group2.memory.symtable.InstNode;
import fr.umlp.m1info.projectvms.group2.memory.symtable.SymNode;

/**
 * Stack
 */
public class Stack {
	private InstNode head;
	private final int MAX_DEPTH;
	private int depth;

	/**
	 * Constructs a new stack.
	 *
	 * @param maxDepth the maximal Stack depth
	 * @throws IllegalArgumentException if maxDepth <= 0
	 */
	public Stack(int maxDepth) throws IllegalArgumentException {
		if (maxDepth <= 0) {
			throw new IllegalArgumentException("Cannot create an empty sized stack.");
		}
		this.MAX_DEPTH = maxDepth;
		this.head = null;
		this.depth = 0;
	}

	/**
	 * @return the maximum depth of the stack
	 */
	public int getMaxDepth() {
		return MAX_DEPTH;
	}

	/**
	 * Adds an element on top of the stack.
	 *
	 * @param val    the element's value to add
	 * @param symbol the element's symbol to add
	 * @throws StackOverflowException when the stack depth exceeds MAX_DEPTH
	 */
	public <T> void push(T val, SymNode symbol) throws StackOverflowException {
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

	/**
	 * Gets the head of the stack
	 * 
	 * @return the head of the stack
	 */
	public InstNode getHead() {
		return head;
	}
}
