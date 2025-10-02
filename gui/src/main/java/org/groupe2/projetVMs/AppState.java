package org.groupe2.projetVMs;

import java.io.File;
import java.util.Optional;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * AppState
 */
public class AppState {

    /* Workspace */
    private Optional<File> workspaceDir;
    private Optional<File[]> workspaceFiles;
    private Optional<File> openedFile;

    /* Properties */
    private StringProperty code = new SimpleStringProperty("");

    public StringProperty codeProperty() {
        return code;
    }

}
