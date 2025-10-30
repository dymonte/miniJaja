package fr.umlp.m1info.projectvms.group2;

import fr.umlp.m1info.projectvms.group2.memory.symtable.Type;
import fr.umlp.m1info.projectvms.group2.memory.symtable.SymTable;
import fr.umlp.m1info.projectvms.group2.memory.symtable.SymNode;
import fr.umlp.m1info.projectvms.group2.memory.symtable.InstNode;
import fr.umlp.m1info.projectvms.group2.memory.symtable.MemInfo;
import fr.umlp.m1info.projectvms.group2.memory.symtable.ObjectKind;
import fr.umlp.m1info.projectvms.group2.memory.stack.Stack;
import fr.umlp.m1info.projectvms.group2.memory.heap.Heap;
import fr.umlp.m1info.projectvms.group2.exception.BadSymbolKindException;
import fr.umlp.m1info.projectvms.group2.exception.SymbolNotFoundException;
import fr.umlp.m1info.projectvms.group2.exception.NoInstanceException;
import fr.umlp.m1info.projectvms.group2.memory.Memory;
import fr.umlp.m1info.projectvms.group2.memory.Quad;

import java.util.Optional;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import org.mockito.Mockito;

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
	public void testDeclVarNotPresent() {
		SymTable symTable = new SymTable(100);
		String ident = "i";
		int value = 2;
		Type sorte = Type.INT;

		SymTable spySymTable = Mockito.spy(symTable);
		Mockito.doReturn(Optional.empty()).when(spySymTable).add(ident, ObjectKind.VAR, sorte);
		Memory memory = new Memory(spySymTable, new Stack(100), new Heap(100));

		memory.declVar(ident, value, sorte);
		Assert.assertThrows(SymbolNotFoundException.class, () -> memory.getVal(ident));
	}

	@Test
	public void testDeclCst() {
		m.declCst("i", 1, Type.INT);
		Assert.assertEquals(1, (int) m.getVal("i"));

		Assert.assertThrows(BadSymbolKindException.class, () -> m.updateVal("i", 2));
	}

	@Test
	public void testDeclCstNotPresent() {
		SymTable symTable = new SymTable(100);
		String ident1 = "i";
		String ident2 = "c";
		int value = 2;
		Type sorte = Type.INT;

		SymTable spySymTable = Mockito.spy(symTable);
		Mockito.doReturn(Optional.empty()).when(spySymTable).add(ident1, ObjectKind.CONST, sorte);
		Mockito.doReturn(Optional.empty()).when(spySymTable).add(ident2, ObjectKind.VCONST, sorte);
		Memory memory = new Memory(spySymTable, new Stack(100), new Heap(100));

		memory.declCst(ident1, value, sorte);
		memory.declCst(ident2, sorte);
		Assert.assertThrows(SymbolNotFoundException.class, () -> memory.getVal(ident1));
		Assert.assertThrows(SymbolNotFoundException.class, () -> memory.getVal(ident2));
	}

	@Test
	public void testVConst() {
		m.declCst("c", Type.INT);
		Assert.assertThrows(NoInstanceException.class, () -> m.getVal("c"));
		m.updateVal("c", 4);
		Assert.assertEquals(4, (int) m.getVal("c"));
		Assert.assertThrows(BadSymbolKindException.class, () -> m.updateVal("c", 5));
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
	public void testDeclArrayNotPresent() {
		SymTable symTable = new SymTable(100);
		String ident = "i";
		int value = 2;
		Type sorte = Type.INT;

		SymTable spySymTable = Mockito.spy(symTable);
		Mockito.doReturn(Optional.empty()).when(spySymTable).add(ident, ObjectKind.ARRAY, sorte, value);
		Memory memory = new Memory(spySymTable, new Stack(100), new Heap(100));

		memory.declArray(ident, value, sorte);
		Assert.assertThrows(SymbolNotFoundException.class, () -> memory.getVal(ident));
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
	public void testRemoveDeclNotInstance() {
		SymTable symTable = new SymTable(100);
		String ident = "i";
		int value = 2;
		Type sorte = Type.INT;

		SymTable spySymTable = Mockito.spy(symTable);
		Memory memory = new Memory(spySymTable, new Stack(100), new Heap(100));

		memory.declVar(ident, value, sorte);

		Optional<SymNode> symNode = spySymTable.search("i");
		symNode.get().removeInstance(symNode.get().getFirstInstance());

		Assert.assertThrows(NoInstanceException.class, () -> memory.getVal(ident));
		Assert.assertThrows(NoInstanceException.class, () -> memory.updateVal(ident, value));
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

	@Test
	public void testGetUpdateValArrayNoInstance() {
		SymTable symTable = new SymTable(100);
		String ident = "i";
		int value = 2;
		Type sorte = Type.INT;

		SymTable spySymTable = Mockito.spy(symTable);
		Memory memory = new Memory(spySymTable, new Stack(100), new Heap(100));

		memory.declArray(ident, value, sorte);

		Optional<SymNode> symNode = spySymTable.search("i");
		symNode.get().removeInstance(symNode.get().getFirstInstance());

		Assert.assertThrows(NoInstanceException.class, () -> memory.getValArray(ident, 0));
		Assert.assertThrows(NoInstanceException.class, () -> memory.updateValArray(ident, 0, value));
	}

	@Test
	public void testGetUpdateValArrayBadKind() {
		SymTable symTable = new SymTable(100);
		String ident = "i";
		int value = 2;
		Type sorte = Type.INT;

		Memory memory = new Memory(symTable, new Stack(100), new Heap(100));

		memory.declVar(ident, value, sorte);

		Assert.assertThrows(BadSymbolKindException.class, () -> memory.getValArray(ident, 0));
		Assert.assertThrows(BadSymbolKindException.class, () -> memory.updateValArray(ident, 0, value));
	}

	@Test
	public void testGetUpdateValArrayNotFound() {
		String ident = "i";
		int value = 2;
		Assert.assertThrows(SymbolNotFoundException.class, () -> m.getValArray(ident, 0));
		Assert.assertThrows(SymbolNotFoundException.class, () -> m.updateValArray(ident, 0, value));
	}

	@Test
	public void testGetLengthNotFound() {
		String ident = "i";
		Assert.assertThrows(SymbolNotFoundException.class, () -> m.getLengthArray(ident));
	}

	@Test
	public void testGetLengthBadSymbol() {
		String ident = "i";
		int value = 2;
		Type sorte = Type.INT;

		m.declVar(ident, value, sorte);

		Assert.assertThrows(BadSymbolKindException.class, () -> m.getLengthArray(ident));
	}

	@Test
	public void testGetLength() {
		String ident = "i";
		int size = 2;
		Type sorte = Type.INT;

		m.declArray(ident, size, sorte);

		Assert.assertEquals(2, m.getLengthArray(ident));
	}

	@Test
	public void testQuad() {
		SymNode symNode = new SymNode("a", new MemInfo(Type.INT, ObjectKind.VAR));

		InstNode inst1 = new InstNode(1, symNode, null);
		InstNode inst2 = new InstNode(2, symNode, inst1);
		InstNode inst3 = new InstNode(3, symNode, inst2);

		Quad quad3 = new Quad(inst3);
		Assert.assertFalse(quad3.isEmpty());
		Assert.assertEquals("a", quad3.getIdent());
		Assert.assertEquals(3, quad3.getVal());
		Assert.assertEquals(ObjectKind.VAR, quad3.getKind());
		Assert.assertEquals(Type.INT, quad3.getSorte());

		Quad quad2 = quad3.next();
		Assert.assertFalse(quad2.isEmpty());
		Assert.assertEquals("a", quad2.getIdent());
		Assert.assertEquals(2, quad2.getVal());
		Assert.assertEquals(ObjectKind.VAR, quad2.getKind());
		Assert.assertEquals(Type.INT, quad2.getSorte());

		Quad quad1 = quad2.next();
		Assert.assertFalse(quad1.isEmpty());
		Assert.assertEquals("a", quad1.getIdent());
		Assert.assertEquals(1, quad1.getVal());
		Assert.assertEquals(ObjectKind.VAR, quad1.getKind());
		Assert.assertEquals(Type.INT, quad1.getSorte());

		Quad quad = new Quad(null);
		Assert.assertTrue(quad.isEmpty());

		Assert.assertEquals("< a, 2, VAR, INT >", quad2.toString());
	}
}
