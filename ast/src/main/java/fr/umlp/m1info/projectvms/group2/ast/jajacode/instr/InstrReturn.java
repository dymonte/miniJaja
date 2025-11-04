package fr.umlp.m1info.projectvms.group2.ast.jajacode.instr;

public class InstrReturn extends Instr{
    public InstrReturn(int lineId) {
        super(lineId);
    }

    @Override
    public String toString() {
        return super.toString() + "return";
    }
}
