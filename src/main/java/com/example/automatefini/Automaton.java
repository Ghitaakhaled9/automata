package com.example.automatefini;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;


public class Automaton {

    private int numStates, numTerminals, numFinalStates;
    private char terminals[];
    private State states[];
    private Transition transitions[];
    private  transition_table tr_table;
    private State inState;
    private State fiStates[];
    private int initialState;
    //private Set<Integer> finalStates;

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
        return null; // State not found
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

    public void setStates(State[] states) {
        this.states = states;
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
        return null; // Or throw an exception if no transition is found
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
        for (char symbol : input.toCharArray()) {
            int terminalIndex = indexOfTerminal(symbol);
            if (terminalIndex == -1) {
                System.out.println("Invalid symbol '" + symbol + "' in the input string.");
                return false;
            }
            currentState = transitions[currentState * numTerminals + terminalIndex].getTargetState().getStateNumber();

        }
        return states[currentState].isFinal();
    }

    public int indexOfTerminal(char terminal) {
        for (int i = 0; i < numTerminals; i++) {
            if (terminals[i] == terminal) {
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

        // Retrieve the terminals
        String[] terminalsStr = lines.get(0).split(" ");
        terminals = new char[terminalsStr.length];
        for (int i = 0; i < terminalsStr.length; i++) {
            terminals[i] = terminalsStr[i].charAt(0);
        }

        // Retrieve the states
        String[] statesStr = lines.get(1).split(" ");
        states = new State[statesStr.length];
        for (int i = 0; i < statesStr.length; i++) {
            states[i] = new State(i, false);
        }

        // Retrieve the transitions
        List<Transition> transitionsList = new ArrayList<>();
        for (int i = 2; i < lines.size() - 1; i++) { // Changed condition to lines.size() - 1
            String[] parts = lines.get(i).split(" ");
            if (parts.length == 3) {
                // Create and add a transition
                int sourceState = Integer.parseInt(parts[0]);
                char terminal = parts[1].charAt(0);
                int targetState = Integer.parseInt(parts[2]);
                transitionsList.add(new Transition(states[sourceState], terminal, states[targetState]));
            }
        }
        transitions = transitionsList.toArray(new Transition[0]);

        // Retrieve the initial state
        int initialStateIndex = Integer.parseInt(lines.get(lines.size() - 1)); // Changed index to lines.size() - 1
        if (initialStateIndex >= 0 && initialStateIndex < states.length) {
            initialState = states[initialStateIndex];
        }

        // Retrieve the final states
        String[] finalStatesStr = lines.get(lines.size() - 2).split(" "); // Changed index to lines.size() - 2
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
        return null; // Si l'état n'est pas trouvé
    }


    // Méthode utilitaire pour concaténer deux tableaux de caractères
    private static char[] concatArrays(char[] arr1, char[] arr2) {
        char[] result = new char[arr1.length + arr2.length];
        System.arraycopy(arr1, 0, result, 0, arr1.length);
        System.arraycopy(arr2, 0, result, arr1.length, arr2.length);
        return result;
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

    public static List<String> toMathematicalForm(Automaton automaton) {
        List<String> lines = new ArrayList<>();

        // Forme mathématique pour les états
        StringBuilder statesLine = new StringBuilder();
        statesLine.append("Q");
        for (State state : automaton.states) {
            statesLine.append("q").append(state.getStateNumber());
            if (state.isFinal()) {
                statesLine.append(" (Final State)");
            }
            statesLine.append(", ");
        }
        statesLine.delete(statesLine.length() - 2, statesLine.length()); // Supprimer la dernière virgule et l'espace
        statesLine.append("}");
        lines.add(statesLine.toString());

        // Forme mathématique pour l'alphabet
        StringBuilder alphabetLine = new StringBuilder();
        alphabetLine.append("Σ = {");
        for (char terminal : automaton.terminals) {
            alphabetLine.append(terminal).append(", ");
        }
        alphabetLine.delete(alphabetLine.length() - 2, alphabetLine.length()); // Supprimer la dernière virgule et
        // l'espace
        alphabetLine.append("}");
        lines.add(alphabetLine.toString());

        // Forme mathématique pour les transitions
        StringBuilder transitionsLine = new StringBuilder();
        transitionsLine.append("δ = {\n");
        for (Transition transition : automaton.transitions) {
            transitionsLine.append("\tq").append(transition.getSourceState().getStateNumber())
                    .append(", ").append(transition.getTerminal()).append(", ")
                    .append("q").append(transition.getTargetState().getStateNumber()).append(",\n");
        }
        transitionsLine.append("}");
        lines.add(transitionsLine.toString());

        // Forme mathématique pour l'état initial
        lines.add("q0 = q" + automaton.inState.getStateNumber());

        // Forme mathématique pour les états finaux
        StringBuilder finalStatesLine = new StringBuilder();
        finalStatesLine.append("F = {");
        for (State finalState : automaton.fiStates) {
            finalStatesLine.append("q").append(finalState.getStateNumber()).append(", ");
        }
        finalStatesLine.delete(finalStatesLine.length() - 2, finalStatesLine.length()); // Supprimer la dernière virgule
        // et l'espace
        finalStatesLine.append("}");
        lines.add(finalStatesLine.toString());

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
        // Iterate through all transitions to find the matching one
        for (Transition transition : transitions) {
            // Check if the source state of the transition matches the current state
            if (transition.getSourceState().equals(currentState)) {
                // Check if the terminal symbol of the transition matches the given symbol
                if (transition.getTerminal() == symbol) {
                    // Return the target state if the transition is valid
                    return transition.getTargetState();
                }
            }
        }
        // Return null if no valid transition is found
        return null;
    }
    public Automaton intersection(Automaton automaton1, Automaton automaton2) {
        Automaton resultAutomaton = new Automaton();

        // Iterate through the states of automaton1 and automaton2 to create the transition table
        for (State stateAfd1 : automaton1.getStates()) {
            for (State stateAfd2 : automaton2.getStates()) {
                transition_row row = new transition_row(); // Create a new row for each state combination

                transition_cell cell = new transition_cell(); // Create a new cell for each state combination
                cell.setAfd1State(stateAfd1);
                cell.setAfd2State(stateAfd2);

                row.addcell(cell); // Add the cell to the row
                tr_table.addrow(row); // Add the row to the transition table
            }
        }

        // Display the content of each cell in the transition table
        for (transition_row row : tr_table.getTrasition_table()) {
            for (transition_cell cell : row.getTransition_row()) {
                System.out.println("Afd1 State: " + cell.getAfd1State().getStateNumber() + ", Afd2 State: " + cell.getAfd2State().getStateNumber());
            }
        }
        //we are going to identify the terminals in common of thoes both
        char terminals1[]= automaton1.getTerminals();
        char terminals2[]= automaton2.getTerminals();
        char terminalsInter[]= interTerminal(terminals1,terminals2);

        //test if the intersection terminals are correct
        for( int i=0; i<terminalsInter.length;i++) {
            System.out.println("terminal["+i+"]"+"="+terminalsInter[i]);
        }


        //now we are going to create the next cells for each terminal


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



        // Display the content of each cell in the transition table
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
            System.out.println(); // Move to the next line after printing all cells of the row
        }

        //  we are going to create the states of the intersection of the two states
        //first we are going to mark up the final states
        // Iterate over the rows in the transition table
        for (int rowIndex = 0; rowIndex < tr_table.getTrasition_table().size(); rowIndex++) {
            transition_row currentRow = tr_table.getTrasition_table().get(rowIndex);
            transition_cell firstCell = currentRow.getTransition_row().get(0); // Get the first cell in the row

            // Get the states from the first cell
            State state1 = firstCell.getAfd1State();
            State state2 = firstCell.getAfd2State();

            // Create the intersection state
            State intersectionState = new State();
            intersectionState.setStateNumber(rowIndex); // Use the row index as the number of the intersection state

            // Check if both states are final
            if (state1.isFinal() && state2.isFinal()) {
                intersectionState.setFinal(true); // Set the intersection state to be final
            }

            // Set the intersection state for the first cell
            firstCell.setIntersectionState(intersectionState);
        }
        // Iterate over the rows in the transition table
        for (int rowIndex = 0; rowIndex < tr_table.getTrasition_table().size(); rowIndex++) {
            transition_row currentRow = tr_table.getTrasition_table().get(rowIndex);
            transition_cell firstCell = currentRow.getTransition_row().get(0); // Get the first cell in the row

            // Iterate over the rest of the cells in the row (excluding the first cell)
            for (int cellIndex = 1; cellIndex < currentRow.getTransition_row().size(); cellIndex++) {
                transition_cell currentCell = currentRow.getTransition_row().get(cellIndex);

                // Get the states from the current cell
                State currentState1 = currentCell.getAfd1State();
                State currentState2 = currentCell.getAfd2State();

                // Find the corresponding first cell with matching Afd1State and Afd2State numbers
                for (transition_row row : tr_table.getTrasition_table()) {
                    transition_cell firstCellOfRow = row.getTransition_row().get(0);
                    State firstCellState1 = firstCellOfRow.getAfd1State();
                    State firstCellState2 = firstCellOfRow.getAfd2State();

                    // Check if the numbers of states of both automata match
                    if (firstCellState1.getStateNumber() == currentState1.getStateNumber() &&
                            firstCellState2.getStateNumber() == currentState2.getStateNumber()) {
                        // Set the intersection state of the current cell to match the first cell
                        currentCell.setIntersectionState(firstCellOfRow.getIntersectionState());
                        break; // Exit the loop once a matching first cell is found
                    }
                }
            }
        }

        // Display the content of the intersectionState for each cell in the transition table
        for (transition_row row : tr_table.getTrasition_table()) {
            StringBuilder rowContent = new StringBuilder(); // StringBuilder to hold the content of each row

            for (transition_cell cell : row.getTransition_row()) {
                // Append the attributes of the intersectionState to the row content
                rowContent.append("(Intersection State: ")
                        .append(cell.getIntersectionState().getStateNumber())
                        .append(", Final: ")
                        .append(cell.getIntersectionState().isFinal())
                        .append(") ");
            }

            // Print the content of the row
            System.out.println(rowContent.toString());
        }




        interNode finalList = new interNode(); // Create a list to store interNode objects
        for (transition_row row : tr_table.getTrasition_table()) {
            listeNodes list = new listeNodes(); // Create a new list for each row
            for (transition_cell cell : row.getTransition_row()) {
                State intersectionState = cell.getIntersectionState();
                list.addNode(intersectionState);
            }
            finalList.addList(list); // Add the list for the current row to the final list
        }


        //display final list elements

        for (int i = 0; i < finalList.getInterStates().size(); i++) {
            for (int j = 0; j < finalList.getInterStates().get(i).getListNodes().size(); j++) {
                System.out.println("row " + i + " cell " + j + ": " + finalList.getInterStates().get(i).getListNodes().get(j).getStateNumber());
            }
        }
        //construction de afd reusultat de intersection
        State[] states;
        Transition[] transitions;
        State inState;
        State[] fiStates;
        List<Transition> intersectionTransitions = new ArrayList<>();
        List<State>   intersectionStates = new ArrayList<>();
        List<State>   intersectionfiStates = new ArrayList<>();
        //now we are going to create the transitions

        for (int i = 0; i < finalList.getInterStates().size(); i++) {
            List<State> nodeList = finalList.getInterStates().get(i).getListNodes();
            State sourceState = nodeList.get(0);

            for (int k =0;k<terminalsInter.length;k++) {
                State targetState = nodeList.get(k+1);
                Transition transition = new Transition(sourceState, terminalsInter[k], targetState);
                intersectionTransitions.add(transition);
            }
        }


        //display the transitons
        System.out.println(intersectionTransitions.size());
        for (Transition transition : intersectionTransitions) {

            System.out.println(transition.toString()); // Assuming there's a proper toString() method in the Transition class
        }
        //etats


        for (int i = 0; i < finalList.getInterStates().size(); i++) {
            List<State> nodeList = finalList.getInterStates().get(i).getListNodes();
            State sourceState = nodeList.get(0);
            intersectionStates.add(sourceState);


        }
        //display the etatas
        System.out.println(intersectionStates.size());
        for (State etat : intersectionStates) {
            System.out.println(etat.toString()); // Assuming there's a proper toString() method in the Transition class
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
            System.out.println(etat.toString()); // Assuming there's a proper toString() method in the Transition class
        }

        //construction du automaton reusultat
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

        // Create a set for the intersection
        Set<Character> intersection = new HashSet<>(set1);
        intersection.retainAll(set2);

        // Convert the intersection set back to an array
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

        // Fonction de transition
        mathematicalForm.append("Fonction de transition :\n");
        for (Transition transition : transitions) {
            mathematicalForm.append("δ(").append(transition.getSourceState()).append(", ").append(transition.getTerminal()).append(") = ").append(transition.getTargetState()).append("\n");
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
        // Si l'automate a un nombre infini d'états, retourne vrai
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



}