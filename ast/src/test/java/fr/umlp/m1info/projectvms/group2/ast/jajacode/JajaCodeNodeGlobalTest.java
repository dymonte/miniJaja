package fr.umlp.m1info.projectvms.group2.ast.jajacode;

import fr.umlp.m1info.projectvms.group2.ast.jajacode.instr.InstrNil;
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
}
