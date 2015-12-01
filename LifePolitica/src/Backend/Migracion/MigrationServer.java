package Backend.Migracion;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.CountDownLatch;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import Backend.LogicaCiudad.Ciudad;
import Backend.LogicaCiudad.NombrePartido;
import Backend.LogicaCiudad.NombreShape;
import Backend.LogicaCiudad.Persona;

/**
 * Clase que incluye toda la logica de servidor, es decir, encargada de recibir
 * conexiones entrantes de parte de otros mundos.
 * 
 * @author Grupo 10
 * 
 */
public class MigrationServer {

    public static int PUERTO = 1234;
    static ServerSocket serverSocket;
    DataOutputStream output;
    String receivedMessage;

    /**
     * Metodo que inicia el servidor y le deja escuchando en caso de que algun
     * mundo envie alguna entidad.
     * 
     * @throws IOException
     */
    public void initServer() throws IOException {
        serverSocket = new ServerSocket(PUERTO);

        while (true) {
            final Socket socket = serverSocket.accept();
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        dealWithIncomingConnection(socket);
                    } catch (IOException | SAXException
                            | ParserConfigurationException e) {
                        e.printStackTrace();
                    }
                }
            });
            t.start();
        }
    }
    
    Persona ret = null;
    CountDownLatch latchRet = new CountDownLatch(1);
    /**
     * Se inicializa el servidor, pero para el test
     * @param latch
     * @throws IOException
     */
    public Persona initServer(CountDownLatch latch) throws IOException {
        serverSocket = new ServerSocket(PUERTO);
        latch.countDown();
        final Socket socket = serverSocket.accept();
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    ret = dealWithIncomingConnectionTest(socket);
                    latchRet.countDown();
                } catch (IOException | SAXException
                        | ParserConfigurationException e) {
                    e.printStackTrace();
                }
            }
        });
        t.start();
        try {
            latchRet.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        return ret;
    }

    /**
     * Una vez que se establece una conexion con otro mundo, este metodo recibe
     * el socket creado y se encarga de crear el documento XML para poder
     * ingresar la nueva entidad a LifePolitica.
     * 
     * @param s
     * @throws IOException
     * @throws SAXException
     * @throws ParserConfigurationException
     */
    public void dealWithIncomingConnection(Socket s) throws IOException, SAXException, ParserConfigurationException {
        Document xmlDoc = null;
        BufferedReader clientReader = new BufferedReader(new InputStreamReader(s.getInputStream()));
        
        String clientMessage = clientReader.readLine();
        String otraLinea = "";
        while (otraLinea != null) {
            clientMessage += otraLinea;
            otraLinea = clientReader.readLine();
        }

        //System.out.println(clientMessage);
        
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        InputSource inputSource = new InputSource();
        inputSource.setCharacterStream(new StringReader(clientMessage));

        try {
            xmlDoc = builder.parse(inputSource);
        } catch (Exception e) {
            e.printStackTrace();
        }

        insertEntity(xmlDoc);
    }
    
    
    /**
     * Igual al metodo anterior, pero usado para los tests
     */
    public Persona dealWithIncomingConnectionTest(Socket s) throws IOException, SAXException, ParserConfigurationException {
        Document xmlDoc = null;
        BufferedReader clientReader = new BufferedReader(new InputStreamReader(s.getInputStream()));
        
        String clientMessage = clientReader.readLine();
        String otraLinea = "";
        while (otraLinea != null) {
            clientMessage += otraLinea;
            otraLinea = clientReader.readLine();
        }

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        InputSource inputSource = new InputSource();
        inputSource.setCharacterStream(new StringReader(clientMessage));

        try {
            xmlDoc = builder.parse(inputSource);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return getPersona(xmlDoc);
    }

    /**
     * Metodo encargado de insertar una entidad una vez que esta es recibida de
     * parte de otro mundo.
     * 
     * @param doc
     */
    public void insertEntity(Document doc) {

        NodeList shapeNode;
        NodeList partidoNode;

        String shapeString = null;
        String partidoString = null;

        NombreShape shape = null;
        NombrePartido partido = null;

        try {
            shapeNode = doc.getElementsByTagName("Shape");
            shapeString = shapeNode.item(0).getTextContent();
            shape = NombreShape.valueOf(shapeString);
            
            partidoNode = doc.getElementsByTagName("Partido");
            partidoString = partidoNode.item(0).getTextContent();
            partido = NombrePartido.valueOf(partidoString);

        } catch (Exception e) {
            shape = NombreShape.random();
            partido = NombrePartido.random();
        }

        Ciudad.crearPersonas(true, shape, partido, doc);
    }
    
    /**
     * Metodo que crea personas, usado en los tests
     * @param doc
     * @return
     */
    public Persona getPersona(Document doc)
    {
        NodeList shapeNode;
        NodeList partidoNode;

        String shapeString = null;
        String partidoString = null;

        NombreShape shape = null;
        NombrePartido partido = null;

        try {
            shapeNode = doc.getElementsByTagName("Shape");
            shapeString = shapeNode.item(0).getTextContent();
            shape = NombreShape.valueOf(shapeString);
            
            partidoNode = doc.getElementsByTagName("Partido");
            partidoString = partidoNode.item(0).getTextContent();
            partido = NombrePartido.valueOf(partidoString);

        } catch (Exception e) {
            shape = NombreShape.random();
            partido = NombrePartido.random();
        }
        
        Persona persona = new Persona();
        persona.setPartido(partido, true);
        persona.setShape(shape);
        
        return persona;
    }
}