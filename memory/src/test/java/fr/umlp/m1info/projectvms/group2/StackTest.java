package fr.umlp.m1info.projectvms.group2;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import fr.umlp.m1info.projectvms.group2.exception.StackOverflowException;
import fr.umlp.m1info.projectvms.group2.memory.stack.Stack;
import fr.umlp.m1info.projectvms.group2.memory.symtable.SymNode;
import fr.umlp.m1info.projectvms.group2.memory.symtable.InstNode;

/**
 * StackTest
 */
public class StackTest {

	private Stack s;

	@Before
	public void setUp() {
		s = new Stack(32000);
	}

	@Test
	public void testPushAndPop() {
		SymNode symbol = Mockito.mock(SymNode.class);
		s.push(10, symbol);
		s.push(20, symbol);
		s.push(30, symbol);
		Assert.assertEquals( 30, s.pop().getValue());
		Assert.assertEquals(20, s.pop().getValue());
		Assert.assertEquals(10, s.pop().getValue());
	}

	@Test
	public void testSwap() {
		SymNode symbol = Mockito.mock(SymNode.class);
		s.push(10, symbol);
		s.push(20, symbol);
		s.push(30, symbol);
		s.swap();
		InstNode in = s.pop();
		Assert.assertEquals(20, in.getValue());
		Assert.assertEquals(30, in.getNextInStack().getValue());
		Assert.assertEquals(10, in.getNextInStack().getNextInStack().getValue());
	}

	@Test
	public void testPopEmptyStack() {
		Assert.assertNull(s.pop());
	}

	@Test
	public void testStackOverflow() {
		SymNode symbol = Mockito.mock(SymNode.class);
		for (int i = 0; i < s.getMaxDepth(); i++) {
			s.push(1, symbol);
		}
		Assert.assertThrows(StackOverflowException.class, () -> s.push(1, symbol));
	}

	@Test
	public void testConstructorException() {
		Assert.assertThrows(IllegalArgumentException.class, () -> new Stack(0));
		Assert.assertThrows(IllegalArgumentException.class, () -> new Stack(-1));
	}

	@Test
	public void testRemove() {
		Assert.assertFalse(s.remove(null));
		InstNode in = Mockito.mock(InstNode.class);

		SymNode symbol = Mockito.mock(SymNode.class);
		s.push(10, symbol);
		s.push(20, symbol);
		s.push(30, symbol);
		s.push(40, symbol);
		Assert.assertFalse(s.remove(in));
		Assert.assertEquals(4, s.getDepth(), 4);
		Assert.assertTrue(s.remove(s.getHead()));
		Assert.assertEquals(3, s.getDepth());
		Assert.assertTrue(s.remove(s.getHead().getNextInStack()));
		Assert.assertEquals(2, s.getDepth());

	}
}
