package fr.umlp.m1info.projectvms.group2.memory.heap;

import java.nio.ByteBuffer;
import java.util.ArrayList;

import fr.umlp.m1info.projectvms.group2.exception.HeapInvalidAddressException;
import fr.umlp.m1info.projectvms.group2.exception.HeapOutOfBoundsException;
import fr.umlp.m1info.projectvms.group2.exception.HeapOutOfMemoryException;
import fr.umlp.m1info.projectvms.group2.memory.symtable.Type;

/**
 * Heap
 */
public class Heap {
	private final int HEAP_SIZE;
	private ByteBuffer heap;
	private HeapBlockNode[] blocks;
	private ArrayList<HeapSymbol> localTable;

	/**
	 * Constructs a new Heap
	 *
	 * @throws IllegalArgumentException when heapSize <= 0 (empty heap)
	 */
	public Heap(int heapSize) {
		if (heapSize <= 0) {
			throw new IllegalArgumentException("Cannot create an empty sized heap.");
		}

		this.HEAP_SIZE = heapSize;
		this.heap = ByteBuffer.allocate(HEAP_SIZE);

		/**
		 * This array is of size log2(HEAP_SIZE).
		 * each index i corresponds to blocks of size 2^(i-1)
		 * for instance, addresses of blocks of size 256 are stored at index 7.
		 */
		this.blocks = new HeapBlockNode[(int) (Math.log(HEAP_SIZE) / Math.log(2)) + 1];
		this.blocks[blocks.length - 1] = new HeapBlockNode(0);
		this.localTable = new ArrayList<>();
	}

	/**
	 * @return The heap size
	 */
	public int getHeapSize() {
		return HEAP_SIZE;
	}

	/**
	 * Allocates a block of memory of given size and type in the heap
	 * 
	 * @param size The number of elements to allocate
	 * @param type The type of the elements to allocate
	 * @return The address in the heap local table of the newly allocated data
	 *         (note: this should then be stored as the variable value in the
	 *         stack/symtable)
	 */
	public int allocate(int size, Type type) {
		if (type == Type.VOID) {
			throw new IllegalArgumentException("Cannot allocate array of void");
		}
		if (size <= 0) {
			throw new IllegalArgumentException("Size must be positive");
		}
		if (size >= HEAP_SIZE) {
			throw new HeapOutOfMemoryException();
		}

		// Check type size
		int requestedSize = (type == Type.INT) ? size * 4 : size;

		// Search block > size
		int blockIndex = (int) Math.ceil(Math.log(size) / Math.log(2));
		int blockSize = 1 << blockIndex;

		if (blockSize > HEAP_SIZE) {
			throw new HeapOutOfMemoryException();
		}

		HeapBlockNode block = getFreeBlock(blockIndex);

		// Add to local table
		HeapSymbol symbol = new HeapSymbol(block.getAddress(), requestedSize, blockSize, type);
		localTable.add(symbol);

		return localTable.size() - 1;
	}

	/**
	 * Searches for a free block of the given index (size 2^index)
	 * If none is found, searches for a bigger block and splits it until the
	 * desired size is reached
	 * 
	 * @param blockIndex The index of the desired block size
	 * @return A free block of the desired size
	 */
	private HeapBlockNode getFreeBlock(int blockIndex) {

		if (blocks[blockIndex] != null) {
			HeapBlockNode block = blocks[blockIndex];
			blocks[blockIndex] = block.next; // Remove block from list
			return block;
		}

		// Search free block in HeapBlockNode blocks
		int biggerIndex = blockIndex + 1;
		while (biggerIndex < blocks.length && blocks[biggerIndex] == null) {
			biggerIndex++;
		}
		if (biggerIndex >= blocks.length) {
			throw new HeapOutOfMemoryException();
		}
		HeapBlockNode biggerBlock = blocks[biggerIndex];
		blocks[biggerIndex] = biggerBlock.next; // Remove block from list

		// Split until we reach the desired block size
		while (biggerIndex > blockIndex) {
			biggerIndex--;
			int halfSize = 1 << biggerIndex;
			int addr1 = biggerBlock.getAddress();
			int addr2 = addr1 + halfSize;

			HeapBlockNode buddy = new HeapBlockNode(addr2);
			buddy.next = blocks[biggerIndex]; // Add to list
			blocks[biggerIndex] = buddy;
		}

		return biggerBlock;
	}

	/**
	 * Increments the reference count of the block at the given address
	 * 
	 * @param address
	 * @throws HeapInvalidAddressException
	 */
	public void incRef(int address) throws HeapInvalidAddressException {
		if (address < 0 || address >= localTable.size() || localTable.get(address) == null) {
			throw new HeapInvalidAddressException(address);
		}
		localTable.get(address).incRef();
	}

	/**
	 * Decrements the reference count of the block at the given address
	 * 
	 * @param address
	 * @throws HeapInvalidAddressException
	 */
	public void decRef(int address) throws HeapInvalidAddressException {
		if (address < 0 || address >= localTable.size() || localTable.get(address) == null) {
			throw new HeapInvalidAddressException(address);
		}
		localTable.get(address).decRef();

		if (localTable.get(address).getRef() <= 0) {
			free(address);
		}
	}

	/**
	 * Frees a block of memory at the given address
	 * 
	 * @param address The address of the block to free
	 */
	private void free(int address) throws HeapInvalidAddressException {
		if (address < 0 || address >= localTable.size() || localTable.get(address) == null) {
			throw new HeapInvalidAddressException(address);
		}

		HeapSymbol symbol = localTable.get(address);
		int blockAddr = symbol.getAddress();
		int blockSize = localTable.get(address).getSize();
		int blockIndex = (int) Math.ceil(Math.log(blockSize) / Math.log(2));

		localTable.set(address, null);

		// Replace the block in the free list
		HeapBlockNode newBlock = new HeapBlockNode(blockAddr);
		newBlock.next = blocks[blockIndex];
		blocks[blockIndex] = newBlock;

		mergeBlocks(blockIndex);
	}

	/**
	 * Merges free blocks starting from the given block index
	 * 
	 * @param blockIndex The index of the block size to start merging from
	 */
	private void mergeBlocks(int blockIndex) {
		boolean merged = true;

		while (merged && blockIndex < blocks.length - 1) {
			merged = false;

			HeapBlockNode prev = null;
			HeapBlockNode curr = blocks[blockIndex];

			// For each block, search for its buddy
			while (curr != null) {
				int buddyAddr = curr.address ^ (1 << blockIndex);
				HeapBlockNode search = curr.next;
				HeapBlockNode searchPrev = curr;

				// search for buddy
				while (search != null) {
					if (search.address == buddyAddr) {
						// Buddy found!
						int mergedAddr = Math.min(curr.address, search.address);

						// Remove both blocks from the list
						if (prev == null) {
							blocks[blockIndex] = curr.next;
						} else {
							prev.next = curr.next;
						}

						if (searchPrev == curr) {
							// special case: buddy is right after
							curr.next = search.next;
						} else {
							searchPrev.next = search.next;
						}

						// Add merged block to upper list
						HeapBlockNode mergedBlock = new HeapBlockNode(mergedAddr);
						mergedBlock.next = blocks[blockIndex + 1];
						blocks[blockIndex + 1] = mergedBlock;

						// Restart merging for upper size
						merged = true;
						break;
					}

					searchPrev = search;
					search = search.next;
				}

				if (merged) {
					break;
				}

				prev = curr;
				curr = curr.next;
			}

			if (merged) {
				blockIndex++;
			}
		}
	}

	/**
	 * Searches for a value in the heap at the given address and index
	 * 
	 * @param address The address to search
	 * @param index   The index of the value to search
	 * @return The value found at the given address and index
	 * @throws HeapOutOfBoundsException    If the address is out of bounds
	 * @throws HeapInvalidAddressException If the address is invalid
	 */
	public <T> T search(int address, int index) throws HeapOutOfBoundsException, HeapInvalidAddressException {
		if (address < 0 || address >= localTable.size()) {
			throw new HeapInvalidAddressException(address);
		}

		HeapSymbol symbol = localTable.get(address);
		if (symbol == null) {
			throw new HeapInvalidAddressException(address);
		}

		int baseAddr = symbol.getAddress();
		int typeSize = (symbol.getType() == Type.INT) ? 4 : 1;
		int offset = index * typeSize;

		if (offset + typeSize > symbol.getSize()) {
			throw new HeapOutOfBoundsException(address, index);
		}

		heap.position(baseAddr + offset);

		Object value;
		if (symbol.getType() == Type.INT) {
			value = heap.getInt();
		} else if (symbol.getType() == Type.BOOL) {
			byte b = heap.get();
			value = (b != 0);
		} else {
			throw new IllegalArgumentException("Unsupported type: " + symbol.getType());
		}

		return (T) value;
	}

	/**
	 * Sets a value in the heap at the given address and index
	 * 
	 * @param address The address to set the value at
	 * @param index   The index of the value to set
	 * @param value   The value to set
	 * @throws HeapOutOfBoundsException    If the address is out of bounds
	 * @throws HeapInvalidAddressException If the address is invalid
	 */
	public <T> void setValue(int address, int index, T value)
			throws HeapOutOfBoundsException, HeapInvalidAddressException {
		if (address < 0 || address >= localTable.size()) {
			throw new HeapInvalidAddressException(address);
		}

		HeapSymbol symbol = localTable.get(address);
		if (symbol == null) {
			throw new HeapInvalidAddressException(address);
		}

		int baseAddr = symbol.getAddress();
		int typeSize = (symbol.getType() == Type.INT) ? 4 : 1;
		int offset = index * typeSize;

		heap.position(baseAddr + offset);

		if (symbol.getType() == Type.INT) {
			if (!(value instanceof Integer)) {
				throw new IllegalArgumentException("Expected Integer for type INT");
			}
			heap.putInt((Integer) value);

		} else if (symbol.getType() == Type.BOOL) {
			if (!(value instanceof Boolean)) {
				throw new IllegalArgumentException("Expected Boolean for type BOOL");
			}
			heap.put((byte) ((Boolean) value ? 1 : 0));
		}
	}
}
