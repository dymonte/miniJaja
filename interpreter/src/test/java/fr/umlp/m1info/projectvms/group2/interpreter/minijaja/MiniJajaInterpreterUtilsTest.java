package fr.umlp.m1info.projectvms.group2.interpreter.minijaja;

import org.junit.Test;

import fr.umlp.m1info.projectvms.group2.exception.InterpretRuntimeException;
import fr.umlp.m1info.projectvms.group2.memory.Memory;
import fr.umlp.m1info.projectvms.group2.memory.heap.Heap;
import fr.umlp.m1info.projectvms.group2.memory.stack.Stack;
import fr.umlp.m1info.projectvms.group2.memory.symtable.SymTable;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.expr.ASTLValue;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.instr.ASTIncrementOp;

import org.junit.Assert;
import org.mockito.Mockito;

public class MiniJajaInterpreterUtilsTest {
	@Test
	public void addValuesTest() {
		Assert.assertThrows(InterpretRuntimeException.class, () -> MiniJajaInterpreterUtils.addValues(null, 0));
		Assert.assertThrows(InterpretRuntimeException.class, () -> MiniJajaInterpreterUtils.addValues(0, null));
		Assert.assertEquals(true, MiniJajaInterpreterUtils.addValues(true, true));
		Assert.assertEquals(true, MiniJajaInterpreterUtils.addValues(true, false));
		Assert.assertEquals(true, MiniJajaInterpreterUtils.addValues(false, true));
		Assert.assertEquals(false, MiniJajaInterpreterUtils.addValues(false, false));

		Assert.assertEquals(1, MiniJajaInterpreterUtils.addValues(true, 0));
		Assert.assertEquals(0, MiniJajaInterpreterUtils.addValues(false, 0));
		Assert.assertEquals(1, MiniJajaInterpreterUtils.addValues(0, true));
		Assert.assertEquals(0, MiniJajaInterpreterUtils.addValues(0, false));

		Assert.assertEquals(1, MiniJajaInterpreterUtils.addValues(0, 1));
		Assert.assertEquals(1, MiniJajaInterpreterUtils.addValues(1, 0));
		Assert.assertEquals(2, MiniJajaInterpreterUtils.addValues(1, 1));
		Assert.assertEquals(-2, MiniJajaInterpreterUtils.addValues(-1, -1));

		Assert.assertThrows(InterpretRuntimeException.class, () -> MiniJajaInterpreterUtils.addValues("String", 0));
		Assert.assertThrows(InterpretRuntimeException.class, () -> MiniJajaInterpreterUtils.addValues(0, "String"));
	}

	@Test
	public void resolveLValueExceptionTest() {
		class ASTInvalidLValue extends ASTLValue {
			public ASTInvalidLValue(int line, int column) {
				super(line, column);
			}

			@Override
			public String toString() {
				return "";
			}
		}

		ASTLValue invalidLValue = new ASTInvalidLValue(0, 0);

		Memory memory = new Memory(new SymTable(10), new Stack(10), new Heap(10));
		MiniJajaInterpreter interpreter = new MiniJajaInterpreter(memory);
		Assert.assertThrows(InterpretRuntimeException.class,
				() -> MiniJajaInterpreterUtils.resolveLValue(memory, invalidLValue, interpreter));
	}
}
