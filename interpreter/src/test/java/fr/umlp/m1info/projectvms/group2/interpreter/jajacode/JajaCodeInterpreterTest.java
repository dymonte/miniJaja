package fr.umlp.m1info.projectvms.group2.interpreter.jajacode;

import java.util.concurrent.atomic.AtomicBoolean;

import fr.umlp.m1info.projectvms.group2.ast.jajacode.instr.*;
import fr.umlp.m1info.projectvms.group2.ast.jajacode.instr.oper.InstrOperBinary;
import fr.umlp.m1info.projectvms.group2.ast.jajacode.instr.oper.JajaCodeBinaryOper;
import fr.umlp.m1info.projectvms.group2.ast.jajacode.value.ValAddress;
import fr.umlp.m1info.projectvms.group2.ast.jajacode.value.ValBool;
import fr.umlp.m1info.projectvms.group2.ast.jajacode.value.ValIdent;
import fr.umlp.m1info.projectvms.group2.ast.jajacode.value.ValNbre;
import fr.umlp.m1info.projectvms.group2.ast.jajacode.var.VarKind;
import fr.umlp.m1info.projectvms.group2.ast.jajacode.var.VarType;
import fr.umlp.m1info.projectvms.group2.exception.InterpretRuntimeException;
import fr.umlp.m1info.projectvms.group2.memory.Memory;
import fr.umlp.m1info.projectvms.group2.memory.heap.Heap;
import fr.umlp.m1info.projectvms.group2.memory.stack.Stack;
import fr.umlp.m1info.projectvms.group2.memory.symtable.SymTable;
import fr.umlp.m1info.projectvms.group2.memory.symtable.Type;
import fr.umlp.m1info.projectvms.group2.ast.jajacode.utils.Kind;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Arrays;

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
		JajaCodeInterpreter spyInterpreter = Mockito.spy(interpreter);
		Mockito.doNothing().when(spyInterpreter).interpret(stop);
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
		Assert.assertTrue(wasInterrupted.get());
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
		JajaCodeInterpreter spyInterpreter = Mockito.spy(interpreter);
		Mockito.doNothing().when(spyInterpreter).interpret(stop);
		spyInterpreter.interpret(swap);
		Assert.assertEquals(1, memory.pop());
		Assert.assertEquals(2, memory.pop());
	}

	@Test
	public void testInterpretNewVar() {
		memory.push(0);
		memory.push(2);
		InstrNew instrNew = new InstrNew(1, new ValIdent(1, "x"),
				new VarKind(1, Kind.VAR),
				new VarType(1, fr.umlp.m1info.projectvms.group2.ast.jajacode.utils.Type.INT), new ValAddress(1, 1));

		InstrStop stop = new InstrStop(2);
		interpreter = new JajaCodeInterpreter(memory, Arrays.asList(instrNew, stop));
		JajaCodeInterpreter spyInterpreter = Mockito.spy(interpreter);
		Mockito.doNothing().when(spyInterpreter).interpret(stop);
		spyInterpreter.interpret(instrNew);
		Assert.assertEquals(Type.INT, memory.getType("x"));
	}

	@Test
	public void testInterpretNewConst() {
		memory.push(5);
		InstrNew instrNew = new InstrNew(1, new ValIdent(1, "c"),
				new VarKind(1, Kind.CONST),
				new VarType(1, fr.umlp.m1info.projectvms.group2.ast.jajacode.utils.Type.INT), new ValAddress(1, 1));
		InstrStop stop = new InstrStop(2);
		interpreter = new JajaCodeInterpreter(memory, Arrays.asList(instrNew, stop));
		JajaCodeInterpreter spyInterpreter = Mockito.spy(interpreter);
		Mockito.doNothing().when(spyInterpreter).interpret(stop);
		spyInterpreter.interpret(instrNew);
		Assert.assertEquals(5, memory.getVal("c"));
		Assert.assertEquals(Type.INT, memory.getType("c"));
	}

	@Test
	public void testInterpretNewMeth() {
		memory.push(10);
		InstrNew instrNew = new InstrNew(1, new ValIdent(1, "f"),
				new VarKind(1, Kind.METH),
				new VarType(1, fr.umlp.m1info.projectvms.group2.ast.jajacode.utils.Type.INT), new ValAddress(1, 1));
		InstrStop stop = new InstrStop(2);
		interpreter = new JajaCodeInterpreter(memory, Arrays.asList(instrNew, stop));
		JajaCodeInterpreter spyInterpreter = Mockito.spy(interpreter);
		Mockito.doNothing().when(spyInterpreter).interpret(stop);
		spyInterpreter.interpret(instrNew);
		Assert.assertEquals(10, memory.getVal("1f"));
	}

	@Test
	public void testInterpretNewArray() {
		memory.push(5);
		InstrNewArray instrNewArray = new InstrNewArray(1, new ValIdent(1, "t"),
				new VarType(1, fr.umlp.m1info.projectvms.group2.ast.jajacode.utils.Type.INT));
		InstrStop stop = new InstrStop(2);
		interpreter = new JajaCodeInterpreter(memory, Arrays.asList(instrNewArray, stop));
		JajaCodeInterpreter spyInterpreter = Mockito.spy(interpreter);
		Mockito.doNothing().when(spyInterpreter).interpret(stop);
		spyInterpreter.interpret(instrNewArray);
		Assert.assertEquals(Type.INT, memory.getType("t"));
	}

}
