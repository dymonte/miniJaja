package fr.umlp.m1info.projectvms.group2.ast.jajacode.instr;

/**
 * InstrDugois
 */
public class InstrDugois extends Instr {

    /**
     * Instruction to sleep the execution thread
     * for a specified amount of time (in seconds)
     * 
     * @param lineId the line number of the instruction
     */
    public InstrDugois(int lineId) {
        super(lineId);
    }

    /**
     * @return the string reprensentation of the instruction
     */
    @Override
    public String toString() {
        return super.toString() + "dugois";
    }
}
