package Frontend;

import java.awt.Font;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.CategoryDataset;

/**
 * Interfaz que despliega graficos de la evolucion de la distribucion de los partidos.
 * @author Grupo 10
 *
 */
@SuppressWarnings("serial")
public class InformeFrame extends JFrame{
    
    private JPanel finalPanel;

    private ChartPanel chartpanel;
    
    private JFreeChart grafica;
    
    private JLabel headerLabel;
    
    public void pintar(CategoryDataset dataset){        
        this.headerLabel = new JLabel("Informe");
        this.headerLabel.setFont(new Font("Tahoma", Font.BOLD, 24));
        
        this.grafica = ChartFactory.createBarChart("Movimientos", "Iteraciones", "Numero de Adherentes", dataset);
        
        this.chartpanel = new ChartPanel(grafica);
        
        this.finalPanel = new JPanel();
        this.finalPanel.setLayout(new BoxLayout(this.finalPanel, BoxLayout.Y_AXIS));
        this.finalPanel.add(this.headerLabel);
        this.finalPanel.add(this.chartpanel);
        
        this.getContentPane().add(this.finalPanel);

        this.setTitle("LifePolitica");
        this.setResizable(true);
        this.pack();

        this.setLocationRelativeTo(null);
        this.setVisible(true);   
    }
}
