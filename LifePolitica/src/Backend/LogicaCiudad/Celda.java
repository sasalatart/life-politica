package Backend.LogicaCiudad;

import java.awt.Point;

/**
 * Representa una posicion dentro de la grilla. Es la unidad basica que compone
 * y conoce a cada entidad.
 * 
 * @author Grupo 10
 * 
 */
public class Celda {
    private Point point;
    private Entidad parent;

    public Celda(Point point, Entidad e) {
        this.setPoint(point);
        this.parent = e;
    }

    public Point getPoint() {
        return point;
    }

    public void setPoint(Point point) {
        this.point = point;
    }
    
    public Entidad getParent() {
        return this.parent;
    }
}