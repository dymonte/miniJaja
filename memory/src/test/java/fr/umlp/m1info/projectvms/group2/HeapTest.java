package fr.umlp.m1info.projectvms.group2;

import org.junit.Test;

import fr.umlp.m1info.projectvms.group2.exception.HeapInvalidAddress;
import fr.umlp.m1info.projectvms.group2.exception.HeapOutOfBounds;
import fr.umlp.m1info.projectvms.group2.exception.HeapOutOfMemory;
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
		Heap heap = new Heap();
		int arraySize = 10;
		Assert.assertThrows(IllegalArgumentException.class, () -> heap.allocate(arraySize, Type.VOID));
	}

	@Test
	public void testTooMuchAllocation() {
		Heap heap = new Heap();
		Assert.assertThrows(HeapOutOfMemory.class, () -> heap.allocate(Heap.HEAP_SIZE + 1, Type.INT));
		heap.allocate(Heap.HEAP_SIZE - 1, Type.INT);
		Assert.assertThrows(HeapOutOfMemory.class, () -> heap.allocate(10, Type.INT));
	}

	@Test
	public void testSetValue() {
		Heap heap = new Heap();
		int arraySize = 10;
		int addr = heap.allocate(arraySize, Type.INT);
		for (int i = 0; i < arraySize; i++) {
			Assert.assertTrue(heap.setValue(addr, i, i));
		}
	}

	@Test
	public void testSearchElement() {
		Heap heap = new Heap();
		int arraySize = 10;
		int addr = heap.allocate(arraySize, Type.INT);
		for (int i = 0; i < arraySize; i++) {
			heap.setValue(addr, i, i);
		}
		for (int i = 0; i < arraySize; i++) {
			Assert.assertEquals((int) heap.search(addr, i), i);
		}
	}

	@Test
	public void testSearchOutOfBounds() {
		Heap heap = new Heap();
		int arraySize = 10;
		int addr = heap.allocate(arraySize, Type.INT);
		for (int i = 0; i < arraySize; i++) {
			heap.setValue(addr, i, i);
		}
		Assert.assertThrows(HeapOutOfBounds.class, () -> heap.search(addr, arraySize));

	}

	@Test
	public void testSearchAfterFree() {
		Heap heap = new Heap();
		int arraySize = 10;
		int addr = heap.allocate(arraySize, Type.INT);
		heap.free(addr);
		Assert.assertThrows(HeapInvalidAddress.class, () -> heap.search(addr, 0));
	}

	@Test
	public void testFree() {
		Heap heap = new Heap();
		int arraySize = 10;
		int addr = heap.allocate(arraySize, Type.INT);
		heap.setValue(addr, 0, 10);
		Assert.assertEquals((int) heap.search(addr, 0), 10);
		heap.free(addr);
		Assert.assertThrows(HeapInvalidAddress.class, () -> heap.search(addr, 0));
	}
}
