package fr.umlp.m1info.projectvms.group2.interpreter;

import fr.umlp.m1info.projectvms.group2.logger.InterpreterLogger;

import org.junit.Assert;
import org.junit.Before;
import org.junit.After;
import org.junit.Test;

public class InterpreterLoggerTest {

	private InterpreterLogger logger;

	@Before
	public void setUp() {
		logger = InterpreterLogger.getInstance();
		logger.clearListeners();
	}

	@After
	public void tearDown() {
		logger.clearListeners();
	}

	@Test
	public void testSingletonIsSame() {
		Assert.assertSame(logger, InterpreterLogger.getInstance());
	}

	@Test
	public void testClearListeners() {
		logger.addListener((msg, level) -> {
		});
		logger.addListener((msg, level) -> {
		});
		Assert.assertFalse(logger.getListeners().isEmpty());

		logger.clearListeners();
		Assert.assertTrue(logger.getListeners().isEmpty());
	}

	@Test
	public void testNullListener() {
		InterpreterLogger.getInstance().addListener(null);
		Assert.assertEquals(0, InterpreterLogger.getInstance().getListeners().size());
	}

	@Test
	public void testRemoveListener() {
		InterpreterLogger.getInstance().removeListener(null);
		InterpreterLogger.InterpreterListener listener = (msg, level) -> {
		};
		InterpreterLogger.getInstance().addListener(listener);
		Assert.assertEquals(1, InterpreterLogger.getInstance().getListeners().size());
		InterpreterLogger.getInstance().removeListener(listener);
		Assert.assertEquals(0, InterpreterLogger.getInstance().getListeners().size());
	}

	@Test
	public void testLog() {
		logger.log((String) null, InterpreterLogger.MessageLevel.INFO);
		logger.log("ah", null);

		InterpreterLogger.InterpreterListener listener = (msg, level) -> {
		};
		InterpreterLogger.getInstance().addListener(listener);
		Assert.assertEquals(1, InterpreterLogger.getInstance().getListeners().size());
		logger.log("ah", InterpreterLogger.MessageLevel.INFO);
	}
}
