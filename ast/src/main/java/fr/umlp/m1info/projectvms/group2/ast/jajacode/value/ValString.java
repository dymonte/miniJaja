package fr.umlp.m1info.projectvms.group2.ast.jajacode.value;

/**
 * Class representing a string value
 */
public final class ValString extends Val {
    private final String value;

    /**
     * @param lineId the line number of the value
     * @param value the string value
     */
    public ValString(int lineId, String value) {
        super(lineId);
        if (value == null)
            throw new NullPointerException("value cannot be null");
        this.value = value;
    }

    /**
     * @return the value of the string
     */
    public String getValue() {
        return value;
    }

    /**
     * @return the string representation of the value
     */
    @Override
    public String toString() {
        return value;
    }
}
