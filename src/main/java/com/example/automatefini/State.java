package com.example.automatefini;

public class State {
    private int stateNumber;
    private boolean isFinal;

    private String stateName;
    public State(int stateNumber, boolean isFinal) {
        this.stateNumber = stateNumber;
        this.isFinal = isFinal;
    }
    public State() {
        super();
    }

    public State(String stateName) {
        this.stateName = stateName;
    }
    public State(int stateNumber, boolean isFinal, String stateName) {
        this.stateNumber = stateNumber;
        this.isFinal = isFinal;
        this.stateName = stateName;
    }

    public int getStateNumber() {
        return stateNumber;
    }
    public State(int i) {
        this.stateNumber=i;
    }
    public boolean isFinal() {
        return isFinal;
    }
    public String getStateName() {
        return stateName;
    }
    public void setStateNumber(int stateNumber) {
        this.stateNumber = stateNumber;
    }

    public void setFinal(boolean isFinal) {
        this.isFinal = isFinal;
    }

    @Override
    public String toString() {
        return "q" + stateNumber + (isFinal() ? " (Final State)" : "");
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        State state = (State) o;
        return stateNumber == state.stateNumber &&
                isFinal == state.isFinal;
    }

}
