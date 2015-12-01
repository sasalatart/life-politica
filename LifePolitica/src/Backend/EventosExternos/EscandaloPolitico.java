package Backend.EventosExternos;

import java.util.List;

import Backend.BackendController;
import Backend.LogicaCiudad.NombrePartido;
import Backend.LogicaCiudad.Persona;

public class EscandaloPolitico implements EventoExterno{

    /**
     * Con una probabilidad del 33%, cambia a un partido aleatorio a cada entidad perteneciente
     * al partido politico que sufre el escandalo.
     */
    @Override
    public synchronized void execute(List<Persona> ciudadanos, NombrePartido partido) {
        for (Persona persona : ciudadanos){
            if (persona.getPartido().equals(partido)){
                double randomNumber = BackendController.random.nextDouble();
                
                if (randomNumber < 0.33){
                    persona.setPartido(NombrePartido.random(), false);
                }
            }
        }
    }
}