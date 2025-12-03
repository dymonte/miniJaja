package fr.umlp.m1info.projectvms.group2.ast.jajacode.instr;

/**
 * Class representing a writeln instruction
 */
public class InstrWriteln extends Instr{

    /**
     * Instruction for writing a value to the standard output with a newline
     * @param lineId the line number of the instruction
     */
    public InstrWriteln(int lineId) {
        super(lineId);
    }

    /**
     * @return the string representation of the instruction
     */
    @Override
    public String toString() {
        return super.toString() + "writeln";
    }
}
