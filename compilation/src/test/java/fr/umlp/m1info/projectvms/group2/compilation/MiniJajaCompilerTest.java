package fr.umlp.m1info.projectvms.group2.compilation;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class MiniJajaCompilerTest {
    MiniJajaCompiler miniJajaCompiler;
    ByteArrayOutputStream outputStream;

    @Before
    public void setUp() {
        outputStream = new ByteArrayOutputStream();
        PrintStream out = new PrintStream(outputStream);
        miniJajaCompiler = new MiniJajaCompiler(out);
    }

}
