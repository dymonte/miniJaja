package fr.umlp.m1info.projectvms.group2.ast.jajacode;

import fr.umlp.m1info.projectvms.group2.ast.jajacode.instr.Instr;

public abstract class JajaCodeNode {
    protected final int lineId;

    public JajaCodeNode(int lineId) {
        if (lineId <= 0) {
            throw new IllegalArgumentException("Line id must be greater than 0");
        }
        this.lineId = lineId;
    }

    public int getLineId() {
        return lineId;
    }
}
