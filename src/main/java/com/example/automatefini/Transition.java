package com.example.automatefini;

public class Transition {
    private State sourceState;
    private Character terminal;
    private State targetState;

    public Transition(State sourceState, Character terminal, State targetState) {
        this.sourceState = sourceState;
        this.terminal = terminal;
        this.targetState = targetState;
    }
    public State getSourceState() {
        return sourceState;
    }

    public void setSourceState(State sourceState) {
        this.sourceState = sourceState;
    }

    public Character getTerminal() {
        return terminal;
    }

    public void setTerminal(Character terminal) {
        this.terminal = terminal;
    }

    public State getTargetState() {
        return targetState;
    }

    public void setTargetState(State targetState) {
        this.targetState = targetState;
    }
    @Override
    public String toString() {
        return "δ(" + sourceState.getStateNumber() + ", '" + terminal + "') = " + targetState.getStateNumber();
    }

}