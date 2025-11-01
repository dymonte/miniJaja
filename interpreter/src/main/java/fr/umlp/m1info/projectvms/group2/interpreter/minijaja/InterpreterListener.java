package fr.umlp.m1info.projectvms.group2.interpreter.minijaja;

public interface InterpreterListener {
    void onOutput(String message);
    void onMemoryUpdate(String memoryState);
    void onError(String errorMessage);
}
