package co.edu.uniquindio.poo.model;


import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * Lista simplemente enlazada propia y genérica.
 * No usa ninguna colección de Java: solo nodos conectados entre sí.
 */
public class ListaEnlazada<T> implements Coleccion<T> {

    // Cada nodo guarda un valor y la referencia al siguiente nodo
    private class Nodo {
        T valor;
        Nodo siguiente;

        Nodo(T valor) {
            this.valor = valor;
        }
    }

    private Nodo primero;
    private Nodo ultimo;   // referencia al último para agregar en O(1)
    private int tamanio;

    // Agrega al final: O(1)
    @Override
    public void agregar(T elemento) {
        Nodo nuevo = new Nodo(elemento);   // el nuevo nodo aún no apunta a nada
        if (estaVacia()) {
            primero = nuevo;                 // lista vacía: el nuevo es el primero
        } else {
            ultimo.siguiente = nuevo;        // el último actual apunta al nuevo
        }
        ultimo = nuevo;                      // el nuevo pasa a ser el último
        tamanio++;
    }

    // Elimina la primera aparición del elemento: O(n)
    @Override
    public boolean eliminar(T elemento) {
        // anterior va un paso detrás de actual para poder "saltarse" el nodo eliminado
        Nodo anterior = null;
        Nodo actual = primero;

        while (actual != null) {
            if (Objects.equals(actual.valor, elemento)) {
                if (anterior == null) {
                    primero = actual.siguiente;          // se eliminó el primero
                } else {
                    anterior.siguiente = actual.siguiente; // intermedio o último
                }
                if (actual == ultimo) {
                    ultimo = anterior;                   // se eliminó el último
                }
                tamanio--;
                return true;
            }
            anterior = actual;
            actual = actual.siguiente;
        }
        return false; // el elemento no existe
    }

    // Obtiene por posición: O(n)
    @Override
    public T obtener(int posicion) {
        if (posicion < 0 || posicion >= tamanio) {
            throw new IndexOutOfBoundsException("Posición inválida: " + posicion);
        }
        // Se avanza nodo por nodo hasta llegar a la posición pedida
        Nodo actual = primero;
        for (int i = 0; i < posicion; i++) {
            actual = actual.siguiente;
        }
        return actual.valor;
    }

    @Override
    public int tamanio() {
        return tamanio;
    }

    @Override
    public boolean estaVacia() {
        return tamanio == 0;
    }

    // Iterable: permite usar for-each sin exponer los nodos
    @Override
    public Iterator<T> iterator() {
        return new IteradorLista();
    }

    // Iterator: recorre la lista de uno en uno
    private class IteradorLista implements Iterator<T> {
        private Nodo actual = primero;   // nodo que se entregará en el próximo next()

        @Override
        public boolean hasNext() {
            return actual != null;
        }

        @Override
        public T next() {
            if (!hasNext()) {
                throw new NoSuchElementException("No hay más elementos.");
            }
            T valor = actual.valor;
            actual = actual.siguiente;
            return valor;
        }
    }
}

