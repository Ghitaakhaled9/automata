package com.example.automatefini;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.ScrollEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.CubicCurveTo;
import javafx.scene.shape.Line;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.stage.Stage;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javafx.scene.Group;






import com.example.automatefini.AutomatonVisualizer.TransitionVisualizer;

import javafx.geometry.Point2D;

public class VisualAFDGraph extends Application {
    
    @Override
    public void start(Stage primaryStage) {
        String filePath1 = "C:/Users/oki/Desktop/AI/m1.txt";
        Automaton automaton_test = new Automaton(filePath1);
        AutomatonVisualizer automaton = automaton_test.convertFromAutomatonToVisualAFD(automaton_test);

        // Example Automaton
        int numStates = automaton.getNumStates();
        int numTerminals = automaton.getNumTerminals();
        int numFinalStates = automaton.getNumFinalStates();
        char[] terminals = automaton.getTerminals();
        AutomatonVisualizer.StateVisualizer[] states = automaton.getStates();
        AutomatonVisualizer.TransitionVisualizer[] transitions = automaton.getTransitions();
        AutomatonVisualizer.StateVisualizer inState = automaton.getInState();
        AutomatonVisualizer.StateVisualizer[] fiStates = automaton.getFiStates();

         // Create AFD Graph
         Map<Integer, Map<Character, TransitionVisualizer>> graph = new HashMap<>();
         for (TransitionVisualizer transition : automaton.getTransitions()) {
             if (!graph.containsKey(transition.fromState)) {
                 graph.put(transition.fromState, new HashMap<>());
             }
             graph.get(transition.fromState).put(transition.symbol, transition);
         }
 
         Group root = new Group();
 
         // Calculate center of the scene
         double centerX = 300;
         double centerY = 300;
 
         // Calculate circle positions
         double radius = 150;
         double angleIncrement = 2 * Math.PI / numStates;
         Map<Integer, Circle> stateCircles = new HashMap<>();
         for (int stateId = 0; stateId < numStates; stateId++) {
             double angle = angleIncrement * stateId;
             double x = centerX + radius * Math.cos(angle);
             double y = centerY + radius * Math.sin(angle);
 
             Circle circle = new Circle(x, y, 30, Color.TRANSPARENT);
             circle.setStroke(Color.BLACK);
             if (automaton.getStates()[stateId].isFinal) {
                 Circle innerCircle = new Circle(x, y, 25);
                 innerCircle.setFill(Color.TRANSPARENT);
                 innerCircle.setStroke(Color.BLACK);
                 innerCircle.setStrokeWidth(2);
                 root.getChildren().add(innerCircle);
             }
             root.getChildren().add(circle);
 
             // Add state label
             Label label = new Label("q" + stateId);
             label.setLayoutX(x - 10);
             label.setLayoutY(y - 10);
             label.setTextFill(Color.BLACK);
             root.getChildren().add(label);
 
             stateCircles.put(stateId, circle);
         }
 
         // Create a map to store used start and end points for each state
         Map<Integer, Set<Point2D>> usedPoints = new HashMap<>();
      // Keep track of states with self-loop transitions
         Map<Integer, Integer> selfLoopCount = new HashMap<>();
 
         // Add transitions as lines/arrows between circles
         for (Map.Entry<Integer, Map<Character, TransitionVisualizer>> entry : graph.entrySet()) {
             int fromState = entry.getKey();
             double fromX = stateCircles.get(fromState).getCenterX();
             double fromY = stateCircles.get(fromState).getCenterY();
 
             Map<Character, TransitionVisualizer> transitionsMap = entry.getValue();
             double angleOffset = 2 * Math.PI / transitionsMap.size(); // Offset for angle between arrows
             double currentAngle = 0; // Start angle
 
             int transitionCount = transitionsMap.size(); // Number of transitions from this state
             int transitionIndex = 0; // Index of current transition
 
             // Store visited to-states to avoid overlapping lines
             Set<Integer> visitedToStates = new HashSet<>();
 
             for (Map.Entry<Character, TransitionVisualizer> transitionEntry : transitionsMap.entrySet()) {
                TransitionVisualizer transition = transitionEntry.getValue();
                 int toState = transition.toState;
 
                 // Check if the transition source state is the same as the target state
                 if (fromState == toState) {
                     // Increment self-loop count for the state
                     selfLoopCount.put(fromState, selfLoopCount.getOrDefault(fromState, 0) + 1);
 
                     // Only draw the self-loop if it's the first one
                     if (selfLoopCount.get(fromState) == 1) {
                         // Draw loop transition (self-loop)
                         double loopRadius = 90;
                         double loopStartAngle = Math.PI / 4;
                         double loopEndAngle = 7 * Math.PI / 4;
                         double loopCenterX = fromX + loopRadius * Math.cos(loopStartAngle);
                         double loopCenterY = fromY + loopRadius * Math.sin(loopStartAngle);
 
                         // Calculate the starting point on the circle's circumference
                         double loopStartX = fromX + 30 * Math.cos(loopStartAngle);
                         double loopStartY = fromY + 30 * Math.sin(loopStartAngle);
 
                         // Calculate the ending point on the circle's circumference
                         double loopEndX = fromX + 30 * Math.cos(loopEndAngle);
                         double loopEndY = fromY + 30 * Math.sin(loopEndAngle);
 
                         // Calculate the control points for the arc
                         double control1X = loopCenterX + 40 * Math.cos(Math.PI / 2 + loopStartAngle);
                         double control1Y = loopCenterY + 40 * Math.sin(Math.PI / 2 + loopStartAngle);
                         double control2X = loopCenterX + 40 * Math.cos(Math.PI / 2 + loopEndAngle);
                         double control2Y = loopCenterY + 40 * Math.sin(Math.PI / 2 + loopEndAngle);
 
                         // Draw the arc
                         Path loopArc = new Path();
                         loopArc.getElements().add(new MoveTo(loopStartX, loopStartY));
                         loopArc.getElements().add(new CubicCurveTo(control1X, control1Y, control2X, control2Y, loopEndX, loopEndY));
                         loopArc.setStroke(transition.color);
                         loopArc.setFill(Color.TRANSPARENT);
                         root.getChildren().add(loopArc);
 
                         // Add transition label
                         char symbol = transition.symbol;
                         Color color = transition.color;
                         Label label = new Label(String.valueOf(symbol));
 
                         // Position the label at the bottom of the curve
                         double labelX = (loopStartX + loopEndX) / 2;
                         double labelY = (loopStartY + loopEndY) / 2;
                         if (control1Y > loopCenterY && control2Y > loopCenterY) {
                             labelY += 50; // Adjust label position to appear below the curve
                         }
                         label.setLayoutX(labelX);
                         label.setLayoutY(labelY);
                         label.setTextFill(color);
                         root.getChildren().add(label);
                     }
 
                     // Add the terminal to the label for subsequent self-loops
                     if (selfLoopCount.get(fromState) > 1) {
                         Label existingLabel = (Label) root.getChildren().stream()
                                 .filter(node -> node instanceof Label && ((Label) node).getText().equals(String.valueOf(transition.symbol)))
                                 .findFirst()
                                 .orElse(null);
                         if (existingLabel != null) {
                             existingLabel.setText(existingLabel.getText() + ", " + String.valueOf(transition.symbol));
                         }
                     }
                 
 
                 } else {
                     // Draw regular arrow transition
                     double toX = stateCircles.get(toState).getCenterX();
                     double toY = stateCircles.get(toState).getCenterY();
 
                     char symbol = transition.symbol;
                     Color color = transition.color;
 
                  // Calculate the angle of the line
                     double angle = Math.atan2(toY - fromY, toX - fromX);
 
                     // Calculate arrow points on the circle's edge
                     double arrowStartX = fromX + 30 * Math.cos(angle);
                     double arrowStartY = fromY + 30 * Math.sin(angle);
                     double arrowEndX = toX - 30 * Math.cos(angle);
                     double arrowEndY = toY - 30 * Math.sin(angle);
 
                     // If multiple transitions, adjust positions
                     if (transitionCount > 1) {
                         double angleAdjustment = Math.toRadians(10) * (transitionIndex - (transitionCount - 1) / 2.0);
                         arrowStartX += Math.cos(angle + angleAdjustment) * 10;
                         arrowStartY += Math.sin(angle + angleAdjustment) * 10;
                         arrowEndX += Math.cos(angle + angleAdjustment) * 10;
                         arrowEndY += Math.sin(angle + angleAdjustment) * 10;
                     }
 
                     // Check if the calculated points have already been used
                     Point2D adjustedStartPoint = new Point2D(arrowStartX, arrowStartY);
                     Point2D adjustedEndPoint = new Point2D(arrowEndX, arrowEndY);
                     if (usedPoints.containsKey(fromState)) {
                         while (usedPoints.get(fromState).contains(adjustedStartPoint)) {
                             arrowStartX -= 15 * Math.cos(angle + Math.PI / 2);
                             arrowStartY -= 15 * Math.sin(angle + Math.PI / 2);
                             adjustedStartPoint = new Point2D(arrowStartX, arrowStartY);
                         }
                     }
                     if (usedPoints.containsKey(toState)) {
                         while (usedPoints.get(toState).contains(adjustedEndPoint)) {
                             arrowEndX -= 15 * Math.cos(angle + Math.PI / 2);
                             arrowEndY -= 15 * Math.sin(angle + Math.PI / 2);
                             adjustedEndPoint = new Point2D(arrowEndX, arrowEndY);
                         }
                     }
 
                     Line line = new Line(arrowStartX, arrowStartY, arrowEndX, arrowEndY);
                     line.setStroke(color);
                     root.getChildren().add(line);
 
                     // Add arrowhead
                     double arrowLength = 10;
                     double arrowAngle = Math.atan2(arrowEndY - arrowStartY, arrowEndX - arrowStartX);
                     double x1 = arrowEndX - arrowLength * Math.cos(arrowAngle - Math.PI / 6);
                     double y1 = arrowEndY - arrowLength * Math.sin(arrowAngle - Math.PI / 6);
                     double x2 = arrowEndX - arrowLength * Math.cos(arrowAngle + Math.PI / 6);
                     double y2 = arrowEndY - arrowLength * Math.sin(arrowAngle + Math.PI / 6);
                     Line arrow1 = new Line(arrowEndX, arrowEndY, x1, y1);
                     arrow1.setStroke(color);
                     Line arrow2 = new Line(arrowEndX, arrowEndY, x2, y2);
                     arrow2.setStroke(color);
                     root.getChildren().addAll(arrow1, arrow2);
 
                     // Add transition label
                     Label label = new Label(String.valueOf(symbol));
                     label.setLayoutX((arrowStartX + arrowEndX) / 2);
                     label.setLayoutY((arrowStartY + arrowEndY) / 2);
                     label.setTextFill(color);
                     root.getChildren().add(label);
 
                     // Increment angle for the next arrow
                     currentAngle += angleOffset;
                     transitionIndex++;
 
                     // Mark the to-state as visited
                     visitedToStates.add(toState);
 
                     // Add used points to the map
                     if (!usedPoints.containsKey(fromState)) {
                         usedPoints.put(fromState, new HashSet<>());
                     }
                     if (!usedPoints.containsKey(toState)) {
                         usedPoints.put(toState, new HashSet<>());
                     }
                     usedPoints.get(fromState).add(new Point2D(arrowStartX, arrowStartY));
                     usedPoints.get(toState).add(new Point2D(arrowEndX, arrowEndY));
 
                 }
 
                 // Increment angle for the next arrow
                 currentAngle += angleOffset;
                 transitionIndex++;
 
                 // Mark the to-state as visited
                 visitedToStates.add(toState);
             }
         }
 
         // Create ScrollPane and add root to it
         ScrollPane scrollPane = new ScrollPane();
         scrollPane.setContent(root);
         scrollPane.setFitToWidth(true);
         scrollPane.setFitToHeight(true);
 
         // Add zoom functionality
         final double SCALE_DELTA = 1.1;
         scrollPane.setOnScroll((ScrollEvent event) -> {
             event.consume();
             if (event.getDeltaY() == 0) {
                 return;
             }
             double scaleFactor = (event.getDeltaY() > 0) ? SCALE_DELTA : 1 / SCALE_DELTA;
             root.setScaleX(root.getScaleX() * scaleFactor);
             root.setScaleY(root.getScaleY() * scaleFactor);
         });
 
         Scene scene = new Scene(scrollPane, 600, 600);
         primaryStage.setTitle("AFD Graph Visualization");
         primaryStage.setScene(scene);
         primaryStage.show();
     }
 
    

    public static void main(String[] args) {
        launch(args);
    }

}
