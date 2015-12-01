package Backend.LogicaCiudad;

import Backend.BackendController;

/**
 * Enumeracion de las posibles direcciones en las que se puede mover una
 * entidad.
 * 
 * @author Grupo 10
 * 
 */
public enum NombreDireccion {

    Up, Down, Left, Right;

    /**
     * Retorna una direccion aleatoria dentro de la enumeracion.
     * 
     * @return una direccion aleatoria dentro de la enumeracion.
     */
    public static NombreDireccion random() {
        int probability = BackendController.random.nextInt(4);

        switch (probability) {
        case 0:
            return NombreDireccion.Up;
        case 1:
            return NombreDireccion.Down;
        case 2:
            return NombreDireccion.Right;
        default:
            return NombreDireccion.Left;
        }
    }
}