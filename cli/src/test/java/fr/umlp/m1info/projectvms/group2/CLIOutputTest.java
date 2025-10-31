package fr.umlp.m1info.projectvms.group2;

import org.junit.Assert;
import org.junit.Before;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.After;
import org.junit.Test;

import fr.umlp.m1info.projectvms.group2.cli.CLIOutput;
import fr.umlp.m1info.projectvms.group2.logger.MemoryLogger;

import fr.umlp.m1info.projectvms.group2.logger.InterpreterLogger.MessageLevel;
import fr.umlp.m1info.projectvms.group2.logger.ParserLogger;

/**
 * CLIOutputTest
 */
public class CLIOutputTest {
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private final PrintStream originalErr = System.err;

    private CLIOutput output;

    @Before
    public void setUpOutput() {
        output = CLIOutput.getInstance();
    }

    @Before
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
    }

    @After
    public void restoreStreams() {
        System.setOut(originalOut);
        System.setErr(originalErr);
    }

    @Test
    public void testWrite() {
        String message = "test";
        CLIOutput.write(message);
        Assert.assertTrue(outContent.toString().contains(message));
    }

    @Test
    public void testInfo() {
        String message = "test";
        CLIOutput.info(message);
        Assert.assertTrue(outContent.toString().contains(message));
    }

    @Test
    public void testDebug() {
        String message = "test";
        CLIOutput.debug(message);
        Assert.assertTrue(outContent.toString().contains(message));
    }

    @Test
    public void testWarning() {
        String message = "test";
        CLIOutput.warning(message);
        Assert.assertTrue(outContent.toString().contains(message));
    }

    @Test
    public void testError() {
        String message = "test";
        CLIOutput.error(message);
        Assert.assertTrue(errContent.toString().contains(message));
    }

    @Test
    public void testErrorWithCtx() {
        String message = "test";
        String context = "in function f";
        CLIOutput.error(context, message);
        Assert.assertTrue(errContent.toString().contains(context));
        Assert.assertTrue(errContent.toString().contains(message));
    }

    @Test
    public void testReceiveMessageParserLogger() {
        String message1 = "test1";
        String message2 = "test2";
        String message3 = "test3";
        output.receiveMessage(message1, ParserLogger.MessageLevel.INFO);
        output.receiveMessage(message2, ParserLogger.MessageLevel.DEBUG);
        output.receiveMessage(message3, ParserLogger.MessageLevel.WARNING);
        Assert.assertTrue(outContent.toString().contains(message1));
        Assert.assertTrue(outContent.toString().contains(message2));
        Assert.assertTrue(outContent.toString().contains(message3));
        output.receiveMessage(message1, ParserLogger.MessageLevel.ERROR);
        Assert.assertTrue(errContent.toString().contains(message1));
    }

    @Test
    public void testReceiveMessageInterpreterLogger() {
        String message1 = "test1";
        String message2 = "test2";
        String message3 = "test3";
        String message4 = "test4";
        output.receiveMessage(message1, MessageLevel.INFO);
        output.receiveMessage(message2, MessageLevel.DEBUG);
        output.receiveMessage(message3, MessageLevel.WARNING);
        output.receiveMessage(message4, MessageLevel.WRITE);
        Assert.assertTrue(outContent.toString().contains(message1));
        Assert.assertTrue(outContent.toString().contains(message2));
        Assert.assertTrue(outContent.toString().contains(message3));
        Assert.assertTrue(outContent.toString().contains(message4));
        output.receiveMessage(message1, MessageLevel.ERROR);
        Assert.assertTrue(errContent.toString().contains(message1));
    }

}
