package Backend.EventosExternos;

import java.util.List;

import Backend.BackendController;
import Backend.LogicaCiudad.NombrePartido;
import Backend.LogicaCiudad.Persona;

public class ManiobraPolitica implements EventoExterno{

    /**
     * Con una probabilidad del 33%, cambia el partido politico de cada entidad al partido
     * que llevo a cabo la Maniobra Politica.
     */
    @Override
    public synchronized void execute(List<Persona> ciudadanos, NombrePartido partido) {
        for (Persona persona : ciudadanos) {
            if (!persona.getPartido().equals(partido)) {
                double randomNumber = BackendController.random.nextDouble();
                
                if (randomNumber < 0.33){
                    persona.setPartido(partido, false);
                }
            }
        }
    }
}