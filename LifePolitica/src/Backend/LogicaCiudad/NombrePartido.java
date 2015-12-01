package Backend.LogicaCiudad;

import Backend.BackendController;
import Backend.ParametrosIniciales;

/**
 * Enumeracion de los nombres de los partidos politicos participantes.
 * 
 * @author Grupo 10
 * 
 */
public enum NombrePartido {

    ComuNAUS(0), SoliSoli(1), ElGremio(2), Creceres(3);

    private int code;

    private NombrePartido(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    /**
     * Retorna el nombre del partido politico correspondiente al indice
     * ingresado.
     * 
     * @param index
     *            indice del partido cuyo nombre se desea obtener.
     * @return el nombre del partido solicitado.
     */
    public static String getNameAndColorByIndex(int index) {
        switch (index) {
        case 0:
            return ComuNAUS.toString() + " (VERDE)";
        case 1:
            return SoliSoli.toString() + " (ROJO)";
        case 2:
            return ElGremio.toString() + " (NARANJO)";
        case 3:
            return Creceres.toString() + " (AZUL)";
        default:
            return ComuNAUS.toString() + " (VERDE)";
        }
    }

    /**
     * Retorna un nombre aleatorio dentro de la enumeracion.
     * 
     * @return un nombre aleatorio dentro de la enumeracion.
     */
    public static NombrePartido random() {
        int numPartidos = ParametrosIniciales.getInstance()
                .getNumeroDePartidos();
        int probability = BackendController.random.nextInt(numPartidos);

        switch (probability) {
        case 0:
            return NombrePartido.ComuNAUS;
        case 1:
            return NombrePartido.SoliSoli;
        case 2:
            return NombrePartido.ElGremio;
        default:
            return NombrePartido.Creceres;
        }
    }
}