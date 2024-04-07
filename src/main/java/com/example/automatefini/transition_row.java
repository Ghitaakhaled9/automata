package com.example.automatefini;

import java.util.ArrayList;
import java.util.List;

public class transition_row {


    private List<transition_cell> transition_row;



    public transition_row() {
        super();
        transition_row = new ArrayList<>();
    }



    public transition_row(List<transition_cell> transition_row) {
        super();
        this.transition_row = transition_row;
    }



    public List<transition_cell> getTransition_row() {
        return transition_row;
    }



    public void setTransition_row(List<transition_cell> transition_row) {
        this.transition_row = transition_row;
    }

    public void addcell ( transition_cell cell) {
        this.transition_row.add(cell);
    }






}
