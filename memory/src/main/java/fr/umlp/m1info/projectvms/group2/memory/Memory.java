package fr.umlp.m1info.projectvms.group2.memory;

import java.util.Optional;

import fr.umlp.m1info.projectvms.group2.memory.heap.Heap;
import fr.umlp.m1info.projectvms.group2.memory.symtable.ObjectKind;
import fr.umlp.m1info.projectvms.group2.memory.symtable.SymTable;
import fr.umlp.m1info.projectvms.group2.memory.symtable.SymNode;
import fr.umlp.m1info.projectvms.group2.memory.symtable.Type;

import fr.umlp.m1info.projectvms.group2.memory.stack.Stack;

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
	 * @param sorte the variable type TODO: maybe add here type checking
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
	 * @param sorte the constant type TODO: maybe add here type checking
	 */
	public <T> void declCst(String ident, T value, Type sorte) {
		Optional<SymNode> symbol = symTable.add(ident, ObjectKind.CONST, sorte);
		if (symbol.isPresent()) {
			stack.push(value, symbol.get());
		}
	}

	/**
	 * TODO: javadoc
	 */
	public void declArray(String ident, int size, Type sorte) {
		// TODO: implement this
		throw new UnsupportedOperationException();
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
	 * TODO: javadoc
	 */
	public <T> T getValArray(String ident, int index) {
		// TODO: implement this
		throw new UnsupportedOperationException();
	}

	/**
	 * TODO: javadoc
	 */
	public <T> void updateValArray(String ident, int index, T val) {
		// TODO: implement this
		throw new UnsupportedOperationException();
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
