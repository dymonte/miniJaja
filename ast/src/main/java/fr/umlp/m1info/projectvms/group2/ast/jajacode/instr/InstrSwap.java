package fr.umlp.m1info.projectvms.group2.ast.jajacode.instr;

/**
 * Class representing a swap instruction
 */
public class InstrSwap extends Instr{

    /**
     * Instruction for swapping the top two values on the stack
     * @param lineId the line number of the instruction
     */
    public InstrSwap(int lineId) {
        super(lineId);
    }

    /**
     * @return the string representation of the instruction
     */
    @Override
    public String toString() {
        return super.toString() + "swap";
    }
}
