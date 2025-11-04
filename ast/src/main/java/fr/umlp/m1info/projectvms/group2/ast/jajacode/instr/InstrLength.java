package fr.umlp.m1info.projectvms.group2.ast.jajacode.instr;

import fr.umlp.m1info.projectvms.group2.ast.jajacode.value.ValIdent;

/**
 * Class representing a length instruction
 */
public class InstrLength extends Instr{
    private final ValIdent ident;

    /**
     * Instruction for getting the length of an array
     * @param lineId the line number of the instruction
     * @param ident the identifier of the array
     */
    public InstrLength(int lineId, ValIdent ident) {
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
        return super.toString() + "length " + ident;
    }
}
