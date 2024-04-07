package com.example.automatefini;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

        // outputTextArea.setWrapText(true);

        Button typeLangageButton = new Button("Type Langage");
        Button etapeCalculButton = new Button("Étape Calcul");
        Button expressionRegulierButton = new Button("Expression Régulière");
       /* Button operationButton = new Button("Complémentaire");*/


        typeLangageButton.setStyle("-fx-background-color: #4C4CFF; -fx-text-fill: white;");
        etapeCalculButton.setStyle("-fx-background-color: #4C4CFF; -fx-text-fill: white;");
        expressionRegulierButton.setStyle("-fx-background-color: #4C4CFF; -fx-text-fill: white;");
    /*    operationButton.setStyle("-fx-background-color: #4C4CFF; -fx-text-fill: white;");*/
        backButton.setStyle("-fx-background-color: #4C4CFF; -fx-text-fill: white;");




        typeLangageButton.setMinWidth(150);
        etapeCalculButton.setMinWidth(150);
        expressionRegulierButton.setMinWidth(150);
      /*  operationButton.setMinWidth(150);*/
        backButton.setMinWidth(150);
        VBox rightButtons = new VBox(10, typeLangageButton, etapeCalculButton, expressionRegulierButton,
               /* operationButton,*/minButton);

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

                // Now you can use the 'automaton' object as needed in the rest of your code.
            }
        });
     /*   operationButton.setOnAction(e -> {
            Complementaire comp = new Complementaire();
            comp.start(new Stage());
            primaryStage.close();
        });*/

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
                // Find and display the final states
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

        etapeCalculButton.setOnAction(e -> {
            // Récupérer le mot saisi par l'utilisateur
            TextInputDialog inputDialog = new TextInputDialog();
            inputDialog.setHeaderText("Étape Calcul");
            inputDialog.setContentText("Entrez un mot :");
            inputDialog.setTitle("Étape Calcul");
            inputDialog.showAndWait().ifPresent(word -> {
                AutomatonEvaluator.evaluateWord(automaton, word);
            });
        });

        minButton.setOnAction(e -> {
            if (automaton != null) {
                // Perform AFD minimization
                AFDMinimizer minimizer = new AFDMinimizer();
                Automaton minimizedAutomaton = minimizer.minimize(automaton);

                // Display the minimized AFD
                StringBuilder content = new StringBuilder();
                content.append("Minimized AFD Configuration:\n");
                content.append("Number of States: ").append(minimizedAutomaton.getNumStates()).append("\n");
                content.append("Number of Terminals: ").append(minimizedAutomaton.getNumTerminals()).append("\n");
                content.append("Number of Final States: ").append(minimizedAutomaton.getNumFinalStates()).append("\n");
                content.append("Terminals: ").append(Arrays.toString(minimizedAutomaton.getTerminals())).append("\n");
                content.append("States: ").append(Arrays.toString(minimizedAutomaton.getStates())).append("\n");
                content.append("Transitions: \n");
                for (Transition transition : minimizedAutomaton.getTransitions()) {
                    content.append("\t").append(transition.toString()).append("\n");
                }
                content.append("Initial State: ").append("q0").append("\n");
                // Find and display the final states
                content.append("Final States: ");
                for (State state : minimizedAutomaton.getStates()) {
                    if (state.isFinal()) {
                        content.append(state).append(" ");
                    }
                }
                content.append("\n");

                outputTextArea.setText(content.toString());
            } else {
                // If no AFD has been loaded, display an alert
                Alert alert = new Alert(AlertType.WARNING);
                alert.setTitle("Warning");
                alert.setHeaderText(null);
                alert.setContentText("No AFD has been loaded. Please download an AFD first.");
                alert.showAndWait();
            }
        });


        drawButton.setOnAction(e -> {
            drawPane.getChildren().clear();
            if (selectedFile != null) {
                try {
                    List<String> lines = readLinesFromFile(selectedFile);
                    int numStates = lines.get(0).split(", ").length;
                    double centerX = drawPane.getWidth() / 2.0;
                    double centerY = drawPane.getHeight() / 2.0;
                    double radius = Math.min(centerX, centerY) * 0.8;
                    double angleIncrement = 360.0 / numStates;
                    Map<String, Circle> stateCircles = new HashMap<>();
                    double angle = -90; // Commencer à -90 degrés (en haut)
                    for (String state : lines.get(0).split(", ")) {
                        double x = centerX + radius * Math.cos(Math.toRadians(angle));
                        double y = centerY + radius * Math.sin(Math.toRadians(angle));
                        Circle circle = new Circle(x, y, 20, Color.WHITE);
                        circle.setStroke(Color.BLACK);
                        circle.setStrokeWidth(2);
                        Text text = new Text(x - 5, y + 5, state);
                        drawPane.getChildren().addAll(circle, text);
                        stateCircles.put(state, circle);
                        angle += angleIncrement;
                    }
                    for (int i = 2; i < lines.size(); i++) {
                        String[] transition = lines.get(i).split(",");
                        if (transition.length == 3) { // Vérifier s'il y a suffisamment d'éléments dans la transition
                            Circle startCircle = stateCircles.get(transition[0]);
                            Circle endCircle = stateCircles.get(transition[2]);
                            if (startCircle != null && endCircle != null) {
                                Line line = new Line(startCircle.getCenterX(), startCircle.getCenterY(),
                                        endCircle.getCenterX(), endCircle.getCenterY());
                                Text text = new Text((startCircle.getCenterX() + endCircle.getCenterX()) / 2,
                                        (startCircle.getCenterY() + endCircle.getCenterY()) / 2, transition[1]);
                                drawPane.getChildren().addAll(line, text);
                                if (lines.get(lines.size() - 1).contains(transition[2])) { // Vérifier si l'état est
                                    // final
                                    Circle finalCircle = new Circle(endCircle.getCenterX(), endCircle.getCenterY(), 15,
                                            Color.TRANSPARENT);
                                    finalCircle.setStroke(Color.BLACK);
                                    drawPane.getChildren().add(finalCircle);
                                }
                            }
                        } else {
                            System.out.println("La ligne de transition est invalide : " + lines.get(i));
                        }
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
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



        HBox buttonBox = new HBox(10, downloadButton, showMathButton, drawButton);
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

    public static void main(String[] args) {
        launch(args);
    }


}

