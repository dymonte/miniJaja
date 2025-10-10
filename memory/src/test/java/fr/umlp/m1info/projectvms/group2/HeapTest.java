package fr.umlp.m1info.projectvms.group2;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import fr.umlp.m1info.projectvms.group2.exception.HeapInvalidAddress;
import fr.umlp.m1info.projectvms.group2.exception.HeapOutOfBounds;
import fr.umlp.m1info.projectvms.group2.exception.HeapOutOfMemory;
import fr.umlp.m1info.projectvms.group2.heap.Heap;
import fr.umlp.m1info.projectvms.group2.symtable.Type;

/**
 * HeapTest
 */
public class HeapTest {
	private Heap heap;

    @Before
    public void setup() {
        heap = new Heap();
    }

	@Test
	public void testAllocation() {
		int arraySize = 10;
		int addr = heap.allocate(arraySize, Type.INT);
		Assert.assertTrue(addr >= 0);
	}

	@Test
	public void testAllocationVoidArray() {
		int arraySize = 10;
		Assert.assertThrows(IllegalArgumentException.class, () -> heap.allocate(arraySize, Type.VOID));
	}

	@Test
	public void testTooMuchAllocation() {
		Assert.assertThrows(HeapOutOfMemory.class, () -> heap.allocate(Heap.HEAP_SIZE + 1, Type.INT));
		heap.allocate(Heap.HEAP_SIZE - 1, Type.INT);
		Assert.assertThrows(HeapOutOfMemory.class, () -> heap.allocate(10, Type.INT));
	}

	@Test
	public void testSearchElement() {
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
		int arraySize = 10;
		int addr = heap.allocate(arraySize, Type.INT);
		for (int i = 0; i < arraySize; i++) {
			heap.setValue(addr, i, i);
		}
		Assert.assertThrows(HeapOutOfBounds.class, () -> heap.search(addr, arraySize));

	}

	@Test
	public void testSearchAfterFree() {
		int arraySize = 10;
		int addr = heap.allocate(arraySize, Type.INT);
		heap.decRef(addr);
		Assert.assertThrows(HeapInvalidAddress.class, () -> heap.search(addr, 0));
	}

	@Test
	public void testFree() {
		int arraySize = 10;
		int addr = heap.allocate(arraySize, Type.INT);
		heap.setValue(addr, 0, 10);
		Assert.assertEquals((int) heap.search(addr, 0), 10);
		heap.decRef(addr);
		Assert.assertThrows(HeapInvalidAddress.class, () -> heap.search(addr, 0));
	}

	@Test
	public void testFreeMultipleRefs() {
		int arraySize = 10;
		int addr = heap.allocate(arraySize, Type.INT);
		heap.incRef(addr);
		heap.decRef(addr);
		Assert.assertEquals((int) heap.search(addr, 0), 0);
		heap.decRef(addr);
		Assert.assertThrows(HeapInvalidAddress.class, () -> heap.search(addr, 0));
	}

	@Test
    public void testSimpleFreeAndMerge() throws HeapInvalidAddress {
        int a1 = heap.allocate(4, Type.INT);
        int a2 = heap.allocate(4, Type.INT);

		heap.decRef(a1);
        heap.decRef(a2);

        int large = heap.allocate(8, Type.INT);
        Assert.assertTrue(large >= 0);
    }

	@Test
    public void testRecursiveMerge() throws HeapInvalidAddress {
        int a1 = heap.allocate(4, Type.INT);
        int a2 = heap.allocate(4, Type.INT);
        int a3 = heap.allocate(4, Type.INT);
        int a4 = heap.allocate(4, Type.INT);

        heap.decRef(a1);
        heap.decRef(a2);
        heap.decRef(a3);
        heap.decRef(a4);

        // Merge twice to get a block of 16
        int large = heap.allocate(16, Type.INT);
        Assert.assertTrue(large >= 0);
    }
}
