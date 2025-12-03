package fr.umlp.m1info.projectvms.group2.ast.jajacode.var;

import fr.umlp.m1info.projectvms.group2.ast.jajacode.JajaCodeNode;
import fr.umlp.m1info.projectvms.group2.utils.Type;

public class VarType extends JajaCodeNode {
	private final Type type;

	public VarType(int lineId, Type type) {
		super(lineId);
		if (type == null)
			throw new NullPointerException("type cannot be null");
		this.type = type;
	}

	/**
	 * @return the type of an variable
	 */
	public Type getType() {
		return type;
	}

	/**
	 * @return the string representation of the type
	 */
	@Override
	public String toString() {
		return type.toString();
	}
}
