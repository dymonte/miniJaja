package fr.umlp.m1info.projectvms.group2;

import java.io.File;
import java.util.Optional;

import fr.umlp.m1info.projectvms.group2.memory.Memory;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * AppState
 */
public class AppState {

    /* Workspace */
    private Optional<File> workspaceDir = Optional.empty();
    private Optional<File[]> workspaceFiles = Optional.empty();
    private Optional<File> openedFile = Optional.empty();

    /* Properties */
    private StringProperty code = new SimpleStringProperty("");

    private Memory memory;

    public Optional<File> getOpenedFile() { return openedFile; }
    public void setOpenedFile(File file) { openedFile = Optional.of(file); }
    public void closeOpenedFile() { openedFile = Optional.empty(); }

    public Optional<File> getWorkspaceDir() { return workspaceDir; }
    public void setWorkspaceDir(File file) { this.workspaceDir = Optional.of(file); }

    public Optional<File[]> getWorkspaceFiles() { return workspaceFiles; }
    public void setWorkspaceFiles(File[] files) { this.workspaceFiles = Optional.of(files); }

    /**
     * @return the code property
     */
    public StringProperty codeProperty() {
        return code;
    }

    /**
     * @return the memory
     */
    public Memory getMemory() {
        return memory;
    }

    /**
     * @param memory the memory
     */
    public void setMemory(Memory memory) {
        this.memory = memory;
    }
}