package fr.umlp.m1info.projectvms.group2;

import fr.umlp.m1info.projectvms.group2.memory.symtable.Type;
import fr.umlp.m1info.projectvms.group2.memory.symtable.SymTable;
import fr.umlp.m1info.projectvms.group2.memory.stack.Stack;
import fr.umlp.m1info.projectvms.group2.memory.heap.Heap;
import fr.umlp.m1info.projectvms.group2.exception.BadSymbolKindException;
import fr.umlp.m1info.projectvms.group2.memory.Memory;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * MemoryTest
 */
public class MemoryTest {

	private Memory m;

	@Before
	public void setUp() {
		m = new Memory(new SymTable(100), new Stack(100), new Heap(100));
	}

	@Test
	public void testDeclVar() {
		String ident = "i";
		int value = 2;
		Type sorte = Type.INT;

		m.declVar(ident, value, sorte);
		int retrievedValue = (int) m.getVal(ident);

		Assert.assertEquals(value, retrievedValue);
	}

	@Test
	public void testDeclCst() {
		m.declCst("i", 1, Type.INT);
		Assert.assertEquals((int) m.getVal("i"), 1);

		Assert.assertThrows(BadSymbolKindException.class, () -> m.updateVal("i", 2));
	}

	@Test
	public void testDeclArray() {
		int arraySize = 10;
		m.declArray("arr", arraySize, Type.INT);
		for (int i = 0; i < arraySize; i++) {
			m.updateValArray("arr", i, i + 1);
		}
		for (int i = 0; i < arraySize; i++) {
			Assert.assertEquals((int) m.getValArray("arr", i), i + 1);
		}
	}

	@Test(expected = UnsupportedOperationException.class)
	public void testDeclMeth() {
		m.declMeth("meth", 0, Type.VOID);
	}

	@Test(expected = UnsupportedOperationException.class)
	public void testRemoveDecl() {
		m.removeDecl("i");
	}

	@Test
	public void testUpdateVal() {
		m.declVar("i", 0, Type.INT);
		Assert.assertEquals((int) m.getVal("i"), 0);
		m.updateVal("i", 10);
		Assert.assertEquals((int) m.getVal("i"), 10);
	}

}
