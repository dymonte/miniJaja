package fr.umlp.m1info.projectvms.group2;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.testfx.api.FxAssert.verifyThat;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import javafx.scene.Node;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

@ExtendWith(ApplicationExtension.class)
public class AppFunctionalTest {

    @Start
    public void start(Stage stage) throws Exception {
        new App().start(stage);
    }

    //@Test
    // void testOpenFolderMenu(FxRobot robot) {
    //     robot.clickOn("#menuFile");
    //     robot.clickOn("#menuOpenFolder");

    //     verifyThat("Open Folder", isVisible());
    // }

    @Test
    void testWriteAndRunProgram(FxRobot robot) {
        robot.clickOn("#codeArea");

        robot.write("class Test {\n");
        robot.write("   int x = 42;\n");
        robot.write("   main {\n");
        robot.write("      writeln(x);\n");
        robot.write("   }\n");
        robot.write("}");

        robot.clickOn("#menuExecution");
        robot.clickOn("#menuStart");

        robot.clickOn("#outputTab");
        
        verifyThat("#outputArea", (Node node) -> {
            String text = ((TextArea) node).getText();
            return text.contains("42");
        });
    }
}
