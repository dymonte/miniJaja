package fr.umlp.m1info.projectvms.group2.interpreter;

import org.junit.Test;
import org.junit.Before;
import org.junit.Assert;

import fr.umlp.m1info.projectvms.group2.memory.Memory;
import fr.umlp.m1info.projectvms.group2.memory.heap.Heap;
import fr.umlp.m1info.projectvms.group2.memory.stack.Stack;
import fr.umlp.m1info.projectvms.group2.memory.symtable.SymTable;

import fr.umlp.m1info.projectvms.group2.interpreter.minijaja.MiniJajaInterpreterInteractive;

public class MiniJajaInterpreterInteractiveTest {
	MiniJajaInterpreterInteractive interactive;
	Memory memory;

	@Before
	public void setUp() {
		memory = new Memory(new SymTable(100), new Stack(100), new Heap(100));
		interactive = new MiniJajaInterpreterInteractive(memory);
	}
}
