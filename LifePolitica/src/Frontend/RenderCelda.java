package Frontend;

import java.awt.Component;
import java.awt.Color;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import Backend.LogicaCiudad.Celda;
import Backend.LogicaCiudad.Muro;
import Backend.LogicaCiudad.Persona;

/**
 * Asigna un color a cada celda de la grilla dependiendo de lo que represente
 * (calles son grises, murallas son negras, y el resto de los colores
 * corresponden a los partidos politicos).
 * 
 * @author Grupo 10
 * 
 */
@SuppressWarnings("serial")
public class RenderCelda extends DefaultTableCellRenderer {

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        Component cell = super.getTableCellRendererComponent(table, value,
                isSelected, hasFocus, row, column);

        if (value instanceof Celda) {
            Celda celda = (Celda) value;

            if (celda.getParent() == null) {
                cell.setBackground(Color.GRAY);
                cell.setForeground(Color.GRAY);
            } else if (celda.getParent() instanceof Muro) {
                if (((Muro) celda.getParent()).isPortal()) {
                    cell.setBackground(Color.WHITE);
                    cell.setForeground(Color.WHITE);
                }
                else {
                    cell.setBackground(Color.BLACK);
                    cell.setForeground(Color.BLACK);
                }
            } else if (celda.getParent() instanceof Persona) {
                switch (((Persona) celda.getParent()).getPartido()) {
                case ComuNAUS:
                    cell.setBackground(Color.GREEN);
                    cell.setForeground(Color.GREEN);
                    break;
                case SoliSoli:
                    cell.setBackground(Color.RED);
                    cell.setForeground(Color.RED);
                    break;
                case ElGremio:
                    cell.setBackground(Color.ORANGE);
                    cell.setForeground(Color.ORANGE);
                    break;
                case Creceres:
                    cell.setBackground(Color.BLUE);
                    cell.setForeground(Color.BLUE);
                    break;
                }
            }
        }

        return cell;
    }
}