package fr.umlp.m1info.projectvms.group2.interpreter.jajacode;

import fr.umlp.m1info.projectvms.group2.ast.jajacode.instr.InstrWrite;
import fr.umlp.m1info.projectvms.group2.ast.jajacode.value.ValIdent;
import fr.umlp.m1info.projectvms.group2.exception.InterpretRuntimeException;
import fr.umlp.m1info.projectvms.group2.memory.Memory;
import fr.umlp.m1info.projectvms.group2.memory.heap.Heap;
import fr.umlp.m1info.projectvms.group2.memory.stack.Stack;
import fr.umlp.m1info.projectvms.group2.memory.symtable.SymTable;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class JajaCodeInterpreterInteractiveTest {
    JajaCodeInterpreterInteractive interactive;
    Memory memory;

    @Before
    public void setUp() {
        memory = new Memory(new SymTable(100), new Stack(100), new Heap(100));
        interactive = new JajaCodeInterpreterInteractive(memory);
    }

    @Test
    public void testAll() {
        interactive.setStepModeEnabled(true);
        interactive.nextStep();
        List<Integer> list = Arrays.asList(1, 2);
        interactive.setBreakpoints(list);
        interactive.addBreakPoint(1);
        interactive.removeBreakpoint(1);
        Assert.assertThrows(InterpretRuntimeException.class, () -> interactive.interpret(null));
    }

    @Test
    public void testHitBreakPoint() {
        interactive.addBreakPoint(2);
        interactive.setStepModeEnabled(false);
        ValIdent ident = new ValIdent(1, "a");
        ValIdent ident1 = new ValIdent(2, "b");
        Assert.assertThrows(InterpretRuntimeException.class, () -> interactive.interpret(ident));

        Thread t = new Thread(
                () -> Assert.assertThrows(InterpretRuntimeException.class, () -> interactive.interpret(ident1)));

        t.start();
        interactive.nextStep();
    }

    @Test
    public void testStepModeEnabled() {
        ValIdent ident = new ValIdent(1, "a");

        Thread t = new Thread(
                () -> Assert.assertThrows(InterpretRuntimeException.class, () -> interactive.interpret(ident)));

        t.start();
        interactive.nextStep();
    }

    @Test
    public void testLock() {
        ValIdent ident = new ValIdent(1, "a");


        Thread t = new Thread(
                () -> Assert.assertThrows(InterpretRuntimeException.class, () -> interactive.interpret(ident)));

        t.start();
        t.interrupt();

        interactive.nextStep();
    }

    @Ignore
    public void testInterpret() {
        InstrWrite instrWrite = new InstrWrite(1);

        interactive.setStepModeEnabled(false);
        //interactive.interpret(astWrite);
        Assert.assertTrue(true);
    }
}
