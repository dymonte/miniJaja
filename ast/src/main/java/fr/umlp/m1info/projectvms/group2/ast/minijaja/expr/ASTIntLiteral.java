package fr.umlp.m1info.projectvms.group2.ast.minijaja.expr;

public class ASTIntLiteral extends ASTLiteral {
	private final int value;

	public ASTIntLiteral(int value, int line, int column) {
		super(line, column);
		this.value = value;
	}

	@Override
	public String toString() {
		return Integer.toString(value);
	}
}
