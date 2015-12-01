package Frontend;

import javax.swing.table.AbstractTableModel;

import Backend.LogicaCiudad.Celda;

/**
 * Modela graficamente la grilla que representa la ciudad, en base a la matriz
 * de celdas ingresada.
 * 
 * @author Grupo 10
 * 
 */
@SuppressWarnings("serial")
public class ModeloTabla extends AbstractTableModel {

    private String[] columnNames;

    Celda[][] data;

    public ModeloTabla(Celda[][] celdas) {
        this.data = celdas;

        this.columnNames = new String[celdas.length];
    }

    @Override
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public Class getColumnClass(int column) {
        return getValueAt(0, column).getClass();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    @Override
    public int getRowCount() {
        return data.length;
    }

    @Override
    public Object getValueAt(int row, int column) {
        return data[row][column];
    }
}