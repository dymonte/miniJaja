package fr.umlp.m1info.projectvms.group2.memory;

import java.util.List;
import java.util.Optional;

import fr.umlp.m1info.projectvms.group2.exception.BadSymbolKindException;
import fr.umlp.m1info.projectvms.group2.exception.EmptyStackException;
import fr.umlp.m1info.projectvms.group2.exception.NoInstanceException;
import fr.umlp.m1info.projectvms.group2.exception.StackInvalidAddressException;
import fr.umlp.m1info.projectvms.group2.exception.SymbolNotFoundException;
import fr.umlp.m1info.projectvms.group2.memory.heap.Heap;
import fr.umlp.m1info.projectvms.group2.memory.stack.Stack;
import fr.umlp.m1info.projectvms.group2.memory.symtable.InstNode;
import fr.umlp.m1info.projectvms.group2.memory.symtable.MemInfo;
import fr.umlp.m1info.projectvms.group2.memory.symtable.ObjectKind;
import fr.umlp.m1info.projectvms.group2.memory.symtable.SymNode;
import fr.umlp.m1info.projectvms.group2.memory.symtable.SymTable;
import fr.umlp.m1info.projectvms.group2.memory.symtable.Type;

/**
 * Memory
 */
public class Memory {
	SymTable symTable;
	Stack stack;
	Heap heap;

	/**
	 * Constructs a new Memory
	 *
	 * @param symTable the memory symbol table
	 * @param stack    the memory stack
	 * @param heap     the memory heap
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
	 * Add a new constant declaration with it's value to the program memory.
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
	 * Add a new constant declaration to the program memory.
	 * 
	 * @param ident the constant identifier
	 * @param sorte the constant type
	 */
	public void declCst(String ident, Type sorte) {
		Optional<SymNode> symbol = symTable.add(ident, ObjectKind.VCONST, sorte);
		if (symbol.isPresent()) {
			stack.push(null, symbol.get());
		}

	}

	/**
	 * Add a new array declaration to the program memory.
	 * 
	 * @param ident the array identifier
	 * @param size  the array size
	 * @param sorte the array type
	 */
	public void declArray(String ident, int size, Type sorte) {
		Optional<SymNode> symbol = symTable.add(ident, ObjectKind.ARRAY, sorte, size);

		if (symbol.isPresent()) {
			int address = heap.allocate(size, sorte);
			stack.push(address, symbol.get());
		}
	}

	/**
	 * Add a new method declaration to the program memory.
	 *
	 * @param ident       the method identifier
	 * @param meth        node in ast
	 * @param paramsTypes list of the method parameters types
	 * @param sorte       the method type
	 */
	public void declMeth(String ident, Object meth, List<Type> paramsTypes, Type sorte) {
		String identWithTypes = getMethKey(ident, paramsTypes);
		Optional<SymNode> symbol = symTable.add(identWithTypes, ObjectKind.METH, sorte);
		symbol.ifPresent(symNode -> stack.push(meth, symNode));
	}

	/**
	 * Returns the method identifier with its parameters types.
	 * example: "myMethod", "[int, int]" -> "myMethod(int_int)"
	 *
	 * @param ident  the method identifier
	 * @param params list of the method parameters types
	 * @return the method identifier with its parameters types
	 */
	public String getMethKey(String ident, List<Type> params) {
		String size = String.format("%d", ident.length());
		StringBuilder ret = new StringBuilder(size);
		ret.append(ident);

		for (Type t : params) {
			ret.append(t.toString().charAt(0));
		}
		return ret.toString();
	}

	/* Memory modification methods */

	/**
	 * Removes the latest instance of symbol ident.
	 *
	 * @param ident the identifier of the instance to remove
	 * @throws SymbolNotFoundException thrown when the symbol of identifier ident
	 *                                 does not
	 *                                 belong to this memory's symbol table
	 * @throws NoInstanceException     thrown when the symbol does not have any
	 *                                 instance/value
	 */
	public void removeDecl(String ident) throws SymbolNotFoundException, NoInstanceException {
		Optional<SymNode> symbolOpt = symTable.search(ident);
		if (symbolOpt.isEmpty()) {
			throw new SymbolNotFoundException(ident);
		}
		InstNode instance = symbolOpt.get().getFirstInstance();
		if (instance == null) {
			throw new NoInstanceException(ident);
		}
		if (symbolOpt.get().getInfo().getKind() == ObjectKind.ARRAY) {
			heap.decRef((int) instance.getValue());
		}
		symTable.remove(instance);
	}

	/**
	 * Sets the value stored in the first instance of the symbol of identifier
	 * ident
	 * 
	 * @param ident the symbol identifier
	 * @param val   the new value to set
	 * @throws SymbolNotFoundException thrown when the symbol of identifier ident
	 *                                 does not
	 *                                 belong to this memory's symbol table
	 * @throws BadSymbolKindException  thrown when the symbol data type is not a
	 *                                 variable
	 * @throws NoInstanceException     thrown when the symbol does not have any
	 *                                 instance/value
	 */
	public void updateVal(String ident, Object val) {
		Optional<SymNode> symbolOpt = symTable.search(ident);
		if (symbolOpt.isEmpty()) {
			throw new SymbolNotFoundException(ident);
		}

		SymNode symbol = symbolOpt.get();
		ObjectKind kind = symbol.getInfo().getKind();

		if (kind == ObjectKind.ARRAY) {
			heap.incRef((int) val);
		}

		if (kind != ObjectKind.VAR && kind != ObjectKind.ARRAY && kind != ObjectKind.VCONST) {
			throw new BadSymbolKindException(ident, kind);
		}

		if (kind == ObjectKind.VCONST) {
			symbol.setKind(ObjectKind.CONST);
		}

		InstNode instance = symbol.getFirstInstance();
		if (instance == null) {
			throw new NoInstanceException(ident);
		}

		instance.updateValue(val);
	}

	/**
	 * Returns the value stored in the first instance of the symbol of identifier
	 * ident
	 * 
	 * @param ident the symbol identifier
	 * @return the value stored in the symbol's first instance
	 * @throws SymbolNotFoundException thrown when the symbol of identifier ident
	 *                                 does not
	 *                                 belong to this memory's symbol table
	 * @throws BadSymbolKindException  thrown when the symbol data type is an array
	 *                                 or a method
	 * @throws NoInstanceException     thrown when the symbol does not have any
	 *                                 instance/value
	 */
	public Object getVal(String ident) throws SymbolNotFoundException, BadSymbolKindException, NoInstanceException {
		Optional<SymNode> symbolOpt = symTable.search(ident);
		if (symbolOpt.isEmpty()) {
			throw new SymbolNotFoundException(ident);
		}

		SymNode symbol = symbolOpt.get();

		if (symbol.getInfo().getKind() == ObjectKind.VCONST) {
			throw new NoInstanceException(ident);
		}

		InstNode instance = symbol.getFirstInstance();
		if (instance == null) {
			throw new NoInstanceException(ident);
		}

		return instance.getValue();
	}

	/**
	 * Get the value at a given index in an array.
	 * 
	 * @param ident the array identifier
	 * @param index the index to read
	 * @return the value at the given index in the array
	 * @throws SymbolNotFoundException thrown when the symbol of identifier ident
	 *                                 does not
	 *                                 belong to this memory's symbol table
	 * @throws BadSymbolKindException  thrown when the symbol data type is not an
	 *                                 array
	 * @throws NoInstanceException     thrown when the symbol does not have any
	 *                                 instance/value
	 */
	public Object getValArray(String ident, int index)
			throws SymbolNotFoundException, BadSymbolKindException, NoInstanceException {
		Optional<SymNode> symbolOpt = symTable.search(ident);
		if (symbolOpt.isEmpty()) {
			throw new SymbolNotFoundException(ident);
		}

		SymNode symbol = symbolOpt.get();

		if (symbol.getInfo().getKind() != ObjectKind.ARRAY) {
			throw new BadSymbolKindException(ident, symbol.getInfo().getKind(), ObjectKind.ARRAY);
		}

		// Get first instance of the symbol
		InstNode instance = symbol.getFirstInstance();
		if (instance == null) {
			throw new NoInstanceException(ident);
		}

		int address = (int) instance.getValue();

		return heap.search(address, index);
	}

	/**
	 * Update the value at a given index in an array.
	 *
	 * @param ident the array identifier
	 * @param index the index to write to
	 * @param val   the new value
	 * @throws SymbolNotFoundException thrown when the symbol of identifier ident
	 *                                 does not
	 *                                 belong to this memory's symbol table
	 * @throws BadSymbolKindException  thrown when the symbol data type is not an
	 *                                 array
	 * @throws NoInstanceException     thrown when the symbol does not have any
	 *                                 instance/value
	 */
	public void updateValArray(String ident, int index, Object val)
			throws SymbolNotFoundException, BadSymbolKindException, NoInstanceException {
		Optional<SymNode> symbolOpt = symTable.search(ident);
		if (symbolOpt.isEmpty()) {
			throw new SymbolNotFoundException(ident);
		}

		SymNode symbol = symbolOpt.get();

		if (symbol.getInfo().getKind() != ObjectKind.ARRAY) {
			throw new BadSymbolKindException(ident, symbol.getInfo().getKind(), ObjectKind.ARRAY);
		}

		// Get first instance of the symbol
		InstNode instance = symbol.getFirstInstance();
		if (instance == null) {
			throw new NoInstanceException(ident);
		}

		int address = (int) instance.getValue();

		heap.setValue(address, index, val);
	}

	/**
	 * Get the size of an array
	 *
	 * @param ident the array identifier
	 * @throws SymbolNotFoundException thrown when the symbol of identifier ident
	 *                                 does not belong to this memory's symbol table
	 * @throws BadSymbolKindException  thrown when the symbol data type is not an
	 *                                 array
	 */
	public int getLengthArray(String ident) throws SymbolNotFoundException, BadSymbolKindException {
		Optional<SymNode> symbolOpt = symTable.search(ident);
		if (symbolOpt.isEmpty()) {
			throw new SymbolNotFoundException(ident);
		}

		SymNode symbol = symbolOpt.get();
		MemInfo info = symbol.getInfo();

		if (info.getKind() != ObjectKind.ARRAY) {
			throw new BadSymbolKindException(ident, info.getKind(), ObjectKind.ARRAY);
		}
		return info.getSize();
	}

	/**
	 * Gets the type of the symbol of indentifier ident
	 * 
	 * @param ident the symbol identifier
	 * @returns the symbol's data type
	 * @throws SymbolNotFoundException thrown when the symbol of identifier ident
	 *                                 does not
	 *                                 belong to this memory's symbol table
	 */
	public Type getType(String ident) {
		Optional<SymNode> symbolOpt = symTable.search(ident);
		if (symbolOpt.isEmpty()) {
			throw new SymbolNotFoundException(ident);
		}

		return symbolOpt.get().getInfo().getSorte();
	}

	/**
	 * Gets the head of the stack as a Quad
	 * 
	 * @return the head of the stack as a Quad
	 */
	public Quad getHead() {
		return new Quad(stack.getHead());
	}

	/**
	 * Sets the type of the symbol of indentifier ident
	 * 
	 * @param ident the symbol identifier
	 * @param sorte the new type to set
	 * @throws SymbolNotFoundException thrown when the symbol of identifier ident
	 *                                 does not
	 *                                 belong to this memory's symbol table
	 */
	public void updateType(String ident, Type sorte) {
		Optional<SymNode> symbolOpt = symTable.search(ident);
		if (symbolOpt.isEmpty()) {
			throw new SymbolNotFoundException(ident);
		}

		symbolOpt.get().setType(sorte);
	}

	/**
	 * @return a string representation of the heap
	 */
	public String getHeap() {
		return heap.toString();
	}

	/* JajaCode related methods */

	/**
	 * Pushes a new value on top of the stack
	 * 
	 * @param value the value to add on top of the stack
	 */
	public void push(Object value) {
		SymNode symbol = new SymNode("w", new MemInfo(Type.INT, ObjectKind.CONST));
		stack.push(value, symbol);
	}

	/**
	 * Swaps the element on top of the memory stack with the element below it.
	 */
	public void swap() {
		stack.swap();
	}

	/**
	 * Removes the element on top of the stack and returns it.
	 *
	 * @return the deleted element
	 */
	public Object pop() {
		InstNode popped = stack.pop();
		if (popped == null) {
			throw new EmptyStackException();
		}
		return popped != null ? popped.getValue() : null;
	}

	/**
	 * Modifies the sorte, identifier and kind of the stack's at address adress
	 * (address 0 begin the stack's head, address 1 being the second element etc...)
	 *
	 * @param identifier the new Identifier of the stack's head
	 * @param kind       the new object kind of the stack's head
	 * @param sorte      the new type of the stack's head
	 * @param address    the element's address on the stack
	 * @throws IllegalArgumentException     when address < 0
	 * @throws StackInvalidAddressException when address is exceding stack's depth
	 *
	 */
	public void identVal(String identifier, ObjectKind kind, Type sorte, int address) {
		if (address < 0) {
			throw new IllegalArgumentException("address should be >= 0.");
		}

		Optional<SymNode> symbolOpt = symTable.add(identifier, kind, sorte);
		if (symbolOpt.isEmpty()) {
			return;
		}
		SymNode symbol = symbolOpt.get();

		if (address == 0) {
			InstNode head = stack.pop();
			symTable.remove(head);
			stack.push(head.getValue(), symbol);
			return;
		}

		InstNode bef = null;
		InstNode cur = stack.getHead();
		while (address > 0 && cur != null) {
			bef = cur;
			cur = cur.getNextInStack();
			address--;
		}

		if (address > 0 || cur == null) {
			throw new StackInvalidAddressException(address);
		}

		symTable.remove(cur);
		InstNode newInstance = new InstNode(cur.getValue(), symbol, cur.getNextInStack());
		bef.setNextInStack(newInstance);
	}
}
