package fr.umlp.m1info.projectvms.group2.exception;

/**
 * SymbolNotFoundException
 *
 * This exception is thrown when wanting to search
 * a symbol with it's identifier from the symbol table, 
 * but no corresponding symbol was found
 */
public class SymbolNotFoundException extends RuntimeException {

	/**
	 * Constructs a SymbolNotFoundException.
	 *
	 * @param identifier the symbol identifier
	 */
	public SymbolNotFoundException(String identifier) {
		super(identifier + " was not found in the symbol table.");
	}

}
