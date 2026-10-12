package co.edu.uniquindio.poo.model;

public interface Coleccion<T> extends Iterable<T> {

    void agregar(T elemento);

    // Devuelve true si el elemento existía y fue eliminado
    boolean eliminar(T elemento);

    // Obtiene el elemento de la posición indicada (empieza en 0)
    T obtener(int posicion);

    int tamanio();

    boolean estaVacia();
}
