package Backend;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;
import java.util.Random;

import Backend.EventosExternos.EscandaloPolitico;
import Backend.EventosExternos.EventoExterno;
import Backend.EventosExternos.ManiobraPolitica;
import Backend.LogicaCiudad.Ciudad;
import Backend.LogicaCiudad.DisplayUpdater;
import Backend.LogicaCiudad.NombrePartido;
import Backend.LogicaCiudad.Persona;
import Backend.Migracion.MigrationClient;
import Backend.Migracion.MigrationServer;
import Frontend.Display;
import Frontend.WelcomeFrame;

/**
 * Se preocupa de inicializar la simulacion y coordinar todo tipo de evento
 * relacionado con la interfaz, tales como presionar botones de Eventos
 * Externos, de Solicitud de Informes, para detener la simulacion, y para
 * reiniciarla.
 * 
 * @author Grupo 10
 * 
 */
public class BackendController {

    private Simulador simulador;
    private WelcomeFrame ventanaInicial;
    private Display display;

    public static Random random = new Random();

    private static MigrationServer server = new MigrationServer();

    private Thread threadDisplayUpdater;

    private long startTime;
    private long currentTime;
    private long timePaused;

    public static Object lock = new Object();

    public BackendController(WelcomeFrame frame, Simulador simulador)
            throws IOException {
        this.simulador = simulador;

        this.ventanaInicial = frame;
        this.ventanaInicial.setVisible(true);
        this.ventanaInicial.getButton().addActionListener(
                new InitiateSimulationEvent(this));

        this.display = new Display();
        server.initServer();
        this.timePaused = 0;
    }

    /**
     * Retorna TRUE si los parametros iniciales son validos, y FALSE si no.
     * 
     * @param velocidad
     *            segun la cual se adaptara de manera aleatoria la velocidad de
     *            cada entidad.
     * @param numPartidos
     *            cantidad de partidos politicos que participaran en la
     *            simulacion.
     * @param mapIDPartido
     *            distribucion porcentual de la poblacion en los partidos
     *            participantes.
     * @return
     */
    public boolean ingresarParametrosIniciales(int velocidad, int time,
            int numPartidos, HashMap<Integer, Integer> mapIDPartido) {
        return ParametrosIniciales.getInstance().setParametros(velocidad, time,
                numPartidos, mapIDPartido);
    }

    public void iniciarSimulacion() {
        Simulador.iniciarSimulacion();
    }

    public void solicitarInforme() {
        this.simulador.generarInforme();
    }

    public void guardarEstado() {
        Simulador.guardarEstado();
    }

    public Simulador getSimulador() {
        return simulador;
    }

    public WelcomeFrame getVentanaInicial() {
        return ventanaInicial;
    }

    public Display getDisplay() {
        return display;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(long currentTime) {
        this.currentTime = currentTime;
    }

    public long getTimePaused() {
        return timePaused;
    }

    public void setTimePaused(long timePaused) {
        this.timePaused = timePaused;
    }

    public Thread getThreadDisplayUpdater() {
        return threadDisplayUpdater;
    }

    public void setThreadDisplayUpdater(Thread threadDisplayUpdater) {
        this.threadDisplayUpdater = threadDisplayUpdater;
    }
}

/**
 * Inicia la simulacion al apretar el boton "OK" de la ventana inicial.
 * 
 * @author Grupo 10
 * 
 */
class InitiateSimulationEvent implements ActionListener {

    private BackendController backendController;

    private WelcomeFrame ventanaInicial;
    private Display display;

    private HashMap<Integer, Integer> percentages;
    private int velocidad;
    private int time;
    private int partidos;

    public InitiateSimulationEvent(BackendController backendController) {
        this.backendController = backendController;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void actionPerformed(ActionEvent e) {
        this.ventanaInicial = this.backendController.getVentanaInicial();
        this.display = this.backendController.getDisplay();

        this.percentages = this.ventanaInicial.getValues();
        this.velocidad = this.ventanaInicial.getVelocidad();
        this.time = this.ventanaInicial.getTime();
        this.partidos = this.ventanaInicial.getPartidos();

        if (this.backendController.ingresarParametrosIniciales(this.velocidad,
                this.time, this.partidos,
                (HashMap<Integer, Integer>) this.percentages.clone())) {
            this.backendController.getVentanaInicial().dispose();

            this.backendController.setStartTime(System.currentTimeMillis());

            this.backendController.iniciarSimulacion();
            this.backendController.getDisplay().init();

            this.backendController.setThreadDisplayUpdater(new Thread(
                    new DisplayUpdater(this.backendController)));
            this.backendController.getThreadDisplayUpdater().start();

            this.display.setEventosBox(this.partidos);
            this.display.getWorldBox().addActionListener(
                    new SelectMigrationEvent(this.backendController));
            this.display.getStopButton().addActionListener(
                    new StopEvent(this.backendController));
            this.display.getEventosButton().addActionListener(
                    new ExternalEvent(this.backendController));
            this.display.getInformeButton().addActionListener(
                    new InformeEvent(this.backendController));
        } else {
            this.ventanaInicial.resetValues();
            this.ventanaInicial
                    .showError("La velocidad debe ser un numero entero menor a 100\n"
                            + "El tiempo debe ser un numero positivo\n"
                            + "Los porcentajes deben ser numeros enteros entre 0 y 100\n"
                            + "La suma de los porcentajes debe dar 100");
        }
    }
}

class SelectMigrationEvent implements ActionListener {

    private BackendController backendController;
    private HashMap<String, String> hostNames = new HashMap<String, String>();

    public SelectMigrationEvent(BackendController backendController) {
        this.backendController = backendController;
        setIps();
    }

    private void setIps() {
        Properties prop = new Properties();
        
        InputStream input = null;
        String filename = "config.properties";
        try {
            input = new FileInputStream(filename);
            prop.load(input);
            input.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        hostNames.put("LifePolitica (default - internal IP) PC1", prop.getProperty("g10pc1"));
        hostNames.put("G1: LifeZombie PC1", prop.getProperty("g1pc1"));
        hostNames.put("G2: LifeSpace PC1", prop.getProperty("g2pc1"));
        hostNames.put("G3: LifeGalaxy PC1", prop.getProperty("g3pc1"));
        hostNames.put("G4: LifeDisease PC1", prop.getProperty("g4pc1"));
        hostNames.put("G5: LifeCivilization PC1", prop.getProperty("g5pc1"));
        hostNames.put("G6: LifeTetris PC1", prop.getProperty("g6pc1"));
        hostNames.put("G7: LifeFisica PC1", prop.getProperty("g7pc1"));
        hostNames.put("G8: LifeGenetics PC1", prop.getProperty("g8pc1"));
        hostNames.put("G9: LifeWar PC1", prop.getProperty("g9pc1"));
        
        hostNames.put("LifePolitica (default - internal IP) PC2", prop.getProperty("g10pc2"));
        hostNames.put("G1: LifeZombie PC2", prop.getProperty("g1pc2"));
        hostNames.put("G2: LifeSpace PC2", prop.getProperty("g2pc2"));
        hostNames.put("G3: LifeGalaxy PC2", prop.getProperty("g3pc2"));
        hostNames.put("G4: LifeDisease PC2", prop.getProperty("g4pc2"));
        hostNames.put("G5: LifeCivilization PC2", prop.getProperty("g5pc2"));
        hostNames.put("G6: LifeTetris PC2", prop.getProperty("g6pc2"));
        hostNames.put("G7: LifeFisica PC2", prop.getProperty("g7pc2"));
        hostNames.put("G8: LifeGenetics PC2", prop.getProperty("g8pc2"));
        hostNames.put("G9: LifeWar PC2", prop.getProperty("g9pc2"));
        
        MigrationServer.PUERTO = Integer.parseInt(prop.getProperty("puertoEntrada"));
        MigrationClient.hostPort = Integer.parseInt(prop.getProperty("puertoSalida"));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String selection = this.backendController.getDisplay().getWorldBox()
                .getSelectedItem().toString();
        MigrationClient.setHostName(hostNames.get(selection));
    }
}

/**
 * Ejecuta el Evento Externo seleccionado.
 * 
 * @author Grupo 10
 * 
 */
class ExternalEvent implements ActionListener {

    private BackendController backendController;
    private EventoExterno eventoExterno;
    private ArrayList<Persona> ciudadanos;
    private NombrePartido partido;

    public ExternalEvent(BackendController backendController) {
        this.backendController = backendController;
        backendController.getSimulador();
        this.ciudadanos = Ciudad.getPersonas();
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        switch (this.backendController.getDisplay().getEventosBox()
                .getSelectedItem().toString()) {
        case "ComuNAUS":
            this.partido = NombrePartido.ComuNAUS;
            break;
        case "SoliSoli":
            this.partido = NombrePartido.SoliSoli;
            break;
        case "ElGremio":
            this.partido = NombrePartido.ElGremio;
            break;
        case "Creceres":
            this.partido = NombrePartido.Creceres;
            break;
        default:
            return;
        }

        if (this.backendController.getDisplay().getCampanaPolitica()
                .isSelected()) {
            this.eventoExterno = new ManiobraPolitica();
        } else if (this.backendController.getDisplay().getEscandaloPolitico()
                .isSelected()) {
            this.eventoExterno = new EscandaloPolitico();
        } else {
            return;
        }

        synchronized (BackendController.lock) {
            this.eventoExterno.execute(this.ciudadanos, this.partido);
        }
    }
}

/**
 * Ejecuta el Evento Externo que ha sido seleccionado por el usuario.
 * 
 * @author Grupo 10
 * 
 */
class InformeEvent implements ActionListener {

    private BackendController backendController;

    public InformeEvent(BackendController backendController) {
        this.backendController = backendController;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        this.backendController.guardarEstado();
        this.backendController.solicitarInforme();
    }
}

/**
 * Pausa/Continua la simulacion.
 * 
 * @author Grupo 10
 * 
 */
class StopEvent implements ActionListener {

    private BackendController backendController;

    public StopEvent(BackendController backendController) {
        this.backendController = backendController;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (Persona.isPaused()) {
            Persona.setPaused(false);
            this.backendController.getDisplay().getStopButton()
                    .setText("Pause");
            this.backendController.setTimePaused(this.backendController
                    .getTimePaused()
                    + (System.currentTimeMillis() - Persona.getPauseTime())
                    / 1000);
        } else {
            Persona.setPaused(true);
            this.backendController.getDisplay().getStopButton()
                    .setText("Continue");
            Persona.setPauseTime(System.currentTimeMillis());
        }
    }
}