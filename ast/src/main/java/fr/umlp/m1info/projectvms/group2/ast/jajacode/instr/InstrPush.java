package fr.umlp.m1info.projectvms.group2.ast.jajacode.instr;

import fr.umlp.m1info.projectvms.group2.ast.jajacode.value.Val;

public class InstrPush extends Instr{
    private final Val val;

    public InstrPush(int lineId, Val val) {
        super(lineId);
        this.val = val;
    }

    public Val getVal() {
        return val;
    }

    @Override
    public String toString() {
        return super.toString() + "push " + val;
    }
}
