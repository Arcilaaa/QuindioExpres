package co.edu.uniquindio.poo;

import java.util.List;

package co.edu.uniquindio.poo;

import java.util.List;

public final class Algoritmos {

    private Algoritmos() { }

    // RECURSIVIDAD (sin ciclos)

    /**
     * Suma el peso de los paquetes de un municipio.
     * Caso base: indice == tamaño (ya no hay paquetes) -> 0.
     * Caso recursivo: peso del actual (si aplica) + resultado del resto.
     * Complejidad: tiempo O(n), espacio O(n) por la pila de llamadas.
     */
    public static double pesoTotalMunicipio(List<Paquete> paquetes, String municipio, int indice) {
        if (indice == paquetes.size()) {
            return 0;
        }
        Paquete actual = paquetes.get(indice);
        // Si el destino coincide se suma su peso; si no, aporta 0
        double pesoActual = 0;
        if (actual.getDestino().equalsIgnoreCase(municipio)) {
            pesoActual = actual.getPeso();
        }
        // Llamada recursiva: avanza una posición hacia el caso base
        return pesoActual + pesoTotalMunicipio(paquetes, municipio, indice + 1);
    }

    /**
     * Cuenta los paquetes con prioridad >= prioridadMinima.
     * Caso base: indice == tamaño -> 0.
     * Complejidad: tiempo O(n), espacio O(n).
     */
    public static int contarPrioridadMinima(List<Paquete> paquetes, int prioridadMinima, int indice) {
        if (indice == paquetes.size()) {
            return 0;
        }
        // Suma 1 si cumple la prioridad mínima, 0 si no
        int cuenta = 0;
        if (paquetes.get(indice).getPrioridad() >= prioridadMinima) {
            cuenta = 1;
        }
        return cuenta + contarPrioridadMinima(paquetes, prioridadMinima, indice + 1);
    }

    //  BÚSQUEDA BINARIA

    /**
     * Busca un código en una lista ORDENADA por código (precondición).
     * Devuelve la posición o -1 si no existe.
     * Complejidad: tiempo O(log n), espacio O(1).
     */
    public static int busquedaBinaria(List<Paquete> ordenada, String codigo) {
        int inicio = 0;
        int fin = ordenada.size() - 1;
        // inicio y fin delimitan la zona donde todavía puede estar el código

        while (inicio <= fin) {
            int medio = (inicio + fin) / 2;   // posición central
            // compareTo < 0: el del medio es menor al buscado; > 0: es mayor
            int comparacion = ordenada.get(medio).getCodigo().compareTo(codigo);

            if (comparacion == 0) {
                return medio;           // encontrado
            } else if (comparacion < 0) {
                inicio = medio + 1;     // buscar en la mitad derecha
            } else {
                fin = medio - 1;        // buscar en la mitad izquierda
            }
        }
        return -1;
    }

    //  DIVIDE Y VENCERÁS

    /** Paquete de mayor peso de toda la lista (null si está vacía). */
    public static Paquete paqueteMayorPeso(List<Paquete> paquetes) {
        if (paquetes.isEmpty()) {
            return null;
        }
        return mayorPeso(paquetes, 0, paquetes.size() - 1);
    }

    /**
     * Divide y vencerás sobre el rango [inicio, fin].
     * 1. Caso base: un solo elemento -> ese es el mayor.
     * 2. Dividir: calcular el punto medio.
     * 3. Vencer: resolver mitad izquierda y mitad derecha.
     * 4. Combinar: quedarse con el de mayor peso.
     * Complejidad: tiempo O(n), espacio O(log n) por la pila.
     */
    private static Paquete mayorPeso(List<Paquete> paquetes, int inicio, int fin) {
        if (inicio == fin) {
            return paquetes.get(inicio);
        }
        int medio = (inicio + fin) / 2;   // DIVIDIR
        // VENCER: cada mitad se resuelve por separado
        Paquete mayorIzquierda = mayorPeso(paquetes, inicio, medio);
        Paquete mayorDerecha = mayorPeso(paquetes, medio + 1, fin);

        // COMBINAR: el resultado es el mayor de los dos
        if (mayorIzquierda.getPeso() >= mayorDerecha.getPeso()) {
            return mayorIzquierda;
        }
        return mayorDerecha;
    }
}