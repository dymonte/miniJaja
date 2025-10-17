package fr.umlp.m1info.projectvms.group2.logger;

import org.junit.Test;

import org.junit.Before;
import org.junit.After;
import org.junit.Assert;

public class ParserLoggerTest {

	private ParserLogger logger;

	@Before
	public void setUp() {
		logger = ParserLogger.getInstance();
		logger.clearListeners();
	}

	@After
	public void tearDown() {
		logger.clearListeners();
	}

	@Test
	public void testSingletonInstanceIsSame() {
		Assert.assertSame(logger, ParserLogger.getInstance());
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
		ParserLogger.getInstance().addListener(null);
	}

	@Test
	public void testRemoveListener() {
		ParserLogger.getInstance().removeListener(null);
		ParserLogger.ParserListener listener = (msg, level) -> {
		};
		ParserLogger.getInstance().addListener(listener);
		ParserLogger.getInstance().removeListener(listener);
	}

	@Test
	public void testLog() {
		logger.log((String) null, ParserLogger.MessageLevel.INFO);
		logger.log("ah", null);

		ParserLogger.ParserListener listener = (msg, level) -> {
		};
		ParserLogger.getInstance().addListener(listener);
		logger.log("ah", ParserLogger.MessageLevel.INFO);
	}
}
