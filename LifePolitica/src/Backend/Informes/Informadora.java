package Backend.Informes;

/**
 * Interfaz de las clases que se preocupan de crear informes.
 * @author Grupo 10
 *
 * @param <T>
 */
public interface Informadora<T> {

    public void publishInfo();
    public void acquireInfo(Caretaker caretaker);
    public void processInfo(T t);   
}