package fr.umlp.m1info.projectVMs.group2.symtable;

/**
 * SymNode
 */
public class SymNode {
	String ident;
	MemInfo info;
	SymNode nextSymbol;
	InstNode firstInstance;

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

	// TODO: remove symbol and search symbol and remove instance
}
