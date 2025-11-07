package fr.umlp.m1info.projectvms.group2.ast.jajacode.value;

/**
 * Class representing an identifier value
 */
public final class ValIdent extends Val{
    private final String ident;

    /**
     * @param lineId the line number of the value
     * @param id the identifier
     */
    public ValIdent(int lineId, String id) {
        super(lineId);
        this.ident = id;
    }

    /**
     * @return the identifier
     */
    public String getIdent() {
        return ident;
    }

    /**
     * @return the string representation of the value
     */
    @Override
    public String toString() {
        return "jcident(" + ident + ")";
    }
}
