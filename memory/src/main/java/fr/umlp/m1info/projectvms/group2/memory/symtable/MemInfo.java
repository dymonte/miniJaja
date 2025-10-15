package fr.umlp.m1info.projectvms.group2.memory.symtable;

/**
 * MemInfo
 */
public class MemInfo {
	Type sorte;
	ObjectKind kind;
	int size;

	public MemInfo(Type sorte, ObjectKind kind, int size) {
		this.sorte = sorte;
		this.kind = kind;
		this.size = size;
	}

	public MemInfo(Type sorte, ObjectKind kind) {
		this(sorte, kind, 1);
	}

	public Type getSorte() {
		return sorte;
	}

	public ObjectKind getKind() {
		return kind;
	}

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
}
