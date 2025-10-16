package fr.umlp.m1info.projectvms.group2.memory;

import java.util.Optional;

import fr.umlp.m1info.projectvms.group2.exception.HeapOutOfBounds;
import fr.umlp.m1info.projectvms.group2.heap.Heap;
import fr.umlp.m1info.projectvms.group2.stack.Stack;
import fr.umlp.m1info.projectvms.group2.symtable.InstNode;
import fr.umlp.m1info.projectvms.group2.symtable.ObjectKind;
import fr.umlp.m1info.projectvms.group2.symtable.SymNode;
import fr.umlp.m1info.projectvms.group2.symtable.SymTable;
import fr.umlp.m1info.projectvms.group2.symtable.Type;

/**
 * Memory
 */
public class Memory {
	SymTable symTable;
	Stack stack;
	Heap heap;

	/*
	 * NOTE: if <T> makes errors with the InstNode Object val, then replace <T> with
	 * Object.
	 */

	public Memory(SymTable symTable, Stack stack, Heap heap) {
		this.symTable = symTable;
		this.stack = stack;
		this.heap = heap;
	}

	/* stack Constructor methods */

	/**
	 * Add a new variable declaration to the program memory.
	 * 
	 * @param ident the variable identifier
	 * @param value the variable initial value
	 * @param sorte the variable type
	 */
	public <T> void declVar(String ident, T value, Type sorte) {
		Optional<SymNode> symbol = symTable.add(ident, ObjectKind.VAR, sorte);
		if (symbol.isPresent()) {
			stack.push(value, symbol.get());
		}
	}

	/**
	 * Add a new constant declaration to the program memory.
	 * 
	 * @param ident the constant identifier
	 * @param value the constant initial value
	 * @param sorte the constant type
	 */
	public <T> void declCst(String ident, T value, Type sorte) {
		Optional<SymNode> symbol = symTable.add(ident, ObjectKind.CONST, sorte);
		if (symbol.isPresent()) {
			stack.push(value, symbol.get());
		}
	}

	/**
	 * Add a new array declaration to the program memory.
	 * 
	 * @param ident the array identifier
	 * @param size  the array size
	 * @param sorte the array type TODO: maybe add here type checking
	 */
	public void declArray(String ident, int size, Type sorte) {
		Optional<SymNode> symbol = symTable.add(ident, ObjectKind.ARRAY, sorte, size);

		if (symbol.isPresent()) {
			int address = heap.allocate(size, sorte);
			stack.push(address, symbol.get());
		}
	}

	/**
	 * TODO: javadoc
	 * TODO: think about how to pass parameters
	 */
	public <T> void declMeth(String ident, T value, Type sorte) {
		// TODO: implement this
		throw new UnsupportedOperationException();
	}

	/* Memory modification methods */

	// TODO: maybe place print here ?? (Afficher, AfficherLn : VAL × MEM → MEM)

	/**
	 * TODO: javadoc
	 */
	public void removeDecl(String ident) {
		// TODO: implement this
		throw new UnsupportedOperationException();
	}

	/**
	 * TODO: javadoc
	 */
	public <T> void updateVal(String ident, T val) {
		// TODO: implement this
		throw new UnsupportedOperationException();
	}

	/**
	 * TODO: javadoc
	 */
	public <T> T getVal(String ident) {
		// TODO: implement this
		throw new UnsupportedOperationException();
	}

	/**
	 * Get the value at a given index in an array.
	 * 
	 * @param ident the array identifier
	 * @param index the index to read
	 * @return the value at the given index in the array
	 */
	public <T> T getValArray(String ident, int index) {
		Optional<SymNode> symbolOpt = symTable.search(ident);
		if (symbolOpt.isEmpty()) {
			throw new RuntimeException("Error : array '" + ident + "' doesn't exist.");
		}

		SymNode symbol = symbolOpt.get();

		if (symbol.getInfo().getKind() != ObjectKind.ARRAY) {
			throw new RuntimeException("Error : '" + ident + "' isn't an array.");
		}

		// Get first instance of the symbol
		InstNode instance = symbol.getFirstInstance();
		if (instance == null) {
			throw new RuntimeException("Error : no instance for array '" + ident + "'.");
		}

		int address = (int) instance.getValue();

		try {
			return heap.search(address, index);
		} catch (HeapOutOfBounds e) {
			throw new RuntimeException("Error : reading array '" + ident + "' : " + e.getMessage());
		}
	}

	/**
	 * Update the value at a given index in an array.
	 *
	 * @param ident the array identifier
	 * @param index the index to write to
	 * @param val   the new value
	 */
	public <T> void updateValArray(String ident, int index, T val) {
		Optional<SymNode> symbolOpt = symTable.search(ident);
		if (symbolOpt.isEmpty()) {
			throw new RuntimeException("Error : array '" + ident + "' doesn't exist.");
		}

		SymNode symbol = symbolOpt.get();

		if (symbol.getInfo().getKind() != ObjectKind.ARRAY) {
			throw new RuntimeException("Error: '" + ident + "' isn't an array.");
		}

		// Get first instance of the symbol
		InstNode instance = symbol.getFirstInstance();
		if (instance == null) {
			throw new RuntimeException("Error : no instance for array '" + ident + "'.");
		}

		int address = (int) instance.getValue();

		try {
			heap.setValue(address, index, val);
		} catch (HeapOutOfBounds e) {
			throw new RuntimeException("Error: writing array '" + ident + "' : " + e.getMessage());
		}
	}

	/**
	 * TODO: javadoc
	 */
	public void updateType(String ident, Type sorte) {
		// TODO: implement this
		throw new UnsupportedOperationException();
	}

	/**
	 * TODO: javadoc
	 */
	// TODO: understand expParam

	/*
	 * Accès aux infos de
	 * T:ID×VAL×
	 * MEM Long D EM ALOj
	 * t:ID×MEM→OBJ ∪S
	 * rte : I MM→ SRE ∪ {P
	 * ramètre :I ×MM →DCLS ∪é
	 * laaio ID×MEM → E
	 * LS ∪ {w}
	 * Corps : ID × MEM → INSTRS ∪ {w}
	 */
}
