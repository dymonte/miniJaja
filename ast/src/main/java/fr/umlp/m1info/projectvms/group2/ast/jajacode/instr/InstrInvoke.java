package fr.umlp.m1info.projectvms.group2.ast.jajacode.instr;

import fr.umlp.m1info.projectvms.group2.ast.jajacode.value.ValIdent;

/**
 * Class representing an invoke instruction
 */
public class InstrInvoke extends Instr {
    private final ValIdent valIdent;

    /**
     * Instruction for invoking a method
     * @param lineId the line number of the instruction
     * @param valIdent the identifier of the method
     */
    public InstrInvoke(int lineId, ValIdent valIdent) {
        super(lineId);
        if(valIdent == null) throw new NullPointerException("valIdent cannot be null");
        this.valIdent = valIdent;
    }

    /**
     * @return the identifier of the method
     */
    public ValIdent getValIdent() {
        return valIdent;
    }

    /**
     * @return the string representation of the instruction
     */
    @Override
    public String toString() {
        return super.toString() + "invoke " + valIdent;
    }
}
