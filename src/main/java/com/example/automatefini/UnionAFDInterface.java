package com.example.automatefini;

import com.example.automatefini.*;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class UnionAFDInterface extends Application {

    private Automaton automaton1;
    private Automaton automaton2;
    private Automaton automaton3;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        Button chooseFile1Button = new Button("Choose File 1");
        Button chooseFile2Button = new Button("Choose File 2");
        Button calculateUnionButton = new Button("Calculate Union");
        Button showMathButton = new Button("Show Math Details");
        Button backButton = new Button("Retour");

        chooseFile2Button.setStyle("-fx-background-color: #4C4CFF; -fx-text-fill: white;");
        chooseFile1Button.setStyle("-fx-background-color: #4C4CFF; -fx-text-fill: white;");
        calculateUnionButton.setStyle("-fx-background-color: #4C4CFF; -fx-text-fill: white;");
        showMathButton.setStyle("-fx-background-color: #4C4CFF; -fx-text-fill: white;");
        backButton.setStyle("-fx-background-color: #4C4CFF; -fx-text-fill: white;");




        chooseFile2Button.setMinWidth(150);
        chooseFile1Button.setMinWidth(150);
        calculateUnionButton.setMinWidth(150);
        showMathButton.setMinWidth(150);
        backButton.setMinWidth(150);

        TextArea outputTextArea = new TextArea();
        outputTextArea.setEditable(false);

        VBox root = new VBox(10, outputTextArea, chooseFile1Button, chooseFile2Button, calculateUnionButton, showMathButton, backButton);
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

        calculateUnionButton.setOnAction(e -> {
            try {
                automaton3 = union(automaton1, automaton2);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });

        backButton.setOnAction(e -> {
            // Redirect to the first interface
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
        primaryStage.setTitle("Calculate Union");
        primaryStage.show();
    }

    public File chooseFile(Stage stage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose File");
        return fileChooser.showOpenDialog(stage);
    }

    public Automaton union(Automaton automaton1, Automaton automaton2) throws IOException {
        automaton1 = Complementaire.complementAutomaton(automaton1);
        automaton2 = Complementaire.complementAutomaton(automaton2);

        // Create a new instance of Automaton
        automaton3 = new Automaton();

        // Calculate the intersection of automaton1 and automaton2
        automaton3 = automaton3.intersection(automaton1, automaton2);

        // Calculate the complement of the intersection
        automaton3 = Complementaire.complementAutomaton(automaton3);

        return automaton3;
    }

}
