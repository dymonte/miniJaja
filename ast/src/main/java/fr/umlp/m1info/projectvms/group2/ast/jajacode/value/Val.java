package fr.umlp.m1info.projectvms.group2.ast.jajacode.value;

import fr.umlp.m1info.projectvms.group2.ast.jajacode.JajaCodeNode;

/**
 * Abstract class representing a value in JajaCode
 */
public abstract class Val extends JajaCodeNode {
    public Val(int lineId) {
        super(lineId);
    }
}
