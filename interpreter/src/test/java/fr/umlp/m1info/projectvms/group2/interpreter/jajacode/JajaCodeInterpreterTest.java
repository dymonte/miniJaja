package fr.umlp.m1info.projectvms.group2.interpreter.jajacode;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import fr.umlp.m1info.projectvms.group2.ast.jajacode.instr.*;
import fr.umlp.m1info.projectvms.group2.ast.jajacode.instr.oper.InstrOperBinary;
import fr.umlp.m1info.projectvms.group2.ast.jajacode.instr.oper.InstrOperUnary;
import fr.umlp.m1info.projectvms.group2.ast.jajacode.instr.oper.JajaCodeBinaryOper;
import fr.umlp.m1info.projectvms.group2.ast.jajacode.instr.oper.JajaCodeUnaryOper;
import fr.umlp.m1info.projectvms.group2.ast.jajacode.value.ValAddress;
import fr.umlp.m1info.projectvms.group2.ast.jajacode.value.ValBool;
import fr.umlp.m1info.projectvms.group2.ast.jajacode.value.ValIdent;
import fr.umlp.m1info.projectvms.group2.ast.jajacode.value.ValNbre;
import fr.umlp.m1info.projectvms.group2.ast.jajacode.var.VarKind;
import fr.umlp.m1info.projectvms.group2.ast.jajacode.var.VarType;
import fr.umlp.m1info.projectvms.group2.exception.EmptyStackException;
import fr.umlp.m1info.projectvms.group2.exception.InterpretRuntimeException;
import fr.umlp.m1info.projectvms.group2.memory.Memory;
import fr.umlp.m1info.projectvms.group2.memory.heap.Heap;
import fr.umlp.m1info.projectvms.group2.memory.stack.Stack;
import fr.umlp.m1info.projectvms.group2.memory.symtable.SymTable;
import fr.umlp.m1info.projectvms.group2.utils.Type;
import fr.umlp.m1info.projectvms.group2.utils.Kind;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Arrays;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class JajaCodeInterpreterTest {
	JajaCodeInterpreter interpreter;
	Memory memory;

	@Before
	public void setUp() {
		memory = new Memory(new SymTable(100), new Stack(100), new Heap(100));
	}

	@Test
	public void testInterpretInit() {
		InstrInit init = new InstrInit(1);
		InstrStop stop = new InstrStop(2);
		interpreter = new JajaCodeInterpreter(memory, Arrays.asList(init, stop));
		JajaCodeInterpreter spyInterpreter = spy(interpreter);
		doNothing().when(spyInterpreter).interpret(stop);
		spyInterpreter.interpret(init);
	}

	@Test
	public void testInterpretStop() {
		InstrStop stop = new InstrStop(1);
		interpreter = new JajaCodeInterpreter(memory, Arrays.asList(stop));
		AtomicBoolean wasInterrupted = new AtomicBoolean(false);
		Thread thread = new Thread(() -> {
			interpreter.interpret(stop);
			wasInterrupted.set(Thread.currentThread().isInterrupted());
		});
		thread.start();
		try {
			thread.join(1000);
			if (thread.isAlive()) {
				thread.interrupt();
				Assert.fail("Interpreter thread did not finish in time");
			}
		} catch (InterruptedException e) {
			Assert.fail("Test thread should not be interrupted");
		}
		assertTrue(wasInterrupted.get());
	}

	@Test
	public void testInterpretNop() {
		InstrNop nop = new InstrNop(1);
		interpreter = new JajaCodeInterpreter(memory, Arrays.asList(nop));
		interpreter.interpret(nop);
	}

	@Test
	public void testInterpretSwap() {
		memory.push(1);
		memory.push(2);
		InstrSwap swap = new InstrSwap(1);
		InstrStop stop = new InstrStop(2);
		interpreter = new JajaCodeInterpreter(memory, Arrays.asList(swap, stop));
		JajaCodeInterpreter spyInterpreter = spy(interpreter);
		doNothing().when(spyInterpreter).interpret(stop);
		spyInterpreter.interpret(swap);
		assertEquals(1, memory.pop());
		assertEquals(2, memory.pop());
	}

	@Test
	public void testInterpretNewVar() {
		memory.push(0);
		memory.push(2);
		InstrNew instrNew = new InstrNew(1, new ValIdent(1, "x"),
				new VarKind(1, Kind.VAR),
				new VarType(1, Type.INT), new ValAddress(1, 1));

		InstrStop stop = new InstrStop(2);
		interpreter = new JajaCodeInterpreter(memory, Arrays.asList(instrNew, stop));
		JajaCodeInterpreter spyInterpreter = spy(interpreter);
		doNothing().when(spyInterpreter).interpret(stop);
		spyInterpreter.interpret(instrNew);
		assertEquals(Type.INT, memory.getType("x"));
	}

	@Test
	public void testInterpretNewConst() {
		memory.push(5);
		InstrNew instrNew = new InstrNew(1, new ValIdent(1, "c"),
				new VarKind(1, Kind.CONST),
				new VarType(1, Type.INT), new ValAddress(1, 1));
		InstrStop stop = new InstrStop(2);
		interpreter = new JajaCodeInterpreter(memory, Arrays.asList(instrNew, stop));
		JajaCodeInterpreter spyInterpreter = spy(interpreter);
		doNothing().when(spyInterpreter).interpret(stop);
		spyInterpreter.interpret(instrNew);
		assertEquals(5, memory.getVal("c"));
		assertEquals(Type.INT, memory.getType("c"));
	}

	@Test
	public void testInterpretNewMeth() {
		memory.push(10);
		InstrNew instrNew = new InstrNew(1, new ValIdent(1, "f"),
				new VarKind(1, Kind.METH),
				new VarType(1, Type.INT), new ValAddress(1, 1));
		InstrStop stop = new InstrStop(2);
		interpreter = new JajaCodeInterpreter(memory, Arrays.asList(instrNew, stop));
		JajaCodeInterpreter spyInterpreter = spy(interpreter);
		doNothing().when(spyInterpreter).interpret(stop);
		spyInterpreter.interpret(instrNew);
		assertEquals(10, memory.getVal("1f"));
	}

	@Test
	public void testInterpretNewArray() {
		memory.push(5);
		InstrNewArray instrNewArray = new InstrNewArray(1, new ValIdent(1, "t"),
				new VarType(1, Type.INT));
		InstrStop stop = new InstrStop(2);
		interpreter = new JajaCodeInterpreter(memory, Arrays.asList(instrNewArray, stop));
		JajaCodeInterpreter spyInterpreter = spy(interpreter);
		doNothing().when(spyInterpreter).interpret(stop);
		spyInterpreter.interpret(instrNewArray);
		assertEquals(Type.INT, memory.getType("t"));
	}

	@Test
	public void testInterpretPush() {
		ValNbre val = new ValNbre(1, 42);
		InstrPush pushNode = new InstrPush(1, val);
		InstrStop stopNode = new InstrStop(2);
		interpreter = new JajaCodeInterpreter(memory, List.of(pushNode, stopNode));
		JajaCodeInterpreter spyInterpreter = Mockito.spy(interpreter);
		Mockito.doNothing().when(spyInterpreter).interpret(stopNode);
		spyInterpreter.interpretPush(pushNode);
		assertEquals(42, memory.pop());
	}

	@Test
	public void testInterpretPop() {
		memory.push(100);
		InstrPop pop = new InstrPop(1);
		InstrStop stop = new InstrStop(2);
		interpreter = new JajaCodeInterpreter(memory, Arrays.asList(pop, stop));
		JajaCodeInterpreter spy = spy(interpreter);
		doNothing().when(spy).interpret(stop);
		spy.interpret(pop);
		try {
			memory.pop();
			fail("pop() should have thrown EmptyStackException");
		} catch (EmptyStackException ignored) {
			/* OK */ }
	}

	@Test
	public void testInterpretIf_True() {
		memory.push(true);
		InstrIf instrIf = new InstrIf(1, new ValAddress(1, 3));
		InstrNop nop = new InstrNop(2);
		InstrStop stop = new InstrStop(3);

		interpreter = new JajaCodeInterpreter(memory, Arrays.asList(instrIf, nop, stop));
		JajaCodeInterpreter spy = spy(interpreter);
		doNothing().when(spy).interpret(stop);

		spy.interpret(instrIf);

		verify(spy).interpret(stop);
		verify(spy, never()).interpret(nop);
	}

	@Test
	public void testInterpretIf_False() {
		memory.push(false);
		InstrIf instrIf = new InstrIf(1, new ValAddress(1, 3));
		InstrNop nop = new InstrNop(2);
		InstrStop stop = new InstrStop(3);

		interpreter = new JajaCodeInterpreter(memory, Arrays.asList(instrIf, nop, stop));
		JajaCodeInterpreter spy = spy(interpreter);
		doNothing().when(spy).interpret(stop);
		doNothing().when(spy).interpret(nop);

		spy.interpret(instrIf);

		verify(spy).interpret(nop);
		verify(spy, never()).interpret(stop);
	}

	@Test
	public void testInterpretOperBinary_Add() {
		memory.push(3);
		memory.push(4);
		InstrOperBinary add = new InstrOperBinary(1, JajaCodeBinaryOper.ADD);
		InstrStop stop = new InstrStop(2);
		interpreter = new JajaCodeInterpreter(memory, Arrays.asList(add, stop));
		JajaCodeInterpreter spy = spy(interpreter);
		doNothing().when(spy).interpret(stop);
		spy.interpret(add);
		assertEquals(7, memory.pop());
	}

	@Test
	public void testInterpretOperUnary_Neg() {
		memory.push(5);
		InstrOperUnary neg = new InstrOperUnary(1, JajaCodeUnaryOper.NEG);
		InstrStop stop = new InstrStop(2);
		interpreter = new JajaCodeInterpreter(memory, Arrays.asList(neg, stop));
		JajaCodeInterpreter spy = spy(interpreter);
		doNothing().when(spy).interpret(stop);
		spy.interpret(neg);
		assertEquals(-5, memory.pop());
	}

	@Test
	public void testInterpretOperUnary_Not() {
		memory.push(true);
		InstrOperUnary not = new InstrOperUnary(1, JajaCodeUnaryOper.NOT);
		InstrStop stop = new InstrStop(2);
		interpreter = new JajaCodeInterpreter(memory, Arrays.asList(not, stop));
		JajaCodeInterpreter spy = spy(interpreter);
		doNothing().when(spy).interpret(stop);
		spy.interpret(not);
		assertEquals(false, memory.pop());
	}

	@Test
	public void testInterpretInc() {
		memory.push(10);
		memory.identVal("x", Kind.VAR, Type.INT, 0);
		memory.push(3);
		InstrInc inc = new InstrInc(1, new ValIdent(1, "x"));
		InstrStop stop = new InstrStop(2);
		interpreter = new JajaCodeInterpreter(memory, Arrays.asList(inc, stop));
		JajaCodeInterpreter spy = spy(interpreter);
		doNothing().when(spy).interpret(stop);
		spy.interpret(inc);
		assertEquals(13, memory.getVal("x"));
	}

	@Test
	public void testInterpretAInc() {
		memory.declArray("arr", 3, Type.INT);
		memory.updateValArray("arr", 1, 20);
		memory.push(1);
		memory.push(5);
		InstrAInc ainc = new InstrAInc(1, new ValIdent(1, "arr"));
		InstrStop stop = new InstrStop(2);
		interpreter = new JajaCodeInterpreter(memory, Arrays.asList(ainc, stop));
		JajaCodeInterpreter spy = spy(interpreter);
		doNothing().when(spy).interpret(stop);
		spy.interpret(ainc);
		assertEquals(25, memory.getValArray("arr", 1));
	}

}
