package fr.umlp.m1info.projectvms.group2.ast.jajacode.instr;


/**
 * Class representing a nil instruction
 */
public class InstrNil extends Instr {
    public InstrNil(int lineId) {
        super(lineId);
    }

    /**
     * @return the string representation of the instruction
     */
    @Override
    public String toString() {
        return super.toString() + "jcnil";
    }
}
