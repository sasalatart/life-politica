package Backend.LogicaCiudad;

import java.awt.Point;
import java.util.LinkedList;

/**
 * Representacion de los muros.
 * 
 * @author Grupo 10
 * 
 */
public class Muro extends Entidad {

    private boolean isPortal;
    
    /**
     * Situa al muro desde un punto de partida, con un largo definido, y en una
     * direccion dada.
     * 
     * @param start
     *            punto de partida del muro.
     * @param length
     *            longitud del muro.
     * @param direccion
     *            direccion en la que se situira el muro.
     */
    public Muro(Point start, int length, NombreDireccion direccion, boolean isPortal) {
        this.isPortal = isPortal;
        
        this.componentes = new LinkedList<Celda>();

        Celda[][] grilla = Ciudad.getCeldas();

        for (int i = 0; i < length; i++) {
            Celda celda = new Celda(start, this);
            grilla[start.x][start.y] = celda;
            this.componentes.add(celda);

            start = Ciudad.movePoint(start, direccion);
        }
    }

    public boolean isPortal() {
        return isPortal;
    }
}