package Backend;

import java.util.HashMap;
import java.util.Observable;

import Backend.LogicaCiudad.NombrePartido;

/**
 * Singleton que contiene a los parametros iniciales de la simulacion, y los
 * actualiza a medida que ella corre.
 * 
 * @author Grupo 10
 * 
 */
public class ParametrosIniciales extends Observable {

    private static volatile ParametrosIniciales instance = null;
    private int velocidad;
    private long time;

    private HashMap<Integer, NombrePartido> mapIDPartido = new HashMap<Integer, NombrePartido>();
    private HashMap<Integer, Integer> mapIDPorcentaje = new HashMap<Integer, Integer>();

    private ParametrosIniciales() {

    }

    /**
     * Retorna la instancia unica de ParametrosIniciales.
     * 
     * @return la instancia unica de ParametrosIniciales.
     */
    public static ParametrosIniciales getInstance() {
        if (instance == null) {
            synchronized (ParametrosIniciales.class) {
                if (instance == null) {
                    instance = new ParametrosIniciales();
                }
            }
        }

        return instance;
    }

    /**
     * Valida los parametros ingresados por el usuario.
     * 
     * @param velocidad
     *            La velocidad ingresada del usuario.
     * @param numPartidos
     *            El numero de partidos ingresado del usuario.
     * @param porPartidos
     *            Distribucion porcentual de los partidos.
     * @return TRUE si los valores ingresados son validos, FALSE si no.
     */
    public boolean setParametros(int velocidad, int time, int numPartidos, HashMap<Integer, Integer> porPartidos) {

        boolean velocidadMal = velocidad < 0 || velocidad > 100;
        boolean timeMal = time < 0;
        boolean numPartidosMal = numPartidos < 1 || numPartidos > 4;
        boolean porPartidosMal = porPartidos.size() != numPartidos;
        boolean mal = velocidadMal || timeMal || numPartidosMal || porPartidosMal;
        
        if (mal) {
            return false;
        }

        for (int i = 0; i < porPartidos.size(); i++) {
            int porcentaje = porPartidos.get(i);
            if (porcentaje < 0 || porcentaje > 100)
                return false;
        }

        int sumaPorcentajes = 0;
        for (int i = 0; i < porPartidos.size(); i++) {
            sumaPorcentajes += porPartidos.get(i);
        }
        if (sumaPorcentajes != 100) {
            return false;
        }

        for (int i = 0; i < numPartidos; i++) {
            this.mapIDPartido.put(i, NombrePartido.values()[i]);
        }

        this.velocidad = velocidad;
        this.time = time;
        this.mapIDPorcentaje = porPartidos;

        return true;
    }

    /**
     * Actualiza la distribucion porcentual de los partidos.
     * 
     * @param partido
     *            El numero corrispondiente al partido.
     * @param porcentaje
     *            El porcentaje del partido.
     */
    public synchronized void setPorcentaje(int partido, int porcentaje) {
        getPorcentajes().put(partido, porcentaje);
        this.setChanged();
        this.notifyObservers(this.getPorcentajes());
    }

    public int getVelocidad() {
        return velocidad;
    }
    
    public long getTime() {
        return time;
    }

    public int getNumeroDePartidos() {
        return mapIDPartido.size();
    }

    public HashMap<Integer, NombrePartido> getPartidos() {
        return this.mapIDPartido;
    }

    public synchronized HashMap<Integer, Integer> getPorcentajes() {
        return mapIDPorcentaje;
    }

    public NombrePartido getPartidoRandom() {
        int r = BackendController.random.nextInt(getNumeroDePartidos());
        return mapIDPartido.get(r);
    }

    public void setPartidos(HashMap<Integer, NombrePartido> mapIDPartido) {
        this.mapIDPartido = mapIDPartido;
    }
}