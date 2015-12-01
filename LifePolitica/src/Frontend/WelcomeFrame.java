package Frontend;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;

import java.util.ArrayList;
import java.awt.FlowLayout;

import javax.swing.SwingConstants;

import Backend.ParametrosIniciales;
import Backend.Informes.InformeParcial;
import Backend.LogicaCiudad.NombrePartido;
import Backend.LogicaCiudad.PartidoPolitico;

import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Component;
import javax.swing.Box;

/**
 * Formulario para ingresar los parametros iniciales de la simulacion.
 * 
 * @author Grupo 10
 * 
 */
@SuppressWarnings("serial")
public class WelcomeFrame extends JFrame implements ActionListener {

    private JPanel finalPanel;
    private JPanel headerPanel;
    private JPanel velocidadPanel;
    private JPanel timerPanel;
    private JPanel movimientosPanel;
    private JPanel buttonsPanel;

    private JLabel headerLabel;
    private JLabel velocidadLabel;
    private JLabel timerLabel;
    private JLabel movimientosLabel;
    private JLabel mov1Label;
    private JLabel mov2Label;
    private JLabel mov3Label;
    private JLabel mov4Label;

    private JTextField velocidadTxt;
    private JTextField timerTxt;
    private JTextField mov1Txt;
    private JTextField mov2Txt;
    private JTextField mov3Txt;
    private JTextField mov4Txt;

    private JPanel mov1Panel;
    private JPanel mov2Panel;
    private JPanel mov3Panel;
    private JPanel mov4Panel;

    private JComboBox<String> movimientosBox;

    private JButton okButton;

    private Color backGroundColor;
    private Color buttonsColor;
    private Component verticalStrut_1;
    private Component verticalStrut_2;
    private Component verticalStrut_0;

    public WelcomeFrame() {

        this.backGroundColor = new Color(175, 205, 240);
        this.buttonsColor = new Color(100, 150, 210);

        this.headerLabel = new JLabel("Ingresar Parametros Iniciales");
        this.headerLabel.setFont(new Font("Tahoma", Font.BOLD, 14));
        this.headerPanel = new JPanel();
        this.headerPanel.setBackground(this.backGroundColor);
        this.headerPanel.add(this.headerLabel);
        
        this.velocidadLabel = new JLabel("Velocidad Base (1 - 100):");
        this.velocidadLabel.setHorizontalAlignment(SwingConstants.CENTER);
        this.velocidadTxt = new JTextField();
        this.velocidadTxt.setPreferredSize(new Dimension(50, 25));
        this.velocidadPanel = new JPanel();
        this.velocidadPanel.setLayout(new GridLayout(0, 2, 0, 0));
        this.velocidadPanel.setBackground(this.backGroundColor);
        this.velocidadPanel.add(this.velocidadLabel);
        this.velocidadPanel.add(this.velocidadTxt);
        
        this.timerLabel = new JLabel("<html><center>Duracion de la Simulacion (s):<br />[Ingresar 0 para Duracion Infinita]</center></html>");
        this.timerLabel.setHorizontalAlignment(SwingConstants.CENTER);
        this.timerTxt = new JTextField();
        this.timerTxt.setPreferredSize(new Dimension(50, 25));
        this.timerPanel = new JPanel();
        this.timerPanel.setBackground(this.backGroundColor);
        this.timerPanel.setLayout(new GridLayout(0, 2, 0, 0));
        this.timerPanel.add(this.timerLabel);
        this.timerPanel.add(this.timerTxt);
        
        this.movimientosLabel = new JLabel("N° Movimientos Politicos:");
        this.movimientosLabel.setHorizontalAlignment(SwingConstants.CENTER);
        this.movimientosBox = new JComboBox<String>(new String[] { "1", "2", "3", "4" });
        this.movimientosBox.setPreferredSize(new Dimension(50, 25));
        this.movimientosBox.addActionListener(this);
        this.movimientosPanel = new JPanel();
        this.movimientosPanel.add(this.movimientosLabel);
        this.movimientosPanel.add(this.movimientosBox);
        this.movimientosPanel.setBackground(this.backGroundColor);
        this.movimientosPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        
        this.okButton = new JButton("OK");
        this.okButton.setBackground(this.buttonsColor);
        this.buttonsPanel = new JPanel();
        this.buttonsPanel.setBackground(this.backGroundColor);
        this.buttonsPanel.add(this.okButton);
        
        this.mov1Label = new JLabel("Porcentaje Movimiento 1:");
        this.mov1Txt = new JTextField();
        this.mov1Txt.setPreferredSize(new Dimension(200, 25));
        this.mov1Panel = new JPanel();
        this.mov1Panel.setBackground(this.backGroundColor);
        this.mov1Panel.add(this.mov1Label);
        this.mov1Panel.add(this.mov1Txt);
        
        this.mov2Label = new JLabel("Porcentaje Movimiento 2:");
        this.mov2Txt = new JTextField();
        this.mov2Txt.setPreferredSize(new Dimension(200, 25));
        this.mov2Panel = new JPanel();
        this.mov2Panel.setBackground(this.backGroundColor);
        this.mov2Panel.add(this.mov2Label);
        this.mov2Panel.add(this.mov2Txt);
        this.mov2Panel.setVisible(false);

        this.mov3Label = new JLabel("Porcentaje Movimiento 3:");
        this.mov3Txt = new JTextField();
        this.mov3Txt.setPreferredSize(new Dimension(200, 25));
        this.mov3Panel = new JPanel();
        this.mov3Panel.setBackground(this.backGroundColor);
        this.mov3Panel.add(this.mov3Label);
        this.mov3Panel.add(this.mov3Txt);
        this.mov3Panel.setVisible(false);
        
        this.mov4Label = new JLabel("Porcentaje Movimiento 4:");
        this.mov4Txt = new JTextField();
        this.mov4Txt.setPreferredSize(new Dimension(200, 25));
        this.mov4Panel = new JPanel();
        this.mov4Panel.setBackground(this.backGroundColor);
        this.mov4Panel.add(this.mov4Label);
        this.mov4Panel.add(this.mov4Txt);
        this.mov4Panel.setVisible(false);

        this.verticalStrut_0 = Box.createVerticalStrut(20);
        this.verticalStrut_1 = Box.createVerticalStrut(10);
        this.verticalStrut_2 = Box.createVerticalStrut(10);
        
        this.finalPanel = new JPanel();
        this.finalPanel.setLayout(new BoxLayout(this.finalPanel, BoxLayout.Y_AXIS));
        this.finalPanel.add(this.headerPanel);      
        this.finalPanel.add(this.verticalStrut_0);
        this.finalPanel.add(this.velocidadPanel);
        this.finalPanel.add(this.verticalStrut_1);
        this.finalPanel.add(this.timerPanel);
        this.finalPanel.add(this.verticalStrut_2);
        this.finalPanel.add(this.movimientosPanel);
        this.finalPanel.add(this.mov1Panel);
        this.finalPanel.add(this.mov2Panel);
        this.finalPanel.add(this.mov3Panel);
        this.finalPanel.add(this.mov4Panel);
        this.finalPanel.add(this.buttonsPanel);
        this.finalPanel.setBackground(this.backGroundColor);
        
        this.getContentPane().add(this.finalPanel);

        this.setTitle("LifePolitica");
        this.getRootPane().setDefaultButton(this.okButton);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.pack();

        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    /**
     * Retorna la velocidad insertada por el usuario.
     * 
     * @return La velocidad
     */
    public int getVelocidad() {
        try {
            return Integer.parseInt(this.velocidadTxt.getText());
        } catch (Exception e) {
            return 0;
        }

    }

    /**
     * Retorna el tiempo ingresado por el usuario.
     * 
     * @return El tiempo
     */
    public int getTime() {
        try {
            return Integer.parseInt(this.timerTxt.getText());
        } catch (Exception e) {
            return 0;
        }

    }

    /**
     * Retorna el numero de movimientos ingresado del usuario.
     * 
     * @return El numero de movimientos elegido.
     */
    public int getPartidos() {
        try {
            return Integer.parseInt(this.movimientosBox.getSelectedItem()
                    .toString());
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * Retorna el porcentaje del movimiento 1, ingresado por el usuario.
     * 
     * @return El porcentaje del movimiento 1.
     */
    public int getPart1() {
        try {
            if (!this.mov1Txt.getText().isEmpty()) {
                return Integer.parseInt(this.mov1Txt.getText());
            } else {
                return 0;
            }
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * Retorna el porcentaje del movimiento 2, ingresado por el usuario.
     * 
     * @return El porcentaje del movimiento 2.
     */
    public int getPart2() {
        try {
            if (!mov2Txt.getText().isEmpty()) {
                return Integer.parseInt(this.mov2Txt.getText());
            } else {
                return 0;
            }
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * Retorna el porcentaje del movimiento 3, ingresado por el usuario.
     * 
     * @return El porcentaje del movimiento 3.
     */
    public int getPart3() {
        try {
            if (!this.mov3Txt.getText().isEmpty()) {
                return Integer.parseInt(this.mov3Txt.getText());
            } else {
                return 0;
            }
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * Retorna el porcentaje del movimiento 4, ingresado por el usuario.
     * 
     * @return El porcentaje del movimiento 4.
     */
    public int getPart4() {
        try {
            if (!this.mov4Txt.getText().isEmpty()) {
                return Integer.parseInt(this.mov4Txt.getText());
            } else {
                return 0;
            }
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * Retorna los porcentajes de cada movimiento, ingresados por el usuario.
     * 
     * @return Un HashMap que mapea el ID de cada movimiento con su porcentaje.
     */
    public HashMap<Integer, Integer> getValues() {
        int[] arrayRefVar = { this.getPart1(), this.getPart2(),
                this.getPart3(), this.getPart4() };

        HashMap<Integer, Integer> values = new HashMap<Integer, Integer>();

        for (int i = 0; i < getPartidos(); i++) {
            values.put(i, arrayRefVar[i]);
        }

        int numPartidos = ParametrosIniciales.getInstance()
                .getNumeroDePartidos();

        final PartidoPolitico p1 = new PartidoPolitico(NombrePartido.ComuNAUS);
        final PartidoPolitico p2 = new PartidoPolitico(NombrePartido.SoliSoli);
        final PartidoPolitico p3 = new PartidoPolitico(NombrePartido.ElGremio);
        final PartidoPolitico p4 = new PartidoPolitico(NombrePartido.Creceres);

        ArrayList<PartidoPolitico> partidosEnCiudad = new ArrayList<PartidoPolitico>();

        switch (numPartidos) {
        case 1:
            partidosEnCiudad.add(p1);
            break;

        case 2:
            partidosEnCiudad.add(p1);
            partidosEnCiudad.add(p2);
            break;

        case 3:
            partidosEnCiudad.add(p1);
            partidosEnCiudad.add(p2);
            partidosEnCiudad.add(p3);
            break;

        default:
            partidosEnCiudad.add(p1);
            partidosEnCiudad.add(p2);
            partidosEnCiudad.add(p3);
            partidosEnCiudad.add(p4);
            break;
        }

        p1.setAdherentes(this.getPart1());
        p2.setAdherentes(this.getPart2());
        p3.setAdherentes(this.getPart3());
        p4.setAdherentes(this.getPart4());

        InformeParcial.GuardarEstadoInforme(0, partidosEnCiudad);

        return values;
    }

    public JButton getButton() {
        return this.okButton;
    }

    /**
     * Borra todos los valores ingresados por el usuario, en caso de que alguno
     * de que alguno de ellos sea invalido.
     */
    public void resetValues() {
        this.velocidadTxt.setText("");
        this.mov1Txt.setText("");
        this.mov2Txt.setText("");
        this.mov3Txt.setText("");
        this.mov4Txt.setText("");
    }

    /**
     * Muestra una ventana de error si el usuario no ingresa valores correctos.
     * 
     * @param message
     *            El mensaje de error
     */
    public void showError(String message) {
        new UIManager();
        UIManager.put("OptionPane.background", backGroundColor);
        UIManager.put("Panel.background", backGroundColor);
        JOptionPane.showMessageDialog(finalPanel, message, "Error",
                JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Ajusta el panel de porcentajes de cada partido segun la cantidad de
     * partidos participantes.
     */
    @Override
    public void actionPerformed(ActionEvent arg0) {

        this.mov2Panel.setVisible(false);
        this.mov3Panel.setVisible(false);
        this.mov4Panel.setVisible(false);

        switch (this.movimientosBox.getSelectedItem().toString()) {
        case "1":
            this.mov1Panel.setVisible(true);
            break;

        case "2":
            this.mov1Panel.setVisible(true);
            this.mov2Panel.setVisible(true);
            break;

        case "3":
            this.mov1Panel.setVisible(true);
            this.mov2Panel.setVisible(true);
            this.mov3Panel.setVisible(true);
            break;

        case "4":
            this.mov1Panel.setVisible(true);
            this.mov2Panel.setVisible(true);
            this.mov3Panel.setVisible(true);
            this.mov4Panel.setVisible(true);
            break;
        }

        this.pack();
    }
}