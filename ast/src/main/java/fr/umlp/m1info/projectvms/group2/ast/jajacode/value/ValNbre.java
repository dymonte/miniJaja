package fr.umlp.m1info.projectvms.group2.ast.jajacode.value;

/**
 * Class representing a number value
 */
public final class ValNbre extends Val{
    private final int value;

    /**
     * @param lineId the line number of the value
     * @param value the number value
     */
    public ValNbre(int lineId, int value) {
        super(lineId);
        this.value = value;
    }

    /**
     * @return the value of the node
     */
    public int getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "jcnbre(" + value + ")";
    }
}
