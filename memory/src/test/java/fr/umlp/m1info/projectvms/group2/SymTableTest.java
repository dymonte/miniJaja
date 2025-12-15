package fr.umlp.m1info.projectvms.group2;

import java.util.Optional;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import fr.umlp.m1info.projectvms.group2.memory.symtable.InstNode;
import fr.umlp.m1info.projectvms.group2.memory.symtable.MemInfo;

import fr.umlp.m1info.projectvms.group2.memory.symtable.SymNode;
import fr.umlp.m1info.projectvms.group2.memory.symtable.SymTable;
import fr.umlp.m1info.projectvms.group2.utils.Kind;
import fr.umlp.m1info.projectvms.group2.utils.Type;

/**
 * SymTableTest
 */
public class SymTableTest {

	private SymTable symTable;

	@Before
	public void setUp() {
		int tableLength = 32;
		symTable = new SymTable(tableLength);
	}

	@Test
	public void invalidTableSizeTest() {
		Assert.assertThrows(IllegalArgumentException.class, () -> new SymTable(0));
		Assert.assertThrows(IllegalArgumentException.class, () -> new SymTable(-1));
	}

	@Test
	public void addTest() {
		Optional<SymNode> s = symTable.add("i", Kind.VAR, Type.INT);
		Assert.assertTrue(s.isPresent());
		Assert.assertEquals(s.get().getIdent(), "i");
		Assert.assertEquals(s.get().getInfo().getKind(), Kind.VAR);
		Assert.assertEquals(s.get().getInfo().getSorte(), Type.INT);
	}

	@Test
	public void addSameIdentifierTest() {
		Optional<SymNode> s1 = symTable.add("i", Kind.VAR, Type.INT);
		Optional<SymNode> s2 = symTable.add("i", Kind.VAR, Type.INT);
		Assert.assertTrue(s1.isPresent());
		Assert.assertTrue(s2.isPresent());
		Assert.assertEquals(s1.get(), s2.get());
	}

	@Test
	public void addDifferentIdentifierSameHashTest() {
		Optional<SymNode> s1 = symTable.add("ab", Kind.VAR, Type.INT);
		Optional<SymNode> s2 = symTable.add("ba", Kind.METH, Type.BOOL);
		Assert.assertTrue(s1.isPresent());
		Assert.assertTrue(s2.isPresent());
		Assert.assertNotEquals(s1.get(), s2.get());

		Optional<SymNode> s3 = symTable.search("ab");
		Optional<SymNode> s4 = symTable.search("ba");
		Assert.assertTrue(s3.isPresent());
		Assert.assertTrue(s4.isPresent());
		Assert.assertEquals(s1.get(), s3.get());
		Assert.assertEquals(s2.get(), s4.get());
	}

	@Test
	public void addNegativeSizedArrayTest() {
		Assert.assertThrows(IllegalArgumentException.class, () -> symTable.add("i", Kind.VAR, Type.INT, -1));
	}

	@Test
	public void addNullSizedArrayTest() {
		Assert.assertThrows(IllegalArgumentException.class, () -> symTable.add("i", Kind.VAR, Type.INT, 0));
	}

	@Test
	public void searchTest() {
		Optional<SymNode> s1 = symTable.add("i", Kind.VAR, Type.INT);
		Optional<SymNode> s2 = symTable.search("i");
		Assert.assertTrue(s2.isPresent());
		Assert.assertEquals(s1.get(), s2.get());
	}

	@Test
	public void searchNotFoundTest() {
		Optional<SymNode> s = symTable.search("nonexistent");
		Assert.assertFalse(s.isPresent());
	}

	@Test
	public void addSameIdentifier() {
		symTable.add("i", Kind.VAR, Type.INT);
		Optional<SymNode> s2 = symTable.add("i", Kind.VAR, Type.BOOL);
		Assert.assertTrue(s2.isPresent());

		symTable.add("j", Kind.VAR, Type.INT);
		Optional<SymNode> s3 = symTable.add("j", Kind.METH, Type.INT);
		Assert.assertTrue(s3.isPresent());

		symTable.add("k", Kind.VAR, Type.INT, 1);
		Optional<SymNode> s4 = symTable.add("k", Kind.VAR, Type.INT, 2);
		Assert.assertTrue(s4.isPresent());

		Optional<SymNode> s5 = symTable.add("l", Kind.VAR, Type.INT, 1);
		Optional<SymNode> s6 = symTable.add("l", Kind.VAR, Type.INT, 1);
		Assert.assertTrue(s5.isPresent());
		Assert.assertTrue(s6.isPresent());
		Assert.assertEquals(s5, s6);
	}

	@Test
	public void removeTest() {
		Optional<SymNode> s = symTable.add("i", Kind.VAR, Type.INT);
		Assert.assertTrue(s.isPresent());
		new InstNode(0, s.get(), null);
		Assert.assertTrue(symTable.remove(s.get().getFirstInstance()));
		Assert.assertFalse(symTable.search("i").isPresent());
	}

	@Test
	public void removeOneOfMultipleInstancesTest() {
		Optional<SymNode> s = symTable.add("i", Kind.VAR, Type.INT);
		Assert.assertTrue(s.isPresent());
		new InstNode(0, s.get(), null);
		new InstNode(1, s.get(), null);
		Assert.assertTrue(symTable.remove(s.get().getFirstInstance()));
		Assert.assertTrue(symTable.search("i").isPresent());
	}

	@Test
	public void removeNonExistentInstanceTest() {
		Optional<SymNode> s = symTable.add("i", Kind.VAR, Type.INT);
		Assert.assertTrue(s.isPresent());
		new InstNode(0, s.get(), null);
		SymNode sym = new SymNode("b", new MemInfo(Type.INT, Kind.VAR, 1));
		InstNode nonExistent = new InstNode(1, sym, null);
		symTable.remove(nonExistent);
		Assert.assertFalse(symTable.remove(nonExistent));
		Assert.assertTrue(symTable.search("i").isPresent());
	}

	@Test
	public void removeFromEmptyTableTest() {
		SymNode sym = new SymNode("i", new MemInfo(Type.INT, Kind.VAR, 1));
		InstNode nonExistent = new InstNode(0, sym, null);
		Assert.assertFalse(symTable.remove(nonExistent));
	}

	@Test
	public void removeNullTest() {
		Assert.assertFalse(symTable.remove(null));
	}

	@Test
	public void removeWithMultipleSymbolsSameHash() {
		Optional<SymNode> s1 = symTable.add("ab", Kind.VAR, Type.INT);
		Optional<SymNode> s2 = symTable.add("ba", Kind.VAR, Type.INT);
		Assert.assertTrue(s1.isPresent());
		Assert.assertTrue(s2.isPresent());
		new InstNode(0, s2.get(), null);
		Assert.assertTrue(symTable.remove(s2.get().getFirstInstance()));
	}

	@Test
	public void memInfoTest() {
		Type sorte = Type.INT;
		Kind kind = Kind.VAR;
		Assert.assertThrows(IllegalArgumentException.class, () -> new MemInfo(sorte, kind, -1));
		MemInfo memInfo = new MemInfo(sorte, kind);
		Assert.assertEquals(sorte, memInfo.getSorte());
		Assert.assertEquals(kind, memInfo.getKind());
		Assert.assertEquals(1, memInfo.getSize());

		Assert.assertFalse(memInfo.equals(false));
		Assert.assertEquals(memInfo.hashCode(), memInfo.hashCode());
	}

	@Test
	public void removeSymTableTest() {
		SymTable symTable = new SymTable(10);

		symTable.add("a", Kind.VAR, Type.INT);
		symTable.add("b", Kind.VAR, Type.INT);
		symTable.add("c", Kind.VAR, Type.INT);

		SymNode symNode = symTable.search("a").get();
		InstNode instNode = new InstNode(1, symNode, null);
		symNode.addInstance(instNode);

		Assert.assertFalse(symTable.remove(null));
		Assert.assertNotNull(symTable.search("a"));
		Assert.assertNotNull(symTable.search("b"));
		Assert.assertNotNull(symTable.search("c"));
	}

	@Test
	public void symNodeTest() {
		SymNode a = new SymNode("a", new MemInfo(Type.INT, Kind.VAR, 1));
		SymNode b = new SymNode("b", new MemInfo(Type.INT, Kind.VAR, 1));
		SymNode c = new SymNode("c", new MemInfo(Type.INT, Kind.VAR, 1));

		a.addSymbol(b);
		a.addSymbol(c);
		// a -> b -> c
		Assert.assertEquals(b, a.getNextSymbol());
		Assert.assertEquals(c, b.getNextSymbol());
		Assert.assertEquals(null, c.getNextSymbol());

		Assert.assertThrows(NullPointerException.class, () -> a.addSymbol(null));

		Assert.assertFalse(a.removeInstance(null));

		InstNode inst1 = new InstNode(1, a, null);
		InstNode inst2 = new InstNode(2, a, null);
		a.addInstance(inst1);
		a.addInstance(inst2);

		Assert.assertTrue(a.removeInstance(inst1));
		Assert.assertEquals(inst2, a.getFirstInstance());
	}

	@Test
	public void instNodeTest() {
		Assert.assertThrows(NullPointerException.class, () -> new InstNode(1, null, null));
		SymNode a = new SymNode("a", new MemInfo(Type.INT, Kind.VAR, 1));

		InstNode stack = new InstNode("stack", a, null);
		InstNode inst1 = new InstNode(1, a, stack);
		InstNode inst2 = new InstNode(2, a, stack);
		InstNode inst3 = new InstNode(3, a, stack);

		inst1.setNextInstance(inst2);
		inst2.setNextInstance(inst3);
		// inst1 -> inst2 -> inst3

		Assert.assertFalse(inst1.removeInstance(null));
		Assert.assertFalse(stack.removeInstance(inst1));
		Assert.assertTrue(inst1.removeInstance(inst3));
	}
}
