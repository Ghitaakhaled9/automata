package com.example.automatefini;

import java.util.HashMap;
import java.util.Map;

public class State {
    private int stateNumber;
    private boolean isFinal;


    //private Map<Character, State> transitions;
    private String stateName;
    public State(int stateNumber, boolean isFinal) {
        this.stateNumber = stateNumber;
        this.isFinal = isFinal;
        //  this.transitions = new HashMap<>();
    }


    public State() {
        super();
    }


    public State(String stateName) {
        this.stateName = stateName;
        //  this.transitions = new HashMap<>();
    }
    public State(int stateNumber, boolean isFinal, String stateName) {
        this.stateNumber = stateNumber;
        this.isFinal = isFinal;
        this.stateName = stateName;
        // this.transitions = new HashMap<>();
    }

    public int getStateNumber() {
        return stateNumber;
    }

    public boolean isFinal() {
        return isFinal;
    }
    // Getter method for stateName
    public String getStateName() {
        return stateName;
    }
    public void setStateNumber(int stateNumber) {
        this.stateNumber = stateNumber;
    }

    public void setFinal(boolean isFinal) {
        this.isFinal = isFinal;
    }



    // Other methods, such as toString, hashCode, equals, etc.


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
