package fr.umlp.m1info.projectvms.group2.controller;

import fr.umlp.m1info.projectvms.group2.AppState;
import fr.umlp.m1info.projectvms.group2.memory.Memory;
import fr.umlp.m1info.projectvms.group2.memory.Quad;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

/**
 * MemorySectionController
 */
public class MemorySectionController implements StatefulController {

    @FXML
    private TextArea memoryTextArea;

    @Override
    public void bindState(AppState appState) {
        Memory memory = appState.getMemory();
        if (memory == null) {
            memoryTextArea.setText("Empty memory.");
            return;
        }

        Quad quad = memory.getHead();
        if (quad == null || quad.isEmpty()) {
            memoryTextArea.setText("Empty stack.");
            return;
        }

        StringBuilder result = new StringBuilder();

        while (quad != null && !quad.isEmpty()) {
            result.append(quad.toString()).append("\n");
            quad = quad.next();
        }

        memoryTextArea.setText(result.toString());
    }
}
