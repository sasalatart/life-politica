package Backend.LogicaCiudad;

import java.awt.Point;
import java.util.LinkedList;
import java.util.concurrent.TimeUnit;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import Backend.BackendController;
import Backend.ParametrosIniciales;
import Backend.Migracion.MigrationClient;

/**
 * Representacion de las personas.
 * 
 * @author Grupo 10
 * 
 */
public class Persona extends Entidad implements Runnable {

    private double tick;
    private int ticksPartidoBloqueado;

    private static boolean isPaused = false;
    private static long pauseTime = 0;

    private boolean sent = false;

    private Thread thread;

    private NombreShape shape;
    private NombrePartido partido;

    private boolean l1;
    private boolean l2;
    private boolean l3;
    private boolean l4;
    private boolean i1;
    private boolean i2;
    private boolean square;

    /**
     * Crea a la persona, asignandole un tick base que variara de manera
     * aleatoria, y que decidira la velocidad en la que operara.
     */
    public Persona() {

        this.componentes = new LinkedList<Celda>();

        this.tick = 100 + (BackendController.random.nextDouble())
                * (500 - 5 * ParametrosIniciales.getInstance().getVelocidad());
    }

    /**
     * Iteracion infinita (hasta que el thread se detenga) que ejecuta los
     * metodos de revisar vecinos y moverse,
     */
    @Override
    public void run() {
        while (true) {

            this.pausar();

            if (!isPaused) {
                this.revisarVecinos();
                this.mover();

                if (this.checkMigration() && !sent) {
                    try {
                        MigrationClient.sendEntity(this);
                        Ciudad.killPerson(this);
                        break;
                    } catch (ParserConfigurationException
                            | TransformerException e) {
                        e.getStackTrace();
                    }
                }
            }
        }
    }

    /**
     * Pausa la ejecucion de la persona segun su tick generado de manera
     * aleatoria en el constructor.
     */
    private void pausar() {
        try {
            TimeUnit.MILLISECONDS.sleep((long) tick);
        } catch (InterruptedException e) {
        }
    }

    /**
     * Revisa los vecinos de la Persona.
     */
    private void revisarVecinos() {
        synchronized (BackendController.lock) {
            if (this.ticksPartidoBloqueado == 0) {
                Ciudad.revisarVecinosPersona(this);
            }

            setTicksPartidoBloqueado(this.ticksPartidoBloqueado - 1);
        }
    }

    /**
     * Mueve a la persona en una direccion aleatoria.
     */
    public void mover() {

        boolean inPortal = true;
        boolean outPortal = true;

        int maxCol = Ciudad.getCeldas().length - 1;

        for (Celda celda : this.componentes) {
            int col = celda.getPoint().y;
            int row = celda.getPoint().x;

            boolean celdaAfueraDeOutPortal = row < 1 || row > 8
                    || col < maxCol - 4 || col > maxCol - 1;
            boolean celdaAfueraDeInPortal = row < 1 || row > 10 || col < 2
                    || col > 4;

            if (celdaAfueraDeOutPortal) {
                outPortal = false;
            }
            if (celdaAfueraDeInPortal) {
                inPortal = false;
            }
        }

        if (inPortal) {
            Ciudad.moverEntidad(this, NombreDireccion.Up);
        } else if (outPortal) {
            Ciudad.moverEntidad(this, NombreDireccion.Down);
        } else {
            Ciudad.moverEntidad(this, NombreDireccion.random());
        }
    }

    public boolean checkMigration() {

        for (int i = 0; i < this.componentes.size(); i++) {
            int row = this.componentes.get(i).getPoint().y;
            int col = this.componentes.get(i).getPoint().x;

            if (row < 1 || row > 5 || col < 1 || col > 5) {
                return false;
            }
        }

        return true;
    }

    public int getTicksPartidoBloqueado() {
        return ticksPartidoBloqueado;
    }

    public void setTicksPartidoBloqueado(int ticksPartidoBloqueado) {
        if (ticksPartidoBloqueado < 0) {
            this.ticksPartidoBloqueado = 0;
        } else {
            this.ticksPartidoBloqueado = ticksPartidoBloqueado;
        }
    }

    /**
     * Se le da forma y ubicacion a cada persona, situandola dentro del mapa.
     */
    public synchronized void placeComponentes(boolean migrated) {

        Celda[][] grilla = Ciudad.getCeldas();

        int size = 2;

        int maxCol = Ciudad.getCeldas().length - 1;

        while (true) {

            int x;
            int y;

            this.square = false;
            this.l1 = false;
            this.l2 = false;
            this.l3 = false;
            this.l4 = false;
            this.i1 = false;
            this.i2 = false;

            if (migrated) {
                y = maxCol - 4;
                x = 3;
            } else {
                x = BackendController.random.nextInt(grilla.length - 2);
                y = BackendController.random.nextInt(grilla[0].length - 2);

                boolean prohibitedPositions = x > 1 && x < 5 && y > 1 && y < 5;

                if (prohibitedPositions) {
                    continue;
                }
            }

            if (this.shape == null) {
                this.setBooleanShapes(x, y);
                this.setNombreShapeFromBoolean();
            } else {
                this.setBooleanFromNombreShape();
            }

            boolean valid = square || l1 || l2 || l3 || l4 || i1 || i2;

            if (!valid) {
                continue;
            }

            for (int i = x; i < x + size; i++) {
                for (int j = y; j < y + size; j++) {
                    if (grilla[i][j].getParent() == null) {
                        Celda celda = new Celda(new Point(i, j), this);
                        grilla[i][j] = celda;
                        this.componentes.add(celda);
                    }
                }
            }

            if (migrated) {
                this.moldearPersona(x, y);
            }

            break;
        }
    }

    public void setBooleanShapes(int x, int y) {
        this.setl1(x, y);
        this.setl2(x, y);
        this.setl3(x, y);
        this.setl4(x, y);
        this.seti1(x, y);
        this.seti2(x, y);
        this.setSquare(x, y);
    }

    public void setl1(int x, int y) {

        Celda[][] grilla = Ciudad.getCeldas();

        l1 = grilla[x][y].getParent() != null
                && grilla[x][y + 1].getParent() == null
                && grilla[x + 1][y].getParent() == null
                && grilla[x + 1][y + 1].getParent() == null;
    }

    public void setl2(int x, int y) {

        Celda[][] grilla = Ciudad.getCeldas();

        l2 = grilla[x][y].getParent() == null
                && grilla[x][y + 1].getParent() != null
                && grilla[x + 1][y].getParent() == null
                && grilla[x + 1][y + 1].getParent() == null;
    }

    public void setl3(int x, int y) {

        Celda[][] grilla = Ciudad.getCeldas();

        l3 = grilla[x][y].getParent() == null
                && grilla[x][y + 1].getParent() == null
                && grilla[x + 1][y].getParent() != null
                && grilla[x + 1][y + 1].getParent() == null;
    }

    public void setl4(int x, int y) {

        Celda[][] grilla = Ciudad.getCeldas();

        l4 = grilla[x][y].getParent() == null
                && grilla[x][y + 1].getParent() == null
                && grilla[x + 1][y].getParent() == null
                && grilla[x + 1][y + 1].getParent() != null;
    }

    public void seti1(int x, int y) {

        Celda[][] grilla = Ciudad.getCeldas();

        i1 = grilla[x][y].getParent() != null
                && grilla[x][y + 1].getParent() != null
                && grilla[x + 1][y].getParent() == null
                && grilla[x + 1][y + 1].getParent() == null;
    }

    public void seti2(int x, int y) {

        Celda[][] grilla = Ciudad.getCeldas();

        i2 = grilla[x][y].getParent() == null
                && grilla[x][y + 1].getParent() != null
                && grilla[x + 1][y].getParent() == null
                && grilla[x + 1][y + 1].getParent() != null;
    }

    public void setSquare(int x, int y) {
        Celda[][] grilla = Ciudad.getCeldas();

        square = grilla[x][y].getParent() == null
                && grilla[x][y + 1].getParent() == null
                && grilla[x + 1][y].getParent() == null
                && grilla[x + 1][y + 1].getParent() == null;
    }

    public void setNombreShapeFromBoolean() {
        if (l1) {
            this.shape = NombreShape.l1;
        } else if (l2) {
            this.shape = NombreShape.l2;
        } else if (l3) {
            this.shape = NombreShape.l3;
        } else if (l4) {
            this.shape = NombreShape.l4;
        } else if (i1) {
            this.shape = NombreShape.i1;
        } else if (i2) {
            this.shape = NombreShape.i2;
        } else if (square) {
            this.shape = NombreShape.Square;
        }
    }

    public void setBooleanFromNombreShape() {
        switch (this.shape.toString()) {
        case ("l1"):
            l1 = true;
            break;
        case ("l2"):
            l2 = true;
            break;
        case ("l3"):
            l3 = true;
            break;
        case ("l4"):
            l4 = true;
            break;
        case ("i1"):
            i1 = true;
            break;
        case ("i2"):
            i2 = true;
            break;
        default:
            square = true;
            break;
        }
    }

    public void moldearPersona(int x, int y) {

        Celda[][] grilla = Ciudad.getCeldas();

        LinkedList<Integer> posToRemove = new LinkedList<Integer>();

        if (l1) {
            posToRemove.add(0);
        } else if (l2) {
            posToRemove.add(1);
        } else if (l3) {
            posToRemove.add(2);
        } else if (l4) {
            posToRemove.add(3);
        } else if (i1) {
            posToRemove.add(0);
            posToRemove.add(0);
        } else if (i2) {
            posToRemove.add(1);
            posToRemove.add(2);
        }

        for (int k : posToRemove) {
            int i = this.componentes.get(k).getPoint().x;
            int j = this.componentes.get(k).getPoint().y;

            Celda celda = new Celda(new Point(i, j), null);
            grilla[i][j] = celda;
            this.componentes.remove(k);
        }
    }

    public static boolean isPaused() {
        return isPaused;
    }

    public static void setPaused(boolean isPaused) {
        Persona.isPaused = isPaused;
    }

    public int getNumeroPartido() {
        return partido.getCode();
    }

    public NombrePartido getPartido() {
        return partido;
    }

    public synchronized void setPartido(NombrePartido partido, boolean migrated) {

        boolean macuqueado = this.partido != null && partido != null
                && !migrated;
        boolean killed = this.partido != null && partido == null && !migrated;

        if (macuqueado) {
            int percentage = ParametrosIniciales.getInstance().getPorcentajes()
                    .get(this.getNumeroPartido());
            ParametrosIniciales.getInstance().setPorcentaje(
                    this.getNumeroPartido(), percentage - 1);

            percentage = ParametrosIniciales.getInstance().getPorcentajes()
                    .get(partido.getCode());
            ParametrosIniciales.getInstance().setPorcentaje(partido.getCode(),
                    percentage + 1);
        } else if (killed) {
            int percentage = ParametrosIniciales.getInstance().getPorcentajes()
                    .get(this.getNumeroPartido());
            ParametrosIniciales.getInstance().setPorcentaje(
                    this.getPartido().getCode(), percentage - 1);
        } else if (migrated) {
            int percentage = ParametrosIniciales.getInstance().getPorcentajes()
                    .get(partido.getCode());
            ParametrosIniciales.getInstance().setPorcentaje(partido.getCode(),
                    percentage + 1);
        }

        this.partido = partido;
    }

    public Thread getThread() {
        return thread;
    }

    public void setThread(Thread thread) {
        this.thread = thread;
    }

    public NombreShape getShape() {
        return shape;
    }

    public void setShape(NombreShape shape) {
        this.shape = shape;
    }

    public static long getPauseTime() {
        return pauseTime;
    }

    public static void setPauseTime(long pauseTime) {
        Persona.pauseTime = pauseTime;
    }
}