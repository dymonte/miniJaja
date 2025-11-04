package fr.umlp.m1info.projectvms.group2.ast.jajacode.instr;

import fr.umlp.m1info.projectvms.group2.ast.jajacode.value.ValIdent;

/**
 * Class representing a store instruction
 */
public class InstrStore extends Instr{
    private final ValIdent ident;

    /**
     * Instruction for storing a value in a variable
     * @param lineId the line number of the instruction
     * @param ident the identifier of the variable
     */
    public InstrStore(int lineId, ValIdent ident) {
        super(lineId);
        this.ident = ident;
    }

    /**
     * @return the identifier to store
     */
    public ValIdent getIdent() {
        return ident;
    }

    /**
     * @return the string representation of the instruction
     */
    @Override
    public String toString() {
        return super.toString() + "store " + ident;
    }
}
