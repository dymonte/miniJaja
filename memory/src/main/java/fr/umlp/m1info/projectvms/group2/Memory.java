package fr.umlp.m1info.projectvms.group2;

import fr.umlp.m1info.projectvms.group2.heap.Heap;
import fr.umlp.m1info.projectvms.group2.symtable.SymTable;

import fr.umlp.m1info.projectvms.group2.stack.Stack;

/**
 * Memory
 */
public class Memory {
	SymTable symTable;
	Stack stack;
	Heap heap;

	/**
	 * TODO: javadoc
	 */
	public boolean addMemState() {
		// TODO: implement this
		throw new UnsupportedOperationException();
	}
	/*
	 * TODO: all those operations should be implement here
	 * DeclVar : ID × VAL × SORTE × MEM → MEM
	 * IdentVal : ID × SORTE × MEM × NAT → MEM
	 * DeclCst : ID × VAL × SORTE × MEM → MEM
	 * DeclTab : ID × VAL × SORTE × MEM → MEM
	 * DeclMeth : ID × VAL × SORTE × MEM → MEM
	 *
	 * Afficher, AfficherLn : VAL × MEM → MEM
	 * RetirerDecl : ID × MEM → MEM
	 * AffecterVal : ID × VAL × MEM → MEM
	 * AffecterValT : ID × VAL × VAL × MEM → MEM
	 * AffecterType : ID × SORTE × MEM → MEM
	 * ExpParam : VAL × VAL × MEM → MEM
	 */

}
