package fr.umlp.m1info.projectvms.group2.controller.popup;

import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.Scene;
import javafx.beans.value.ChangeListener;

public class StepPopupManager {

	private Stage popup;

	public void open(Stage stage, Runnable next, Runnable stop, Runnable skip) {
		popup = new Stage();
		popup.initOwner(stage);
		popup.setAlwaysOnTop(true);
		popup.initStyle(StageStyle.UNDECORATED);

		Button nextBtn = new Button("Next");
		nextBtn.setOnAction(e -> {
			next.run();
		});

		Button stopBtn = new Button("Stop");
		stopBtn.setOnAction(e -> {
			stop.run();
		});

		Button skipBtn = new Button("Skip Breakpoint");
		skipBtn.setOnAction(e -> {
			skip.run();
		});

		HBox root;
		if (skip != null) {
			root = new HBox(10, stopBtn, nextBtn, skipBtn);
		} else {
			root = new HBox(10, stopBtn, nextBtn);
		}

		root.setStyle("-fx-padding: 20;");
		popup.setScene(new Scene(root, 150 + (skip != null ? 150 : 0), 50));

		setStepPopupPos(stage, popup, skip != null);

		popup.show();
	}

	private void setStepPopupPos(Stage stage, Stage popupStage, boolean hasBreakpoints) {
		double x = stage.getX() + stage.getWidth() / 2 - 75 - (hasBreakpoints ? 27.5 : 0);
		double y = stage.getY() + 25;
		popupStage.setX(x);
		popupStage.setY(y);

		ChangeListener<Number> xListener = (obs, oldVal, newVal) -> {
			popupStage.setX(newVal.doubleValue() + stage.getWidth() / 2 - 75);
		};
		ChangeListener<Number> yListener = (obs, oldVal, newVal) -> {
			popupStage.setY(newVal.doubleValue() + 25);
		};
		stage.xProperty().addListener(xListener);
		stage.yProperty().addListener(yListener);

		popupStage.setOnHidden(e -> {
			stage.xProperty().removeListener(xListener);
			stage.yProperty().removeListener(yListener);
		});
	}

	public void close() {
		if (popup != null && popup.isShowing()) {
			popup.close();
		}
	}
}
