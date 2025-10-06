package org.groupe2.projetVMs.controller;

import org.groupe2.projetVMs.AppState;

/**
 * StatefulController
 */
public interface StatefulController {

    /**
     * Binds the controller's components to the application state.
     * This method is called to link the UI components' properties
     * (e.g., text fields, buttons) to the corresponding properties in the AppState
     * model,
     * enabling automatic UI updates when the state changes.
     * 
     * @param appState the application state
     */
    void bindState(AppState appState);
}
