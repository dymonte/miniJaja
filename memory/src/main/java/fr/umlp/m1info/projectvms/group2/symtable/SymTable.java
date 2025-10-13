package fr.umlp.m1info.projectvms.group2.symtable;

import java.util.Optional;

/**
 * SymTable
 */
public class SymTable {
	static int TABLE_LENGTH = 32;
	SymNode[] table = new SymNode[TABLE_LENGTH];

	/**
	 * Hashes an identifier into an index in the sym table.
	 *
	 * @param ident the identifier
	 * @return an hash of ident, between 0 and TABLE_LENGTH, so that it can be used
	 *         as an index inside the table.
	 */
	private int hash(String ident) {
		return ident.chars().sum() % TABLE_LENGTH;
	}

	/**
	 * Adds a symbol to the sym table and returns it.
	 * If the symbol already exists inside the table, does nothing but returns it.
	 * If the arguments are not valid, returns an empty optional.
	 * 
	 * @param ident the symbol identifier
	 * @param kind  the symbol kind
	 * @param type  the symbol type
	 * @param size  the size of the data (1 for variables, >0 for arrays...)
	 * @return an optional containing possibly the added (or already existing)
	 *         symbol
	 */
	public Optional<SymNode> add(String ident, ObjectKind kind, Type type, int size) {
		int id = hash(ident);
		Optional<SymNode> n = search(ident);
		if (n.isPresent()) {
			if (n.get().getInfo().getSize() != size) {
				return Optional.empty();
			}
			return n;
		}

		// TODO: maybe do here type checking (no void vars...)
		SymNode node = null;
		if (size < 0) {
			return Optional.empty();
		}
		node = new SymNode(ident, new MemInfo(type, kind, size));

		if (table[id] == null) {
			table[id] = node;
		} else {
			table[id].addSymbol(node);
		}
		return Optional.of(node);
	}

	/**
	 * Adds a symbol to the sym table and returns it.
	 * If the symbol already exists inside the table, does nothing but returns it.
	 * If the arguments are not valid, returns an empty optional.
	 * 
	 * @param ident the symbol identifier
	 * @param kind  the symbol kind
	 * @param type  the symbol type
	 * @return an optional containing possibly the added (or already existing)
	 *         symbol
	 */
	public Optional<SymNode> add(String ident, ObjectKind kind, Type type) {
		return add(ident, kind, type, 1);
	}

	/**
	 * Search for a symbol with an identifier being ident.
	 *
	 * @param ident the symbol identifier to search
	 * @return An optional containing possibly the symbol requested.
	 */
	public Optional<SymNode> search(String ident) {
		int id = hash(ident);
		SymNode node = table[id];
		while (node != null) {
			if (node.getIdent().equals(ident)) {
				return Optional.of(node);
			}
			node = node.getNextSymbol();
		}
		return Optional.empty();

	}

	/**
	 * TODO: javadoc
	 */
	public boolean remove(InstNode instance) {
		// TODO: implement this
		throw new UnsupportedOperationException();
	}
}
