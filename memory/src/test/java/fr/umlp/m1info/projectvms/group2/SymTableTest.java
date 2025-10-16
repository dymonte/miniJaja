package fr.umlp.m1info.projectvms.group2;

import java.util.Optional;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import fr.umlp.m1info.projectvms.group2.memory.symtable.InstNode;
import fr.umlp.m1info.projectvms.group2.memory.symtable.MemInfo;

import fr.umlp.m1info.projectvms.group2.memory.symtable.SymNode;
import fr.umlp.m1info.projectvms.group2.memory.symtable.SymTable;
import fr.umlp.m1info.projectvms.group2.memory.symtable.ObjectKind;
import fr.umlp.m1info.projectvms.group2.memory.symtable.Type;

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
		Optional<SymNode> s = symTable.add("i", ObjectKind.VAR, Type.INT);
		Assert.assertTrue(s.isPresent());
		Assert.assertEquals(s.get().getIdent(), "i");
		Assert.assertEquals(s.get().getInfo().getKind(), ObjectKind.VAR);
		Assert.assertEquals(s.get().getInfo().getSorte(), Type.INT);
	}

	@Test
	public void addSameIdentifierTest() {
		Optional<SymNode> s1 = symTable.add("i", ObjectKind.VAR, Type.INT);
		Optional<SymNode> s2 = symTable.add("i", ObjectKind.VAR, Type.INT);
		Assert.assertTrue(s1.isPresent());
		Assert.assertTrue(s2.isPresent());
		Assert.assertEquals(s1.get(), s2.get());
	}

	@Test
	public void addDifferentIdentifierSameHashTest() {
		Optional<SymNode> s1 = symTable.add("ab", ObjectKind.VAR, Type.INT);
		Optional<SymNode> s2 = symTable.add("ba", ObjectKind.METH, Type.BOOL);
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
		Assert.assertThrows(IllegalArgumentException.class, () -> symTable.add("i", ObjectKind.VAR, Type.INT, -1));
	}

	@Test
	public void addNullSizedArrayTest() {
		Assert.assertThrows(IllegalArgumentException.class, () -> symTable.add("i", ObjectKind.VAR, Type.INT, 0));
	}

	@Test
	public void searchTest() {
		Optional<SymNode> s1 = symTable.add("i", ObjectKind.VAR, Type.INT);
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
	public void addSameIdentifierShouldFailTest() {
		symTable.add("i", ObjectKind.VAR, Type.INT);
		Optional<SymNode> s2 = symTable.add("i", ObjectKind.VAR, Type.BOOL);
		Assert.assertFalse(s2.isPresent());

		symTable.add("j", ObjectKind.VAR, Type.INT);
		Optional<SymNode> s3 = symTable.add("j", ObjectKind.METH, Type.INT);
		Assert.assertFalse(s3.isPresent());

		symTable.add("k", ObjectKind.VAR, Type.INT, 1);
		Optional<SymNode> s4 = symTable.add("k", ObjectKind.VAR, Type.INT, 2);
		Assert.assertFalse(s4.isPresent());

		Optional<SymNode> s5 = symTable.add("l", ObjectKind.VAR, Type.INT, 1);
		Optional<SymNode> s6 = symTable.add("l", ObjectKind.VAR, Type.INT, 1);
		Assert.assertTrue(s5.isPresent());
		Assert.assertTrue(s6.isPresent());
		Assert.assertEquals(s5, s6);
	}

	@Test
	public void removeTest() {
		Optional<SymNode> s = symTable.add("i", ObjectKind.VAR, Type.INT);
		Assert.assertTrue(s.isPresent());
		new InstNode(0, s.get(), null);
		Assert.assertTrue(symTable.remove(s.get().getFirstInstance()));
		Assert.assertFalse(symTable.search("i").isPresent());
	}

	@Test
	public void removeOneOfMultipleInstancesTest() {
		Optional<SymNode> s = symTable.add("i", ObjectKind.VAR, Type.INT);
		Assert.assertTrue(s.isPresent());
		new InstNode(0, s.get(), null);
		new InstNode(1, s.get(), null);
		Assert.assertTrue(symTable.remove(s.get().getFirstInstance()));
		Assert.assertTrue(symTable.search("i").isPresent());
	}

	@Test
	public void removeNonExistentInstanceTest() {
		Optional<SymNode> s = symTable.add("i", ObjectKind.VAR, Type.INT);
		Assert.assertTrue(s.isPresent());
		new InstNode(0, s.get(), null);
		SymNode sym = new SymNode("b", new MemInfo(Type.INT, ObjectKind.VAR, 1));
		InstNode nonExistent = new InstNode(1, sym, null);
		symTable.remove(nonExistent);
		Assert.assertFalse(symTable.remove(nonExistent));
		Assert.assertTrue(symTable.search("i").isPresent());
	}

	@Test
	public void removeFromEmptyTableTest() {
		SymNode sym = new SymNode("i", new MemInfo(Type.INT, ObjectKind.VAR, 1));
		InstNode nonExistent = new InstNode(0, sym, null);
		Assert.assertFalse(symTable.remove(nonExistent));
	}

	@Test
	public void removeNullTest() {
		Assert.assertFalse(symTable.remove(null));
	}
}
