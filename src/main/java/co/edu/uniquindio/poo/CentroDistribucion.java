package co.edu.uniquindio.poo;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.TreeSet;

/**
 * Clase principal de la lógica: usa cada estructura según lo definido en la Parte A.
 */
public class CentroDistribucion {

    // Todos los paquetes en orden exacto de registro (auditoría)
    private final List<Paquete> registro = new ArrayList<>();

    // Necesidad 2 y 7: consulta por código en O(1) promedio
    private final Map<String, Paquete> paquetesPorCodigo = new HashMap<>();

    // Necesidad 1: pendientes en orden de llegada (FIFO)
    private final Queue<Paquete> colaLlegada = new ArrayDeque<>();

    // Necesidad 5: pendientes por prioridad
    private final PriorityQueue<Paquete> colaPrioridad =
            new PriorityQueue<>(Comparadores.DESPACHO_PRIORITARIO);

    // Necesidad 3: municipios sin repetir
    private final Set<String> municipios = new HashSet<>();

    // Necesidad 4: municipios ordenados y sin repetir
    private final TreeSet<String> municipiosOrdenados = new TreeSet<>();

    // Municipio -> paquetes de ese municipio
    private final Map<String, List<Paquete>> paquetesPorMunicipio = new HashMap<>();

    // Repartidores por identificación (LinkedHashMap conserva el orden de registro)
    private final Map<String, Repartidor> repartidores = new LinkedHashMap<>();

    // Código del paquete despachado -> repartidor que lo lleva
    private final Map<String, Repartidor> asignaciones = new HashMap<>();

    // Necesidad 6: historial con la lista enlazada propia
    private final ListaEnlazada<Paquete> historial = new ListaEnlazada<>();

    //  Paquetes

    public void registrarPaquete(Paquete paquete) {
        if (paquetesPorCodigo.containsKey(paquete.getCodigo())) {
            throw new IllegalArgumentException("Ya existe un paquete con el código " + paquete.getCodigo());
        }
        String destino = paquete.getDestino();

        // Se guarda en todas las estructuras a la vez para mantenerlas sincronizadas
        registro.add(paquete);
        paquetesPorCodigo.put(paquete.getCodigo(), paquete);
        colaLlegada.offer(paquete);
        colaPrioridad.offer(paquete);

        // Los Set ignoran automáticamente los municipios repetidos
        municipios.add(destino);
        municipiosOrdenados.add(destino);

        // Si el municipio aún no tiene lista, se crea
        if (!paquetesPorMunicipio.containsKey(destino)) {
            paquetesPorMunicipio.put(destino, new ArrayList<>());
        }
        paquetesPorMunicipio.get(destino).add(paquete);
    }

    public boolean existePaquete(String codigo) {
        return paquetesPorCodigo.containsKey(codigo);
    }

    // get() devuelve null si la clave no existe
    // Devuelve el paquete o null si no existe
    public Paquete consultarPaquete(String codigo) {
        return paquetesPorCodigo.get(codigo);
    }

    public List<Paquete> getRegistro() {
        return Collections.unmodifiableList(registro);
    }

    public int cantidadPendientes() {
        return colaLlegada.size();
    }

    // Municipios

    public Set<String> getMunicipios() {
        return Collections.unmodifiableSet(municipios);
    }

    public Set<String> getMunicipiosOrdenados() {
        return Collections.unmodifiableSet(municipiosOrdenados);
    }

    public List<Paquete> paquetesDeMunicipio(String municipio) {
        List<Paquete> lista = paquetesPorMunicipio.get(municipio);
        if (lista == null) {
            return new ArrayList<>();
        }
        return lista;
    }

    public Set<String> getMunicipiosConPaquetes() {
        return paquetesPorMunicipio.keySet();
    }

    //  Repartidores

    public void registrarRepartidor(Repartidor repartidor) {
        if (repartidores.containsKey(repartidor.getIdentificacion())) {
            throw new IllegalArgumentException(
                    "Ya existe un repartidor con la identificación " + repartidor.getIdentificacion());
        }
        repartidores.put(repartidor.getIdentificacion(), repartidor);
    }

    public Repartidor consultarRepartidor(String identificacion) {
        return repartidores.get(identificacion);
    }

    public List<Repartidor> getRepartidores() {
        return new ArrayList<>(repartidores.values());
    }

    // Despacho

    /**
     * Despacha el siguiente paquete y lo asigna a un repartidor.
     * @param prioritario true = por prioridad, false = por orden de llegada
     * @return el paquete despachado, o null si no hay pendientes o no hay
     *         repartidor disponible en la zona (en ese caso el paquete sigue pendiente).
     */
    public Paquete despacharSiguiente(boolean prioritario) {
        // peek() mira el siguiente sin sacarlo (aún no sabemos si hay repartidor)
        Paquete siguiente;
        if (prioritario) {
            siguiente = colaPrioridad.peek();
        } else {
            siguiente = colaLlegada.peek();
        }
        if (siguiente == null) {
            return null;
        }

        Repartidor repartidor = buscarRepartidor(siguiente);
        if (repartidor == null) {
            return null;
        }

        // Se retira de la cola usada y de la otra (para que no quede duplicado)
        if (prioritario) {
            colaPrioridad.poll();
            colaLlegada.remove(siguiente);
        } else {
            colaLlegada.poll();
            colaPrioridad.remove(siguiente);
        }

        // El repartidor queda ocupado y se anota qué paquete lleva
        repartidor.setDisponible(false);
        asignaciones.put(siguiente.getCodigo(), repartidor);
        return siguiente;
    }

    // Primer repartidor disponible cuya zona coincide con el destino
    private Repartidor buscarRepartidor(Paquete paquete) {
        for (Repartidor r : repartidores.values()) {
            if (r.puedeAtender(paquete)) {
                return r;
            }
        }
        return null;
    }

    public Repartidor getRepartidorAsignado(String codigo) {
        return asignaciones.get(codigo);
    }

    // Registra la entrega: pasa al historial y libera al repartidor
    public void entregarPaquete(String codigo) {
        Paquete paquete = paquetesPorCodigo.get(codigo);
        if (paquete == null) {
            throw new IllegalArgumentException("El paquete " + codigo + " no existe.");
        }
        // remove devuelve el repartidor asignado (o null si nunca fue despachado)
        Repartidor repartidor = asignaciones.remove(codigo);
        if (repartidor == null) {
            throw new IllegalStateException("El paquete " + codigo + " no ha sido despachado.");
        }
        repartidor.setDisponible(true);
        historial.agregar(paquete);
    }

    public ListaEnlazada<Paquete> getHistorial() {
        return historial;
    }

    //  Ordenamientos y algoritmos

    // Copia ordenada por un criterio (no modifica el registro original)
    public List<Paquete> ordenarPor(Comparator<Paquete> criterio) {
        // Se ordena una COPIA para no alterar el orden original de registro
        List<Paquete> copia = new ArrayList<>(registro);
        Collections.sort(copia, criterio);
        return copia;
    }

    // Copia ordenada por orden natural (código)
    public List<Paquete> ordenadosPorCodigo() {
        List<Paquete> copia = new ArrayList<>(registro);
        Collections.sort(copia);
        return copia;
    }

    // Busca por código usando búsqueda binaria sobre la lista ordenada
    public Paquete buscarBinario(String codigo) {
        // Precondición de la búsqueda binaria: la lista debe estar ordenada
        List<Paquete> ordenada = ordenadosPorCodigo();
        int posicion = Algoritmos.busquedaBinaria(ordenada, codigo);
        if (posicion == -1) {
            return null;
        }
        return ordenada.get(posicion);
    }

    public double pesoTotalMunicipio(String municipio) {
        return Algoritmos.pesoTotalMunicipio(registro, municipio, 0);
    }

    public int contarPrioridadMinima(int prioridadMinima) {
        return Algoritmos.contarPrioridadMinima(registro, prioridadMinima, 0);
    }

    public Paquete paqueteMayorPeso() {
        return Algoritmos.paqueteMayorPeso(registro);
    }
}