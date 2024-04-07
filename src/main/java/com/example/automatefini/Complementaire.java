package com.example.automatefini;

import java.io.File;
import java.util.Arrays;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class Complementaire extends Application {

    private Automaton automaton2;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        Button chooseFile1Button = new Button("Choisissez un fichier");
        Button calculatecompButton = new Button("Calculer le complémentaire");
        /*Button showMathButton = new Button("Show Math Details");*/
        Button backButton = new Button("Retour");

        calculatecompButton.setStyle("-fx-background-color: #4C4CFF; -fx-text-fill: white;");
       /* showMathButton.setStyle("-fx-background-color: #4C4CFF; -fx-text-fill: white;");*/
        backButton.setStyle("-fx-background-color: #4C4CFF; -fx-text-fill: white;");
        chooseFile1Button.setStyle("-fx-background-color: #4C4CFF; -fx-text-fill: white;");

        calculatecompButton.setMinWidth(200);
     /*   showMathButton.setMinWidth(200);*/
        backButton.setMinWidth(200);
        chooseFile1Button.setMinWidth(200);

        TextArea outputTextArea = new TextArea();
        outputTextArea.setEditable(false);
        VBox root = new VBox(10,outputTextArea, chooseFile1Button, calculatecompButton,/* showMathButton,*/ backButton );
        root.setPadding(new Insets(10));
        root.setStyle("-fx-background-color: white;");

        chooseFile1Button.setOnAction(e -> {
            File selectedFile1 = chooseFile(primaryStage);
            automaton2 = new Automaton(selectedFile1.getAbsolutePath());
        });

        calculatecompButton.setOnAction(e -> {
            if (automaton2 != null) {
                automaton2 = complementAutomaton(automaton2);
                updateOutputTextArea(outputTextArea, automaton2);
            }
        });

        backButton.setOnAction(e -> {
            HelloApplication helloApplication = new HelloApplication();
            helloApplication.start(new Stage());
            primaryStage.close();
        });

        primaryStage.setScene(new Scene(root, 600, 600));
        primaryStage.setTitle("Calculer le complémentaire");
        primaryStage.show();
    }

    public File chooseFile(Stage stage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choisissez un fichier");
        return fileChooser.showOpenDialog(stage);
    }

    public static Automaton complementAutomaton(Automaton automaton) {
        for (State state : automaton.getStates()) {
            state.setFinal(!state.isFinal());
        }
        return automaton;
    }

    private void updateOutputTextArea(TextArea outputTextArea, Automaton automaton) {
        StringBuilder content = new StringBuilder();
        content.append("Number of States: ").append(automaton.getNumStates()).append("\n");
        content.append("Number of Terminals: ").append(automaton.getNumTerminals()).append("\n");
        content.append("Number of Final States: ").append(automaton.getNumFinalStates()).append("\n");
        content.append("Terminals: ").append(Arrays.toString(automaton.getTerminals())).append("\n");
        content.append("States: ").append(Arrays.toString(automaton.getStates())).append("\n");
        content.append("Transitions: \n");
        for (Transition transition : automaton.getTransitions()) {
            content.append("\t").append(transition.toString()).append("\n");
        }

        content.append("Initial State: ").append("q0").append("\n");
        content.append("Final States: ");
        for (State state : automaton.getStates()) {
            if (state.isFinal()) {
                content.append(state).append(" ");
            }
        }
        content.append("\n");

        outputTextArea.setText(content.toString());
    }
}
