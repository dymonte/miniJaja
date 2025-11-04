package fr.umlp.m1info.projectvms.group2.ast.jajacode.instr;

public class InstrStop extends Instr{
    public InstrStop(int lineId) {
        super(lineId);
    }

    @Override
    public String toString() {
        return super.toString() + "stop";
    }
}
