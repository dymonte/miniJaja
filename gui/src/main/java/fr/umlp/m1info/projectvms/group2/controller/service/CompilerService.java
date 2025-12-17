package fr.umlp.m1info.projectvms.group2.controller.service;

import java.io.File;
import java.io.FileWriter;
import java.nio.charset.StandardCharsets;

import fr.umlp.m1info.projectvms.group2.AppState;
import fr.umlp.m1info.projectvms.group2.PopUp;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.ASTNode;
import fr.umlp.m1info.projectvms.group2.checker.Diagnostic;
import fr.umlp.m1info.projectvms.group2.checker.TypeChecker;
import fr.umlp.m1info.projectvms.group2.compilation.MiniJajaCompiler;
import fr.umlp.m1info.projectvms.group2.logger.InterpreterLogger;
import fr.umlp.m1info.projectvms.group2.logger.MemoryLogger.MessageLevel;
import fr.umlp.m1info.projectvms.group2.utils.ParserUtils;
import javafx.application.Platform;
import javafx.concurrent.Task;

public class CompilerService {

	private Thread compilerThread;

	private AppState state;

	public CompilerService(AppState state) {
		if (state == null) {
			throw new NullPointerException("AppState cannot be null");
		}
		this.state = state;
	}

	/**
	 * Indicates whether an interpreter thread is currently running.
	 *
	 * @return true if running, false otherwise
	 */
	public boolean isRunning() {
		return compilerThread != null && compilerThread.isAlive();
	}

	/**
	 * Interrupts the interpreter thread if it is running.
	 * This causes the interpreter to stop execution.
	 */
	public void stop() {
		if (isRunning())
			compilerThread.interrupt();
	}

	public void compile(Runnable refreshTree) {
		Task<Void> task = new Task<>() {
			protected Void call() {
				try {
					ASTNode programRoot = loadMiniJajaProgram();
					checker(programRoot);
					File outputFile = prepareOutputFile(state.getOpenedFile().get());
					compileToFile(programRoot, outputFile);
					Platform.runLater(refreshTree);
				} catch (Exception e) {
					InterpreterLogger.getInstance().log(e.getMessage(), InterpreterLogger.MessageLevel.ERROR);
				}
				return null;
			}
		};

		compilerThread = new Thread(task);
		compilerThread.start();
	}

	private ASTNode loadMiniJajaProgram() throws Exception {
		ASTNode programRoot;
		if (state.getOpenedFile().isEmpty()) {
			programRoot = ParserUtils.parseString(state.codeProperty().get());
		} else {
			programRoot = ParserUtils.parseFile(state.getOpenedFile().get().getAbsolutePath());
		}
		return programRoot;
	}

	private File prepareOutputFile(File inputFile) {
		File targetDir = (inputFile == null)
				? new File("target")
				: new File(inputFile.getParent(), "target");

		targetDir.mkdirs();

		String baseName = (inputFile == null)
				? "untitled"
				: inputFile.getName().replace(".mjj", "");

		return new File(targetDir, baseName + ".jjc");
	}

	private void compileToFile(ASTNode node, File outputFile) throws Exception {
		StringBuilder result = new StringBuilder();
		MiniJajaCompiler compiler = new MiniJajaCompiler(result);
		compiler.compile(node);

		try (FileWriter writer = new FileWriter(outputFile, StandardCharsets.UTF_8)) {
			writer.write(result.toString());
		}
	}

	private void checker(ASTNode node) throws Exception {
		Diagnostic diag = new Diagnostic();
		TypeChecker typeChecker = new TypeChecker(diag);
		typeChecker.check(node);

		if (!diag.issues().isEmpty()) {
			StringBuilder sb = new StringBuilder();
			for (Diagnostic.Issue issue : diag.issues()) {
				sb.append(issue.toString()).append("\n");
			}

			throw new Exception(sb.toString());
		}
	}
}
