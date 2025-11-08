package fr.umlp.m1info.projectvms.group2.ast.jajacode;

import fr.umlp.m1info.projectvms.group2.ast.jajacode.instr.InstrNil;
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
}
