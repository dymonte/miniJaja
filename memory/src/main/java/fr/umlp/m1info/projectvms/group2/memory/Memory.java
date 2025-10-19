package fr.umlp.m1info.projectvms.group2.memory;

import java.util.List;
import java.util.Optional;

import fr.umlp.m1info.projectvms.group2.exception.SymbolNotFoundException;
import fr.umlp.m1info.projectvms.group2.exception.NoInstanceException;
import fr.umlp.m1info.projectvms.group2.exception.BadSymbolKindException;
import fr.umlp.m1info.projectvms.group2.memory.heap.Heap;
import fr.umlp.m1info.projectvms.group2.memory.stack.Stack;
import fr.umlp.m1info.projectvms.group2.memory.symtable.*;

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
     * Add a new method declaration to the program memory.
     *
     * @param ident         the method identifier
     * @param meth          node in ast
     * @param paramsTypes   list of the method parameters types
     * @param sorte         the method type
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
     * @param ident     the method identifier
     * @param params    list of the method parameters types
     * @return          the method identifier with its parameters types
     */
    public String getMethKey(String ident, List<Type> params) {
        String size = String.format("%d",ident.length());
        StringBuilder ret = new StringBuilder(size);
        ret.append(ident);

        for(Type t : params) {
            ret.append(t.toString().charAt(0));
        }
        return ret.toString();
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

		if (kind != ObjectKind.VAR) {
			throw new BadSymbolKindException(ident, kind);
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
		ObjectKind kind = symbol.getInfo().getKind();

		if (kind == ObjectKind.ARRAY) {
			throw new BadSymbolKindException(ident, kind);
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
