package fr.umlp.m1info.projectvms.group2.ast.jajacode.instr;

public class InstrWriteln extends Instr{
    public InstrWriteln(int lineId) {
        super(lineId);
    }

    @Override
    public String toString() {
        return super.toString() + "writeln";
    }
}
