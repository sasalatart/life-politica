package Backend.LogicaCiudad;

import java.util.concurrent.TimeUnit;

import Backend.BackendController;
import Backend.ParametrosIniciales;
import Frontend.Display;

public class DisplayUpdater implements Runnable {
    
    BackendController backendController;
    
    public DisplayUpdater(BackendController backendController) {
        this.backendController = backendController;
    }
    
    @Override
    public void run() {
        
        long startTime = this.backendController.getStartTime();
        long time = ParametrosIniciales.getInstance().getTime();
        
        boolean finished = false;
        
        while(true){
            
            if (time != 0) {
                if (!Persona.isPaused()) {
                    this.backendController.setCurrentTime(time + this.backendController.getTimePaused() - (System.currentTimeMillis() - startTime) / 1000);
                    this.backendController.getDisplay().getActualTime().setText("Tiempo: " + this.backendController.getCurrentTime());
                }
                
                finished = this.backendController.getCurrentTime() < 1;
                
                if (finished) {
                    Ciudad.stopThreads();
                    this.backendController.guardarEstado();
                    this.backendController.solicitarInforme();
                    break;
                }   
            }
            else {
                this.backendController.getDisplay().getActualTime().setText("Tiempo: INFINITO");
            }
            
            try {
                TimeUnit.MILLISECONDS.sleep(75);
            } catch (InterruptedException e) {
            }
            
            Display.getModeloTabla().fireTableDataChanged();
        }
    }
}