package Backend.LogicaCiudad;

import java.util.concurrent.TimeUnit;

import Backend.BackendController;

public class VaciadorCola implements Runnable {
    
    private boolean enPortal;
    
    private int maxCol;
        
    @Override
    public void run() {
        this.maxCol = Ciudad.getCeldas().length - 1;
        while(true) {
            
            try {
                TimeUnit.MILLISECONDS.sleep(100);
            } catch (InterruptedException e) {
            }
            
            this.enPortal = false;
            
            for (int x = 2; x < 6; x++) {
                for (int y = this.maxCol - 4; y < this.maxCol - 1; y++) {
                    if (Ciudad.getCeldas()[x][y].getParent() != null) {
                        this.enPortal = true;
                    }
                }
            }
            
            if (!Ciudad.getStackShapes().isEmpty() && !this.enPortal) {
                synchronized (BackendController.lock) {
                    Persona persona = new Persona();
                    
                    persona.setPartido(Ciudad.getStackPartidos().get(0), true);
                    persona.setShape(Ciudad.getStackShapes().get(0));
                    persona.setDoc(Ciudad.getStackDocument().get(0));
                    persona.placeComponentes(true);
                    Ciudad.getPersonas().add(persona);
                    
                    Thread thread = new Thread(persona);
                    thread.start();
                    persona.setThread(thread);
                    
                    Ciudad.getStackPartidos().remove(0);
                    Ciudad.getStackShapes().remove(0);
                    Ciudad.getStackDocument().remove(0);
                }
            }
        }
    }   
}