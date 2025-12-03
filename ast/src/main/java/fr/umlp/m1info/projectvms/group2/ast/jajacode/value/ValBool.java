package fr.umlp.m1info.projectvms.group2.ast.jajacode.value;

/**
 * Class representing a boolean value
 */
public final class ValBool extends Val{
    private final boolean value;

    /**
     * @param lineId the line number of the value
     * @param value the boolean value
     */
    public ValBool(int lineId, boolean value) {
        super(lineId);
        this.value = value;
    }

    /**
     * @return the value of the boolean
     */
    public boolean getValue() {
        return value;
    }

    /**
     * @return the string representation of the value
     */
    @Override
    public String toString() {
        return (value ? "jcvrai" : "jcfaux");
    }
}
