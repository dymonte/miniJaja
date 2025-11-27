package fr.umlp.m1info.projectvms.group2;

import org.junit.jupiter.api.Assertions;

import java.io.File;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import fr.umlp.m1info.projectvms.group2.memory.Memory;
import fr.umlp.m1info.projectvms.group2.memory.heap.Heap;
import fr.umlp.m1info.projectvms.group2.memory.stack.Stack;
import fr.umlp.m1info.projectvms.group2.memory.symtable.SymTable;

public class AppStateIT {

	private AppState appState;

	@BeforeEach
	public void setUp() {
		appState = new AppState();
	}

	@Test
	public void testWorkspaceDir() {
		Assertions.assertFalse(appState.getWorkspaceDir().isPresent(), "Workspace should be empty initially");

		File dir = new File("testWorkspace");
		appState.setWorkspaceDir(dir);
		Assertions.assertTrue(appState.getWorkspaceDir().isPresent(), "Workspace should be present after setting");
		Assertions.assertEquals(dir, appState.getWorkspaceDir().get(), "Workspace should match the file set");
	}

	@Test
	public void testWorkspaceFiles() {
		Assertions.assertFalse(appState.getWorkspaceFiles().isPresent(), "Workspace files should be empty initially");

		File[] files = { new File("file1.txt"), new File("file2.txt") };
		appState.setWorkspaceFiles(files);
		Assertions.assertTrue(appState.getWorkspaceFiles().isPresent(),
				"Workspace files should be present after setting");
		Assertions.assertArrayEquals(files, appState.getWorkspaceFiles().get(),
				"Workspace files should match the files set");
	}

	@Test
	public void testOpenedFile() {
		Assertions.assertFalse(appState.getOpenedFile().isPresent(), "Opened file should be empty initially");

		File file = new File("opened.txt");
		appState.setOpenedFile(file);
		Assertions.assertTrue(appState.getOpenedFile().isPresent(), "Opened file should be present after setting");
		Assertions.assertEquals(file, appState.getOpenedFile().get(), "Opened file should match the file set");

		appState.closeOpenedFile();
		Assertions.assertFalse(appState.getOpenedFile().isPresent(), "Opened file should be empty after closing");
	}

	@Test
	public void testCodeProperty() {
		Assertions.assertNotNull(appState.codeProperty(), "Code property should not be null initially");
		Assertions.assertEquals("", appState.codeProperty().get(), "Initial code should be empty");

		String testCode = "print('Hello World')";
		appState.codeProperty().set(testCode);
		Assertions.assertEquals(testCode, appState.codeProperty().get(), "Code property should reflect the value set");
	}

	@Test
	public void testMemory() {
		Assertions.assertNull(appState.getMemory(), "Memory should be null initially");
		Memory memory = new Memory(new SymTable(10), new Stack(10), new Heap(10)); // Assuming default constructor
		appState.setMemory(memory);
		Assertions.assertEquals(memory, appState.getMemory(), "Memory should match the object set");
	}
}
