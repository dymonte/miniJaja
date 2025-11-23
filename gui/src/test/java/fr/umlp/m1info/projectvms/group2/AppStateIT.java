package fr.umlp.m1info.projectvms.group2;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.Before;
import org.junit.Test;

import fr.umlp.m1info.projectvms.group2.memory.Memory;
import fr.umlp.m1info.projectvms.group2.memory.heap.Heap;
import fr.umlp.m1info.projectvms.group2.memory.stack.Stack;
import fr.umlp.m1info.projectvms.group2.memory.symtable.SymTable;

public class AppStateIT {

	private AppState appState;

	@Before
	public void setUp() {
		appState = new AppState();
	}

	@Test
	public void testWorkspaceDir() {
		assertFalse("Workspace should be empty initially", appState.getWorkspaceDir().isPresent());

		File dir = new File("testWorkspace");
		appState.setWorkspaceDir(dir);
		assertTrue("Workspace should be present after setting", appState.getWorkspaceDir().isPresent());
		assertEquals("Workspace should match the file set", dir, appState.getWorkspaceDir().get());
	}

	@Test
	public void testWorkspaceFiles() {
		assertFalse("Workspace files should be empty initially", appState.getWorkspaceFiles().isPresent());

		File[] files = { new File("file1.txt"), new File("file2.txt") };
		appState.setWorkspaceFiles(files);
		assertTrue("Workspace files should be present after setting", appState.getWorkspaceFiles().isPresent());
		assertArrayEquals("Workspace files should match the files set", files, appState.getWorkspaceFiles().get());
	}

	@Test
	public void testOpenedFile() {
		assertFalse("Opened file should be empty initially", appState.getOpenedFile().isPresent());

		File file = new File("opened.txt");
		appState.setOpenedFile(file);
		assertTrue("Opened file should be present after setting", appState.getOpenedFile().isPresent());
		assertEquals("Opened file should match the file set", file, appState.getOpenedFile().get());

		appState.closeOpenedFile();
		assertFalse("Opened file should be empty after closing", appState.getOpenedFile().isPresent());
	}

	@Test
	public void testCodeProperty() {
		assertNotNull("Code property should not be null initially", appState.codeProperty());
		assertEquals("Initial code should be empty", "", appState.codeProperty().get());

		String testCode = "print('Hello World')";
		appState.codeProperty().set(testCode);
		assertEquals("Code property should reflect the value set", testCode, appState.codeProperty().get());
	}

	@Test
	public void testMemory() {
		assertNull("Memory should be null initially", appState.getMemory());
		Memory memory = new Memory(new SymTable(10), new Stack(10), new Heap(10)); // Assuming default constructor
		appState.setMemory(memory);
		assertEquals("Memory should match the object set", memory, appState.getMemory());
	}
}
