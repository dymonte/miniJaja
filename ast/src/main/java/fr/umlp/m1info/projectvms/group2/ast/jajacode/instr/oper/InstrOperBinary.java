package fr.umlp.m1info.projectvms.group2.ast.jajacode.instr.oper;

import fr.umlp.m1info.projectvms.group2.ast.jajacode.instr.Instr;

public class InstrOperBinary extends Instr {
    private final JajaCodeBinaryOper oper;

    /**
     * Constructs a new InstrOperBinary with its associated operator
     *
     * @param lineId the line number of the instruction
     * @param oper the operator associated with this instruction
     */
    public InstrOperBinary(int lineId, JajaCodeBinaryOper oper) {
        super(lineId);
        this.oper = oper;
    }

    /**
     * @return the operator associated with this instruction
     */
    public JajaCodeBinaryOper getOper() {
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
