package com.example.automatefini;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class Difference extends Application {
    private Complementaire comp = new Complementaire();
    private  Automaton automaton1;
    private Automaton automaton2;
    private Automaton automaton3;

    @Override
    public void start(Stage primaryStage) {
        Button chooseFile1Button = new Button("Choisir fichier 1");
        Button chooseFile2Button = new Button("Choisir fichier 2");
        Button calculateDifferenceButton = new Button("Calculer la difference");
        Button showMathButton = new Button("Forme mathématique ");
        Button backButton = new Button("Retour");

        chooseFile1Button.setStyle("-fx-background-color: #4C4CFF; -fx-text-fill: white;");
        chooseFile2Button.setStyle("-fx-background-color: #4C4CFF; -fx-text-fill: white;");
        calculateDifferenceButton.setStyle("-fx-background-color: #4C4CFF; -fx-text-fill: white;");
        showMathButton.setStyle("-fx-background-color: #4C4CFF; -fx-text-fill: white;");
        backButton.setStyle("-fx-background-color: #4C4CFF; -fx-text-fill: white;");

        chooseFile1Button.setMinWidth(150);
        chooseFile2Button.setMinWidth(150);
        calculateDifferenceButton.setMinWidth(150);
        showMathButton.setMinWidth(150);
        backButton.setMinWidth(150);

        TextArea outputTextArea = new TextArea();
        outputTextArea.setEditable(false);

        VBox root = new VBox(10,outputTextArea, chooseFile1Button, chooseFile2Button, calculateDifferenceButton, showMathButton, backButton );
        root.setPadding(new Insets(10));
        root.setStyle("-fx-background-color: white;");

        chooseFile1Button.setOnAction(e -> {
            File selectedFile1 = chooseFile(primaryStage);
            automaton1 = new Automaton(selectedFile1.getAbsolutePath());
        });

        chooseFile2Button.setOnAction(e -> {
            File selectedFile2 = chooseFile(primaryStage);
            automaton2 = new Automaton(selectedFile2.getAbsolutePath());
        });

        calculateDifferenceButton.setOnAction(e -> {

            try {
                automaton3 = difference(automaton1, automaton2);
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        });
        backButton.setOnAction(e -> {
            HelloApplication helloApplication = new HelloApplication();
            helloApplication.start(new Stage());
            primaryStage.close();
        });
        showMathButton.setOnAction(e -> {
            if (automaton3 != null) {
                StringBuilder content = new StringBuilder();
                content.append("Number of States: ").append(automaton3.getNumStates()).append("\n");
                content.append("Number of Terminals: ").append(automaton3.getNumTerminals()).append("\n");
                content.append("Number of Final States: ").append(automaton3.getNumFinalStates()).append("\n");
                content.append("Terminals: ").append(Arrays.toString(automaton3.getTerminals())).append("\n");
                content.append("States: ").append(Arrays.toString(automaton3.getStates())).append("\n");
                content.append("Transitions: \n");
                for (Transition transition : automaton3.getTransitions()) {
                    content.append("\t").append(transition.toString()).append("\n");
                }
                content.append("Initial State: ").append("q0").append("\n");
                // Find and display the final states
                content.append("Final States: ");
                for (State state : automaton3.getStates()) {
                    if (state.isFinal()) {
                        content.append(state).append(" ");
                    }
                }
                content.append("\n");
                outputTextArea.setText(content.toString());
            }
        });

        primaryStage.setScene(new Scene(root, 600, 600));
        primaryStage.setTitle("Calculate Difference");
        primaryStage.show();
    }

    public File chooseFile(Stage stage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose File");
        return fileChooser.showOpenDialog(stage);
    }

    public Automaton difference(Automaton automaton1, Automaton automaton2) throws IOException {
        Automaton complementAutomaton2= new Automaton();
        complementAutomaton2= Complementaire.complementAutomaton(automaton2);
        Automaton intersectionAutomaton = new Automaton();
        intersectionAutomaton = automaton1.intersection(automaton1, complementAutomaton2);
        return intersectionAutomaton;
    }
}
