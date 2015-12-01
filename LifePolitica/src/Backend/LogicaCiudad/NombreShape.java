package Backend.LogicaCiudad;

import Backend.BackendController;

public enum NombreShape {
    Square, BigSquare, l1, l2, l3, l4, i1, i2;

    public static NombreShape random() {
        int probability = BackendController.random.nextInt(7);

        switch (probability) {
        case 0:
            return NombreShape.Square;
        case 1:
            return NombreShape.l1;
        case 2:
            return NombreShape.l2;
        case 3:
            return NombreShape.l3;
        case 4:
            return NombreShape.l4;
        case 5:
            return NombreShape.i1;
        default:
            return NombreShape.i2;
        }
    }
}