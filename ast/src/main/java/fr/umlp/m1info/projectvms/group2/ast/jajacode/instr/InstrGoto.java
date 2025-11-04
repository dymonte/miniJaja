package fr.umlp.m1info.projectvms.group2.ast.jajacode.instr;

import fr.umlp.m1info.projectvms.group2.ast.jajacode.value.ValAddress;

public class InstrGoto extends Instr{
    private final ValAddress address;

    public InstrGoto(int lineId, ValAddress address) {
        super(lineId);
        this.address = address;
    }

    public ValAddress getAddress() {
        return address;
    }

    @Override
    public String toString() {
        return super.toString() + "goto " + address;
    }
}
