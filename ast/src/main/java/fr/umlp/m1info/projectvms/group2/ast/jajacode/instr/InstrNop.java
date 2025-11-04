package fr.umlp.m1info.projectvms.group2.ast.jajacode.instr;

/**
 * Class representing a nop instruction
 */
public class InstrNop extends Instr{
    public InstrNop(int lineId) {
        super(lineId);
    }

    /**
     * @return the string representation of the instruction
     */
    @Override
    public String toString() {
        return super.toString() + "nop";
    }
}
