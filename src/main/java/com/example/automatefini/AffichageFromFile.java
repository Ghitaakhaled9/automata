package com.example.automatefini;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import static com.example.automatefini.Automaton.determineLanguageType;

public class AffichageFromFile extends Application{
    private File selectedFile;
    private TextArea outputTextArea;
    private Pane drawPane;
    private Automaton automaton= new Automaton();

    @Override
    public void start(Stage primaryStage) {
        Button downloadButton = new Button("Télécharger l'AFD");
        Button showMathButton = new Button("Afficher la forme mathématique");
        Button drawButton = new Button("Dessiner l'AFD");
        Button minButton = new Button("minimiser l'AFD");
        Button backButton = new Button("Retour");
        downloadButton.setStyle("-fx-background-color: #4C4CFF; -fx-text-fill: white;");
        minButton.setStyle("-fx-background-color: #4C4CFF; -fx-text-fill: white;");
        drawButton.setStyle("-fx-background-color: #4C4CFF; -fx-text-fill: white;");
        showMathButton.setStyle("-fx-background-color: #4C4CFF; -fx-text-fill: white;");
        downloadButton.setMinWidth(150);
        minButton.setMinWidth(150);
        drawButton.setMinWidth(150);
        showMathButton.setMinWidth(150);

        outputTextArea = new TextArea();
        outputTextArea.setEditable(false);
        outputTextArea.setPrefRowCount(30);
        outputTextArea.setPrefWidth(1000);
        outputTextArea.setPrefHeight(1000);

        Button typeLangageButton = new Button("Type Langage");
        Button etapeCalculButton = new Button("Étape Calcul");
        Button expressionRegulierButton = new Button("Expression Régulière");

        typeLangageButton.setStyle("-fx-background-color: #4C4CFF; -fx-text-fill: white;");
        etapeCalculButton.setStyle("-fx-background-color: #4C4CFF; -fx-text-fill: white;");
        expressionRegulierButton.setStyle("-fx-background-color: #4C4CFF; -fx-text-fill: white;");
        backButton.setStyle("-fx-background-color: #4C4CFF; -fx-text-fill: white;");

        typeLangageButton.setMinWidth(150);
        etapeCalculButton.setMinWidth(150);
        expressionRegulierButton.setMinWidth(150);
        backButton.setMinWidth(150);
        VBox rightButtons = new VBox(10, typeLangageButton);

        drawPane = new Pane();

        downloadButton.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Télécharger le fichier AFD");
            selectedFile = fileChooser.showOpenDialog(primaryStage);
            if (selectedFile != null) {
                try {
                    automaton = automaton.readAutomatonFromFile(selectedFile);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });

        showMathButton.setOnAction(e -> {
            if (selectedFile != null) {
                Automaton automaton = new Automaton(selectedFile.getAbsolutePath());
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
        });

        backButton.setOnAction(e -> {
            AffichageMathématiqueParInteraction firstInterface = new AffichageMathématiqueParInteraction();
            firstInterface.start(new Stage());
            primaryStage.close();
        });

        typeLangageButton.setOnAction(e -> {
            if (automaton != null) {
                Automaton.LanguageType languageType = determineLanguageType(automaton);
                String message;
                switch (languageType) {
                    case EMPTY:
                        message = "Le langage est vide.";
                        break;
                    case INFINITE:
                        message = "Le langage est infini.";
                        break;
                    case FINITE:
                        message = "Le langage est fini.";
                        break;
                    default:
                        message = "Le type de langage n'a pas pu être déterminé.";
                }
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Type de Langage");
                alert.setHeaderText(null);
                alert.setContentText(message);
                alert.showAndWait();
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Avertissement");
                alert.setHeaderText(null);
                alert.setContentText("Veuillez charger un automate avant de déterminer le type de langage.");
                alert.showAndWait();
            }
        });

        HBox buttonBox = new HBox(10, downloadButton, showMathButton);
        buttonBox.setPadding(new Insets(10));

        HBox backButtonBox = new HBox(backButton);
        backButtonBox.setPadding(new Insets(10));

        BorderPane root = new BorderPane(new HBox(outputTextArea, drawPane), null, backButtonBox, buttonBox,
                rightButtons);
        root.setStyle("-fx-background-color: white;");
        Scene scene = new Scene(root, 600, 600);

        scene.widthProperty().addListener((observable, oldValue, newValue) -> {
            drawPane.setPrefWidth(newValue.doubleValue());
            drawPane.setMaxWidth(newValue.doubleValue());
        });

        scene.heightProperty().addListener((observable, oldValue, newValue) -> {
            drawPane.setPrefHeight(newValue.doubleValue());
            drawPane.setMaxHeight(newValue.doubleValue());
        });
        primaryStage.setTitle("AFD JavaFX App");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private List<String> readLinesFromFile(File file) throws IOException {
        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        }
        return lines;
    }

}

