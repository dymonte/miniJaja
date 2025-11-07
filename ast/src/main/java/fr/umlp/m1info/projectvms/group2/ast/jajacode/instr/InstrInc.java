package fr.umlp.m1info.projectvms.group2.ast.jajacode.instr;

import fr.umlp.m1info.projectvms.group2.ast.jajacode.value.ValIdent;

/**
 * Class representing an increment instruction
 */
public class InstrInc extends Instr{
    private final ValIdent valIdent;

    /**
     * Instruction for incrementing a variable
     * @param lineId the line number of the instruction
     * @param valIdent the valIdentifier of the variable
     */
    public InstrInc(int lineId, ValIdent valIdent) {
        super(lineId);
        this.valIdent = valIdent;
    }

    /**
     * @return the valIdentifier of the variable
     */
    public ValIdent getValIdent() {
        return valIdent;
    }

    /**
     * @return the string representation of the instruction
     */
    @Override
    public String toString() {
        return super.toString() + "inc(" + valIdent + ")";
    }
}
