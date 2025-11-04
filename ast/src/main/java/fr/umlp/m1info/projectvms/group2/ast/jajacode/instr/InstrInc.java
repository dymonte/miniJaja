package fr.umlp.m1info.projectvms.group2.ast.jajacode.instr;

import fr.umlp.m1info.projectvms.group2.ast.jajacode.value.ValIdent;

public class InstrInc extends Instr{
    private final ValIdent ident;

    public InstrInc(int lineId, ValIdent ident) {
        super(lineId);
        this.ident = ident;
    }

    public ValIdent getIdent() {
        return ident;
    }

    @Override
    public String toString() {
        return super.toString() + "inc " + ident;
    }
}
