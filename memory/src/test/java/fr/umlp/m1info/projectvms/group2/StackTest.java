package fr.umlp.m1info.projectvms.group2;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import fr.umlp.m1info.projectvms.group2.exception.StackOverflow;
import fr.umlp.m1info.projectvms.group2.stack.Stack;
import fr.umlp.m1info.projectvms.group2.symtable.SymNode;
import fr.umlp.m1info.projectvms.group2.symtable.InstNode;

/**
 * StackTest
 */
public class StackTest {

	@Test
	public void testPushAndPop() {
		Stack s = new Stack();
		SymNode symbol = Mockito.mock(SymNode.class);
		s.push(10, symbol);
		s.push(20, symbol);
		s.push(30, symbol);
		Assert.assertEquals(s.pop().getValue(), 30);
		Assert.assertEquals(s.pop().getValue(), 20);
		Assert.assertEquals(s.pop().getValue(), 10);
	}

	@Test
	public void testSwap() {
		Stack s = new Stack();
		SymNode symbol = Mockito.mock(SymNode.class);
		s.push(10, symbol);
		s.push(20, symbol);
		s.push(30, symbol);
		s.swap();
		InstNode in = s.pop();
		Assert.assertEquals(in.getValue(), 20);
		Assert.assertEquals(in.getNextInStack().getValue(), 30);
		Assert.assertEquals(in.getNextInStack().getNextInStack().getValue(), 10);
	}

	@Test
	public void testPopEmptyStack() {
		Stack s = new Stack();
		Assert.assertNull(s.pop());
	}

	@Test
	public void testStackOverflow() {
		Stack s = new Stack();
		SymNode symbol = Mockito.mock(SymNode.class);
		for (int i = 0; i < Stack.MAX_DEPTH; i++) {
			s.push(1, symbol);
		}
		Assert.assertThrows(StackOverflow.class, () -> s.push(1, symbol));
	}

}
