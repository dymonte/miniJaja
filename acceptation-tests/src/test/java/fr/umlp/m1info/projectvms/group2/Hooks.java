package fr.umlp.m1info.projectvms.group2;

import io.cucumber.java.Before;
import io.cucumber.java.After;

public class Hooks {
	@Before(order = 0)
	public void setupJavaFX() throws InterruptedException {
		JavaFxInitializer.initialize();
	}

	@Before(order = 1)
	public void launchApp() throws InterruptedException {
		JavaFxInitializer.closeLastStage();
		JavaFxInitializer.launchScenario(App.class);
	}

	@After
	public void tearDown() throws InterruptedException {
		JavaFxInitializer.closeLastStage();
	}
}
