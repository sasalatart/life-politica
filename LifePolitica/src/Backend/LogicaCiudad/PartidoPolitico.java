package Backend.LogicaCiudad;

/**
 * Representa un partido politico. Tiene un nombre y una cantidad de adherentes.
 * 
 * @author Grupo 10
 * 
 */
public class PartidoPolitico {

    private NombrePartido nombre;
    private int adherentes;

    public PartidoPolitico(NombrePartido nombre) {
        this.nombre = nombre;
    }

    public NombrePartido getNombre() {
        return nombre;
    }

    public int getAdherentes() {
        return adherentes;
    }

    public void setAdherentes(int adherentes) {
        this.adherentes = adherentes;
    }

    public Integer getValorPrimario() {
        return null;
    }
}