package fr.umlp.m1info.projectvms.group2.ast.jajacode.instr;

/**
 * Class representing an initialization instruction
 */
public class InstrInit extends Instr{

    /**
     * Instruction for initializing the stack
     * @param lineId the line number of the instruction
     */
    public InstrInit(int lineId) {
        super(lineId);
    }

    /**
     * @return the string representation of the instruction
     */
    @Override
    public String toString() {
        return super.toString() + "init";
    }
}
