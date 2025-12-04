package fr.umlp.m1info.projectvms.group2;

import fr.umlp.m1info.projectvms.group2.App;

import io.cucumber.java.Before;
import io.cucumber.java.After;

public class Hooks {
	@Before(order = 0)
	public void setupJavaFX() {
		JavaFxInitializer.initialize();
	}

	@Before(order = 1)
	public void launchApp() {
		JavaFxInitializer.closeLastStage();
		JavaFxInitializer.launchScenario(App.class);
	}

	@After
	public void tearDown() {
		JavaFxInitializer.closeLastStage();
	}
}
