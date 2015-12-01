package Backend.LogicaCiudad;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.w3c.dom.Document;

import Backend.BackendController;
import Backend.ParametrosIniciales;

/**
 * Representa el conjunto de personas y muros que componen la ciudad.
 * 
 * @author Grupo 10
 * 
 */
public class Ciudad {

    private static Celda[][] celdas;
    private static ArrayList<Persona> personas = new ArrayList<Persona>();
    private static ArrayList<NombreShape> stackShapes = new ArrayList<NombreShape>();
    private static ArrayList<NombrePartido> stackPartidos = new ArrayList<NombrePartido>();
    private static ArrayList<Document> stackDocument = new ArrayList<Document>();

    private Thread threadVaciadorCola;
    
    public Ciudad() {
        celdas = new Celda[40][40];
        
        this.crearCalles();
        this.crearPortales();
        crearPersonas(false, null, null, null);
        
        startThreads();
        
        this.threadVaciadorCola = new Thread(new VaciadorCola());
        this.threadVaciadorCola.start();
    }

    /**
     * Se crea la poblacion de la ciudad, de acuerdo a la distribucion de
     * partidos politicos ingresados por el usuario.
     */
    public synchronized static void crearPersonas(boolean migrated, NombreShape shape, NombrePartido partido, Document doc) {
                
        if (migrated) {
            stackShapes.add(shape);
            stackPartidos.add(partido);
            stackDocument.add(doc);
        }
        else {
            HashMap<Integer, NombrePartido> partidos = ParametrosIniciales.getInstance().getPartidos();
            HashMap<Integer, Integer> distribucion = ParametrosIniciales.getInstance().getPorcentajes();

            for (int id : partidos.keySet()) {
                for (int j = 0; j < distribucion.get(id); j++) {
                    Persona persona = new Persona();
                    persona.setPartido(partidos.get(id), migrated);
                    persona.placeComponentes(migrated);
                    personas.add(persona);
                }
            }
        }
    }
    
    /**
     * Crea y ubica en la ciudad muros de largo variable, y en posiciones
     * aleatorias.
     */
    @SuppressWarnings("unused")
    public void crearPortales() {
        
        int row = 1;
        int col = 1;
        int size = 5;
        Muro muro;
                
        muro = new Muro(new Point(row, col), size, NombreDireccion.Down, true);
        muro = new Muro(new Point(row, col), size, NombreDireccion.Right, true);
        col =+ size;
        muro = new Muro(new Point(row, col), size, NombreDireccion.Down, true);
        
        col = celdas.length - 1 - size;
        muro = new Muro(new Point(row, col), size, NombreDireccion.Down, true);
        muro = new Muro(new Point(row, col), size, NombreDireccion.Right, true);
        col += size - 1;
        muro = new Muro(new Point(row, col), size, NombreDireccion.Down, true);
    }

    @SuppressWarnings("unused")
    public static void crearMuro(int row, int col) {
        Muro muro = new Muro(new Point(row, col), 1, NombreDireccion.random(), false);
    }
    
    /**
     * Rellena todos los espacios que no contengan a una persona o muro con
     * calles.
     */
    public void crearCalles() {
        for (int i = 0; i < celdas.length; i++) {
            for (int j = 0; j < celdas[0].length; j++) {
                if (celdas[i][j] == null) {
                    celdas[i][j] = new Celda(new Point(i, j), null);
                }
            }
        }
    }

    /**
     * Mueve una entidad en la direccion senalada.
     * 
     * @param entidad
     *            entidad que se movera.
     * @param direccion
     *            direccion en la cual se movera la entidad.
     */
    public static synchronized void moverEntidad(Entidad entidad,
            NombreDireccion direccion) {

        List<Celda> componentesEntidad = entidad.componentes;
        List<Celda> celdasVecinasDireccion = new LinkedList<Celda>();

        for (Celda celda : componentesEntidad) {
            Celda celdaVecina = getCeldaVecina(celda, direccion);
            if (celdaVecina.getParent() != null) {
                celdasVecinasDireccion.add(celdaVecina);
            }
        }

        celdasVecinasDireccion.removeAll(componentesEntidad);

        if (celdasVecinasDireccion.isEmpty()) {
            for (Celda celda : componentesEntidad) {
                moverCelda(celda, direccion);
            }
        }

    }

    /**
     * Mueve una celda en la direccion senalada.
     * 
     * @param entidad
     *            entidad que se movera.
     * @param direccion
     *            direccion en la cual se movera la celda.
     */
    private static void moverCelda(Celda celda, NombreDireccion direccion) {
        Point oldPoint = celda.getPoint();
        Point newPoint = movePoint(oldPoint, direccion);
        celda.setPoint(newPoint);

        int oldX = oldPoint.x;
        int oldY = oldPoint.y;
        int newX = newPoint.x;
        int newY = newPoint.y;

        celdas[newX][newY] = celda;
        if (celdas[oldX][oldY].equals(celda)) {
            celdas[oldX][oldY] = new Celda(new Point(oldX, oldY), null);
        }
    }

    /**
     * Traslada un punto en la direccion senalada.
     * 
     * @param point
     *            punto a trasladar.
     * @param direccion
     *            direccion en la cual se trasladara el punto.
     * @return el punto trasladado.
     */
    public static Point movePoint(Point point, NombreDireccion direccion) {
        int col = point.y;
        int row = point.x;
        int maxRow = celdas[0].length - 1;
        int maxCol = celdas.length - 1;

        switch (direccion) {
        case Up:
            if (row == 0) {
                row = maxCol;
            } else {
                row--;
            }
            break;

        case Down:
            if (row == maxCol) {
                row = 0;
            } else {
                row++;
            }
            break;

        case Right:
            if (col == maxRow) {
                col = 0;
            } else {
                col++;
            }
            break;

        case Left:
            if (col == 0) {
                col = maxRow;
            } else {
                col--;
            }
            break;
        }

        return new Point(row, col);
    }

    /**
     * Retorna la celda vecina a la celda en cuestion.
     * 
     * @param celda
     *            celda cuyo vecino deseamos obtener.
     * @param direccion
     *            direccion del vecino que deseamos obtener.
     * @return la celda vecina a la celda en cuestion.
     */
    public static Celda getCeldaVecina(Celda celda, NombreDireccion direccion) {
        Point puntoVecino = movePoint(celda.getPoint(), direccion);
        int x = puntoVecino.x;
        int y = puntoVecino.y;
        return celdas[x][y];
    }

    /**
     * Revisa las celdas vecinas de una Persona y la cambia de partido.
     * 
     * @param persona
     *            potencial persona que cambiara de partido.
     */
    public static synchronized void revisarVecinosPersona(Persona persona) {
        Set<Celda> celdasVecinasEntidad = celdasVecinasPersona(persona);

        double totalCeldasVecinas = celdasVecinasEntidad.size();

        HashMap<NombrePartido, Integer> mapPartidoCantidad = contarPartidos(celdasVecinasEntidad);

        NombrePartido nuevoPartido = obtenerNuevoPartido(mapPartidoCantidad, totalCeldasVecinas, persona);

        if (nuevoPartido.getCode() != persona.getNumeroPartido()) {
            persona.setPartido(nuevoPartido, false);
        }
    }

    /**
     * Calcula el partido al cual se debe cambiar la Persona.
     * 
     * @param mapPartidoCantidad
     *            cantidad de entidades vecinas adherentes a cada partido.
     * @param totalVecinos
     *            cantidad de entidades vecinas.
     * @param persona
     *            persona que sera analizada para que cambie de partido.
     * @return el partido al cual se debe cambiar la persona.
     */
    private static NombrePartido obtenerNuevoPartido(HashMap<NombrePartido, Integer> mapPartidoCantidad, double totalVecinos, Persona persona) {

        NombrePartido partidoActual = persona.getPartido();
        double porcentaje;
        porcentaje = mapPartidoCantidad.get(partidoActual) / totalVecinos;
        if (porcentaje >= 0.75) {
            persona.setTicksPartidoBloqueado(30);
            return ParametrosIniciales.getInstance().getPartidoRandom();
        }

        for (NombrePartido partido : mapPartidoCantidad.keySet()) {
            if (!partido.equals(partidoActual)) {
                porcentaje = mapPartidoCantidad.get(partido) / totalVecinos;
                if (porcentaje >= 0.25 && BackendController.random.nextInt(2) == 0) {
                    persona.setTicksPartidoBloqueado(30);
                    return partido;
                }
            }
        }

        return partidoActual;
    }

    /**
     * Retorna un HashMap con la cantidad de partidos vecinos.
     * 
     * @param vecinosEntidad
     *            un listado de todos los vecinos de una entidad.
     * @return un HashMap con la cantidad de partidos vecinos.
     */
    private static HashMap<NombrePartido, Integer> contarPartidos(Set<Celda> vecinosEntidad) {

        HashMap<NombrePartido, Integer> mapPartidoCantidad = new HashMap<NombrePartido, Integer>();

        for (NombrePartido partido : ParametrosIniciales.getInstance().getPartidos().values()) {
            mapPartidoCantidad.put(partido, 0);
        }

        for (Celda vecino : vecinosEntidad) {
            if (vecino.getParent() instanceof Persona) {
                Persona personaVecina = (Persona) vecino.getParent();
                NombrePartido partidoVecino = personaVecina.getPartido();
                int cuentaActual = mapPartidoCantidad.get(partidoVecino);
                mapPartidoCantidad.put(partidoVecino, cuentaActual + 1);
            }
        }

        return mapPartidoCantidad;
    }

    /**
     * Retorna las celdas vecinas de una Persona.
     * 
     * @param persona
     *            persona cuyos vecinos deseamos obtener.
     * @return las celdas vecinas de una Persona.
     */
    private static Set<Celda> celdasVecinasPersona(Persona persona) {
        Set<Celda> vecinos = new HashSet<Celda>();

        for (Celda componente : persona.componentes) {
            vecinos.addAll(celdasVecinasCelda(componente));
        }

        vecinos.removeAll(persona.componentes);

        return vecinos;
    }

    /**
     * Retorna las celdas vecinas de una Celda.
     * 
     * @param componente
     *            celda cuyos vecinos deseamos obtener.
     * @return las celdas vecinas de una Celda.
     */
    private static Set<Celda> celdasVecinasCelda(Celda componente) {
        Set<Celda> vecinos = new HashSet<Celda>();

        for (NombreDireccion dir : NombreDireccion.values()) {
            vecinos.add(getCeldaVecina(componente, dir));
        }

        return vecinos;
    }

    public static Celda[][] getCeldas() {
        return celdas;
    }

    public static ArrayList<Persona> getPersonas() {
        return personas;
    }
    
    public static ArrayList<NombreShape> getStackShapes() {
        return stackShapes;
    }

    public static ArrayList<NombrePartido> getStackPartidos() {
        return stackPartidos;
    }
    
    public static ArrayList<Document> getStackDocument() {
        return stackDocument;
    }
    
    public static void killPerson(Persona persona) {
        synchronized (BackendController.lock) {
            personas.remove(persona);
            
            persona.setPartido(null, false);
            
            for (Celda celda : persona.componentes) {
                celdas[celda.getPoint().x][celda.getPoint().y] = new Celda(new Point(celda.getPoint().x, celda.getPoint().y), null);
            }
        }
    }
    
    public static void startThreads() {
        for (Persona persona : personas) {
            Thread thread = new Thread(persona);
            thread.start();
            persona.setThread(thread);
        }
    }
    
    @SuppressWarnings("deprecation")
    public static void stopThreads() {
        for (Persona persona : personas) {
            persona.getThread().stop();
        }
    }
}