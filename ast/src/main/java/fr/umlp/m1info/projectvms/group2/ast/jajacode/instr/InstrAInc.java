package fr.umlp.m1info.projectvms.group2.ast.jajacode.instr;

import fr.umlp.m1info.projectvms.group2.ast.jajacode.value.ValIdent;

/**
 * Class representing an array increment instruction
 */
public class InstrAInc extends Instr{
    private final ValIdent ident;

    /**
     * @param lineId the line number of the instruction
     * @param ident the identifier of the array
     */
    public InstrAInc(int lineId, ValIdent ident) {
        super(lineId);
        this.ident = ident;
    }

    /**
     * @return the identifier of the array
     */
    public ValIdent getIdent() {
        return ident;
    }

    /**
     * @return the string representation of the instruction
     */
    @Override
    public String toString() {
        return super.toString() + "ainc " + ident;
    }
}
