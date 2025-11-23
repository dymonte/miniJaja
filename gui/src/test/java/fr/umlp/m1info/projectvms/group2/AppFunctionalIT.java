package fr.umlp.m1info.projectvms.group2;

import org.fxmisc.richtext.CodeArea;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.testfx.api.FxAssert.verifyThat;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;
import org.testfx.matcher.base.NodeMatchers;
import org.testfx.util.WaitForAsyncUtils;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import javafx.scene.Node;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

@ExtendWith(ApplicationExtension.class)
public class AppFunctionalIT {

    @Start
    public void start(Stage stage) throws Exception {
        new App().start(stage);
    }

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
        robot.clickOn("#menuStartSubmenu");
        robot.clickOn("#menuStart");
        robot.clickOn("#outputTab");

        WaitForAsyncUtils.waitForFxEvents();

        verifyThat("#outputArea", (Node node) -> {
            String text = ((TextArea) node).getText();
            return text.contains("42");
        });
    }

    @Test
    void testComments(FxRobot robot) {
        robot.clickOn("#codeArea");

        robot.write("/* This is a comment */\n");
        robot.write("class Test {\n");
        robot.write("   int x = 24; // Variable declaration\n");
        robot.write("   main {\n");
        robot.write("      writeln(x); // Print x\n");
        robot.write("   }\n");
        robot.write("}");

        robot.clickOn("#menuExecution");
        robot.clickOn("#menuStartSubmenu");
        robot.clickOn("#menuStart");
        robot.clickOn("#outputTab");

        WaitForAsyncUtils.waitForFxEvents();

        verifyThat("#outputArea", (Node node) -> {
            String text = ((TextArea) node).getText();
            return text.contains("24");
        });
    }

    @Test
    void testErrorSyntax(FxRobot robot) throws TimeoutException {
        robot.clickOn("#codeArea");

        robot.write("class Test {\n");
        robot.write("   int x = ;\n"); // Syntax error
        robot.write("   main {\n");
        robot.write("      writeln(x);\n");
        robot.write("   }\n");
        robot.write("}");

        robot.clickOn("#menuExecution");
        robot.clickOn("#menuStartSubmenu");
        robot.clickOn("#menuStart");

        WaitForAsyncUtils.waitForFxEvents();
        WaitForAsyncUtils.waitFor(10, TimeUnit.SECONDS, () -> robot.lookup(".alert").tryQuery().isPresent());

        verifyThat(".alert", NodeMatchers.isVisible());
        verifyThat(".dialog-pane .label",
                (Label label) -> label.getText().contains("error: illegal start of statement"));
    }

    @Test
    void testErrorUndeclaredVariable(FxRobot robot) throws TimeoutException {
        robot.clickOn("#codeArea");
        robot.write("class Test {\n");
        robot.write("   main {\n");
        robot.write("      writeln(y);\n"); // undeclared variable
        robot.write("   }\n");
        robot.write("}");

        robot.clickOn("#menuExecution");
        robot.clickOn("#menuStartSubmenu");
        robot.clickOn("#menuStart");

        WaitForAsyncUtils.waitForFxEvents();
        WaitForAsyncUtils.waitFor(10, TimeUnit.SECONDS, () -> robot.lookup(".alert").tryQuery().isPresent());

        verifyThat(".alert", NodeMatchers.isVisible());
        verifyThat(".dialog-pane .label",
                (Label label) -> label.getText().contains("Error interpreting node ASTIdent"));
    }

    @Test
    void testCloseFile(FxRobot robot) {
        robot.clickOn("#codeArea");

        robot.write("TODO : Just a clear test\n");

        verifyThat("#codeArea", (Node node) -> {
            String text = ((CodeArea) node).getText();
            return !text.isEmpty();
        });

        robot.clickOn("#menuFile");
        robot.clickOn("#menuCloseCurrentFile");
        robot.clickOn("#codeArea");

        WaitForAsyncUtils.waitForFxEvents();

        verifyThat("#codeArea", (Node node) -> {
            String text = ((CodeArea) node).getText();
            return text.isEmpty();
        });
    }

    @Test
    void testCloseApp(FxRobot robot) {
        robot.clickOn("#menuFile");
        robot.clickOn("#menuExitApp");

        WaitForAsyncUtils.waitForFxEvents();

        verifyThat(".dialog-pane", (DialogPane pane) -> {
            String content = pane.getContentText();
            return content != null && content.contains("Are you sure you want to quit?");
        });
    }

    @Test
    void testClearConsole(FxRobot robot) {
        robot.clickOn("#codeArea");

        robot.write("class Test {\n");
        robot.write("   int x = 24;\n");
        robot.write("   main {\n");
        robot.write("      writeln(x);\n");
        robot.write("   }\n");
        robot.write("}");

        robot.clickOn("#menuExecution");
        robot.clickOn("#menuStartSubmenu");
        robot.clickOn("#menuStart");
        robot.clickOn("#outputTab");

        WaitForAsyncUtils.waitForFxEvents();

        verifyThat("#outputArea", (Node node) -> {
            String text = ((TextArea) node).getText();
            return text.contains("24");
        });

        robot.clickOn("#menuExecution");
        robot.clickOn("#menuClearConsole");
        robot.clickOn("#outputTab");

        WaitForAsyncUtils.waitForFxEvents();

        verifyThat("#outputArea", (Node node) -> {
            String text = ((TextArea) node).getText();
            return text.isEmpty();
        });
    }

    /**
     * TODO :
     * En tant qu’utilisateur, je veux pouvoir observer les états mémoire en fin
     * d’exécution des programmes afin de les comparer.
     * En tant que développeur, je veux voir le JajaCode produit par la compilation,
     * afin de comprendre le résultat obtenu.
     * En tant que développeur ayant compilé un programme, je souhaite pouvoir
     * placer des points d'arrêts dans le JajaCode, afin d’analyser l’exécution
     * après compilation.
     * En tant que développeur ayant compilé un programme, je souhaite pouvoir
     * exécuter pas à pas le programme JajaCode obtenu, afin d'analyser plus
     * finement son comportement.
     * En tant que développeur, je veux que l’outil permette de visualiser les
     * résultats mémoire, afin d'analyser plus finement (la gestion de la mémoire
     * dans mon programme. | le comportement de mon programme.)
     * En tant qu’utilisateur, je veux visualiser l’évolution de la pile, du tas et
     * des variables pendant l’exécution pas à pas, afin de comprendre le
     * comportement mémoire du programme.
     */
}
