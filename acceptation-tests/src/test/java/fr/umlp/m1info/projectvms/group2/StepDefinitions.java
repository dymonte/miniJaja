package fr.umlp.m1info.projectvms.group2;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TextArea;

import org.junit.jupiter.api.Assertions;
import org.testfx.api.FxAssert;
import org.testfx.api.FxRobot;
import org.testfx.util.WaitForAsyncUtils;

public class StepDefinitions {
	private FxRobot robot = new FxRobot();

	@Before
	public void before() {
		WaitForAsyncUtils.waitForFxEvents();
		JavaFxInitializer.initialize();
	}

	@After
	public void after() {
		JavaFxInitializer.closeLastStage();
	}

	// --------------------------------------------------------
	// ------------------- GIVEN ------------------------------
	// --------------------------------------------------------
	@Given("I have a valid minijaja program that prints {int}")
	public void i_have_a_valid_minijaja_program_that_prints(int value) {
		robot.clickOn("#codeArea");

		robot.write("class Test {\n");
		robot.write("   int x = " + value + ";\n");
		robot.write("   main {\n");
		robot.write("      write(x);\n");
		robot.write("   }\n");
		robot.write("}");
	}

	@Given("I have a valid minijaja program with comments that prints {string}")
	public void i_have_a_valid_minijaja_program_with_comments_that_prints(String value) {
		robot.clickOn("#codeArea");

		robot.write("class Test {//Hey\n");
		robot.write("   main {/* Another comment */\n");
		robot.write("      write(\"" + value + "\");\n");
		robot.write("   }//Why not\n");
		robot.write("}");
	}

	@Given("I have a not valid minijaja program with syntax issue")
	public void i_have_a_not_valid_minijaja_program_with_syntax_issue() {
		robot.clickOn("#codeArea");

		robot.write("class {\n");
		robot.write("   int x = 1;\n");
		robot.write("   main {\n");
		robot.write("      write(x);\n");
		robot.write("   }\n");
		robot.write("}");
	}

	@Given("I have a not valid minijaja program with memory issue")
	public void i_have_a_not_valid_minijaja_program_with_memory_issue() {
		robot.clickOn("#codeArea");

		robot.write("class Test {\n");
		robot.write("   main {\n");
		robot.write("      write(x);\n");
		robot.write("   }\n");
		robot.write("}");
	}

	// --------------------------------------------------------
	// ------------------- WHEN -------------------------------
	// --------------------------------------------------------

	@When("I execute the program")
	public void i_execute_the_program() {
		robot.clickOn("#menuExecution");
		robot.clickOn("#menuStartSubmenu");
		robot.clickOn("#menuStart");
	}

	@When("I execute the program step by step")
	public void i_execute_the_program_step_by_step() {
		robot.clickOn("#menuExecution");
		robot.clickOn("#menuStartSubmenu");
		robot.clickOn("#menuStepByStep");
	}

	@When("I clear the console")
	public void i_clear_the_console() {
		robot.clickOn("#menuExecution");
		robot.clickOn("#menuClearConsole");
	}

	@When("I quit the app")
	public void i_quit_the_app() {
		robot.clickOn("#menuFile");
		robot.clickOn("#menuExitApp");
	}

	// --------------------------------------------------------
	// ------------------- THEN -------------------------------
	// --------------------------------------------------------

	@Then("The program should run without errors")
	public void the_program_should_run_without_errors() {
		robot.clickOn("#errorTab");
		TextArea error = robot.lookup("#errorArea").queryAs(TextArea.class);

		Assertions.assertNotNull(error);
		Assertions.assertTrue(error.getText().isEmpty());
	}

	@Then("The program should be throw {string}")
	public void the_program_should_be_throw(String issue) {
		robot.clickOn("#errorTab");
		TextArea error = robot.lookup("#errorArea").queryAs(TextArea.class);

		Assertions.assertNotNull(error);
		Assertions.assertFalse(error.getText().isEmpty());
	}

	@Then("The program should run with {string}")
	public void the_program_shoud_run_with_errors(String err) {
		robot.clickOn("#errorTab");
		TextArea error = robot.lookup("#errorArea").queryAs(TextArea.class);

		Assertions.assertNotNull(error);
		Assertions.assertFalse(error.getText().isEmpty());
		Assertions.assertTrue(error.getText().contains(err));
	}

	@Then("The output should be {int}")
	public void the_output_should_be(int value) {
		robot.clickOn("#outputTab");
		TextArea output = robot.lookup("#outputArea").queryAs(TextArea.class);

		Assertions.assertNotNull(output);
		Assertions.assertTrue(output.getText().equals(((Integer) value).toString()));
	}

	@Then("The output should be {string}")
	public void the_output_should_be(String value) {
		robot.clickOn("#outputTab");
		TextArea output = robot.lookup("#outputArea").queryAs(TextArea.class);

		Assertions.assertNotNull(output);
		Assertions.assertTrue(output.getText().equals(value));
	}

	@Then("The output should be empty")
	public void the_output_should_be_empty() {
		robot.clickOn("#outputTab");
		TextArea output = robot.lookup("#outputArea").queryAs(TextArea.class);

		Assertions.assertNotNull(output);
		Assertions.assertTrue(output.getText().isEmpty());
	}

	@Then("The stack is empty")
	public void the_stack_is_empty() {
		TextArea stack = robot.lookup("#memoryStackTextArea").queryAs(TextArea.class);

		Assertions.assertNotNull(stack);
		Assertions.assertTrue(stack.getText().equals("Stack Content:\n\nEmpty stack."));
	}

	@Then("The heap is empty")
	public void the_heap_is_empty() {
		TextArea heap = robot.lookup("#memoryHeapTextArea").queryAs(TextArea.class);

		Assertions.assertNotNull(heap);
		Assertions.assertTrue(isHeapEmpty(heap.getText()));
	}

	@Then("I have a message box with this message : {string}")
	public void i_have_a_message_box(String message) {
		FxAssert.verifyThat(".dialog-pane", (DialogPane pane) -> {
			String content = pane.getContentText();
			return content != null && content.contains(message);
		});
	}

	private static boolean isHeapEmpty(String heap) {
		heap = heap.replaceFirst("(?i)^Heap Content:\\s*", "");

		String[] tokens = heap.split("\\s+");

		for (String token : tokens) {
			if (token.endsWith(":")) {
				continue;
			}

			if (token.isEmpty()) {
				continue;
			}

			if (!token.equalsIgnoreCase("00")) {
				return false;
			}
		}
		return true;
	}
}
