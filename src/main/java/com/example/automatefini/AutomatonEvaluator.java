package com.example.automatefini;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import java.util.*;
import javafx.scene.control.ButtonBar;

public class AutomatonEvaluator {

    public static void evaluateWord(Automaton automaton, String word) {
        System.out.println("wordFirst:" + word);
        boolean isAccepted;
        String initialWord = word;
        StringBuilder calculationSteps = new StringBuilder();
        int currentState = 0;
        for (char symbol : word.toCharArray()) {
            System.out.println("veeeeeeeerify");
            int terminalIndex = automaton.indexOfTerminal(symbol);
            if (terminalIndex == -1) {
                showErrorAlert("Erreur", "Symbole invalide", "Le symbole '" + symbol + "' n'est pas dans l'alphabet.");
                return;
            }
            int targetState = automaton.getTransitions()[currentState * automaton.getNumTerminals()
                    + terminalIndex].getTargetState().getStateNumber();

            calculationSteps.append("On lit '").append(symbol).append("', l'état actuel est q").append(currentState)
                    .append(", le nouvel état est q").append(targetState).append(", et le reste du mot est '")
                    .append(word.substring(1)).append("'\n");
            // Passer à l'état suivant
            currentState = targetState;
            // Supprimer le premier caractère du mot restant
            // word = word.substring(1);
        }
        // Afficher le résultat
        isAccepted = automaton.accepts(word);
        System.out.println("word:" + word);
        if (isAccepted) {
            showSuccessDialog("Succès", "Mot accepté", "Le mot '" + initialWord + "' est accepté par l'automate.",
                    calculationSteps.toString());
        } else {
            showFailureDialog("Échec", "Mot rejeté", "Le mot '" + initialWord + "' est rejeté par l'automate.",
                    calculationSteps.toString());
        }
    }

    private static void showSuccessDialog(String title, String header, String content, String calculationSteps) {
        showResultDialog(Alert.AlertType.INFORMATION, title, header, content, calculationSteps);
    }

    // Méthode pour afficher une dialog d'échec
    private static void showFailureDialog(String title, String header, String content, String calculationSteps) {
        showResultDialog(Alert.AlertType.ERROR, title, header, content, calculationSteps);
    }

    // Méthode générique pour afficher une dialog de résultat
    private static void showResultDialog(Alert.AlertType type, String title, String header, String content,
            String calculationSteps) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle(title);
        dialog.setHeaderText(header);
        // Set the button types.
        ButtonType closeButton = new ButtonType("Close", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(closeButton);
        // Set the content
        TextArea textArea = new TextArea(content + "\n\nÉtapes de calcul :\n" + calculationSteps);
        textArea.setEditable(false);
        textArea.setWrapText(true);
        textArea.setMaxWidth(Double.MAX_VALUE);
        textArea.setMaxHeight(Double.MAX_VALUE);

        GridPane.setVgrow(textArea, Priority.ALWAYS);
        GridPane.setHgrow(textArea, Priority.ALWAYS);
        GridPane gridPane = new GridPane();
        gridPane.setMaxWidth(Double.MAX_VALUE);
        gridPane.add(textArea, 0, 0);

        dialog.getDialogPane().setContent(gridPane);
        // Show the dialog and wait for the user's response
        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent() && result.get() == closeButton) {
            dialog.close();
        }
    }

    // Méthode pour afficher une alerte d'erreur
    static void showErrorAlert(String title, String header, String content) {
        Alert errorAlert = new Alert(Alert.AlertType.ERROR);
        errorAlert.setTitle(title);
        errorAlert.setHeaderText(header);
        errorAlert.setContentText(content);
        errorAlert.showAndWait();
    }

    public static String checkAutomatonType(Automaton automaton) {
        State initialState = automaton.getInState();
        State[] finalStates = automaton.getFiStates();
        Transition[] transitions = automaton.getTransitions();
        // 1. Check if the automaton is empty (no path from the initial state to any final state).
        if (isAutomatonEmpty(initialState, finalStates, transitions)) {
            return "Vide";
        }
        // 2. Check if the automaton is infinite (there's a cycle leading to a final state).
        if (isAutomatonInfinite(initialState, transitions, finalStates)) {
            return "Infini";
        }
        // 3. If it's not empty or infinite, then it must be finite.
        return "Fini";
    }

    private static boolean isAutomatonEmpty(State initialState, State[] finalStates, Transition[] transitions) {
        Set<State> visited = new HashSet<>();
        Queue<State> queue = new LinkedList<>();
        queue.add(initialState);
        while (!queue.isEmpty()) {
            State currentState = queue.poll();
            visited.add(currentState);
            // If current state is one of the final states, it's not empty.
            if (Arrays.asList(finalStates).contains(currentState)) {
                return false;
            }
            // Explore transitions from the current state.
            for (Transition transition : transitions) {
                if (transition.getSourceState()== currentState && !visited.contains(transition.getTargetState())) {
                    queue.add(transition.getTargetState());
                }
            }
        }
        return true;
    }

    private static boolean isAutomatonInfinite(State initialState, Transition[] transitions, State[] finalStates) {
        Map<State, List<State>> adjacencyList = new HashMap<>();
        for (Transition transition : transitions) {
            adjacencyList.computeIfAbsent(transition.getSourceState(), k -> new ArrayList<>()).add(transition.getTargetState());
        }
        return hasCycleWithFinalState(initialState, adjacencyList, new HashSet<>(), new HashSet<>(), finalStates);
    }

    private static boolean hasCycleWithFinalState(State currentState,
                                                  Map<State, List<State>> adjacencyList,
                                                  Set<State> visited,
                                                  Set<State> recStack,
                                                  State[] finalStates) {
        if (recStack.contains(currentState)) {
            return Arrays.asList(finalStates).contains(currentState);
        }
        if (visited.contains(currentState)) {
            return false;
        }
        visited.add(currentState);
        recStack.add(currentState);
        List<State> neighbors = adjacencyList.get(currentState);
        if (neighbors != null) {
            for (State neighbor : neighbors) {
                if (hasCycleWithFinalState(neighbor, adjacencyList, visited, recStack, finalStates)) {
                    return true;
                }
            }
        }
        recStack.remove(currentState);
        return false;
    }
}
