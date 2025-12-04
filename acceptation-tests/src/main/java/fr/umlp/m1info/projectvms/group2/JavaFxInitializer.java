package fr.umlp.m1info.projectvms.group2;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;

public class JavaFxInitializer {

	private static volatile boolean started = false;
	private static final CountDownLatch latch = new CountDownLatch(1);
	private static final AtomicReference<Stage> lastStageRef = new AtomicReference<>(null);

	public static void initialize() {
		if (!started) {
			Platform.setImplicitExit(false);
			Platform.startup(() -> {
				started = true;
				latch.countDown();
			});
		}
		try {
			latch.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public static void launchScenario(Class<? extends Application> appClass) {
		CountDownLatch scenarioLatch = new CountDownLatch(1);
		Platform.runLater(() -> {
			try {
				Application application = appClass.getDeclaredConstructor().newInstance();
				Stage primaryStage = new Stage();
				lastStageRef.set(primaryStage);
				application.start(primaryStage);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				scenarioLatch.countDown();
			}
		});
		try {
			scenarioLatch.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Closes the previous scenario's window if one exists.
	 */
	public static void closeLastStage() {
		Stage stage = lastStageRef.getAndSet(null);
		if (stage != null) {
			CountDownLatch closeLatch = new CountDownLatch(1);
			Platform.runLater(() -> {
				stage.close();
				closeLatch.countDown();
			});
			try {
				closeLatch.await();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
