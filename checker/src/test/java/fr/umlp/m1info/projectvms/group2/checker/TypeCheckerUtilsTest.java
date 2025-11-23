package fr.umlp.m1info.projectvms.group2.checker;

import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import fr.umlp.m1info.projectvms.group2.ast.minijaja.expr.ASTIntLiteral;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.expr.ASTBoolLiteral;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.expr.ASTStringLiteral;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.expr.BinaryOp;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.expr.UnaryOp;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.type.ASTIntType;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.type.ASTBoolType;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.type.ASTVoidType;

import fr.umlp.m1info.projectvms.group2.utils.Type;

public class TypeCheckerUtilsTest {

	@Test
	public void getLiteralTypeTest() {
		Assert.assertEquals(Type.INT, TypeCheckerUtils.getType(new ASTIntLiteral(1, 0, 0)));
		Assert.assertEquals(Type.BOOL, TypeCheckerUtils.getType(new ASTBoolLiteral(true, 0, 0)));
		Assert.assertEquals(Type.STRING, TypeCheckerUtils.getType(new ASTStringLiteral("at", 0, 0)));
	}

	@Test
	public void getMethTypeTest() {
		Assert.assertEquals(Type.INT, TypeCheckerUtils.getTypeMeth(new ASTIntType(0, 0)));
		Assert.assertEquals(Type.BOOL, TypeCheckerUtils.getTypeMeth(new ASTBoolType(0, 0)));
		Assert.assertEquals(Type.VOID, TypeCheckerUtils.getTypeMeth(new ASTVoidType(0, 0)));
	}

	@Test
	public void getTypeTypeTest() {
		Assert.assertEquals(Type.INT, TypeCheckerUtils.getType(new ASTIntType(0, 0)));
		Assert.assertEquals(Type.BOOL, TypeCheckerUtils.getType(new ASTBoolType(0, 0)));
	}

	@Test
	public void getBinaryTypeTest() {
		Assert.assertEquals(Type.INT, TypeCheckerUtils.getBinaryOpType(BinaryOp.ADD));
		Assert.assertEquals(Type.INT, TypeCheckerUtils.getBinaryOpType(BinaryOp.SUB));
		Assert.assertEquals(Type.INT, TypeCheckerUtils.getBinaryOpType(BinaryOp.MUL));
		Assert.assertEquals(Type.INT, TypeCheckerUtils.getBinaryOpType(BinaryOp.DIV));
		Assert.assertEquals(Type.BOOL, TypeCheckerUtils.getBinaryOpType(BinaryOp.EQ));
		Assert.assertEquals(Type.BOOL, TypeCheckerUtils.getBinaryOpType(BinaryOp.AND));
		Assert.assertEquals(Type.BOOL, TypeCheckerUtils.getBinaryOpType(BinaryOp.OR));
	}

	@Test
	public void getUnaryOpTypeTest() {
		Assert.assertEquals(Type.INT, TypeCheckerUtils.getUnaryOpType(UnaryOp.NEG));
		Assert.assertEquals(Type.BOOL, TypeCheckerUtils.getUnaryOpType(UnaryOp.NOT));
	}

	@Test
	public void methodKeyTest() {
		List<Type> types = Arrays.asList(Type.INT, Type.BOOL);
		Assert.assertEquals("-methodINTBOOL", TypeCheckerUtils.methodKey("method", types));
	}
}
