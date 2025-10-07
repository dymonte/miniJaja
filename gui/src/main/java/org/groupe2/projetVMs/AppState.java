package org.groupe2.projetVMs;

import java.io.File;
import java.util.Optional;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import javax.swing.text.html.Option;

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

    public Optional<File> getOpenedFile() { return openedFile; }
    public void setOpenedFile(File file) { openedFile = Optional.of(file); }

    public Optional<File> getWorkspaceDir() { return workspaceDir; }
    public void setWorkspaceDir(File file) { this.workspaceDir = Optional.of(file); }

    public Optional<File[]> getWorkspaceFiles() { return workspaceFiles; }
    public void setWorkspaceFiles(File[] files) { this.workspaceFiles = Optional.of(files); }




    public StringProperty codeProperty() {
        return code;
    }

}
