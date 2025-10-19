package fr.umlp.m1info.projectvms.group2.memory.symtable;

import java.util.Objects;

/**
 * MemInfo
 * The informations of a symbol
 */
public class MemInfo {
	private Type sorte;
	private ObjectKind kind;
	private int size;

	/**
	 * Constructs a new Memory informations object
	 *
	 * @param sorte the data type (INT, BOOL, ...)
	 * @param kind  the object kind (VAR, METH, ...)
	 * @param size  the data size
	 * @throws IllegalArgumentException when size < 0 (negative data size)
	 */
	public MemInfo(Type sorte, ObjectKind kind, int size) throws IllegalArgumentException {
		this.sorte = sorte;
		this.kind = kind;
		if (size < 0) {
			throw new IllegalArgumentException("Cannot create data with negative size");
		}
		this.size = size;
	}

	public MemInfo(Type sorte, ObjectKind kind) {
		this(sorte, kind, 1);
	}

	/**
	 * @return the data type (INT, BOOL, ...)
	 */
	public Type getSorte() {
		return sorte;
	}

	/**
	 * @return the data kind (VAR, METH, ...)
	 */
	public ObjectKind getKind() {
		return kind;
	}

	/**
	 * @return the data size
	 */
	public int getSize() {
		return size;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof MemInfo)) {
			return false;
		}
		MemInfo otherInfo = (MemInfo) obj;
		return otherInfo.getSize() == size &&
				otherInfo.getSorte() == sorte &&
				otherInfo.getKind() == kind;
	}

	@Override
	public int hashCode() {
		return Objects.hash(size, sorte, kind);
	}
}
