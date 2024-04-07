package com.example.automatefini;

public class transition_cell {

    private State afd1State ;
    private State afd2State ;
    private State intersectionState ;
    public transition_cell() {
        super();
    }

    public transition_cell(State afd1State, State afd2State, State intersectionState) {
        super();
        this.afd1State = afd1State;
        this.afd2State = afd2State;
        this.intersectionState = intersectionState;
    }



    public State getAfd1State() {
        return afd1State;
    }



    public void setAfd1State(State afd1State) {
        this.afd1State = afd1State;
    }



    public State getAfd2State() {
        return afd2State;
    }



    public void setAfd2State(State afd2State) {
        this.afd2State = afd2State;
    }



    public State getIntersectionState() {
        return intersectionState;
    }



    public void setIntersectionState(State intersectionState) {
        this.intersectionState = intersectionState;
    }


}
