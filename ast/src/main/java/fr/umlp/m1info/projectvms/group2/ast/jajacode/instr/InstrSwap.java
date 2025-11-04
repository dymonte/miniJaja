package fr.umlp.m1info.projectvms.group2.ast.jajacode.instr;

public class InstrSwap extends Instr{
    public InstrSwap(int lineId) {
        super(lineId);
    }

    @Override
    public String toString() {
        return super.toString() + "swap";
    }
}
