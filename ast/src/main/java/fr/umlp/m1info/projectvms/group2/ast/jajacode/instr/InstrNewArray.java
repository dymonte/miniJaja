package fr.umlp.m1info.projectvms.group2.ast.jajacode.instr;

import fr.umlp.m1info.projectvms.group2.ast.jajacode.value.ValIdent;

import fr.umlp.m1info.projectvms.group2.ast.jajacode.utils.TypeArray;

/**
 * Class representing a new array instruction
 */
public class InstrNewArray extends Instr{
    private final ValIdent ident;
    private final TypeArray type;

    /**
     * Instruction for creating a new array
     * @param lineId the line number of the instruction
     * @param ident the identifier of the array
     * @param type the type of values held by the array
     */
    public InstrNewArray(int lineId, ValIdent ident, TypeArray type) {
        super(lineId);
        this.ident = ident;
        this.type = type;
    }

    /**
     * @return the identifier of the array
     */
    public ValIdent getIdent() {
        return ident;
    }

    /**
     * @return the type of values held by the array
     */
    public TypeArray getType() {
        return type;
    }

    /**
     * @return the string representation of the instruction
     */
    @Override
    public String toString() {
        return "newarray(" + ident + ", " + type + ")";
    }

}
