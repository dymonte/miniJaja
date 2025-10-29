package fr.umlp.m1info.projectvms.group2.controller;

import fr.umlp.m1info.projectvms.group2.AppState;
import fr.umlp.m1info.projectvms.group2.memory.Memory;
import fr.umlp.m1info.projectvms.group2.memory.Quad;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

/**
 * MemorySectionController
 */
public class MemorySectionController implements StatefulController {

    @FXML
    private TextArea memoryStackTextArea;

    @FXML
    private TextArea memoryHeapTextArea;

    @Override
    public void bindState(AppState appState) {
        Memory memory = appState.getMemory();
        if (memory == null) {
            memoryStackTextArea.setText("Empty stack.");
            memoryHeapTextArea.appendText("Empty heap.\n");
            return;
        }

        // Display stack content
        StringBuilder stackDisplay = new StringBuilder();
        stackDisplay.append("Stack Content:\n\n");
        Quad quad = memory.getHead();

        if (quad == null || quad.isEmpty()) {
            stackDisplay.append("Empty stack.");
        } else {
            while (quad != null && !quad.isEmpty()) {
                stackDisplay.append(quad.toString()).append("\n");
                quad = quad.next();
            }
        }
        memoryStackTextArea.setText(stackDisplay.toString());
        Platform.runLater(() -> {
            memoryStackTextArea.positionCaret(0);
            memoryStackTextArea.setScrollTop(0);
        });


        // Display heap content
        StringBuilder heapDisplay = new StringBuilder();
        heapDisplay.append("Heap Content:\n\n");
        String heap = memory.getHeap();

        if (heap == null || heap.isEmpty()) {
            heapDisplay.append("Empty heap.");
        } else {
            heapDisplay.append(heap);
        }
        memoryHeapTextArea.setText(heapDisplay.toString());
        Platform.runLater(() -> {
            memoryHeapTextArea.positionCaret(0);
            memoryHeapTextArea.setScrollTop(0);
        });
    }


}
