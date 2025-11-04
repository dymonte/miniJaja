package fr.umlp.m1info.projectvms.group2.ast.jajacode.instr;

import fr.umlp.m1info.projectvms.group2.ast.jajacode.value.ValIdent;

public class InstrInvoke extends Instr {
    private final ValIdent valIdent;

    public InstrInvoke(int lineId, ValIdent valIdent) {
        super(lineId);
        if(valIdent == null) throw new NullPointerException("valIdent cannot be null");
        this.valIdent = valIdent;
    }

    public ValIdent getValIdent() {
        return valIdent;
    }

    @Override
    public String toString() {
        return super.toString() + "invoke " + valIdent;
    }
}
