package com.example.automatefini;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

import com.example.automatefini.AutomatonVisualizer.StateVisualizer;
import com.example.automatefini.AutomatonVisualizer.TransitionVisualizer;

public class Automaton {
    private int numStates, numTerminals, numFinalStates;
    private char terminals[];
    private State states[];
    private Transition transitions[];
    private transition_table tr_table;
    private State inState;
    private State fiStates[];
    private int initialState;

    public Automaton() {
        this.tr_table = new transition_table();
    }

    public Automaton(int numStates, int numTerminals, int numFinalStates, char[] terminals, State[] states,
            Transition[] transitions, State inState, State[] fiStates) {
        this.numStates = numStates;
        this.numTerminals = numTerminals;
        this.numFinalStates = numFinalStates;
        this.terminals = terminals;
        this.states = states;
        this.transitions = transitions;
        this.inState = inState;
        this.fiStates = fiStates;
    }

    public Automaton(char[] terminals, State[] states, Transition[] transitions, State inState, State[] fiStates) {
        this.terminals = terminals;
        this.states = states;
        this.transitions = transitions;
        this.inState = inState;
        this.fiStates = fiStates;
    }

    public int getNumStates() {
        return numStates;
    }
    public void setNumStates(int numStates) {
        this.numStates = numStates;
    }
    public int getNumTerminals() {
        return numTerminals;
    }
    public void setNumTerminals(int numTerminals) {
        this.numTerminals = numTerminals;
    }
    public int getNumFinalStates() {
        return numFinalStates;
    }
    public State getStateByNumber(int stateNumber) {
        for (State state : this.getStates()) {
            if (state.getStateNumber() == stateNumber) {
                return state;
            }
        }
        return null;
    }
    public void setNumFinalStates(int numFinalStates) {
        this.numFinalStates = numFinalStates;
    }
    public char[] getTerminals() {
        return terminals;
    }
    public void setTerminals(char[] terminals) {
        this.terminals = terminals;
    }
    public State[] getStates() {
        return states;
    }
    public Transition[] getTransitions() {
        return transitions;
    }
    public void setTransitions(Transition[] transitions) {
        this.transitions = transitions;
    }
    public State getInState() {
        return inState;
    }
    public void setInState(State inState) {
        this.inState = inState;
    }
    public State[] getFiStates() {
        return fiStates;
    }
    public void setFiStates(State[] fiStates) {
        this.fiStates = fiStates;
    }
    public State getNextState(State currentState, char symbol) {
        for (Transition transition : transitions) {
            if (transition.getSourceState().equals(currentState) && transition.getTerminal() == symbol) {
                return transition.getTargetState();
            }
        }
        return null;
    }
    public Automaton(String filename) {
        this.tr_table = new transition_table();
        BufferedReader infile = null;
        numStates = 0;
        numTerminals = 0;
        try {
            infile = new BufferedReader(new FileReader(filename));
            numTerminals = Integer.parseInt(infile.readLine());
            String strTerms[] = infile.readLine().split(" ");
            terminals = new char[numTerminals];
            for (int i = 0; i < numTerminals; i++) {
                terminals[i] = strTerms[i].charAt(0);
            }
            numStates = Integer.parseInt(infile.readLine());
            states = new State[numStates];
            for (int i = 0; i < numStates; i++) {
                states[i] = new State(i, false);
            }
            transitions = new Transition[numStates * numTerminals];
            for (int i = 0; i < (numStates * numTerminals); i++) {
                String pathStr[] = infile.readLine().split(" ");
                int sourceState = Integer.parseInt(pathStr[0]);
                int targetState = Integer.parseInt(pathStr[2]);
                char terminal = pathStr[1].charAt(0);

                transitions[i] = new Transition(states[sourceState], terminal, states[targetState]);
            }
            numFinalStates = Integer.parseInt(infile.readLine());
            String[] fs = infile.readLine().trim().split(" ");
            for (int tmp = 0; tmp < numFinalStates; tmp++) {
                int finalStateIndex = Integer.parseInt(fs[tmp]);
                states[finalStateIndex].setFinal(true);
            }
            infile.close();
        } catch (IOException exception) {
            exception.printStackTrace();
        } catch (Exception exception) {
            System.out.println("Malformed input file!");
            exception.printStackTrace();
        }
    }
    public boolean[] getFinalStates() {
        boolean[] finalStates = new boolean[numStates];
        for (int i = 0; i < numStates; i++) {
            finalStates[i] = states[i].isFinal();
        }
        return finalStates;
    }
    public boolean accepts(String input) {
        int currentState = 0;
        System.out.println("input:" + input);
        for (char symbol : input.toCharArray()) {
            int terminalIndex = indexOfTerminal(symbol);
            if (terminalIndex == -1) {
                System.out.println("Invalid symbol '" + symbol + "' in the input string.");
                return false;
            }
            currentState = transitions[currentState * numTerminals + terminalIndex].getTargetState().getStateNumber();
            System.out.println("ccuuuuuuuuuuuuu" + currentState);

        }
        System.out.println("cc" + currentState);

        return states[currentState].isFinal();
    }
    public int indexOfTerminal(char terminal) {
        System.out.println("termmmmmmmmmmmmm" + this.numTerminals);
        for (int i = 0; i < this.numTerminals; i++) {
            System.out.println("term" + i);
            if (this.terminals[i] == terminal) {

                return i;

            }
        }
        return -1;
    }
    public void printConfiguration() {
        System.out.println("DFA Configuration:");
        System.out.println("Q (States): " + Arrays.toString(states));
        System.out.println("Σ (Alphabet): " + Arrays.toString(terminals));

        System.out.println("δ (Transition Function):");
        for (Transition transition : transitions) {
            System.out.println("  δ(" + transition.getSourceState().getStateNumber() +
                    ", '" + transition.getTerminal() + "') = " +
                    transition.getTargetState().getStateNumber());
        }
        System.out.println("q0 (Initial State): " + states[0].getStateNumber());

        System.out.println("F (Final States): " +
                Arrays.toString(getFinalStates()));
        System.out.println("\n------------------------ DFA Transitions:");
        System.out.println(numStates);
        System.out.println(new String(terminals));
        System.out.println(numStates * numTerminals);

        for (Transition transition : transitions) {
            System.out.println(transition.getSourceState().getStateNumber() +
                    " " + transition.getTerminal() +
                    " " + transition.getTargetState().getStateNumber());
        }
        System.out.println(numFinalStates);
        System.out.println(formatBooleanArray(getFinalStates()));
    }

    private String formatBooleanArray(boolean[] array) {
        StringBuilder result = new StringBuilder();
        for (boolean value : array) {
            result.append(value).append(" ");
        }
        return result.toString().trim();
    }

    public static Automaton readAutomatonFromFile(File file) throws IOException {
        State[] states = null;
        char[] terminals = null;
        Transition[] transitions = null;
        State initialState = null;
        State[] finalStates = null;
        List<String> lines = readLinesFromFile(file);
        String[] terminalsStr = lines.get(0).split(" ");
        terminals = new char[terminalsStr.length];
        for (int i = 0; i < terminalsStr.length; i++) {
            terminals[i] = terminalsStr[i].charAt(0);
        }
        String[] statesStr = lines.get(1).split(" ");
        states = new State[statesStr.length];
        for (int i = 0; i < statesStr.length; i++) {
            states[i] = new State(i, false);
        }
        List<Transition> transitionsList = new ArrayList<>();
        for (int i = 2; i < lines.size() - 1; i++) {
            String[] parts = lines.get(i).split(" ");
            if (parts.length == 3) {
                int sourceState = Integer.parseInt(parts[0]);
                char terminal = parts[1].charAt(0);
                int targetState = Integer.parseInt(parts[2]);
                transitionsList.add(new Transition(states[sourceState], terminal, states[targetState]));
            }
        }
        transitions = transitionsList.toArray(new Transition[0]);
        int initialStateIndex = Integer.parseInt(lines.get(lines.size() - 1));
        if (initialStateIndex >= 0 && initialStateIndex < states.length) {
            initialState = states[initialStateIndex];
        }
        String[] finalStatesStr = lines.get(lines.size() - 2).split(" ");
        finalStates = new State[finalStatesStr.length];
        for (int i = 0; i < finalStatesStr.length; i++) {
            int finalStateIndex = Integer.parseInt(finalStatesStr[i]);
            if (finalStateIndex >= 0 && finalStateIndex < states.length) {
                finalStates[i] = states[finalStateIndex];
                finalStates[i].setFinal(true);
            }
        }
        return new Automaton(terminals, states, transitions, initialState, finalStates);
    }

    private static State findState(State[] states, int stateNumber) {
        for (State state : states) {
            if (state.getStateNumber() == stateNumber) {
                return state;
            }
        }
        return null;
    }

    private static List<String> readLinesFromFile(File file) throws IOException {
        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        }
        return lines;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Number of States: ").append(numStates).append("\n");
        builder.append("Number of Terminals: ").append(numTerminals).append("\n");
        builder.append("Number of Final States: ").append(numFinalStates).append("\n");
        builder.append("Terminals: ").append(Arrays.toString(terminals)).append("\n");
        builder.append("States: ").append(Arrays.toString(states)).append("\n");
        builder.append("Transitions: \n");
        for (Transition transition : transitions) {
            builder.append("\t").append(transition.toString()).append("\n");
        }
        builder.append("Initial State: ").append(inState).append("\n");
        builder.append("Final States: ").append(Arrays.toString(fiStates)).append("\n");
        return builder.toString();
    }

    public State getTargetStateForSymbol(State currentState, char symbol) {
        for (Transition transition : transitions) {
            if (transition.getSourceState().equals(currentState)) {
                if (transition.getTerminal() == symbol) {
                    return transition.getTargetState();
                }
            }
        }
        return null;
    }

    public Automaton intersection(Automaton automaton1, Automaton automaton2) {
        Automaton resultAutomaton = new Automaton();
        for (State stateAfd1 : automaton1.getStates()) {
            for (State stateAfd2 : automaton2.getStates()) {
                transition_row row = new transition_row(); // Create a new row for each state combination

                transition_cell cell = new transition_cell(); // Create a new cell for each state combination
                cell.setAfd1State(stateAfd1);
                cell.setAfd2State(stateAfd2);

                row.addcell(cell);
                tr_table.addrow(row);
            }
        }
        for (transition_row row : tr_table.getTrasition_table()) {
            for (transition_cell cell : row.getTransition_row()) {
                System.out.println("Afd1 State: " + cell.getAfd1State().getStateNumber() + ", Afd2 State: "
                        + cell.getAfd2State().getStateNumber());
            }
        }
        char terminals1[] = automaton1.getTerminals();
        char terminals2[] = automaton2.getTerminals();
        char terminalsInter[] = interTerminal(terminals1, terminals2);
        for (int i = 0; i < terminalsInter.length; i++) {
            System.out.println("terminal[" + i + "]" + "=" + terminalsInter[i]);
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
                System.out.print("Afd1 State: " + cell.getAfd1State().getStateNumber() + ", Afd2 State: "
                        + cell.getAfd2State().getStateNumber());
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
                // Append the attributes of the intersectionState to the row content
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
                System.out.println("row " + i + " cell " + j + ": "
                        + finalList.getInterStates().get(i).getListNodes().get(j).getStateNumber());
            }
        }
        State[] states;
        Transition[] transitions;
        State inState;
        State[] fiStates;
        List<Transition> intersectionTransitions = new ArrayList<>();
        List<State> intersectionStates = new ArrayList<>();
        List<State> intersectionfiStates = new ArrayList<>();
        for (int i = 0; i < finalList.getInterStates().size(); i++) {
            List<State> nodeList = finalList.getInterStates().get(i).getListNodes();
            State sourceState = nodeList.get(0);
            for (int k = 0; k < terminalsInter.length; k++) {
                State targetState = nodeList.get(k + 1);
                Transition transition = new Transition(sourceState, terminalsInter[k], targetState);
                intersectionTransitions.add(transition);
            }
        }
        System.out.println(intersectionTransitions.size());
        for (Transition transition : intersectionTransitions) {
            System.out.println(transition.toString());
        }
        for (int i = 0; i < finalList.getInterStates().size(); i++) {
            List<State> nodeList = finalList.getInterStates().get(i).getListNodes();
            State sourceState = nodeList.get(0);
            intersectionStates.add(sourceState);

        }
        // display the etats
        System.out.println(intersectionStates.size());
        for (State etat : intersectionStates) {
            System.out.println(etat.toString()); // Assuming there's a proper toString() method in the Transition class
        }
        // etats finaux
        for (int i = 0; i < finalList.getInterStates().size(); i++) {
            List<State> nodeList = finalList.getInterStates().get(i).getListNodes();
            State sourceState = nodeList.get(0);
            if (sourceState.isFinal()) {
                intersectionfiStates.add(sourceState);
            }

        }
        // display the final states
        System.out.println(intersectionfiStates.size());
        for (State etat : intersectionfiStates) {
            System.out.println(etat.toString());
        }
        resultAutomaton = new Automaton(
                intersectionStates.size(), // numStates
                terminalsInter.length, // numTerminals
                intersectionfiStates.size(), // numFinalStates
                terminalsInter, // terminals
                intersectionStates.toArray(new State[0]), // states
                intersectionTransitions.toArray(new Transition[0]), // transitions
                intersectionStates.get(0), // inState
                intersectionfiStates.toArray(new State[0]) // fiStates
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

    public String toMathematicalForm() {
        StringBuilder mathematicalForm = new StringBuilder();
        // États
        mathematicalForm.append("États : {");
        for (int i = 0; i < numStates; i++) {
            mathematicalForm.append(states[i]);
            if (i < numStates - 1) {
                mathematicalForm.append(", ");
            }
        }
        mathematicalForm.append("}\n");
        // Alphabet
        mathematicalForm.append("Alphabet : {");
        for (int i = 0; i < numTerminals; i++) {
            mathematicalForm.append(terminals[i]);
            if (i < numTerminals - 1) {
                mathematicalForm.append(", ");
            }
        }
        mathematicalForm.append("}\n");
        // État initial
        mathematicalForm.append("État initial : ").append(states[initialState]).append("\n");
        // États finaux
        mathematicalForm.append("États finaux : {");
        for (int i = 0; i < numFinalStates; i++) {
            mathematicalForm.append(fiStates[i]);
            if (i < numFinalStates - 1) {
                mathematicalForm.append(", ");
            }
        }
        mathematicalForm.append("}\n");
        mathematicalForm.append("Fonction de transition :\n");
        for (Transition transition : transitions) {
            mathematicalForm.append("δ(").append(transition.getSourceState()).append(", ")
                    .append(transition.getTerminal()).append(") = ").append(transition.getTargetState()).append("\n");
        }
        return mathematicalForm.toString();
    }

    public void addFinalState(State finalState) {
        if (fiStates == null) {
            fiStates = new State[1];
            fiStates[0] = finalState;
        } else {
            State[] newArray = new State[fiStates.length + 1];
            System.arraycopy(fiStates, 0, newArray, 0, fiStates.length);
            newArray[fiStates.length] = finalState;
            fiStates = newArray;
        }
    }

    public void addTransition(Transition transition) {
        if (transitions == null) {
            transitions = new Transition[1];
            transitions[0] = transition;
        } else {
            Transition[] newArray = new Transition[transitions.length + 1];
            System.arraycopy(transitions, 0, newArray, 0, transitions.length);
            newArray[transitions.length] = transition;
            transitions = newArray;
        }
    }

    public void addState(State state) {
        if (states == null) {
            states = new State[1];
            states[0] = state;
        } else {
            State[] newArray = new State[states.length + 1];
            System.arraycopy(states, 0, newArray, 0, states.length);
            newArray[states.length] = state;
            states = newArray;
        }
    }

    public enum LanguageType {
        FINITE,
        INFINITE,
        EMPTY
    }

    public static Automaton.LanguageType determineLanguageType(Automaton automaton) {
        State[] fiStates = automaton.getFiStates();
        if (fiStates == null || fiStates.length == 0) {
            return Automaton.LanguageType.EMPTY;
        } else if (hasInfiniteStates(automaton) || hasInfiniteTransitions(automaton)) {
            return Automaton.LanguageType.INFINITE;
        } else {
            return Automaton.LanguageType.FINITE;
        }
    }

    private static boolean hasInfiniteStates(Automaton automaton) {
        return automaton.getNumStates() == Integer.MAX_VALUE;
    }

    private static boolean hasInfiniteTransitions(Automaton automaton) {
        // Si l'automate a des boucles infinies dans ses transitions, retourne vrai
        for (Transition transition : automaton.getTransitions()) {
            if (transition.getSourceState().equals(transition.getTargetState())) {
                return true;
            }
        }
        return false;
    }
    

    public AutomatonVisualizer convertFromAutomatonToVisualAFD(com.example.automatefini.Automaton automaton) {
        int numStates = automaton.getNumStates();
        int numTerminals = automaton.getNumTerminals();
        int numFinalStates = automaton.getNumFinalStates();
        char[] terminals = automaton.getTerminals();
        State[] states_original = automaton.getStates();
        Transition[] originalTransition= automaton.getTransitions();
        State[] original_final_states = automaton.getFiStates();

        AutomatonVisualizer.StateVisualizer[] states = new AutomatonVisualizer.StateVisualizer[numStates];
        for (int i = 0; i < numStates; i++) {
            
            states[i] = new StateVisualizer(states_original[i].getStateNumber(), states_original[i].isFinal());
        }

        AutomatonVisualizer.TransitionVisualizer[] transitions = new AutomatonVisualizer.TransitionVisualizer[automaton.getTransitions().length];
        for (int i = 0; i < automaton.getTransitions().length; i++) {
            
            transitions[i] = new TransitionVisualizer(
                    originalTransition[i].getSourceState().getStateNumber(),
                    originalTransition[i].getTargetState().getStateNumber(),
                    originalTransition[i].getTerminal()
            );
        }

        AutomatonVisualizer.StateVisualizer inState = states[0];

        AutomatonVisualizer.StateVisualizer[] fiStates = new AutomatonVisualizer.StateVisualizer[automaton.getFiStates().length];
        for (int i = 0; i < automaton.getFiStates().length; i++) {
            fiStates[i] = states[original_final_states[i].getStateNumber()];
        }

        return new AutomatonVisualizer(numStates, numTerminals, numFinalStates, terminals, states, transitions, inState, fiStates);
    }
}