package fr.umlp.m1info.projectvms.group2.heap;

import java.nio.ByteBuffer;
import java.util.ArrayList;

import fr.umlp.m1info.projectvms.group2.exception.HeapInvalidAddress;
import fr.umlp.m1info.projectvms.group2.exception.HeapOutOfBounds;
import fr.umlp.m1info.projectvms.group2.symtable.Type;
import fr.umlp.m1info.projectvms.group2.exception.HeapOutOfMemory;

/**
 * Heap
 */
public class Heap {
	public static int HEAP_SIZE = 1024; // TODO: maybe this should be passed as CLI/GUI param
	ByteBuffer heap = ByteBuffer.allocate(HEAP_SIZE);

	/**
	 * This array is of size log2(HEAP_SIZE).
	 * each index i corresponds to blocks of size 2^(i-1)
	 * for instance, addresses of blocks of size 256 are stored at index 7.
	 */
	HeapBlockNode[] blocks = new HeapBlockNode[(int) (Math.log(HEAP_SIZE) / Math.log(2)) + 1];

	ArrayList<HeapSymbol> localTable = new ArrayList<HeapSymbol>();

	public Heap() {
		blocks[blocks.length - 1] = new HeapBlockNode(0);
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
			throw new HeapOutOfMemory();
		}

		// Check type size
		int requestedSize = (type == Type.INT) ? size * 4 : size;

		// Search block > size
		int blockIndex = (int) Math.ceil(Math.log(size) / Math.log(2));
		int blockSize = 1 << blockIndex;

		if (blockSize > HEAP_SIZE) {
			throw new HeapOutOfMemory();
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
			throw new HeapOutOfMemory();
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
	 * @throws HeapInvalidAddress
	 */
	public void incRef(int address) throws HeapInvalidAddress {
		if (address < 0 || address >= localTable.size() || localTable.get(address) == null) {
			throw new HeapInvalidAddress(address);
		}
		localTable.get(address).incRef();
	}

	/**
	 * Decrements the reference count of the block at the given address
	 * 
	 * @param address
	 * @throws HeapInvalidAddress
	 */
	public void decRef(int address) throws HeapInvalidAddress {
		if (address < 0 || address >= localTable.size() || localTable.get(address) == null) {
			throw new HeapInvalidAddress(address);
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
	private void free(int address) throws HeapInvalidAddress {
		if (address < 0 || address >= localTable.size() || localTable.get(address) == null) {
			throw new HeapInvalidAddress(address);
		}

		localTable.get(address);

		int blockSize = localTable.get(address).getSize();
		int blockIndex = (int) Math.ceil(Math.log(blockSize) / Math.log(2));

		localTable.remove(address);

		// Replace the block in the free list
		HeapBlockNode block = new HeapBlockNode(address);

		if (blocks[blockIndex] == null) {
			blocks[blockIndex] = block;
			return;
		}

		blocks[blockIndex].addNode(block);

		// TODO : merge with buddy if free
	}

	/**
	 * Searches for a value in the heap at the given address and index
	 * 
	 * @param address The address to search
	 * @param index   The index of the value to search
	 * @return The value found at the given address and index
	 * @throws HeapOutOfBounds  If the address is out of bounds
	 * @throws HeapInvalidAddress If the address is invalid
	 */
	public <T> T search(int address, int index) throws HeapOutOfBounds, HeapInvalidAddress {
		if (address < 0 || address >= localTable.size()) {
			throw new HeapInvalidAddress(address);
		}

		HeapSymbol symbol = localTable.get(address);
		if (symbol == null) {
			throw new HeapInvalidAddress(address);
		}

		int baseAddr = symbol.getAddress();
		int typeSize = (symbol.getType() == Type.INT) ? 4 : 1;
		int offset = index * typeSize;

		if (offset + typeSize > symbol.getSize()) {
			throw new HeapOutOfBounds(address, index);
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
	 * @throws HeapOutOfBounds  If the address is out of bounds
	 * @throws HeapInvalidAddress If the address is invalid
	 */
	public <T> void setValue(int address, int index, T value) throws HeapOutOfBounds, HeapInvalidAddress {
		if (address < 0 || address >= localTable.size()) {
			throw new HeapInvalidAddress(address);
		}

		HeapSymbol symbol = localTable.get(address);
		if (symbol == null) {
			throw new HeapInvalidAddress(address);
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
