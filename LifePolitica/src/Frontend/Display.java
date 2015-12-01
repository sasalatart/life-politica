package Frontend;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BorderFactory;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

import Backend.ParametrosIniciales;
import Backend.LogicaCiudad.Ciudad;
import Backend.LogicaCiudad.NombrePartido;

/**
 * Interfaz grafica de LifePOLITICA, consistente en una grilla que contiene a la
 * ciudad, y los botones para interactuar con el programa.
 * 
 * @author Grupo 10
 * 
 */
public class Display implements Observer {

    private JFrame frame;

    private JPanel finalPanel;
    private JPanel cityPanel;
    private JPanel rightPanel;
    private JPanel helpPanel;
    private JPanel statisticsPanel;
    private JPanel migrationPanel;
    private JPanel eventosPanel;
    private JPanel buttonsPanel;
    private JPanel zoomPanel;
    private JPanel shiftPanel;

    private JScrollPane tablePane;
    private Tabla table;

    private static ModeloTabla modeloTabla;

    private JLabel instructions;
    private JLabel actualTime;
    private JLabel actualData;

    private JLabel worldLabel;
    private JComboBox<String> worldBox;

    private JRadioButton escandaloPolitico;
    private JRadioButton campanaPolitica;
    private ButtonGroup buttonGroup;
    private JComboBox<NombrePartido> eventosBox;
    private JButton eventosButton;

    private JButton informeButton;
    private JButton stopButton;
    private JButton zoomplus;
    private JButton zoomminus;
    private JButton shiftRight;
    private JButton shiftLeft;

    private Color backGroundColor;

    /**
     * Inicializa y arma la ventana que muestra el progreso de la simulacion.
     * Ademas de la grilla, coloca todos los elementos graficos con los que el
     * usuario interactua.
     * 
     * @wbp.parser.entryPoint
     */
    public void init() {
        ParametrosIniciales.getInstance().addObserver(this);

        this.backGroundColor = new Color(175, 205, 240);

        this.frame = new JFrame("LifePolitica");

        this.cityPanel = new JPanel();

        modeloTabla = new ModeloTabla(Ciudad.getCeldas());

        this.table = new Tabla(modeloTabla);

        this.tablePane = new JScrollPane(table, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        this.tablePane.setPreferredSize(new Dimension(601, 601));
        this.tablePane.getViewport().setBackground(this.backGroundColor);
        this.tablePane.setBackground(this.backGroundColor);
        this.tablePane.setBorder(BorderFactory.createEmptyBorder());
        this.cityPanel.add(this.tablePane);

        this.rightPanel = new JPanel();
        this.rightPanel.setLayout(new BoxLayout(this.rightPanel, BoxLayout.Y_AXIS));

        this.helpPanel = new JPanel();
        this.instructions = new JLabel("<html>Ayuda:<br />Hacer clic en el mapa para posicionar muros.</html>");
        this.helpPanel.add(this.instructions);
        this.rightPanel.add(this.helpPanel);

        this.statisticsPanel = new JPanel();
        this.statisticsPanel.setBorder(BorderFactory.createCompoundBorder(new EmptyBorder(10, 10, 10, 10), new EtchedBorder()));
        this.statisticsPanel.setLayout(new GridBagLayout());
        GridBagConstraints statisticsBagConstraints = new GridBagConstraints();
        statisticsBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        statisticsBagConstraints.insets = new Insets(5, 0, 0, 0);
        statisticsBagConstraints.gridy = 2;
        this.actualTime = new JLabel();
        this.actualTime.setFont(new Font("Tahoma", Font.PLAIN, 20));
        this.actualData = new JLabel();
        this.actualData.setFont(new Font("Tahoma", Font.PLAIN, 14));
        this.statisticsPanel.add(this.actualTime);
        this.statisticsPanel.add(this.actualData, statisticsBagConstraints);
        this.rightPanel.add(this.statisticsPanel);

        this.migrationPanel = new JPanel();
        this.migrationPanel.setLayout(new GridBagLayout());
        GridBagConstraints migrationBagConstraints = new GridBagConstraints();
        migrationBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        migrationBagConstraints.insets = new Insets(5, 0, 0, 0);
        migrationBagConstraints.gridy = 2;
        this.worldLabel = new JLabel("Seleccione el Mundo a Emigrar:");
        this.worldLabel.setFont(new Font("Tahoma", Font.BOLD, 16));
        this.worldBox = new JComboBox<String>();
        this.setWorldBox();
        this.migrationPanel.add(this.worldLabel);
        this.migrationPanel.add(this.worldBox, migrationBagConstraints);
        this.rightPanel.add(this.migrationPanel);

        this.eventosPanel = new JPanel();
        this.eventosPanel.setLayout(new GridBagLayout());
        GridBagConstraints eventosBagConstraints = new GridBagConstraints();
        eventosBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        eventosBagConstraints.insets = new Insets(5, 0, 0, 0);
        this.buttonGroup = new ButtonGroup();
        this.escandaloPolitico = new JRadioButton("Escandalo Politico");
        this.escandaloPolitico.setFont(new Font("Tahoma", Font.PLAIN, 16));
        eventosBagConstraints.gridy = 1;
        this.eventosPanel.add(this.escandaloPolitico, eventosBagConstraints);
        this.campanaPolitica = new JRadioButton("Campana Politica");
        this.campanaPolitica.setFont(new Font("Tahoma", Font.PLAIN, 16));
        eventosBagConstraints.gridy = 2;
        this.eventosPanel.add(this.campanaPolitica, eventosBagConstraints);
        this.eventosBox = new JComboBox<NombrePartido>();
        eventosBagConstraints.insets = new Insets(15, 0, 0, 0);
        eventosBagConstraints.gridy = 3;
        this.eventosPanel.add(this.eventosBox, eventosBagConstraints);
        this.eventosButton = new JButton("Ejecutar Evento");
        eventosBagConstraints.insets = new Insets(5, 0, 0, 0);
        eventosBagConstraints.gridy = 4;
        this.eventosPanel.add(this.eventosButton, eventosBagConstraints);
        this.buttonGroup.add(this.campanaPolitica);
        this.buttonGroup.add(this.escandaloPolitico);
        this.rightPanel.add(this.eventosPanel);

        this.buttonsPanel = new JPanel();
        this.buttonsPanel.setLayout(new GridBagLayout());
        GridBagConstraints buttonsBagConstraints = new GridBagConstraints();
        buttonsBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        buttonsBagConstraints.insets = new Insets(5, 0, 0, 0);
        buttonsBagConstraints.gridy = 1;
        this.informeButton = new JButton("Grabar Informe");
        this.informeButton.setFont(new Font("Tahoma", Font.BOLD, 18));
        this.buttonsPanel.add(this.informeButton, buttonsBagConstraints);
        this.stopButton = new JButton("Pause");
        this.stopButton.setFont(new Font("Tahoma", Font.BOLD, 18));
        buttonsBagConstraints.gridy = 2;
        this.buttonsPanel.add(this.stopButton, buttonsBagConstraints);
        this.rightPanel.add(this.buttonsPanel);

        this.zoomPanel = new JPanel();
        this.zoomPanel.setLayout(new GridBagLayout());
        this.zoomplus = new JButton("Zoom +");
        this.zoomminus = new JButton("Zoom -");
        this.zoomPanel.add(this.zoomplus, buttonsBagConstraints);
        this.zoomPanel.add(this.zoomminus, buttonsBagConstraints);
        this.rightPanel.add(this.zoomPanel);
        
        this.shiftPanel = new JPanel();
        this.shiftPanel.setLayout(new GridBagLayout());
        this.shiftLeft = new JButton("Move Left");
        this.shiftRight = new JButton("Move Right");
        this.shiftPanel.add(this.shiftLeft, buttonsBagConstraints);
        this.shiftPanel.add(this.shiftRight, buttonsBagConstraints);
        this.rightPanel.add(this.shiftPanel);
        
        this.cityPanel.setBackground(this.backGroundColor);
        this.rightPanel.setBackground(this.backGroundColor);
        this.helpPanel.setBackground(this.backGroundColor);
        this.statisticsPanel.setBackground(this.backGroundColor);
        this.migrationPanel.setBackground(this.backGroundColor);
        this.eventosPanel.setBackground(this.backGroundColor);
        this.escandaloPolitico.setBackground(this.backGroundColor);
        this.campanaPolitica.setBackground(this.backGroundColor);
        this.buttonsPanel.setBackground(this.backGroundColor);
        this.zoomPanel.setBackground(this.backGroundColor);
        this.shiftPanel.setBackground(this.backGroundColor);

        this.finalPanel = new JPanel();
        this.finalPanel.setLayout(new BoxLayout(this.finalPanel, BoxLayout.X_AXIS));
        this.finalPanel.add(this.cityPanel);
        this.finalPanel.add(this.rightPanel);
        this.finalPanel.setBackground(this.backGroundColor);

        this.frame.getContentPane().add(this.finalPanel);
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.frame.setResizable(false);
        this.frame.setSize(900, 660);
        this.frame.setLocationRelativeTo(null);
        this.frame.setVisible(true);

        this.table.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int row = table.rowAtPoint(evt.getPoint());
                int col = table.columnAtPoint(evt.getPoint());

                int columnCorrector = (col + Tabla.getCoordCorrector()) % Ciudad.getCeldas().length;
                
                while (columnCorrector < 0) {
                    columnCorrector += Ciudad.getCeldas().length;
                }
                
                Ciudad.crearMuro(row, columnCorrector);
            }
        });
        
        this.zoomplus.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                table.increaseZoom();
            }
        });
        
        this.zoomminus.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                table.reduceZoom();
            }
        });
        
        this.shiftLeft.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                table.shiftRight();
            }
        });

        this.shiftRight.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                table.shiftLeft();
            }
        });
        
    }

    public JFrame getFrame() {
        return frame;
    }

    public static ModeloTabla getModeloTabla() {
        return modeloTabla;
    }

    public JComboBox<String> getWorldBox() {
        return worldBox;
    }

    public JRadioButton getCampanaPolitica() {
        return campanaPolitica;
    }

    public JRadioButton getEscandaloPolitico() {
        return escandaloPolitico;
    }

    public JComboBox<NombrePartido> getEventosBox() {
        return eventosBox;
    }

    public JButton getEventosButton() {
        return eventosButton;
    }

    public JButton getInformeButton() {
        return this.informeButton;
    }

    public JButton getStopButton() {
        return stopButton;
    }

    public JLabel getActualTime() {
        return actualTime;
    }
    
    public void setWorldBox() {

        String[] worlds = { "LifePolitica (default - internal IP) PC1","LifePolitica (default - internal IP) PC2",
                "G1: LifeZombie PC1","G1: LifeZombie PC2", "G2: LifeSpace PC1","G2: LifeSpace PC2", "G3: LifeGalaxy PC1","G3: LifeGalaxy PC2",
                "G4: LifeDisease PC1","G4: LifeDisease PC2", "G5: LifeCivilization PC1","G5: LifeCivilization PC2", "G6: LifeTetris PC1",
                "G6: LifeTetris PC2","G7: LifeFisica PC1","G7: LifeFisica PC2", "G8: LifeGenetics PC1","G8: LifeGenetics PC2", "G9: LifeWar PC1","G9: LifeWar PC2" };

        this.worldBox.setModel(new DefaultComboBoxModel<String>(worlds));
    }

    /**
     * Configura el seleccionador de partidos politicos.
     * 
     * @param numPartidos
     *            cantidad de partidos politicos que participaran en la
     *            simulacion.
     */
    public void setEventosBox(int partidos) {
        switch (partidos) {

        case 1:
            this.eventosBox.setModel(new DefaultComboBoxModel<NombrePartido>(
                    new NombrePartido[] { NombrePartido.ComuNAUS }));
            break;

        case 2:
            this.eventosBox.setModel(new DefaultComboBoxModel<NombrePartido>(
                    new NombrePartido[] { NombrePartido.ComuNAUS,
                            NombrePartido.SoliSoli }));
            break;

        case 3:
            this.eventosBox.setModel(new DefaultComboBoxModel<NombrePartido>(
                    new NombrePartido[] { NombrePartido.ComuNAUS,
                            NombrePartido.SoliSoli, NombrePartido.ElGremio }));
            break;

        case 4:
            this.eventosBox.setModel(new DefaultComboBoxModel<NombrePartido>(
                    new NombrePartido[] { NombrePartido.ComuNAUS,
                            NombrePartido.SoliSoli, NombrePartido.ElGremio,
                            NombrePartido.Creceres }));
            break;
        }
    }

    /**
     * Actualiza el panel de distribucion de porcentajes con los datos actuales.
     */
    @Override
    public void update(Observable arg0, Object arg1) {
        @SuppressWarnings("unchecked")
        HashMap<Integer, Integer> porcentajes = (HashMap<Integer, Integer>) arg1;
        int numPartidos = porcentajes.size();

        String text = "<html>";
        for (int i = 0; i < numPartidos; i++) {
            if (i != numPartidos) {
                text += NombrePartido.getNameAndColorByIndex(i).toString()
                        + ": " + porcentajes.get(i) + "<br /><br />";
            } else {
                text += NombrePartido.getNameAndColorByIndex(i).toString()
                        + ": " + porcentajes.get(i) + "<br />";
            }
        }
        text += "</html>";

        try {
            this.actualData.setText(text);
        } catch (Exception e) {

        }
    }
}