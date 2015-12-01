package Backend.EventosExternos;

import java.util.List;

import Backend.LogicaCiudad.NombrePartido;
import Backend.LogicaCiudad.Persona;

public interface EventoExterno {
    
    /**
     * Ejecuta un evento externo sobre todas las personas pertenecientes al partido en cuestion.
     * @param personas un listado con todas las personas de la ciudad.
     * @param partido partido que lleva a cabo el Evento Externo.
     */
    public void execute(List<Persona> personas, NombrePartido partido); 

}