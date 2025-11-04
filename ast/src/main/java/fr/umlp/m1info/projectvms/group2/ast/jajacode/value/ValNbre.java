package fr.umlp.m1info.projectvms.group2.ast.jajacode.value;

public class ValNbre extends Val{
    private final int value;

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
        return Integer.toString(value);
    }
}
