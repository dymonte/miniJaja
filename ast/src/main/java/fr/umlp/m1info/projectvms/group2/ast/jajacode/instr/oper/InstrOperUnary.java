package fr.umlp.m1info.projectvms.group2.ast.jajacode.instr.oper;

import fr.umlp.m1info.projectvms.group2.ast.jajacode.instr.Instr;

public class InstrOperUnary extends Instr {
    private final JajaCodeUnaryOper oper;

    /**
     * Constructs a new InstrOperBinary with its associated operator
     *
     * @param lineId the line number of the instruction
     * @param oper the operator associated with this instruction
     */
    public InstrOperUnary(int lineId, JajaCodeUnaryOper oper) {
        super(lineId);
        this.oper = oper;
    }

    /**
     * @return the operator associated with this instruction
     */
    public JajaCodeUnaryOper getOper() {
        return oper;
    }

    /**
     * @return the string representation of this instruction
     */
    @Override
    public String toString() {
        return super.toString() + oper;
    }
}
