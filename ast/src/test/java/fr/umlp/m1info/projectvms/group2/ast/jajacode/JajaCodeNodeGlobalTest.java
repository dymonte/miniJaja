package fr.umlp.m1info.projectvms.group2.ast.jajacode;

import fr.umlp.m1info.projectvms.group2.ast.jajacode.instr.InstrNil;
import fr.umlp.m1info.projectvms.group2.ast.jajacode.utils.Kind;
import fr.umlp.m1info.projectvms.group2.ast.jajacode.utils.Type;
import fr.umlp.m1info.projectvms.group2.ast.jajacode.value.ValAddress;
import org.junit.Assert;
import org.junit.Test;

public class JajaCodeNodeGlobalTest {
    @Test
    public void testJajaCodeNodeIllegalLineId() {
        IllegalArgumentException e = Assert.assertThrows(IllegalArgumentException.class, () -> {
            new InstrNil(-1);
        });

        Assert.assertEquals("Line id must be positive", e.getMessage());
    }

    @Test
    public void testValAddressIllegal() {
        IllegalArgumentException e = Assert.assertThrows(IllegalArgumentException.class, () -> {
            new ValAddress(1, -1);
        });

        Assert.assertEquals("Address must be greater than 0", e.getMessage());

        e = Assert.assertThrows(IllegalArgumentException.class, () -> {
            new ValAddress(1, 0);
        });

        Assert.assertEquals("Address must be greater than 0", e.getMessage());
    }

    @Test
    public void testKindFromString() {
        Kind kind = Kind.fromString("cst");
        Assert.assertEquals(Kind.CONST, kind);

        kind = Kind.fromString("var");
        Assert.assertEquals(Kind.VAR, kind);

        kind = Kind.fromString("arr");
        Assert.assertEquals(Kind.ARR, kind);

        kind = Kind.fromString("meth");
        Assert.assertEquals(Kind.METH, kind);

        IllegalArgumentException e = Assert.assertThrows(IllegalArgumentException.class, () -> {
            Kind.fromString("unknown");
        });

        Assert.assertEquals("Unknow operation: unknown", e.getMessage());
    }

    @Test
    public void testTypeFromString() {
        Type type = Type.fromString("int");
        Assert.assertEquals(Type.INT, type);

        type = Type.fromString("bool");
        Assert.assertEquals(Type.BOOL, type);

        IllegalArgumentException e = Assert.assertThrows(IllegalArgumentException.class, () -> {
            Type.fromString("unknown");
        });

        Assert.assertEquals("Unknow operation: unknown", e.getMessage());


    }
}
