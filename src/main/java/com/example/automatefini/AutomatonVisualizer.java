package com.example.automatefini;
import java.util.Random;
import javafx.scene.paint.Color;

public class AutomatonVisualizer {

    public static class StateVisualizer {
        int id;
        boolean isFinal;

        public StateVisualizer(int id, boolean isFinal) {
            this.id = id;
            this.isFinal = isFinal;
        }
    }

    public static class TransitionVisualizer {
        int fromState;
        int toState;
        char symbol;
        Color color;

        public TransitionVisualizer(int fromState, int toState, char symbol) {
            this.fromState = fromState;
            this.toState = toState;
            this.symbol = symbol;
            Random random = new Random();
            do {
                this.color = Color.rgb(random.nextInt(256), random.nextInt(256), random.nextInt(256));
            } while (this.color.equals(Color.WHITE));
        }
    }

    private int numStates;
    private int numTerminals;
    private int numFinalStates;
    private char[] terminals;
    private StateVisualizer[] states;
    private TransitionVisualizer[] transitions;
    private StateVisualizer inState;
    private StateVisualizer[] fiStates;
// Default constructor
public AutomatonVisualizer() {
    this.numStates = 0;
    this.numTerminals = 0;
    this.numFinalStates = 0;
    this.terminals = new char[0];
    this.states = new StateVisualizer[0];
    this.transitions = new TransitionVisualizer[0];
    this.inState = null;
    this.fiStates = new StateVisualizer[0];
}
    public AutomatonVisualizer(int numStates, int numTerminals, int numFinalStates, char[] terminals,
                               StateVisualizer[] states, TransitionVisualizer[] transitions,
                               StateVisualizer inState, StateVisualizer[] fiStates) {
        this.numStates = numStates;
        this.numTerminals = numTerminals;
        this.numFinalStates = numFinalStates;
        this.terminals = terminals;
        this.states = states;
        this.transitions = transitions;
        this.inState = inState;
        this.fiStates = fiStates;
    }

    // Getters
    public int getNumStates() {
        return numStates;
    }

    public int getNumTerminals() {
        return numTerminals;
    }

    public int getNumFinalStates() {
        return numFinalStates;
    }

    public char[] getTerminals() {
        return terminals;
    }

    public StateVisualizer[] getStates() {
        return states;
    }

    public TransitionVisualizer[] getTransitions() {
        return transitions;
    }

    public StateVisualizer getInState() {
        return inState;
    }

    public StateVisualizer[] getFiStates() {
        return fiStates;
    }

    // Setters
    public void setNumStates(int numStates) {
        this.numStates = numStates;
    }

    public void setNumTerminals(int numTerminals) {
        this.numTerminals = numTerminals;
    }

    public void setNumFinalStates(int numFinalStates) {
        this.numFinalStates = numFinalStates;
    }

    public void setTerminals(char[] terminals) {
        this.terminals = terminals;
    }

    public void setStates(StateVisualizer[] states) {
        this.states = states;
    }

    public void setTransitions(TransitionVisualizer[] transitions) {
        this.transitions = transitions;
    }

    public void setInState(StateVisualizer inState) {
        this.inState = inState;
    }

    public void setFiStates(StateVisualizer[] fiStates) {
        this.fiStates = fiStates;
    }

}
