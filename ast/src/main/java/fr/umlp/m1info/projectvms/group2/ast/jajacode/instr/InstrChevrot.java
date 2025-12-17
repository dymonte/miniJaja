package fr.umlp.m1info.projectvms.group2.ast.jajacode.instr;

/**
 * InstrChevrot
 */
public class InstrChevrot extends Instr {

    /**
     * Instruction to assert that a certain condition
     * is verified.
     * 
     * @param lineId the line number of the instruction
     */
    public InstrChevrot(int lineId) {
        super(lineId);
    }

    /**
     * @return the string reprensentation of the instruction
     */
    @Override
    public String toString() {
        return super.toString() + "chevrot";
    }
}
