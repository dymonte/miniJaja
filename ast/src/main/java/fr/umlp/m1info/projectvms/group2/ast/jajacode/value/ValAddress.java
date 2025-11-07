package fr.umlp.m1info.projectvms.group2.ast.jajacode.value;

/**
 * Class representing an address value
 */
public final class ValAddress extends Val{
    private final int address;

    /**
     * @param lineId the line number of the value
     * @param address the address of the value
     */
    public ValAddress(int lineId, int address) {
        super(lineId);
        this.address = address;
    }

    /**
     * @return the address of the value
     */
    public int getAddress() {
        return address;
    }

    /**
     * @return the string representation of the instruction
     */
    @Override
    public String toString() {
        return "jcnbre(" + address + ")";
    }
}
