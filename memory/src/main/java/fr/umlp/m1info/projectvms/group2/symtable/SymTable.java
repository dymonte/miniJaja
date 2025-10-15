package fr.umlp.m1info.projectvms.group2.symtable;

import java.util.Optional;

/**
 * SymTable
 */
public class SymTable {
	static int TABLE_LENGTH = 32;
	SymNode[] table = new SymNode[TABLE_LENGTH];

	/**
	 * TODO: javadoc
	 */
	private int hash(String ident) {
		// TODO: implement this
		// Note: should return a number between 0 and TABLE_LENGTH
		throw new UnsupportedOperationException();
	}

	// TODO: verify these method parameters
	/**
	 * TODO: javadoc
	 */
	public boolean add(String ident, int val, ObjectKind kind, Type type) {
		// TODO: implement this
		throw new UnsupportedOperationException();
	}

	/**
	 * TODO: javadoc
	 */
	public boolean add(String ident, boolean val, ObjectKind kind, Type type) {
		// TODO: implement this
		throw new UnsupportedOperationException();
	}

	/**
	 * TODO: javadoc
	 */
	public Optional<SymNode> search(String ident) {
		// TODO: implement this
		throw new UnsupportedOperationException();
	}

	/**
	 * TODO: javadoc
	 */
	public boolean remove(InstNode instance) {
		// TODO: implement this
		throw new UnsupportedOperationException();
	}
}
