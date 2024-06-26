package com.example.automatefini;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.util.ArrayList;
import java.util.List;
import static com.example.automatefini.AutomatonEvaluator.showErrorAlert;

public class AffichageMathématiqueParInteraction extends Application {
    private Automaton automaton = new Automaton();

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("AFD Application");
        Label titleLabel = new Label("Saisir les détails de l'AFD");
        TextArea numStatesTextArea = createTextArea("Nombre d'états...");
        TextArea alphabetTextArea = createTextArea("Alphabet... (ex: a, b, c)");
        TextArea transitionFunctionTextArea = createTextArea("Fonction de transition... (ex: q0,a,q1; q1,b,q2)");
        TextArea initialStateTextArea = createTextArea("État initial... (ex: q0)");
        TextArea finalStatesTextArea = createTextArea("États finaux... (ex: q1, q2)");

        Button regexButton = new Button("Expression Régulière");
        Button langTypeButton = new Button("Type de Langage");
        Button loadFileButton = new Button("Charger à travers un fichier");
        Button submitButton = new Button("Afficher la forme mathématique de l'AFD");
        Button etapeDeCalcul = new Button("étape de calcul");
        Button retour = new Button("Retour");

        submitButton.setStyle("-fx-background-color: #4C4CFF; -fx-text-fill: white;");
        etapeDeCalcul.setStyle("-fx-background-color: #4C4CFF; -fx-text-fill: white;");
        retour.setStyle("-fx-background-color: #4C4CFF; -fx-text-fill: white;");
        regexButton.setStyle("-fx-background-color: #4C4CFF; -fx-text-fill: white;");
        langTypeButton.setStyle("-fx-background-color: #4C4CFF; -fx-text-fill: white;");
        loadFileButton.setStyle("-fx-background-color: #4C4CFF; -fx-text-fill: white;");

        submitButton.setMinWidth(200);
        etapeDeCalcul.setMinWidth(200);
        retour.setMinWidth(200);
        regexButton.setMinWidth(200);
        langTypeButton.setMinWidth(200);
        loadFileButton.setMinWidth(200);

        retour.setOnAction(e -> {
            HelloApplication helloApp = new HelloApplication();
            helloApp.start(new Stage());
            primaryStage.close();
        });

        loadFileButton.setOnAction(e -> {
            AffichageFromFile affichageFromFile = new AffichageFromFile();
            affichageFromFile.start(new Stage());
            primaryStage.close();
        });

        langTypeButton.setOnAction(e -> {
            String numStates = numStatesTextArea.getText();
            String alphabet = alphabetTextArea.getText();
            String transitionFunction = transitionFunctionTextArea.getText();
            String initialState = initialStateTextArea.getText();
            String finalStates = finalStatesTextArea.getText();
            if (numStates.isEmpty() || alphabet.isEmpty() || transitionFunction.isEmpty() || initialState.isEmpty() || finalStates.isEmpty()) {
                showErrorAlert("Erreur", "Champs manquants", "Veuillez remplir tous les champs.");
                return;
            }
            char[] alphabetArray = alphabet.replaceAll("\\s+", "").toCharArray();
            String[] finalStatesArray = finalStates.split("\\s*,\\s*");
            int numberOfStates = Integer.parseInt(numStates);

            automaton = new Automaton();
            automaton.setNumStates(numberOfStates);
            automaton.setTerminals(alphabetArray);

            State[] states = new State[numberOfStates];
            for (int i = 0; i < numberOfStates; i++) {
                states[i] = new State(i);
                automaton.addState(states[i]);
            }

            State initial = automaton.getStateByNumber(Integer.parseInt(initialState.substring(1)));
            automaton.setInState(initial);

            List<State> finalStateList = new ArrayList<>();
            for (String finalStateStr : finalStatesArray) {
                State finalState = automaton.getStateByNumber(Integer.parseInt(finalStateStr.substring(1)));
                automaton.addFinalState(finalState);
                finalStateList.add(finalState);
            }

            automaton.setFiStates(finalStateList.toArray(new State[0]));
            String[] transitionArray = transitionFunction.split(";");
            for (String transitionStr : transitionArray) {
                String[] parts = transitionStr.trim().split(",");
                if (parts.length != 3) {
                    showErrorAlert("Erreur", "Fonction de transition invalide", "Veuillez entrer la fonction de transition sous forme 'état source, symbole, état cible'.");
                    return;
                }

                State source = automaton.getStateByNumber(Integer.parseInt(parts[0].trim().substring(1)));
                State target = automaton.getStateByNumber(Integer.parseInt(parts[2].trim().substring(1)));
                char symbol = parts[1].trim().charAt(0);

                automaton.addTransition(new Transition(source, symbol, target));
            }
            String automatonType = AutomatonEvaluator.checkAutomatonType(automaton);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Type de l'automate");
            alert.setHeaderText(null);
            alert.setContentText("L'automate est : " + automatonType);
            alert.showAndWait();
        });

        submitButton.setOnAction(e -> {
            String numStates = numStatesTextArea.getText();
            String alphabet = alphabetTextArea.getText();
            String transitionFunction = transitionFunctionTextArea.getText();
            String initialState = initialStateTextArea.getText();
            String finalStates = finalStatesTextArea.getText();
            if (numStates.isEmpty() || alphabet.isEmpty() || transitionFunction.isEmpty() || initialState.isEmpty()
                    || finalStates.isEmpty()) {
                showErrorAlert("Erreur", "Champs manquants", "Veuillez remplir tous les champs.");
                return;
            }
            // Diviser la chaîne d'états finaux en un tableau d'états finaux
            String[] finalStatesArray = finalStates.split("\\s*,\\s*");
            // Créer un tableau de caractères à partir de l'alphabet
            char[] alphabetArray = alphabet.replaceAll("\\s+", "").toCharArray();
            // Créer un objet Automaton en utilisant les valeurs saisies
            automaton.setNumStates(Integer.parseInt(numStates));
            automaton.setNumTerminals(alphabetArray.length);
            automaton.setNumFinalStates(finalStatesArray.length);
            automaton.setTerminals(alphabetArray);

            String[] transitions = transitionFunction.split(";");
            for (String transitionStr : transitions) {
                String[] parts = transitionStr.trim().split(",");
                if (parts.length != 3) {
                    showErrorAlert("Erreur", "Fonction de transition invalide",
                            "Veuillez entrer la fonction de transition sous forme 'état source, symbole, état cible'.");
                    return;
                }
                State sourceState = new State();
                sourceState.setStateNumber(Integer.parseInt(parts[0].trim().substring(1)));
                State targetState = new State();
                targetState.setStateNumber(Integer.parseInt(parts[2].trim().substring(1)));
                Transition transition = new Transition(sourceState, parts[1].trim().charAt(0), targetState);
                automaton.addTransition(transition);
            }
            // Définir l'état initial
            State initial = new State();
            initial.setStateNumber(Integer.parseInt(initialState.substring(1)));
            automaton.setInState(initial);
            // Définir les états finaux
            for (String finalStateStr : finalStatesArray) {
                State finalState = new State();
                finalState.setStateNumber(Integer.parseInt(finalStateStr.substring(1)));
                finalState.setFinal(true);
                automaton.addFinalState(finalState);
            }
            for (int i = 0; i < automaton.getNumStates(); i++) {
                State state = new State();
                state.setStateNumber(i);
                for (int j = 0; j < automaton.getFiStates().length; j++) {
                    if (state.getStateNumber() == automaton.getFiStates()[j].getStateNumber()) {
                        state.setFinal(true);
                    }
                }
                automaton.addState(state);
            }
            System.out.println(automaton.getFinalStates()[0]);
            System.out.println(automaton.getFinalStates()[1]);
            // Afficher la forme mathématique de l'AFD
            String mathematicalForm = automaton.toMathematicalForm();
            // Afficher la forme mathématique dans une boîte de dialogue
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Forme mathématique de l'AFD");
            alert.setHeaderText(null);
            alert.setContentText(mathematicalForm);
            alert.showAndWait();
        });

        etapeDeCalcul.setOnAction(e -> {
            TextInputDialog inputDialog = new TextInputDialog();
            inputDialog.setHeaderText("Étape Calcul");
            inputDialog.setContentText("Entrez un mot :");
            inputDialog.setTitle("Étape Calcul");
            inputDialog.showAndWait().ifPresent(word -> {
                // Évaluer le mot avec l'automate et afficher les étapes de calcul
                System.out.println("word00:" + word);
                System.out.println("auuut" + automaton.getTerminals()[0]);
                AutomatonEvaluator.evaluateWord(automaton, word);
            });
        });

        VBox additionalButtons = new VBox(5, langTypeButton, loadFileButton, etapeDeCalcul, retour);
        additionalButtons.setAlignment(Pos.TOP_RIGHT);

        VBox vbox = new VBox(10, titleLabel, numStatesTextArea, alphabetTextArea, transitionFunctionTextArea,
                initialStateTextArea, finalStatesTextArea, submitButton);
        vbox.setPadding(new Insets(10));

        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(vbox);
        borderPane.setRight(additionalButtons);
        borderPane.setStyle("-fx-background-color: white;");
        Scene scene = new Scene(borderPane, 600, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private TextArea createTextArea(String promptText) {
        TextArea textArea = new TextArea();
        textArea.setPromptText(promptText);
        textArea.setPrefRowCount(1); // Réduire le nombre de lignes affichées
        textArea.setMaxHeight(50); // Limiter la hauteur maximale
        return textArea;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
