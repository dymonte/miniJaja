package fr.umlp.m1info.projectvms.group2;

import org.junit.Test;

import fr.umlp.m1info.projectvms.group2.heap.Heap;
import fr.umlp.m1info.projectvms.group2.symtable.Type;

import org.junit.Assert;

/**
 * HeapTest
 */
public class HeapTest {

	@Test
	public void testAllocation() {
		Heap heap = new Heap();
		int arraySize = 10;
		int addr = heap.allocate(arraySize, Type.INT);
		Assert.assertTrue(addr >= 0);
	}

	@Test
	public void testAllocationVoidArray() {
		// TODO: implement this
	}

	@Test
	public void testTooMuchAllocation() {
		// TODO: implement this
	}

	@Test
	public void testGarbageCollector() {
		// TODO: implement this
	}

	@Test
	public void testSetValue() {
		Heap heap = new Heap();
		int arraySize = 10;
		int addr = heap.allocate(arraySize, Type.INT);
		for (int i = 0; i < arraySize; i++) {
			Assert.assertTrue(heap.setValue(addr, i));
		}
	}

	@Test
	public void testSearchElement() {
		Heap heap = new Heap();
		int arraySize = 10;
		int addr = heap.allocate(arraySize, Type.INT);
		for (int i = 0; i < arraySize; i++) {
			heap.setValue(addr, i);
		}
		for (int i = 0; i < arraySize; i++) {
			Assert.asserEquals(heap.search(addr, i), i);
		}
	}

	@Test
	public void testSearchOutOfBounds() {
		// TODO: implement this
	}

	@Test
	public void testSearchAfterFree() {
		// TODO: implement this
	}

}
