package fr.umlp.m1info.projectvms.group2.ast.jajacode.instr;

public class InstrWrite extends Instr{
    public InstrWrite(int lineId) {
        super(lineId);
    }

    @Override
    public String toString() {
        return super.toString() + "write";
    }
}
