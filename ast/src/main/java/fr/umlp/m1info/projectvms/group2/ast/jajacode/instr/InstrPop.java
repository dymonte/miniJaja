package fr.umlp.m1info.projectvms.group2.ast.jajacode.instr;

/**
 * Class representing a pop instruction
 */
public class InstrPop extends Instr{

    /**
     * Constructor
     * @param lineId the line id
     */
    public InstrPop(int lineId) {
        super(lineId);
    }

    /**
     * @return the string representation of the instruction
     */
    @Override
    public String toString() {
        return super.toString() + "pop";
    }
}
