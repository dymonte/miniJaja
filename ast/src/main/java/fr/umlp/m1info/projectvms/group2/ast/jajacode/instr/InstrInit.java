package fr.umlp.m1info.projectvms.group2.ast.jajacode.instr;

public class InstrInit extends Instr{
    public InstrInit(int lineId) {
        super(lineId);
    }

    @Override
    public String toString() {
        return super.toString() + "init";
    }
}
