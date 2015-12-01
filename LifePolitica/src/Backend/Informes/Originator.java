package Backend.Informes;

import java.util.ArrayList;
import java.util.HashMap;

import Backend.LogicaCiudad.PartidoPolitico;

public class Originator {

    public HashMap<Integer, ArrayList<PartidoPolitico>> estado;

    public void set(HashMap<Integer, ArrayList<PartidoPolitico>> state) {
        this.estado = state;
    }

    public Memento saveToMemento() {
        return new Memento(this.estado);
    }

    public void restoreFromMemento(Memento memento) {
        this.estado = memento.getSavedState();
    }
}