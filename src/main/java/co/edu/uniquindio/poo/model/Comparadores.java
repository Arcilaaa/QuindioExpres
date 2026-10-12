package co.edu.uniquindio.poo.model;

import java.util.Comparator;

/**
 * Criterios de ordenamiento para Paquete (independientes del orden natural).
 */
public final class Comparadores {

    // Constructor privado: es una clase solo de constantes, no se instancia
    private Comparadores() { }

    // Prioridad de mayor a menor
    public static final Comparator<Paquete> POR_PRIORIDAD_DESC =
            (a, b) -> Integer.compare(b.getPrioridad(), a.getPrioridad());

    // Peso de mayor a menor
    public static final Comparator<Paquete> POR_PESO_DESC =
            (a, b) -> Double.compare(b.getPeso(), a.getPeso());

    // Tiempo estimado de menor a mayor
    public static final Comparator<Paquete> POR_TIEMPO_ASC =
            (a, b) -> Integer.compare(a.getTiempoEstimado(), b.getTiempoEstimado());

    // Despacho prioritario: 1) mayor prioridad, 2) menor tiempo, 3) código
    public static final Comparator<Paquete> DESPACHO_PRIORITARIO = (a, b) -> {
        // Si un criterio ya desempata, se devuelve; si no, se pasa al siguiente
        int resultado = POR_PRIORIDAD_DESC.compare(a, b);
        if (resultado != 0) {
            return resultado;
        }
        resultado = POR_TIEMPO_ASC.compare(a, b);
        if (resultado != 0) {
            return resultado;
        }
        return a.compareTo(b); // orden natural: código
    };
}