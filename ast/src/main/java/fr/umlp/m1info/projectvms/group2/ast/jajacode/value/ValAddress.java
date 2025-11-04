package fr.umlp.m1info.projectvms.group2.ast.jajacode.value;

public class ValAddress extends Val{
    private final int address;

    public ValAddress(int lineId, int address) {
        super(lineId);
        this.address = address;
    }

    public int getAddress() {
        return address;
    }

    @Override
    public String toString() {
        return Integer.toString(address);
    }
}
