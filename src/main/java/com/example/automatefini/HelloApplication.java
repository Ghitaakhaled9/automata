package com.example.automatefini;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class HelloApplication extends Application {

    @Override
    public void start(Stage primaryStage) {
        Label welcomeLabel = new Label("Bienvenue dans le monde des automates");
        welcomeLabel.setFont(Font.font(30));

        Label titleLabel = new Label("Menu");
        titleLabel.setFont(Font.font(24));

        Button btn1 = createStyledButton("Affichage");
        Button btn2 = createStyledButton("L'union de deux automates");
        Button btn3 = createStyledButton("L'intersection de deux automates");
        Button btn4 = createStyledButton("La différence entre deux automates");
        Button btn5 = createStyledButton("Le complémentaire d'un automate");

        VBox root = new VBox(20);
        root.setPadding(new Insets(10));
        root.setAlignment(Pos.CENTER);
        root.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
        root.getChildren().addAll(welcomeLabel, titleLabel, btn1, btn2, btn3, btn4, btn5);

        btn1.setOnAction(this::handleButtonAction);
        btn2.setOnAction(this::handleButtonAction);
        btn3.setOnAction(this::handleButtonAction);
        btn4.setOnAction(this::handleButtonAction);
        btn5.setOnAction(this::handleButtonAction);

        Scene scene = new Scene(root, 600, 600);
        scene.setFill(Color.WHITE);

        primaryStage.setTitle("Automate Options");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private Button createStyledButton(String text) {
        Button button = new Button(text);
        button.setStyle("-fx-background-color: #4C4CFF; -fx-text-fill: white;");
        button.setMinWidth(300);
        button.setFont(Font.font(14));
        return button;
    }

    private void handleButtonAction(ActionEvent event) {
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        currentStage.close();
        Button btn = (Button) event.getSource();
        String option = btn.getText();
        switch (option) {
            case "Affichage":
                AffichageMathématiqueParInteraction affichageMathématiqueParInteraction = new AffichageMathématiqueParInteraction();
                Stage affichageMathStage = new Stage();
                affichageMathématiqueParInteraction.start(affichageMathStage);
                break;
            case "L'union de deux automates":
                UnionAFDInterface unionAFDInterface = new UnionAFDInterface();
                Stage union = new Stage();
                unionAFDInterface.start(union);
                break;
            case "L'intersection de deux automates":
                intersectionAFD intersectionAFDInterface = new intersectionAFD();
                Stage intersection = new Stage();
                intersectionAFDInterface.start(intersection);
                break;
            case "La différence entre deux automates":
                Difference differenceAFDInterface = new Difference();
                Stage difference = new Stage();
                differenceAFDInterface.start(difference);
                break;
            case "Le complémentaire d'un automate":
                Complementaire complementaireInterface = new Complementaire();
                Stage complementaireStage = new Stage();
                complementaireInterface.start(complementaireStage);
                break;
            default:
                break;
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
