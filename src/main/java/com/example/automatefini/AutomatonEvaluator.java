package com.example.automatefini;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import java.util.Optional;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.TextInputDialog;

public class AutomatonEvaluator {

    public static void evaluateWord(Automaton automaton, String word) {
        // Vérifier si le mot est accepté par l'automate
        boolean isAccepted = automaton.accepts(word);
        String initialWord = word;
        // Afficher les étapes de calcul
        StringBuilder calculationSteps = new StringBuilder();
        int currentState = 0;

        for (char symbol : word.toCharArray()) {
            int terminalIndex = automaton.indexOfTerminal(symbol);
            // Vérifier si le symbole est dans l'alphabet
            if (terminalIndex == -1) {
                showErrorAlert("Erreur", "Symbole invalide", "Le symbole '" + symbol + "' n'est pas dans l'alphabet.");
                return;
            }

            int targetState = automaton.getTransitions()[currentState * automaton.getNumTerminals() + terminalIndex]
                    .getTargetState().getStateNumber();

            calculationSteps.append("On lit '").append(symbol).append("', l'état actuel est q").append(currentState)
                    .append(", le nouvel état est q").append(targetState).append(", et le reste du mot est '")
                    .append(word.substring(1)).append("'\n");

            // Passer à l'état suivant
            currentState = targetState;
            // Supprimer le premier caractère du mot restant
            word = word.substring(1);
        }

        // Afficher le résultat
        if (isAccepted) {
            showSuccessDialog("Succès", "Mot accepté", "Le mot '" + initialWord + "' est accepté par l'automate.",
                    calculationSteps.toString());
        } else {
            showFailureDialog("Échec", "Mot rejeté", "Le mot '" + initialWord + "' est rejeté par l'automate.",
                    calculationSteps.toString());
        }
    }


    // Méthode pour afficher une dialog de succès
    private static void showSuccessDialog(String title, String header, String content, String calculationSteps) {
        showResultDialog(Alert.AlertType.INFORMATION, title, header, content, calculationSteps);
    }

    // Méthode pour afficher une dialog d'échec
    private static void showFailureDialog(String title, String header, String content, String calculationSteps) {
        showResultDialog(Alert.AlertType.ERROR, title, header, content, calculationSteps);
    }

    // Méthode générique pour afficher une dialog de résultat
    private static void showResultDialog(Alert.AlertType type, String title, String header, String content, String calculationSteps) {
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
}
