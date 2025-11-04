package fr.umlp.m1info.projectvms.group2.ast.jajacode.instr;

import fr.umlp.m1info.projectvms.group2.ast.jajacode.utils.Kind;
import fr.umlp.m1info.projectvms.group2.ast.jajacode.utils.TypeVal;
import fr.umlp.m1info.projectvms.group2.ast.jajacode.value.Val;
import fr.umlp.m1info.projectvms.group2.ast.jajacode.value.ValIdent;

/**
 * Class representing a new instruction
 */
public class InstrNew extends Instr{
    private final ValIdent ident;
    private final TypeVal type;
    private final Kind kind;
    private final Val val;


    /**
     * Instruction for declaring a variable or a method
     * @param lineId the line number of the instruction
     * @param ident the identifier of the element
     * @param kind the kind of the element
     * @param type the type of the element
     * @param val the initial value of the element
     */
    public InstrNew(int lineId, ValIdent ident, Kind kind, TypeVal type, Val val) {
        super(lineId);
        this.ident = ident;
        this.type = type;
        this.kind = kind;
        this.val = val;
    }

    /**
     * @return the identifier of the element
     */
    public ValIdent getIdent() {
        return ident;
    }

    /**
     * @return the initial value of the element
     */
    public Val getVal() {
        return val;
    }

    /**
     * @return the kind of the element
     */
    public Kind getKind() {
        return kind;
    }

    /**
     * @return the type of the element
     */
    public TypeVal getType() {
        return type;
    }

    /**
     * @return the string representation of the instruction
     */
    @Override
    public String toString() {
        return "new(" + kind + ", " + ident + ", " + type + ", " + val + ")";
    }
}
