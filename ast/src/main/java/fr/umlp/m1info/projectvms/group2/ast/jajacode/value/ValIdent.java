package fr.umlp.m1info.projectvms.group2.ast.jajacode.value;

public class ValIdent extends Val{
    private final String ident;

    public ValIdent(int lineId, String id) {
        super(lineId);
        this.ident = id;
    }

    public String getIdent() {
        return ident;
    }

    @Override
    public String toString() {
        return ident;
    }
}
