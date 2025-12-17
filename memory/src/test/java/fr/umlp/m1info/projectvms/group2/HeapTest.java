package fr.umlp.m1info.projectvms.group2;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import fr.umlp.m1info.projectvms.group2.exception.HeapInvalidAddressException;
import fr.umlp.m1info.projectvms.group2.exception.HeapOutOfBoundsException;
import fr.umlp.m1info.projectvms.group2.exception.HeapOutOfMemoryException;
import fr.umlp.m1info.projectvms.group2.memory.heap.Heap;
import fr.umlp.m1info.projectvms.group2.utils.Type;

/**
 * HeapTest
 */
public class HeapTest {
	private Heap heap;

	@Before
	public void setup() {
		heap = new Heap(1024);
	}

	@Test
	public void testConstructorMaxSizeZero() {
		Assert.assertThrows(IllegalArgumentException.class, () -> heap = new Heap(0));
	}

	@Test
	public void testAllocation() {
		int arraySize = 10;
		int addr = heap.allocate(arraySize, Type.INT);
		Assert.assertTrue(addr >= 0);
		addr = heap.allocate(arraySize, Type.BOOL);
		Assert.assertTrue(addr >= 0);
	}

	@Test
	public void testAllocationEmpty() {
		Assert.assertThrows(IllegalArgumentException.class, () -> heap.allocate(0, Type.INT));
	}

	@Test
	public void testAllocationVoidArray() {
		int arraySize = 10;
		Assert.assertThrows(IllegalArgumentException.class, () -> heap.allocate(arraySize, Type.VOID));
	}

	@Test
	public void testTooMuchAllocation() {
		int size1 = heap.getHeapSize() + 1;
		Assert.assertThrows(HeapOutOfMemoryException.class, () -> heap.allocate(size1, Type.INT));
		int size2 = heap.getHeapSize() - 1;
		heap.allocate(size2, Type.INT);
		int size3 = 10;
		Assert.assertThrows(HeapOutOfMemoryException.class, () -> heap.allocate(size3, Type.INT));
		int size4 = heap.getHeapSize() * 2;
		Assert.assertThrows(HeapOutOfMemoryException.class, () -> heap.allocate(size4, Type.INT));
	}

	@Test
	public void testTooMuchSmallAllocations() {
		heap = new Heap(10);
		for (int i = 0; i < 8; i++) {
			heap.allocate(1, Type.BOOL);
		}
		Assert.assertThrows(HeapOutOfMemoryException.class, () -> heap.allocate(1, Type.BOOL));
	}

	@Test
	public void testSearchElement() {
		int arraySize = 10;
		for (Type t : Type.values()) {
			if (t == Type.VOID) {
				continue;
			}
			int addr = heap.allocate(arraySize, Type.INT);
			for (int i = 0; i < arraySize; i++) {
				heap.setValue(addr, i, i);
			}
			for (int i = 0; i < arraySize; i++) {
				Assert.assertEquals((int) heap.search(addr, i), i);
			}
		}
	}

	@Test
	public void testSearchOutOfBounds() {
		int arraySize = 10;
		int addr = heap.allocate(arraySize, Type.INT);
		for (int i = 0; i < arraySize; i++) {
			heap.setValue(addr, i, i);
		}
		Assert.assertThrows(HeapOutOfBoundsException.class, () -> heap.search(addr, arraySize));
		Assert.assertThrows(HeapInvalidAddressException.class, () -> heap.search(-1, arraySize));
		Assert.assertThrows(HeapInvalidAddressException.class, () -> heap.search(2048, arraySize));

	}

	@Test
	public void testSetValueInvalidAddress() {
		Assert.assertThrows(HeapInvalidAddressException.class, () -> heap.setValue(-1, 0, 0));
		Assert.assertThrows(HeapInvalidAddressException.class, () -> heap.setValue(1, 0, 0));
		Assert.assertThrows(HeapInvalidAddressException.class, () -> heap.setValue(2048, 0, 0));
	}

	@Test
	public void testSearchAfterFree() {
		int arraySize = 10;
		int addr = heap.allocate(arraySize, Type.INT);
		heap.decRef(addr);
		Assert.assertThrows(HeapInvalidAddressException.class, () -> heap.search(addr, 0));
	}

	@Test
	public void testFree() {
		int arraySize = 10;
		int addr = heap.allocate(arraySize, Type.INT);
		heap.setValue(addr, 0, 10);
		Assert.assertEquals(10, (int) heap.search(addr, 0));
		heap.decRef(addr);
		Assert.assertThrows(HeapInvalidAddressException.class, () -> heap.search(addr, 0));
	}

	@Test
	public void testFreeMultipleRefs() {
		int arraySize = 10;
		int addr = heap.allocate(arraySize, Type.INT);
		heap.incRef(addr);
		heap.decRef(addr);
		Assert.assertEquals(0, (int) heap.search(addr, 0));
		heap.decRef(addr);
		Assert.assertThrows(HeapInvalidAddressException.class, () -> heap.search(addr, 0));
	}

	@Test
	public void incRefException() {
		Assert.assertThrows(HeapInvalidAddressException.class, () -> heap.incRef(-1));
		Assert.assertThrows(HeapInvalidAddressException.class, () -> heap.incRef(1));
		heap.allocate(1, Type.INT);
		heap.decRef(0);
		Assert.assertThrows(HeapInvalidAddressException.class, () -> heap.incRef(0));
	}

	@Test
	public void decRefException() {
		Assert.assertThrows(HeapInvalidAddressException.class, () -> heap.decRef(-1));
		Assert.assertThrows(HeapInvalidAddressException.class, () -> heap.decRef(1));
		heap.allocate(1, Type.INT);
		heap.decRef(0);
		Assert.assertThrows(HeapInvalidAddressException.class, () -> heap.decRef(0));
	}

	@Test
	public void testSimpleFreeAndMerge() throws HeapInvalidAddressException {
		int a1 = heap.allocate(4, Type.INT);
		int a2 = heap.allocate(4, Type.INT);

		heap.decRef(a1);
		heap.decRef(a2);

		int large = heap.allocate(8, Type.INT);
		Assert.assertTrue(large >= 0);
	}

	@Test
	public void testRecursiveMerge() throws HeapInvalidAddressException {
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

	@Test
	public void testAllocationBool() {
		int arraySize = 10;
		int addr = heap.allocate(arraySize, Type.BOOL);
		Assert.assertTrue(addr >= 0);
	}

	@Test
	public void testSetGetValueBool() {
		int arraySize = 10;
		int addr = heap.allocate(arraySize, Type.BOOL);
		for (int i = 0; i < arraySize; i++) {
			heap.setValue(addr, i, i % 2 == 0);
		}
		for (int i = 0; i < arraySize; i++) {
			Assert.assertEquals(i % 2 == 0, (boolean) heap.search(addr, i));
		}
	}

	@Test
	public void testSetValueWrongType() {
		int addr = heap.allocate(1, Type.INT);
		Assert.assertThrows(IllegalArgumentException.class, () -> heap.setValue(addr, 0, true));
	}

	@Test
	public void testSplit() {
		int addr = heap.allocate(256, Type.INT);
		heap.decRef(addr);
		int addr1 = heap.allocate(128, Type.INT);
		int addr2 = heap.allocate(128, Type.INT);
		Assert.assertTrue(addr1 >= 0);
		Assert.assertTrue(addr2 >= 0);
	}

	@Test
	public void testSetValueException() {
		heap.allocate(1, Type.INT);
		heap.decRef(0);
		Assert.assertThrows(HeapInvalidAddressException.class, () -> heap.setValue(0, 0, 1));
	}

	@Test
	public void testSetValueException2() {
		heap.allocate(1, Type.BOOL);
		Assert.assertThrows(IllegalArgumentException.class, () -> heap.setValue(0, 0, 1));
	}

	@Test
	public void testHeapToString() {
		Heap heap = new Heap(4);
		ByteArrayOutputStream content = new ByteArrayOutputStream();
		PrintStream originalOut = System.out;
		System.setOut(new PrintStream(content));
		System.out.println(heap.toString());
		Assert.assertTrue(content.toString().contains("0x0000: 00 00 00 00"));
		System.setOut(originalOut);
	}
}
