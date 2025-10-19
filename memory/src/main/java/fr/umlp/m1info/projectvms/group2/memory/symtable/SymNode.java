package fr.umlp.m1info.projectvms.group2.memory.symtable;

/**
 * SymNode
 * A symbol node
 */
public class SymNode {
	private String ident;
	private MemInfo info;
	private SymNode nextSymbol;
	private InstNode firstInstance;

	/**
	 * Constructs a symbol node
	 *
	 * @param ident the symbol identifier
	 * @param info  the symbol informations
	 */
	public SymNode(String ident, MemInfo info) {
		this.ident = ident;
		this.info = info;
		nextSymbol = null;
		firstInstance = null;
	}

	/**
	 * Adds a symbol to the symbol linked list
	 *
	 * @param symNode the symbol to add at the end of the list
	 */
	public void addSymbol(SymNode symNode) {
		if (symNode.nextSymbol != null) {
			symNode.addSymbol(nextSymbol);
		} else {
			nextSymbol = symNode;
		}
	}

	/**
	 * Adds an instance at the beginning of the instance linked list
	 *
	 * @param instNode the instance node to add at the beginning of the list
	 */
	public void addInstance(InstNode instNode) {
		instNode.setNextInstance(firstInstance);
		firstInstance = instNode;
	}

	/**
	 * @return the symbol identifier
	 */
	public String getIdent() {
		return ident;
	}

	/**
	 * @return the next symbol in the symbol linked list
	 */
	public SymNode getNextSymbol() {
		return nextSymbol;
	}

	/**
	 * @return the informations of this symbol (object kind, type...) stored as a
	 *         MemInfo object
	 */
	public MemInfo getInfo() {
		return info;
	}

	/**
	 * @return the first element of the instance linked list
	 */
	public InstNode getFirstInstance() {
		return firstInstance;
	}

	/**
	 * Changes the type of the symbol.
	 * 
	 * @param type the new symbol's type
	 */
	public void setType(Type type) {
		info = new MemInfo(type, info.getKind(), info.getSize());
	}

	/**
	 * removes an instance node from the instance linked list.
	 *
	 * @param instNode the instance node to remove.
	 */
	public boolean removeInstance(InstNode instNode) {
		if (firstInstance == null) {
			return false;
		}
		if (instNode == firstInstance) {
			firstInstance = firstInstance.getNextInstance();
			return true;
		}

		return firstInstance.removeInstance(instNode);
	}

	/**
	 * Removes the next symbol in the symbol linked list
	 */
	public void removeNextSymbol() {
		nextSymbol = nextSymbol.getNextSymbol();
	}

}
