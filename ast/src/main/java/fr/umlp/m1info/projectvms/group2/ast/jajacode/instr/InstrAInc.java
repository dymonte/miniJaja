package fr.umlp.m1info.projectvms.group2.ast.jajacode.instr;

import fr.umlp.m1info.projectvms.group2.ast.jajacode.value.ValIdent;

/**
 * Class representing an array increment instruction
 */
public class InstrAInc extends Instr{
    private final ValIdent valIdent;

    /**
     * @param lineId the line number of the instruction
     * @param valIdent the identifier of the array
     */
    public InstrAInc(int lineId, ValIdent valIdent) {
        super(lineId);
        this.valIdent = valIdent;
    }

    /**
     * @return the identifier of the array
     */
    public ValIdent getValIdent() {
        return valIdent;
    }

    /**
     * @return the string representation of the instruction
     */
    @Override
    public String toString() {
        return super.toString() + "ainc(" + valIdent + ")";
    }
}
