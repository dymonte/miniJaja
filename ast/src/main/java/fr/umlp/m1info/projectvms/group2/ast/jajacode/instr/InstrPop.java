package fr.umlp.m1info.projectvms.group2.ast.jajacode.instr;

public class InstrPop extends Instr{
    public InstrPop(int lineId) {
        super(lineId);
    }

    @Override
    public String toString() {
        return super.toString() + "pop";
    }
}
