package Backend.LogicaCiudad;

import java.util.LinkedList;

import org.w3c.dom.Document;

/**
 * Representacion de las entidades, las cuales estan compuestas por un listado
 * de celdas.
 * 
 * @author Grupo 10
 * 
 */
public abstract class Entidad {
    
    protected Document doc = null;
    protected LinkedList<Celda> componentes;

    public Document getDoc() {
        return doc;
    }

    public void setDoc(Document doc) {
        this.doc = doc;
    }
    
    public LinkedList<Celda> getComponentes() {
        return componentes;
    }
}