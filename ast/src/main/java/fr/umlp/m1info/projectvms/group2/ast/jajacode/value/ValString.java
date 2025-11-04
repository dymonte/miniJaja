package fr.umlp.m1info.projectvms.group2.ast.jajacode.value;

public class ValString extends Val {
    private final String value;

    public ValString(int lineId, String value) {
        super(lineId);
        this.value = value;
    }

    /**
     * @return the value of the string
     */
    public String getValue() {
        return value;
    }
}
