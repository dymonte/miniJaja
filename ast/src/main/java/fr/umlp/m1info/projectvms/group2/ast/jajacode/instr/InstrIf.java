package fr.umlp.m1info.projectvms.group2.ast.jajacode.instr;

import fr.umlp.m1info.projectvms.group2.ast.jajacode.value.ValAddress;

/**
 * Class representing an if instruction
 */
public class InstrIf extends Instr{
    private final ValAddress address;

    /**
     * Instruction for conditional jumping to a label
     * @param lineId the line number of the instruction
     * @param address the address to jump to
     */
    public InstrIf(int lineId, ValAddress address) {
        super(lineId);
        this.address = address;
    }

    /**
     * @return the address to jump to
     */
    public ValAddress getAddress() {
        return address;
    }

    /**
     * @return the string representation of the instruction
     */
    @Override
    public String toString() {
        return super.toString() + "if " + address;
    }
}
