package Frontend;

import javax.swing.JTable;
import javax.swing.table.TableColumn;

import Backend.LogicaCiudad.Celda;
import Backend.LogicaCiudad.Ciudad;

public class Tabla extends JTable {

    private static final long serialVersionUID = 1L;
    private final int MAX_ZOOM = 50;
    private final int MIN_ZOOM = 0;
    private final int ZOOM_STEP = 1;
    private int zoom = MIN_ZOOM;
    private ModeloTabla modeloTabla;
    private TableColumn column;

    private static int coordCorrector = 0;
    
    public Tabla(ModeloTabla modeloTabla) {
        this.modeloTabla = modeloTabla;
        this.setModel(this.modeloTabla);
        this.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        this.setTableHeader(null);
        this.setDefaultRenderer(Celda.class, new RenderCelda());
        this.setDimension();
    }

    public void increaseZoom() {
        if (zoom < MAX_ZOOM) {
            zoom += ZOOM_STEP;
            this.setDimension();
        }
    }

    public void reduceZoom() {
        if (zoom > MIN_ZOOM) {
            zoom -= ZOOM_STEP;
            this.setDimension();
        }
    }

    public void shiftRight() {
        int n = Ciudad.getCeldas().length;
        for (int i = n - 1; i >= 1; i--) {
            this.moveColumn(i, i - 1);
        }
        
        coordCorrector--;
    }

    public void shiftLeft() {
        int n = Ciudad.getCeldas().length;
        for (int i = 0; i <= n - 2; i++) {
            this.moveColumn(i, i + 1);
        }
        
        coordCorrector++;
    }
    
    private void setDimension() {
        this.setRowHeight(15 + zoom);

        for (int i = 0; i < modeloTabla.getColumnCount(); i++) {
            column = this.getColumnModel().getColumn(i);
            column.setPreferredWidth(15 + zoom);
        }
    }
    
    public static int getCoordCorrector() {
        return coordCorrector;
    }

    public static void setCoordCorrector(int coordCorrector) {
        Tabla.coordCorrector = coordCorrector;
    }
}