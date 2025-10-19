package fr.umlp.m1info.projectvms.group2;

import org.junit.Assert;
import org.junit.Before;
import org.junit.After;
import org.junit.Test;

import fr.umlp.m1info.projectvms.group2.logger.MemoryLogger;

/**
 * MemoryLoggerTest
 */
public class MemoryLoggerTest {
	private MemoryLogger logger;

	@Before
	public void setUp() {
		logger = MemoryLogger.getInstance();
		logger.clearListeners();
	}

	@After
	public void tearDown() {
		logger.clearListeners();
	}

	@Test
	public void testSingletonInstanceIsSame() {
		Assert.assertSame(logger, MemoryLogger.getInstance());
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
		MemoryLogger.getInstance().addListener(null);
		Assert.assertEquals(0, MemoryLogger.getInstance().getListeners().size());
	}

	@Test
	public void testRemoveListener() {
		MemoryLogger.getInstance().removeListener(null);
		MemoryLogger.MemoryListener listener = (msg, level) -> {
		};
		MemoryLogger.getInstance().addListener(listener);
		Assert.assertEquals(1, MemoryLogger.getInstance().getListeners().size());
		MemoryLogger.getInstance().removeListener(listener);
		Assert.assertEquals(0, MemoryLogger.getInstance().getListeners().size());
	}

	@Test
	public void testLog() {
		logger.log((String) null, MemoryLogger.MessageLevel.INFO);
		logger.log("ah", null);

		MemoryLogger.MemoryListener listener = (msg, level) -> {
		};
		MemoryLogger.getInstance().addListener(listener);
		Assert.assertEquals(1, MemoryLogger.getInstance().getListeners().size());
		logger.log("ah", MemoryLogger.MessageLevel.INFO);
	}
}
