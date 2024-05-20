package com.example.automatefini;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class intersectionAFD extends Application {
    private  transition_table tr_table;
    private Automaton automata1;
    private Automaton automata2;
    private File selectedFile1;
    private File selectedFile2;
    private TextArea outputTextArea;

    public intersectionAFD() {
        super();
        this.tr_table = new transition_table();
    }
    public intersectionAFD(transition_table trasition_table, Automaton automata1, Automaton automata2) {
        super();
        this.tr_table = trasition_table;
        this.automata1 = automata1;
        this.automata2 = automata2;
    }
    public transition_table getTrasition_table() {
        return tr_table;
    }
    public void setTrasition_table(transition_table trasition_table) {
        this.tr_table = trasition_table;
    }
    public Automaton getAutomata1() {
        return automata1;
    }
    public void setAutomata1(Automaton automata1) {
        this.automata1 = automata1;
    }
    public Automaton getAutomata2() {
        return automata2;
    }
    public void setAutomata2(Automaton automata2) {
        this.automata2 = automata2;
    }
    public Automaton intersection(Automaton automaton1, Automaton automaton2) {
        Automaton resultAutomaton = new Automaton();
        for (State stateAfd1 : automaton1.getStates()) {
            for (State stateAfd2 : automaton2.getStates()) {
                transition_row row = new transition_row();
                transition_cell cell = new transition_cell();
                cell.setAfd1State(stateAfd1);
                cell.setAfd2State(stateAfd2);
                row.addcell(cell);
                tr_table.addrow(row);
            }
        }
        for (transition_row row : tr_table.getTrasition_table()) {
            for (transition_cell cell : row.getTransition_row()) {
                System.out.println("Afd1 State: " + cell.getAfd1State().getStateNumber() + ", Afd2 State: " + cell.getAfd2State().getStateNumber());
            }
        }
        char terminals1[]= automaton1.getTerminals();
        char terminals2[]= automaton2.getTerminals();
        char terminalsInter[]= interTerminal(terminals1,terminals2);
        for( int i=0; i<terminalsInter.length;i++) {
            System.out.println("terminal["+i+"]"+"="+terminalsInter[i]);
        }
        for (int i = 0; i < automaton1.getStates().length; i++) {
            for (int j = 0; j < automaton2.getStates().length; j++) {
                transition_row currentRow = tr_table.getTrasition_table().get(i * automaton2.getStates().length + j);
                for (char terminal : terminalsInter) {
                    State state1 = automaton1.getTargetStateForSymbol(automaton1.getStates()[i], terminal);
                    State state2 = automaton2.getTargetStateForSymbol(automaton2.getStates()[j], terminal);
                    transition_cell cell = new transition_cell();
                    cell.setAfd1State(state1);
                    cell.setAfd2State(state2);
                    currentRow.addcell(cell);
                }
            }
        }
        for (int i = 0; i < tr_table.getTrasition_table().size(); i++) {
            transition_row row = tr_table.getTrasition_table().get(i);
            System.out.print("Row " + i + ": ");
            for (int j = 0; j < row.getTransition_row().size(); j++) {
                transition_cell cell = row.getTransition_row().get(j);
                System.out.print("Afd1 State: " + cell.getAfd1State().getStateNumber() + ", Afd2 State: " + cell.getAfd2State().getStateNumber());
                if (j < row.getTransition_row().size() - 1) {
                    System.out.print(" | ");
                }
            }
            System.out.println();
        }
        for (int rowIndex = 0; rowIndex < tr_table.getTrasition_table().size(); rowIndex++) {
            transition_row currentRow = tr_table.getTrasition_table().get(rowIndex);
            transition_cell firstCell = currentRow.getTransition_row().get(0);
            State state1 = firstCell.getAfd1State();
            State state2 = firstCell.getAfd2State();
            State intersectionState = new State();
            intersectionState.setStateNumber(rowIndex);
            if (state1.isFinal() && state2.isFinal()) {
                intersectionState.setFinal(true);
            }
            firstCell.setIntersectionState(intersectionState);
        }
        for (int rowIndex = 0; rowIndex < tr_table.getTrasition_table().size(); rowIndex++) {
            transition_row currentRow = tr_table.getTrasition_table().get(rowIndex);
            transition_cell firstCell = currentRow.getTransition_row().get(0);
            for (int cellIndex = 1; cellIndex < currentRow.getTransition_row().size(); cellIndex++) {
                transition_cell currentCell = currentRow.getTransition_row().get(cellIndex);
                State currentState1 = currentCell.getAfd1State();
                State currentState2 = currentCell.getAfd2State();
                for (transition_row row : tr_table.getTrasition_table()) {
                    transition_cell firstCellOfRow = row.getTransition_row().get(0);
                    State firstCellState1 = firstCellOfRow.getAfd1State();
                    State firstCellState2 = firstCellOfRow.getAfd2State();
                    if (firstCellState1.getStateNumber() == currentState1.getStateNumber() &&
                            firstCellState2.getStateNumber() == currentState2.getStateNumber()) {
                        currentCell.setIntersectionState(firstCellOfRow.getIntersectionState());
                        break;
                    }
                }
            }
        }
        for (transition_row row : tr_table.getTrasition_table()) {
            StringBuilder rowContent = new StringBuilder();
            for (transition_cell cell : row.getTransition_row()) {
                rowContent.append("(Intersection State: ")
                        .append(cell.getIntersectionState().getStateNumber())
                        .append(", Final: ")
                        .append(cell.getIntersectionState().isFinal())
                        .append(") ");
            }
            System.out.println(rowContent.toString());
        }
        interNode finalList = new interNode();
        for (transition_row row : tr_table.getTrasition_table()) {
            listeNodes list = new listeNodes();
            for (transition_cell cell : row.getTransition_row()) {
                State intersectionState = cell.getIntersectionState();
                list.addNode(intersectionState);
            }
            finalList.addList(list);
        }
        for (int i = 0; i < finalList.getInterStates().size(); i++) {
            for (int j = 0; j < finalList.getInterStates().get(i).getListNodes().size(); j++) {
                System.out.println("row " + i + " cell " + j + ": " + finalList.getInterStates().get(i).getListNodes().get(j).getStateNumber());
            }
        }
        State[] states;
        Transition[] transitions;
        State inState;
        State[] fiStates;
        List<Transition> intersectionTransitions = new ArrayList<>();
        List<State>   intersectionStates = new ArrayList<>();
        List<State>   intersectionfiStates = new ArrayList<>();
        for (int i = 0; i < finalList.getInterStates().size(); i++) {
            List<State> nodeList = finalList.getInterStates().get(i).getListNodes();
            State sourceState = nodeList.get(0);
            for (int k =0;k<terminalsInter.length;k++) {
                State targetState = nodeList.get(k+1);
                Transition transition = new Transition(sourceState, terminalsInter[k], targetState);
                intersectionTransitions.add(transition);
            }
        }
        System.out.println(intersectionTransitions.size());
        for (Transition transition : intersectionTransitions) {

            System.out.println(transition.toString());
        }
        //etats
        for (int i = 0; i < finalList.getInterStates().size(); i++) {
            List<State> nodeList = finalList.getInterStates().get(i).getListNodes();
            State sourceState = nodeList.get(0);
            intersectionStates.add(sourceState);
        }
        System.out.println(intersectionStates.size());
        for (State etat : intersectionStates) {
            System.out.println(etat.toString());
        }
        //etats finaux
        for (int i = 0; i < finalList.getInterStates().size(); i++) {
            List<State> nodeList = finalList.getInterStates().get(i).getListNodes();
            State sourceState = nodeList.get(0);
            if(sourceState.isFinal()) {
                intersectionfiStates.add(sourceState);
            }
        }
        //display the final etatas
        System.out.println(intersectionfiStates.size());
        for (State etat : intersectionfiStates) {
            System.out.println(etat.toString());
        }
        resultAutomaton = new Automaton(
                intersectionStates.size(),  // numStates
                terminalsInter.length,  // numTerminals
                intersectionfiStates.size(),  // numFinalStates
                terminalsInter,  // terminals
                intersectionStates.toArray(new State[0]),  // states
                intersectionTransitions.toArray(new Transition[0]),  // transitions
                intersectionStates.get(0),  // inState
                intersectionfiStates.toArray(new State[0])  // fiStates
        );
        return resultAutomaton;
    }
    public static char[] interTerminal(char[] arr1, char[] arr2) {
        Set<Character> set1 = arrayToSet(arr1);
        Set<Character> set2 = arrayToSet(arr2);
        Set<Character> intersection = new HashSet<>(set1);
        intersection.retainAll(set2);
        char[] result = new char[intersection.size()];
        int index = 0;
        for (char c : intersection) {
            result[index++] = c;
        }
        return result;
    }

    public static Set<Character> arrayToSet(char[] arr) {
        Set<Character> set = new HashSet<>();
        for (char c : arr) {
            set.add(c);
        }
        return set;
    }

    @Override
    public void start(Stage primaryStage) {
        Button chooseFile1Button = new Button("Choisir fichier 1");
        Button chooseFile2Button = new Button("Choisir fichier 2");
        Button calculateInterButton = new Button("Calculer l'intersection");
        Button showMathButton = new Button("Forme mathématiques");
        Button backButton = new Button("Retour");

        chooseFile1Button.setStyle("-fx-background-color: #4C4CFF; -fx-text-fill: white;");
        chooseFile2Button.setStyle("-fx-background-color: #4C4CFF; -fx-text-fill: white;");
        calculateInterButton.setStyle("-fx-background-color: #4C4CFF; -fx-text-fill: white;");
        showMathButton.setStyle("-fx-background-color: #4C4CFF; -fx-text-fill: white;");
        backButton.setStyle("-fx-background-color: #4C4CFF; -fx-text-fill: white;");

        chooseFile1Button.setMinWidth(150);
        chooseFile2Button.setMinWidth(150);
        calculateInterButton.setMinWidth(150);
        showMathButton.setMinWidth(150);
        backButton.setMinWidth(150);

        TextArea outputTextArea = new TextArea();
        outputTextArea.setEditable(false);

        VBox root = new VBox(5, outputTextArea, chooseFile1Button, chooseFile2Button, calculateInterButton, showMathButton, backButton);
        root.setPadding(new Insets(30));
        root.setStyle("-fx-background-color: white;");
        chooseFile1Button.setOnAction(e -> {
            File selectedFile1 = chooseFile(primaryStage);
            automata1 = new Automaton(selectedFile1.getAbsolutePath());
        });

        chooseFile2Button.setOnAction(e -> {
            File selectedFile2 = chooseFile(primaryStage);
            automata2 = new Automaton(selectedFile2.getAbsolutePath());
        });

        calculateInterButton.setOnAction(e -> {
            automata1=intersection(automata1,automata2);

        });

        backButton.setOnAction(e -> {
            HelloApplication helloApplication = new HelloApplication();
            helloApplication.start(new Stage());
            primaryStage.close();
        });

        showMathButton.setOnAction(e -> {
            if (automata1 != null) {
                StringBuilder content = new StringBuilder();
                content.append("Number of States: ").append(automata1.getNumStates()).append("\n");
                content.append("Number of Terminals: ").append(automata1.getNumTerminals()).append("\n");
                content.append("Number of Final States: ").append(automata1.getNumFinalStates()).append("\n");
                content.append("Terminals: ").append(Arrays.toString(automata1.getTerminals())).append("\n");
                content.append("States: ").append(Arrays.toString(automata1.getStates())).append("\n");
                content.append("Transitions: \n");
                for (Transition transition : automata1.getTransitions()) {
                    content.append("\t").append(transition.toString()).append("\n");
                }
                content.append("Initial State: ").append("q0").append("\n");
                // Find and display the final states
                content.append("Final States: ");
                for (State state : automata1.getStates()) {
                    if (state.isFinal()) {
                        content.append(state).append(" ");
                    }
                }
                content.append("\n");
                outputTextArea.setText(content.toString());
            }
        });
        primaryStage.setScene(new Scene(root, 600, 600));
        primaryStage.setTitle("Calculate intersection ");

        primaryStage.show();
    }

    public File chooseFile(Stage stage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose File");
        return fileChooser.showOpenDialog(stage);
    }
}
