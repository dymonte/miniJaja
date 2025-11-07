package fr.umlp.m1info.projectvms.group2.ast.jajacode.instr;

/**
 * Class representing a stop instruction
 */
public class InstrStop extends Instr{

    /**
     * Instruction for stopping the execution
     * @param lineId the line number of the instruction
     */
    public InstrStop(int lineId) {
        super(lineId);
    }

    /**
     * @return the string representation of the instruction
     */
    @Override
    public String toString() {
        return super.toString() + "jcstop";
    }
}
