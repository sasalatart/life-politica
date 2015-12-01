package Backend.Informes;

import java.util.ArrayList;
import java.util.HashMap;

import Backend.LogicaCiudad.PartidoPolitico;

/**
 * Contiene el estado de la ejecucion del programa en un momento dado.
 * @author Grupo 10
 *
 */
public class Memento {

    public HashMap<Integer, ArrayList<PartidoPolitico>> estado;

    /**
     * Encapsula un estado dentro del memento.
     * @param estadoAGuardar estado que deseamos encapsular.
     */
    public Memento(HashMap<Integer, ArrayList<PartidoPolitico>> estadoAGuardar) {
        estado = estadoAGuardar;
    }

    /**
     * Extrae el estado encapsulado por el memento.
     * @return el estado encapsulado por el memento.
     */
    public HashMap<Integer, ArrayList<PartidoPolitico>> getSavedState() {
        return estado;
    }
}