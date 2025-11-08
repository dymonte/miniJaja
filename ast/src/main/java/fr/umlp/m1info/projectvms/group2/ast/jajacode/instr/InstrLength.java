package fr.umlp.m1info.projectvms.group2.ast.jajacode.instr;

import fr.umlp.m1info.projectvms.group2.ast.jajacode.value.ValIdent;

/**
 * Class representing a length instruction
 */
public class InstrLength extends Instr{
    private final ValIdent valIdent;

    /**
     * Instruction for getting the length of an array
     * @param lineId the line number of the instruction
     * @param valIdent the valIdentifier of the array
     */
    public InstrLength(int lineId, ValIdent valIdent) {
        super(lineId);
        if (valIdent == null)
            throw new NullPointerException("valIdent cannot be null");
        this.valIdent = valIdent;
    }

    /**
     * @return the valIdentifier of the array
     */
    public ValIdent getValIdent() {
        return valIdent;
    }

    /**
     * @return the string representation of the instruction
     */
    @Override
    public String toString() {
        return super.toString() + "length(" + valIdent + ")";
    }
}
