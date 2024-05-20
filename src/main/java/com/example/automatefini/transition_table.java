package com.example.automatefini;

import java.util.ArrayList;
import java.util.List;

public class transition_table {
    private List<transition_row> trasition_table;
    public transition_table(List<transition_row> trasition_table) {
        super();
        this.trasition_table = trasition_table;

    }
    public transition_table() {
        super();
        trasition_table = new ArrayList<>();
    }
    public List<transition_row> getTrasition_table() {
        return trasition_table;
    }
    public void setTrasition_table(List<transition_row> trasition_table) {
        this.trasition_table = trasition_table;
    }
    public void addrow(transition_row row) {
        this.trasition_table.add(row);
    }
}
