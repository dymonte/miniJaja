package fr.umlp.m1info.projectvms.group2.ast.jajacode;

public class JajaCodeNode {
    private final int lineId;

    public JajaCodeNode(int lineId) {
        this.lineId = lineId;
    }

    public int getLineId() {
        return lineId;
    }

    @Override
    public String toString() {
        return lineId + " : ";
    }
}
