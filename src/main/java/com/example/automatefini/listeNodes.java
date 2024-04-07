package com.example.automatefini;
import java.util.ArrayList;
import java.util.List;

public class listeNodes {
    private List<State> listNodes;
    public listeNodes() {
        super();
        listNodes= new ArrayList<>();
    }

    public listeNodes(List<State> listNodes) {
        super();
        this.listNodes = listNodes;
    }

    public List<State> getListNodes() {
        return listNodes;
    }

    public void setListNodes(List<State> listNodes) {
        this.listNodes = listNodes;
    }

    public void addNode(State node) {
        listNodes.add(node);
    }

}

