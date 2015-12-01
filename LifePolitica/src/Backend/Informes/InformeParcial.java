package Backend.Informes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

import Backend.ParametrosIniciales;
import Backend.LogicaCiudad.NombrePartido;
import Backend.LogicaCiudad.PartidoPolitico;
import Frontend.InformeFrame;

public class InformeParcial implements Informadora<Memento> {

    private LinkedHashMap<PartidoPolitico, Integer> adherentesPartidos = new LinkedHashMap<PartidoPolitico, Integer>();
    private LinkedHashMap<PartidoPolitico, LinkedList<Integer>> cambiosEnPartidos = new LinkedHashMap<PartidoPolitico, LinkedList<Integer>>();
    private static HashMap<Integer, ArrayList<PartidoPolitico>> estadoInforme = new HashMap<Integer, ArrayList<PartidoPolitico>>();

    /**
     * Crea una nueva ventana que muestra los graficos de la evolucion de la
     * distribucion de los partidos politicos.
     */
    @Override
    public void publishInfo() {
        InformeFrame informeFrame = new InformeFrame();
        informeFrame.pintar(createDataset());
    }

    /**
     * Se graba el estado de la distribucion de los partidos politicos en un
     * momento dado.
     * 
     * @param iteracion
     *            en la cual nos encontramos (correspondiente a la cantidad de
     *            veces que se ha grabado un estado).
     * @param listaPartidos
     *            los partidos politicos que participan en la simulacion.
     */
    public static void GuardarEstadoInforme(int iteracion,
            ArrayList<PartidoPolitico> listaPartidos) {
        estadoInforme.put(iteracion, listaPartidos);
    }

    /**
     * Retorna el database para crear el grafico.
     * @return el database para crear el grafico.
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    private static CategoryDataset createDataset() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        Iterator iterator = estadoInforme.entrySet().iterator();

        while (iterator.hasNext()) {
            Map.Entry mapEntry = (Map.Entry) iterator.next();
            String category = "Iteracion " + mapEntry.getKey();

            int numPart = ParametrosIniciales.getInstance().getNumeroDePartidos();
            
            PartidoPolitico p1 = new PartidoPolitico(NombrePartido.SoliSoli);
            PartidoPolitico p2 = new PartidoPolitico(NombrePartido.Creceres);
            PartidoPolitico p3 = new PartidoPolitico(NombrePartido.ComuNAUS);
            PartidoPolitico p4 = new PartidoPolitico(NombrePartido.ElGremio);
                        
            if (numPart == 1){
                p1.setAdherentes(0);
                p2.setAdherentes(0);
                p3.setAdherentes(((ArrayList<PartidoPolitico>)mapEntry.getValue()).get(0).getAdherentes());
                p4.setAdherentes(0);
            }
            else if (numPart == 2){
                p1.setAdherentes(((ArrayList<PartidoPolitico>)mapEntry.getValue()).get(1).getAdherentes());
                p2.setAdherentes(0);
                p3.setAdherentes(((ArrayList<PartidoPolitico>)mapEntry.getValue()).get(0).getAdherentes());
                p4.setAdherentes(0);
            }
            else if (numPart == 3){
                p1.setAdherentes(((ArrayList<PartidoPolitico>)mapEntry.getValue()).get(1).getAdherentes());
                p2.setAdherentes(0);
                p3.setAdherentes(((ArrayList<PartidoPolitico>)mapEntry.getValue()).get(0).getAdherentes());
                p4.setAdherentes(((ArrayList<PartidoPolitico>)mapEntry.getValue()).get(2).getAdherentes());
            }
            else if (numPart == 4){
                p1.setAdherentes(((ArrayList<PartidoPolitico>)mapEntry.getValue()).get(1).getAdherentes());
                p2.setAdherentes(((ArrayList<PartidoPolitico>)mapEntry.getValue()).get(3).getAdherentes());
                p3.setAdherentes(((ArrayList<PartidoPolitico>)mapEntry.getValue()).get(0).getAdherentes());
                p4.setAdherentes(((ArrayList<PartidoPolitico>)mapEntry.getValue()).get(2).getAdherentes());
            }
            
            dataset.addValue(p1.getAdherentes(), p1.getNombre(), category);
            dataset.addValue(p2.getAdherentes(), p2.getNombre(), category);
            dataset.addValue(p3.getAdherentes(), p3.getNombre(), category);
            dataset.addValue(p4.getAdherentes(), p4.getNombre(), category);
        }
        
        return dataset;
    }
    
    @Override
    public void acquireInfo(Caretaker caretaker) {
        for (Memento memento : caretaker.getSavedStates()) {
            processInfo(memento);
        }
    }

    /**
     * Se graba, en caso de que se trate de un partido politico, la cantidad de adherentes en ese
     * momento. Como recorremos desde el primer tiempo, solo se guardara el ultimo. Ademas, vamos
     * guardando las estadisticas historicas de adhesion al partido.
     */
    @Override
    public void processInfo(Memento memento) {
        for (int i = 0; i < memento.getSavedState().size(); i++) {
            ArrayList<PartidoPolitico> datos = memento.getSavedState().get(i);
            for (PartidoPolitico partido : datos) {
                if (partido instanceof PartidoPolitico) {

                    if (!adherentesPartidos.containsKey(partido)){
                        adherentesPartidos.put(partido, partido.getValorPrimario());
                    }
                    else {
                        adherentesPartidos.remove(partido);
                        adherentesPartidos.put(partido, partido.getValorPrimario());
                    }
                    
                    if (cambiosEnPartidos.containsKey(partido)){
                        cambiosEnPartidos.get(partido).add(partido.getValorPrimario()); 
                    }
                    else {
                        LinkedList<Integer> list = new LinkedList<Integer>();
                        list.add(partido.getValorPrimario());
                        cambiosEnPartidos.put(partido, list);
                    }
                }
            }
        }
    }
}