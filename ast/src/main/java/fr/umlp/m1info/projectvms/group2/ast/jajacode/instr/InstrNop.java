package fr.umlp.m1info.projectvms.group2.ast.jajacode.instr;

public class InstrNop extends Instr{
    public InstrNop(int lineId) {
        super(lineId);
    }

    @Override
    public String toString() {
        return super.toString() + "nop";
    }
}
