package fr.umlp.m1info.projectvms.group2;

import fr.umlp.m1info.projectvms.group2.memory.symtable.Type;
import fr.umlp.m1info.projectvms.group2.memory.symtable.SymTable;
import fr.umlp.m1info.projectvms.group2.memory.stack.Stack;
import fr.umlp.m1info.projectvms.group2.memory.heap.Heap;
import fr.umlp.m1info.projectvms.group2.memory.Memory;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mockito;

/**
 * MemoryTest
 */
public class MemoryTest {

	private Memory m;
	private SymTable st;
	private Stack s;
	private Heap h;

	@Before
	public void setUp() {
		st = Mockito.mock(SymTable.class);
		s = Mockito.mock(Stack.class);
		h = Mockito.mock(Heap.class);
		m = new Memory(st, s, h);
	}

	/**
	 * IMPORTANT
	 * TODO: maybe we need to do things with the mocks in each test for
	 * those to work. (like setting stack.pop() to a certain value...)
	 */

	@Test
	@Ignore
	public void testDeclVar() {
		m.declVar("i", 2, Type.INT);
		Assert.assertEquals((int) m.getVal("i"), 2);
	}

	@Test
	@Ignore
	public void testDeclCst() {
		m.declCst("i", 1, Type.INT);
		Assert.assertEquals((int) m.getVal("i"), 1);
	}

	@Test
	@Ignore
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
	@Ignore
	public void testDeclMeth() {
		// TODO: understand how to create this and implement it
	}

	@Test
	@Ignore
	public void testRemoveDecl() {
		// TODO: implement this
	}

	@Test
	@Ignore
	public void testUpdateVal() {
		m.declVar("i", 0, Type.INT);
		Assert.assertEquals((int) m.getVal("i"), 0);
		m.updateVal("i", 10);
		Assert.assertEquals((int) m.getVal("i"), 10);
	}

}
