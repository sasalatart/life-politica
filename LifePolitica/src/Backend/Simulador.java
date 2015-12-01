package Backend;

import java.util.ArrayList;

import Backend.Informes.InformeParcial;
import Backend.LogicaCiudad.Ciudad;
import Backend.LogicaCiudad.NombrePartido;
import Backend.LogicaCiudad.PartidoPolitico;
import Backend.LogicaCiudad.Persona;

public class Simulador {

    public static Ciudad ciudad;

    public static int numIteracion;
    
    public Simulador() {
        numIteracion = 0;
    }

    /**
     * Inicializa la simulacion creando una nueva ciudad.
     */
    public static void iniciarSimulacion() {
        ciudad = new Ciudad();
    }
    
    /**
     * Crea un informe parcial.
     */
    public void onInformeParcialSolicitado() {
        InformeParcial informe = new InformeParcial();

        numIteracion++;
        InformeParcial.GuardarEstadoInforme(numIteracion, guardarEstado());
        informe.publishInfo();
    }

    public void generarInforme() {
        onInformeParcialSolicitado();
    }

    /**
     * Analiza las estadisticas de la simulacion en un momento dado.
     * @return una lista con los partidos politicos actualizados.
     */
    public static ArrayList<PartidoPolitico> guardarEstado() {
        ArrayList<Persona> personas = Ciudad.getPersonas();
        ArrayList<PartidoPolitico> partidosEnCiudad = new ArrayList<PartidoPolitico>();

        int numPartidos = ParametrosIniciales.getInstance().getNumeroDePartidos();

        PartidoPolitico p1 = new PartidoPolitico(NombrePartido.ComuNAUS);
        PartidoPolitico p2 = new PartidoPolitico(NombrePartido.SoliSoli);
        PartidoPolitico p3 = new PartidoPolitico(NombrePartido.ElGremio);
        PartidoPolitico p4 = new PartidoPolitico(NombrePartido.Creceres);

        switch (numPartidos) {
        case 1:
            partidosEnCiudad.add(p1);
            break;
        case 2:
            partidosEnCiudad.add(p1);
            partidosEnCiudad.add(p2);
            break;
        case 3:
            partidosEnCiudad.add(p1);
            partidosEnCiudad.add(p2);
            partidosEnCiudad.add(p3);
            break;
        default:
            partidosEnCiudad.add(p1);
            partidosEnCiudad.add(p2);
            partidosEnCiudad.add(p3);
            partidosEnCiudad.add(p4);
            break;
        }

        for (Persona per : personas) {
            if (per.getPartido().equals(p1.getNombre())) {
                p1.setAdherentes(p1.getAdherentes() + 1);
            } else if (per.getPartido().equals(p2.getNombre())) {
                p2.setAdherentes(p2.getAdherentes() + 1);
            } else if (per.getPartido().equals(p3.getNombre())) {
                p3.setAdherentes(p3.getAdherentes() + 1);
            } else if (per.getPartido().equals(p4.getNombre())) {
                p4.setAdherentes(p4.getAdherentes() + 1);
            }
        }

        return partidosEnCiudad;
    }
}