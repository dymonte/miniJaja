package fr.umlp.m1info.projectvms.group2.memory;

import fr.umlp.m1info.projectvms.group2.memory.symtable.InstNode;
import fr.umlp.m1info.projectvms.group2.memory.symtable.ObjectKind;
import fr.umlp.m1info.projectvms.group2.memory.symtable.SymNode;
import fr.umlp.m1info.projectvms.group2.memory.symtable.Type;

/**
 * Quad
 * 
 * A quadruple representing a memory instance
 */
public class Quad {
	private String ident = "";
	private Object val = null;
	private ObjectKind kind = null;
	private Type sorte = null;
	private InstNode ref = null;

	/**
	 * Constructs a Quad from an InstNode
	 * 
	 * @param instance the instance node to construct the Quad from
	 */
	public Quad(InstNode instance) {
		if (instance == null) {
			return;
		}
		SymNode symbol = instance.getSymbol();

		this.ref = instance;
		this.val = instance.getValue();
		this.ident = symbol.getIdent();
		this.kind = symbol.getInfo().getKind();
		this.sorte = symbol.getInfo().getSorte();

	}

	/**
	 * Gets the symbol identifier
	 * 
	 * @return the symbol identifier
	 */
	public String getIdent() {
		return ident;
	}

	/**
	 * Gets the value
	 * 
	 * @return the value
	 */
	public Object getVal() {
		return val;
	}

	/**
	 * Gets the object kind
	 * 
	 * @return the object kind
	 */
	public ObjectKind getKind() {
		return kind;
	}

	/**
	 * Gets the type
	 * 
	 * @return the type
	 */
	public Type getSorte() {
		return sorte;
	}

	/**
	 * Gets the next Quad in the stack
	 * 
	 * @return the next Quad in the stack
	 */
	public Quad next() {
		return new Quad(ref.getNextInStack());
	}

	/**
	 * Know if the quad is empty
	 *
	 * @return if the quad is empty
	 */
	public boolean isEmpty() {
		return ref == null;
	}

	/**
	 * Gets the string representation of the Quad
	 * 
	 * @return the string representation of the Quad
	 */
	@Override
	public String toString() {
		return String.format("< %s, %s, %s, %s >", ident, val.toString(), kind.toString(), sorte.toString());
	}
}
