package Backend.Informes;

import java.util.ArrayList;

/**
 * Almacena los estados de la simulacion en distintos momentos.
 * @author Grupo 10
 *
 */
public class Caretaker {

    private ArrayList<Memento> savedStates = new ArrayList<Memento>();

    /**
     * Agrega un estado al listado de Mementos (estados) del programa.
     * @param memento estado que se grabara.
     */
    public void addMemento(Memento memento) {
        getSavedStates().add(memento);
    }

    /**
     * Retorna un estado del listado de estados pasados.
     * @param index indice del estado que se desea buscar.
     * @return el estado solicitado.
     */
    public Memento getMemento(int index) {
        return getSavedStates().get(index);
    }

    public ArrayList<Memento> getSavedStates() {
        return savedStates;
    }

    public void setSavedStates(ArrayList<Memento> savedStates) {
        this.savedStates = savedStates;
    }
}