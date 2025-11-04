package fr.umlp.m1info.projectvms.group2.ast.jajacode.instr;

import fr.umlp.m1info.projectvms.group2.ast.jajacode.JajaCodeNode;

/**
 * Abstract class representing an instruction in JajaCode
 */
public abstract class Instr extends JajaCodeNode {

    /**
     * Creates a new instruction with its associated line number
     * @param lineId the line number of the instruction
     */
    public Instr(int lineId) {
        super(lineId);
    }
}

