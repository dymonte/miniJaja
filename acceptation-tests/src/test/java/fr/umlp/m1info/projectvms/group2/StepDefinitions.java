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
	public void before() throws InterruptedException {
		WaitForAsyncUtils.waitForFxEvents();
		JavaFxInitializer.initialize();
	}

	@After
	public void after() throws InterruptedException {
		JavaFxInitializer.closeLastStage();
	}

	public void waitingTime(String id) {
		robot.clickOn(id);
		WaitForAsyncUtils.waitForFxEvents();
		robot.sleep(1000);
	}

	// --------------------------------------------------------
	// ------------------- GIVEN ------------------------------
	// --------------------------------------------------------
	@Given("I have a valid minijaja program that prints {int}")
	public void i_have_a_valid_minijaja_program_that_prints(int value) {
		waitingTime("#codeArea");

		robot.write("class Test {\n");
		robot.write("   int x = " + value + ";\n");
		robot.write("   main {\n");
		robot.write("      write(x);\n");
		robot.write("   }\n");
		robot.write("}");
	}

	@Given("I have a valid minijaja program with comments that prints {string}")
	public void i_have_a_valid_minijaja_program_with_comments_that_prints(String value) {
		waitingTime("#codeArea");

		robot.write("class Test {//Hey\n");
		robot.write("   main {/* Another comment */\n");
		robot.write("      write(\"" + value + "\");\n");
		robot.write("   }//Why not\n");
		robot.write("}");
	}

	@Given("I have an invalid minijaja program with a syntax issue")
	public void i_have_a_not_valid_minijaja_program_with_syntax_issue() {
		waitingTime("#codeArea");

		robot.write("class {\n");
		robot.write("   int x = 1;\n");
		robot.write("   main {\n");
		robot.write("      write(x);\n");
		robot.write("   }\n");
		robot.write("}");
	}

	@Given("I have an invalid minijaja program with a memory issue")
	public void i_have_a_not_valid_minijaja_program_with_memory_issue() {
		waitingTime("#codeArea");

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
		waitingTime("#menuExecution");
		waitingTime("#menuStartSubmenu");
		waitingTime("#menuStart");
	}

	@When("I execute the program step by step")
	public void i_execute_the_program_step_by_step() {
		waitingTime("#menuExecution");
		waitingTime("#menuStartSubmenu");
		waitingTime("#menuStepByStep");
	}

	@When("I clear the console")
	public void i_clear_the_console() {
		waitingTime("#menuExecution");
		waitingTime("#menuClearConsole");
	}

	@When("I quit the app")
	public void i_quit_the_app() {
		waitingTime("#menuFile");
		waitingTime("#menuExitApp");
	}

	// --------------------------------------------------------
	// ------------------- THEN -------------------------------
	// --------------------------------------------------------

	@Then("the program should execute without errors")
	public void the_program_should_run_without_errors() {
		waitingTime("#errorTab");
		TextArea error = robot.lookup("#errorArea").queryAs(TextArea.class);

		Assertions.assertNotNull(error);
		Assertions.assertTrue(error.getText().isEmpty());
	}

	@Then("the program should throw {string}")
	public void the_program_should_be_throw(String issue) {
		waitingTime("#errorTab");
		TextArea error = robot.lookup("#errorArea").queryAs(TextArea.class);

		Assertions.assertNotNull(error);
		Assertions.assertFalse(error.getText().isEmpty());
	}

	@Then("The program should run with {string}")
	public void the_program_shoud_run_with_errors(String err) {
		waitingTime("#errorTab");
		TextArea error = robot.lookup("#errorArea").queryAs(TextArea.class);

		Assertions.assertNotNull(error);
		Assertions.assertFalse(error.getText().isEmpty());
		Assertions.assertTrue(error.getText().contains(err));
	}

	@Then("the output should be {int}")
	public void the_output_should_be(int value) {
		waitingTime("#outputTab");
		TextArea output = robot.lookup("#outputArea").queryAs(TextArea.class);

		Assertions.assertNotNull(output);
		Assertions.assertTrue(output.getText().equals(((Integer) value).toString()));
	}

	@Then("the output should be {string}")
	public void the_output_should_be(String value) {
		waitingTime("#outputTab");
		TextArea output = robot.lookup("#outputArea").queryAs(TextArea.class);

		Assertions.assertNotNull(output);
		Assertions.assertTrue(output.getText().equals(value));
	}

	@Then("the output should be empty")
	public void the_output_should_be_empty() {
		waitingTime("#outputTab");
		TextArea output = robot.lookup("#outputArea").queryAs(TextArea.class);

		Assertions.assertNotNull(output);
		Assertions.assertTrue(output.getText().isEmpty());
	}

	@Then("the stack should be empty")
	public void the_stack_is_empty() {
		TextArea stack = robot.lookup("#memoryStackTextArea").queryAs(TextArea.class);

		Assertions.assertNotNull(stack);
		Assertions.assertTrue(stack.getText().equals("Stack Content:\n\nEmpty stack."));
	}

	@Then("the heap should be empty")
	public void the_heap_is_empty() {
		TextArea heap = robot.lookup("#memoryHeapTextArea").queryAs(TextArea.class);

		Assertions.assertNotNull(heap);
		Assertions.assertTrue(isHeapEmpty(heap.getText()));
	}

	@Then("I should see a message box with the message: {string}")
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
