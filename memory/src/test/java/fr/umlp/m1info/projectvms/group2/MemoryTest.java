package fr.umlp.m1info.projectvms.group2;

import fr.umlp.m1info.projectvms.group2.memory.symtable.Type;
import fr.umlp.m1info.projectvms.group2.memory.symtable.SymTable;
import fr.umlp.m1info.projectvms.group2.memory.stack.Stack;
import fr.umlp.m1info.projectvms.group2.memory.heap.Heap;
import fr.umlp.m1info.projectvms.group2.exception.BadSymbolKindException;
import fr.umlp.m1info.projectvms.group2.exception.SymbolNotFoundException;
import fr.umlp.m1info.projectvms.group2.memory.Memory;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

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
		Assert.assertEquals(1, (int) m.getVal("i"));

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

	@Test
	public void testGetMethKey() {
		Assert.assertEquals("4methIB", m.getMethKey("meth", List.of(Type.INT, Type.BOOL)));
	}

	@Test
	public void testRemoveDecl() {
		m.declVar("i", 0, Type.INT);
		m.declVar("i", 1, Type.INT);
		m.removeDecl("i");
		m.removeDecl("i");
		Assert.assertThrows(SymbolNotFoundException.class, () -> m.removeDecl("ii"));
	}

	@Test
	public void testDeclMeth() {
		m.declMeth("meth1", null, List.of(Type.INT, Type.BOOL), Type.VOID);

		Assert.assertNull(m.getVal("5meth1IB"));

		m.declMeth("method2", 0, List.of(), Type.INT);

		Assert.assertEquals(0, (int) m.getVal("7method2"));
	}

	@Test
	public void testUpdateGetVal() {
		m.declVar("i", 0, Type.INT);
		Assert.assertEquals(0, (int) m.getVal("i"));
		m.updateVal("i", 10);

		Assert.assertEquals(10, (int) m.getVal("i"));
		Assert.assertThrows(SymbolNotFoundException.class, () -> m.updateVal("aa", 9));
		Assert.assertThrows(SymbolNotFoundException.class, () -> m.getVal("aa"));
		Assert.assertThrows(SymbolNotFoundException.class, () -> m.getVal("aa"));
		m.declArray("aa", 1, Type.INT);
		Assert.assertThrows(BadSymbolKindException.class, () -> m.getVal("aa"));
	}

	@Test
	public void testUpdateType() {
		m.declVar("i", 0, Type.INT);
		Assert.assertEquals(Type.INT, m.getType("i"));
		m.updateType("i", Type.BOOL);
		Assert.assertEquals(Type.BOOL, m.getType("i"));
		Assert.assertThrows(SymbolNotFoundException.class, () -> m.updateType("aa", Type.BOOL));
		Assert.assertThrows(SymbolNotFoundException.class, () -> m.getType("aa"));
	}

}
