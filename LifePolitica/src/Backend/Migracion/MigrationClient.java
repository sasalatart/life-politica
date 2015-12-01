package Backend.Migracion;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.net.Socket;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import Backend.LogicaCiudad.Persona;

public class MigrationClient {

    private static String hostName = "127.0.0.1";
    public static int hostPort = 1234;
    
    public static void sendEntity(Persona p) throws ParserConfigurationException, TransformerException {
        Socket socket;
        Document doc;
        
        if (p.getDoc() == null) {
            doc = prepareDocument(p);
            p.setDoc(doc);
        }
        else {
            doc = p.getDoc();
        }
        
        try {
            socket = new Socket(hostName, hostPort);
            
            BufferedReader reader = new BufferedReader(new FileReader("testing.xml"));
            
            String line;
            StringBuilder stringBuilder = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }
            
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
            writer.println(stringBuilder.toString());
            socket.close();
            reader.close();
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public static Document prepareDocument(Persona p) throws ParserConfigurationException, TransformerException {
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

        Document doc = docBuilder.newDocument();
        
        Element rootElement = doc.createElement("GameOfLife");

        Element common = doc.createElement("Common");
        
        Element posX = doc.createElement("PosX");
        posX.setTextContent(0 + "");
        
        Element posY = doc.createElement("PosY");
        posY.setTextContent(0 + "");
        
        Element height = doc.createElement("Height");
        height.setTextContent(2 + "");
        
        Element width = doc.createElement("Width");
        width.setTextContent(2 + "");
        
        Element groupId = doc.createElement("OriginalGroupID");
        groupId.setTextContent(10 + "");
        
        Element worldSpecific = doc.createElement("WorldSpecific");

        Element worldID = doc.createElement("World");
        worldID.setAttribute("id", 10 + "");
        
        Element shape = doc.createElement("Shape");
        shape.setTextContent(p.getShape().toString());
        
        Element partido = doc.createElement("Partido");
        partido.setTextContent(p.getPartido().toString());
        
        doc.appendChild(rootElement);
        rootElement.appendChild(common);
        common.appendChild(groupId);
        common.appendChild(posX);
        common.appendChild(posY);
        common.appendChild(height);
        common.appendChild(width);
        rootElement.appendChild(worldSpecific);
        worldSpecific.appendChild(worldID);
        worldID.appendChild(shape);
        worldID.appendChild(partido);
        
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(doc);
                
        StreamResult result = new StreamResult(new File("testing.xml"));
        transformer.transform(source, result);

        return doc;
    }
    
    public static void setHostName(String hostName) {
        MigrationClient.hostName = hostName;
    }

    public static void setHostPort(int hostPort) {
        MigrationClient.hostPort = hostPort;
    }
}