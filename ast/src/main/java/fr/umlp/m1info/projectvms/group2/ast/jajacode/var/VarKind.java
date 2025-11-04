package fr.umlp.m1info.projectvms.group2.ast.jajacode.var;

import fr.umlp.m1info.projectvms.group2.ast.jajacode.JajaCodeNode;
import fr.umlp.m1info.projectvms.group2.ast.jajacode.utils.Kind;

public class VarKind extends JajaCodeNode {
	private final Kind kind;

	public VarKind(int lineId, Kind kind) {
		super(lineId);
		this.kind = kind;
	}

	/**
	 * @return the kind of an variable
	 */
	public Kind getKind() {
		return kind;
	}

	/**
	 * @return the string representation of the type
	 */
	@Override
	public String toString() {
		return kind.toString();
	}
}
