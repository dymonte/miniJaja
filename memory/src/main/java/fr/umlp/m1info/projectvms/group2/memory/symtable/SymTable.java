package fr.umlp.m1info.projectvms.group2.memory.symtable;

import fr.umlp.m1info.projectvms.group2.utils.Kind;
import fr.umlp.m1info.projectvms.group2.utils.Type;

import java.util.Optional;

/**
 * SymTable
 * The memory table of symbols
 * It stores all the program's symbols and it's related informations and
 * instances
 */
public class SymTable {
	final int tableLength;
	SymNode[] table;

	/**
	 * Constructs a new Symbol Table
	 *
	 * @param tableLength the table length
	 * @throws IllegalArgumentException if tableLength <= 0
	 */
	public SymTable(int tableLength) throws IllegalArgumentException {
		if (tableLength <= 0) {
			throw new IllegalArgumentException("The sym table length should be stricly positive.");
		}
		this.tableLength = tableLength;
		table = new SymNode[this.tableLength];
	}

	/**
	 * Hashes an identifier into an index in the sym table.
	 *
	 * @param ident the identifier
	 * @return an hash of ident, between 0 and TABLE_LENGTH, so that it can be used
	 *         as an index inside the table.
	 */
	private int hash(String ident) {
		return ident.chars().sum() % tableLength;
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
	 * @throws IllegalArgumentException is thrown when parameter size <= 0
	 */
	public Optional<SymNode> add(String ident, Kind kind, Type type, int size)
			throws IllegalArgumentException {
		int id = hash(ident);
		Optional<SymNode> n = search(ident);
		if (n.isPresent()) {
			MemInfo otherInfos = n.get().getInfo();
			if (!otherInfos.equals(new MemInfo(type, kind, size))) {
				return Optional.empty();
			}
			return n;
		}

		SymNode node = null;
		if (size <= 0) {
			throw new IllegalArgumentException("Data size should be > 0.");
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
	public Optional<SymNode> add(String ident, Kind kind, Type type) {
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
	 * Checks wether a symbol is in the table or not.
	 *
	 * @param symbol the symbol to look for
	 * @return true if found in the table, false otherwise
	 */
	private boolean hasSymbol(SymNode symbol) {
		int id = hash(symbol.getIdent());
		SymNode current = table[id];
		while (current != null) {
			if (current == symbol) {
				return true;
			}
			current = current.getNextSymbol();
		}
		return false;
	}

	/**
	 * Removes an instance node from it's symbol
	 * If no instance node remains for a symbol, it is deleted
	 * If the instance's symbol does not belong to the table, does nothing.
	 *
	 * @param instance the instance to be removed
	 * @return true if the instance was removed, false otherwise
	 */
	public boolean remove(InstNode instance) {
		if (instance == null) {
			return false;
		}

		SymNode symbol = instance.getSymbol();
		if (!hasSymbol(symbol)) {
			return false;
		}
		boolean isRemoved = symbol.removeInstance(instance);

		if (!isRemoved || symbol.getFirstInstance() != null) {
			return isRemoved;
		}
		// delete symbol node if no instances remains
		int id = hash(symbol.getIdent());
		SymNode current = table[id];
		if (current == null) {
			return isRemoved;
		}

		String ident = symbol.getIdent();

		if (current.getNextSymbol() == null && ident.equals(current.getIdent())) {
			table[id] = null;
			return isRemoved;
		}

		while (current != null) {
			SymNode next = current.getNextSymbol();
			if (next == symbol) {
				current.removeNextSymbol();
				break;
			}
			current = next;
		}

		return isRemoved;
	}
}
