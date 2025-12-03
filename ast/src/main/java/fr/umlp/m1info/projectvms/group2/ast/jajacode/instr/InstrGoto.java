package fr.umlp.m1info.projectvms.group2.ast.jajacode.instr;

import fr.umlp.m1info.projectvms.group2.ast.jajacode.value.ValAddress;

/**
 * Class representing a goto instruction
 */
public class InstrGoto extends Instr{
    private final ValAddress valAddress;

    /**
     * Instruction for unconditionally jumping to a label
     * @param lineId the line number of the instruction
     * @param valAddress the address to jump to
     */
    public InstrGoto(int lineId, ValAddress valAddress) {
        super(lineId);
        if (valAddress == null)
            throw new NullPointerException("valAddress cannot be null");
        this.valAddress = valAddress;
    }

    /**
     * @return the address to jump to
     */
    public ValAddress getValAddress() {
        return valAddress;
    }

    /**
     * @return the string representation of the instruction
     */
    @Override
    public String toString() {
        return super.toString() + "goto(" + valAddress + ")";
    }
}
