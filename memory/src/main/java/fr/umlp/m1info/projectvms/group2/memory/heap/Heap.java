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
	private final int INT_SIZE = 4;
	private final int BOOL_SIZE = 1;

	/**
	 * Constructs a new Heap
	 *
	 * @throws IllegalArgumentException when heapSize <= 0 (empty heap)
	 */
	public Heap(int heapSize) throws IllegalArgumentException {
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
	 * @throws IllegalArgumentException thrown if arguments size <= 0 or type is
	 *                                  VOID
	 * @throws HeapOutOfMemoryException when the argument size >= HEAP_SIZE
	 *                                  or no more space is available
	 */
	public int allocate(int size, Type type) throws IllegalArgumentException, HeapOutOfMemoryException {
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
		int requestedSize = (type == Type.INT) ? (size * INT_SIZE) : size;

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
	 * @throws HeapOutOfMemoryException If there is not enough memory to allocate
	 * @throws IllegalArgumentException If the blockIndex is invalid
	 */
	private HeapBlockNode getFreeBlock(int blockIndex) throws HeapOutOfMemoryException, IllegalArgumentException {
		if (blockIndex < 0 || blockIndex >= blocks.length) {
			throw new IllegalArgumentException("Invalid block index: " + blockIndex);
		}

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
	 * @param address The address of the block to increment the reference count
	 * @throws HeapInvalidAddressException If the address is invalid (negative or not allocated)
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
	 * @param address The address of the block to decrement the reference count
	 * @throws HeapInvalidAddressException If the address is invalid (negative or not allocated)
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
	 * @throws HeapInvalidAddressException If the address is invalid (negative or not allocated)
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
		while (blockIndex < blocks.length - 1) {
			boolean hasMerged = mergeLevel(blockIndex);
			if (!hasMerged) break;
			blockIndex++;
		}
	}

	/**
	 * Try to merge any pair of buddy blocks at a given level
	 *
	 * @param level the block size level
	 * @return true if at least one merge occurred
	 */
	private boolean mergeLevel(int level) {
		HeapBlockNode prev = null;
		HeapBlockNode curr = blocks[level];
		boolean merged = false;

		while (curr != null) {
			int buddyAddr = curr.address ^ (1 << level);
			HeapBlockNode buddy = findAndRemoveBuddy(curr, buddyAddr);

			if (buddy != null) {
				if (prev == null) {
					blocks[level] = curr.next;
				} else {
					prev.next = curr.next;
				}

				int upperLevel = level + 1;
				int mergedAddr = Math.min(curr.address, buddy.address);
				HeapBlockNode mergedBlock = new HeapBlockNode(mergedAddr);
				mergedBlock.next = blocks[upperLevel];
				blocks[upperLevel] = mergedBlock;

				merged = true;
				break; // restart merge at next level
			}

			prev = curr;
			curr = curr.next;
		}

		return merged;
	}

	/**
	 * Search and remove a buddy block (if it exists)
	 * 
	 * @param current   The current block to find the buddy for
	 * @param buddyAddr The address of the buddy block
	 * @return The buddy block if found, null otherwise
	 */
	private HeapBlockNode findAndRemoveBuddy(HeapBlockNode current, int buddyAddr) {
		HeapBlockNode prev = current;
		HeapBlockNode node = current.next;

		while (node != null) {
			if (node.address == buddyAddr) {
				prev.next = node.next; // remove buddy from the list
				return node;
			}
			prev = node;
			node = node.next;
		}
		return null;
	}

	/**
	 * Searches for a value in the heap at the given address and index
	 * 
	 * @param address The address to search
	 * @param index   The index of the value to search
	 * @return The value found at the given address and index
	 * @throws HeapOutOfBoundsException    If the address is out of bounds (index too big)
	 * @throws HeapInvalidAddressException If the address is invalid (negative or not allocated)
	 * @throws IllegalArgumentException    If the type is not supported
	 */
	public <T> T search(int address, int index) throws HeapOutOfBoundsException, HeapInvalidAddressException, IllegalArgumentException {
		if (address < 0 || address >= localTable.size()) {
			throw new HeapInvalidAddressException(address);
		}

		HeapSymbol symbol = localTable.get(address);
		if (symbol == null) {
			throw new HeapInvalidAddressException(address);
		}

		int baseAddr = symbol.getAddress();
		int typeSize = (symbol.getType() == Type.INT) ? INT_SIZE : BOOL_SIZE;
		int offset = index * typeSize;

		if (offset + typeSize > symbol.getSize()) {
			throw new HeapOutOfBoundsException(address, index);
		}

		heap.position(baseAddr + offset);

		Object value = switch (symbol.getType()) {
			case INT -> heap.getInt();
			case BOOL -> (heap.get() != 0);
			default -> throw new IllegalArgumentException("Unsupported type: " + symbol.getType());
		};

		return (T) value;
	}

	/**
	 * Sets a value in the heap at the given address and index
	 * 
	 * @param address The address to set the value at
	 * @param index   The index of the value to set
	 * @param value   The value to set
	 * @throws HeapInvalidAddressException If the address is invalid (negative or not allocated)
	 * @throws IllegalArgumentException    If the type is not supported or if the value type does not match the symbol type
	 */
	public <T> void setValue(int address, int index, T value) throws HeapInvalidAddressException, IllegalArgumentException {
		if (address < 0 || address >= localTable.size()) {
			throw new HeapInvalidAddressException(address);
		}

		HeapSymbol symbol = localTable.get(address);
		if (symbol == null) {
			throw new HeapInvalidAddressException(address);
		}

		int baseAddr = symbol.getAddress();
		int typeSize = (symbol.getType() == Type.INT) ? INT_SIZE : BOOL_SIZE;
		int offset = index * typeSize;

		heap.position(baseAddr + offset);

		switch (symbol.getType()) {
			case INT -> {
				if (!(value instanceof Integer)) {
					throw new IllegalArgumentException("Expected Integer for type INT");
				}
				heap.putInt((Integer) value);
			}
			case BOOL -> {
				if (!(value instanceof Boolean)) {
					throw new IllegalArgumentException("Expected Boolean for type BOOL");
				}
				heap.put((byte) ((Boolean) value ? 1 : 0));
			}
			default -> throw new IllegalArgumentException("Unsupported type: " + symbol.getType());
		}
	}
}
