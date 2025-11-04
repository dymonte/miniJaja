package fr.umlp.m1info.projectvms.group2.ast.jajacode.instr;

import fr.umlp.m1info.projectvms.group2.ast.jajacode.value.ValIdent;

/**
 * Class representing a load instruction
 */
public class InstrLoad extends Instr{
    private final ValIdent ident;

    /**
     * Instruction for loading a variable
     * @param lineId the line number of the instruction
     * @param ident the identifier of the variable
     */
    public InstrLoad(int lineId, ValIdent ident) {
        super(lineId);
        this.ident = ident;
    }

    /**
     * @return the identifier of the variable
     */
    public ValIdent getIdent() {
        return ident;
    }

    /**
     * @return the string representation of the instruction
     */
    @Override
    public String toString() {
        return super.toString() + "load " + ident;
    }
}