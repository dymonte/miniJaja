package fr.umlp.m1info.projectvms.group2.memory.symtable;

/**
 * SymNode
 */
public class SymNode {
	private String ident;
	private MemInfo info;
	private SymNode nextSymbol;
	private InstNode firstInstance;

	public SymNode(String ident, MemInfo info) {
		this.ident = ident;
		this.info = info;
		nextSymbol = null;
		firstInstance = null;
	}

	/**
	 * TODO: javadoc
	 */
	public void addSymbol(SymNode symNode) {
		if (symNode.nextSymbol != null) {
			symNode.addSymbol(nextSymbol);
		} else {
			nextSymbol = symNode;
		}
	}

	/**
	 * TODO: javadoc
	 */
	public void addInstance(InstNode instNode) {
		instNode.addInstance(firstInstance);
		firstInstance = instNode;
	}

	public String getIdent() {
		return ident;
	}

	public SymNode getNextSymbol() {
		return nextSymbol;
	}

	public MemInfo getInfo() {
		return info;
	}

	public InstNode getFirstInstance() {
		return firstInstance;
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
