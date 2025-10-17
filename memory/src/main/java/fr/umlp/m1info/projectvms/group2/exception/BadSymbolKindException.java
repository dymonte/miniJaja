package fr.umlp.m1info.projectvms.group2.exception;

import fr.umlp.m1info.projectvms.group2.memory.symtable.ObjectKind;

/**
 * BadSymbolKindException
 *
 * This exception is thrown when wanting to retrieve
 * a data from a certain data type, but the actual data type
 * is not what was expected.
 * For instance, when trying to update an array t, but t is a variable.
 */
public class BadSymbolKindException extends RuntimeException {

	/**
	 * Constructs a BadSymbolKindException.
	 *
	 * @param identifier the symbol identifier
	 * @param expected   the expected kind
	 * @param actual     the actual kind
	 */
	public BadSymbolKindException(String identifier, ObjectKind expected, ObjectKind actual) {
		super(identifier + " type should be " + expected.name() + " but is actually " + actual.name() + ".");
	}

	/**
	 * Constructs a BadSymbolKindException.
	 *
	 * @param identifier the symbol identifier
	 * @param actual     the actual kind
	 */
	public BadSymbolKindException(String identifier, ObjectKind actual) {
		super(identifier + " type should not be " + actual.name() + ".");
	}

}
