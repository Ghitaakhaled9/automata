package com.example.automatefini;
import java.util.ArrayList;
import java.util.List;

public class interNode {
    private List <listeNodes> interStates;





    public interNode() {
        super();
        interStates=new ArrayList<>();
    }





    public List<listeNodes> getInterStates() {
        return interStates;
    }





    public void setInterStates(List<listeNodes> interStates) {
        this.interStates = interStates;
    }



    public void addList(listeNodes list) {
        interStates.add(list);
    }





}
