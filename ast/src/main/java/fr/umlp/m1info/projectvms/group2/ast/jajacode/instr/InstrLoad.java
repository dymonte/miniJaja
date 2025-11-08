package fr.umlp.m1info.projectvms.group2.ast.jajacode.instr;

import fr.umlp.m1info.projectvms.group2.ast.jajacode.value.ValIdent;

/**
 * Class representing a load instruction
 */
public class InstrLoad extends Instr{
    private final ValIdent valIdent;

    /**
     * Instruction for loading a variable
     * @param lineId the line number of the instruction
     * @param valIdent the valIdentifier of the variable
     */
    public InstrLoad(int lineId, ValIdent valIdent) {
        super(lineId);
        if (valIdent == null)
            throw new NullPointerException("valIdent cannot be null");
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
        return super.toString() + "load(" + valIdent + ")";
    }
}