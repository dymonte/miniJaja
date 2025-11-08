package fr.umlp.m1info.projectvms.group2.ast.jajacode.instr;

import fr.umlp.m1info.projectvms.group2.ast.jajacode.value.Val;

/**
 * Class representing a push instruction
 */
public class InstrPush extends Instr{
    private final Val val;

    /**
     * Instruction for pushing a value on the stack
     * @param lineId the line number of the instruction
     * @param val the value to push
     */
    public InstrPush(int lineId, Val val) {
        super(lineId);
        if (val == null)
            throw new NullPointerException("val cannot be null");
        this.val = val;
    }

    /**
     * @return the value to push
     */
    public Val getValVal() {
        return val;
    }

    /**
     * @return the string representation of the instruction
     */
    @Override
    public String toString() {
        return super.toString() + "push(" + val + ")";
    }
}
