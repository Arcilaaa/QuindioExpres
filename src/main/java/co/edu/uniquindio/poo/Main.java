package co.edu.uniquindio.poo;

import java.util.Comparator;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        // Objeto que contiene toda la lógica del sistema
        CentroDistribucion centro = new CentroDistribucion();

        cargarDatos(centro);

        System.out.println(" 1. REGISTRO Y VALIDACIONES ");
        System.out.println("Paquetes registrados: " + centro.getRegistro().size());
        System.out.println("Repartidores registrados: " + centro.getRepartidores().size());
        probarValidaciones(centro);

        System.out.println("Consulta de repartidor R004: " + centro.consultarRepartidor("R004"));
        System.out.println("Repartidores:");
        for (Repartidor r : centro.getRepartidores()) {
            System.out.println("  " + r);
        }

        System.out.println("\n 2. CONSULTA POR CÓDIGO (HashMap) ");
        System.out.println("¿Existe PQ731? " + centro.existePaquete("PQ731"));
        System.out.println("Datos PQ731: " + centro.consultarPaquete("PQ731"));
        System.out.println("¿Existe PQ000? " + centro.existePaquete("PQ000"));

        System.out.println("\n 3. MUNICIPIOS ");
        System.out.println("Sin repetir (HashSet): " + centro.getMunicipios());
        System.out.println("Ordenados (TreeSet):   " + centro.getMunicipiosOrdenados());
        for (String municipio : centro.getMunicipiosOrdenados()) {
            System.out.println(municipio + " -> " + codigos(centro.paquetesDeMunicipio(municipio)));
        }

        System.out.println("\n 4. DESPACHO POR ORDEN DE LLEGADA (Queue)");
        // El 3.º paquete es de Armenia y sus 2 repartidores están ocupados: no se despacha
        for (int i = 1; i <= 3; i++) {
            Paquete p = centro.despacharSiguiente(false);
            mostrarDespacho(centro, p);
        }
        System.out.println("-> Entregamos PQ731 y se libera su repartidor.");
        centro.entregarPaquete("PQ731");
        mostrarDespacho(centro, centro.despacharSiguiente(false));

        System.out.println("\n5. DESPACHO PRIORITARIO (PriorityQueue) ");
        // Se entregan los paquetes en ruta para liberar a los repartidores de Armenia
        centro.entregarPaquete("PQ942");
        centro.entregarPaquete("PQ829");
        // Orden esperado: PQ603 (5, 15 min), PQ321 (5, 18), PQ105 (5, 25), PQ735 (5, 25)
        for (int i = 1; i <= 4; i++) {
            mostrarDespacho(centro, centro.despacharSiguiente(true));
        }
        System.out.println("Pendientes: " + centro.cantidadPendientes());

        System.out.println("\n 6. HISTORIAL (lista enlazada propia) ");
        centro.entregarPaquete("PQ603");
        centro.entregarPaquete("PQ321");
        ListaEnlazada<Paquete> historial = centro.getHistorial();
        System.out.println("Tamaño: " + historial.tamanio());
        System.out.print("Orden de entrega: ");
        for (Paquete p : historial) {          // for-each gracias a Iterable/Iterator
            System.out.print(p.getCodigo() + " -> ");
        }
        System.out.println("fin");
        System.out.println("Posición 1: " + historial.obtener(1).getCodigo());

        System.out.println("\n7. PRUEBAS DE LA LISTA ENLAZADA (genérica) ");
        probarLista();

        System.out.println("\n 8. RECURSIVIDAD ");
        System.out.printf("Peso total en Armenia: %.1f kg%n", centro.pesoTotalMunicipio("Armenia"));
        System.out.println("Paquetes con prioridad >= 4: " + centro.contarPrioridadMinima(4));

        System.out.println("\n 9. BÚSQUEDA BINARIA (lista ordenada por código) ");
        System.out.println("Buscar PQ731: " + centro.buscarBinario("PQ731"));
        System.out.println("Buscar PQ000: " + centro.buscarBinario("PQ000"));

        System.out.println("\n10. DIVIDE Y VENCERÁS ");
        System.out.println("Paquete de mayor peso: " + centro.paqueteMayorPeso());

        System.out.println("\n 11. ORDENAMIENTOS (Comparable y Comparator) ");
        System.out.println("Orden natural (código): " + codigos(centro.ordenadosPorCodigo()));
        mostrarOrden("Prioridad desc", centro.ordenarPor(Comparadores.POR_PRIORIDAD_DESC));
        mostrarOrden("Peso desc", centro.ordenarPor(Comparadores.POR_PESO_DESC));
        mostrarOrden("Tiempo asc", centro.ordenarPor(Comparadores.POR_TIEMPO_ASC));
        System.out.println("El registro original no cambió: " + codigos(centro.getRegistro().subList(0, 5)) + " ...");
    }

    //  Datos iniciales

    private static void cargarDatos(CentroDistribucion c) {
        // Repartidores (6 o más; dos en Armenia)
        c.registrarRepartidor(new Repartidor("R001", "Carlos Gómez", "Armenia", true));
        c.registrarRepartidor(new Repartidor("R002", "Laura Ríos", "Armenia", true));
        c.registrarRepartidor(new Repartidor("R003", "Andrés Mora", "Calarcá", true));
        c.registrarRepartidor(new Repartidor("R004", "Marta Pérez", "Salento", true));
        c.registrarRepartidor(new Repartidor("R005", "Julián Ortiz", "Montenegro", true));
        c.registrarRepartidor(new Repartidor("R006", "Sofía Díaz", "Quimbaya", true));
        c.registrarRepartidor(new Repartidor("R007", "Pedro Castaño", "Circasia", true));

        // 30 paquetes: código, destino, peso, prioridad, tiempo
        c.registrarPaquete(new Paquete("PQ731", "Armenia", 4.5, 5, 45));
        c.registrarPaquete(new Paquete("PQ942", "Armenia", 7.2, 4, 20));
        c.registrarPaquete(new Paquete("PQ829", "Armenia", 2.3, 2, 35));
        c.registrarPaquete(new Paquete("PQ105", "Salento", 3.0, 5, 25));
        c.registrarPaquete(new Paquete("PQ318", "Calarcá", 12.0, 3, 30));
        c.registrarPaquete(new Paquete("PQ567", "Salento", 5.5, 4, 40));
        c.registrarPaquete(new Paquete("PQ214", "Armenia", 6.0, 3, 25));
        c.registrarPaquete(new Paquete("PQ421", "Calarcá", 3.2, 4, 28));
        c.registrarPaquete(new Paquete("PQ790", "Calarcá", 18.7, 5, 33));
        c.registrarPaquete(new Paquete("PQ249", "Montenegro", 4.0, 42)); // prioridad por defecto (3)
        c.registrarPaquete(new Paquete("PQ513", "Montenegro", 7.4, 4, 36));
        c.registrarPaquete(new Paquete("PQ836", "Quimbaya", 2.7, 3, 52));
        c.registrarPaquete(new Paquete("PQ195", "Quimbaya", 13.3, 4, 44));
        c.registrarPaquete(new Paquete("PQ321", "Circasia", 3.9, 5, 18));
        c.registrarPaquete(new Paquete("PQ659", "Circasia", 6.2, 3, 31));
        c.registrarPaquete(new Paquete("PQ388", "Salento", 2.2, 2, 55));
        c.registrarPaquete(new Paquete("PQ457", "Armenia", 1.8, 1, 50));
        c.registrarPaquete(new Paquete("PQ603", "Armenia", 9.4, 5, 15));
        c.registrarPaquete(new Paquete("PQ876", "Salento", 8.1, 3, 60));
        c.registrarPaquete(new Paquete("PQ152", "Calarcá", 0.9, 1, 22));
        c.registrarPaquete(new Paquete("PQ964", "Calarcá", 6.6, 2, 38));
        c.registrarPaquete(new Paquete("PQ682", "Montenegro", 5.1, 2, 48));
        c.registrarPaquete(new Paquete("PQ377", "Montenegro", 10.5, 5, 27));
        c.registrarPaquete(new Paquete("PQ740", "Quimbaya", 1.5, 1, 65));
        c.registrarPaquete(new Paquete("PQ468", "Quimbaya", 9.0, 2, 58));
        c.registrarPaquete(new Paquete("PQ904", "Circasia", 4.8, 4, 23));
        c.registrarPaquete(new Paquete("PQ287", "Circasia", 11.1, 2, 46));
        c.registrarPaquete(new Paquete("PQ572", "Circasia", 0.7, 1, 20));
        c.registrarPaquete(new Paquete("PQ118", "Armenia", 5.6, 4, 20));
        c.registrarPaquete(new Paquete("PQ735", "Salento", 2.9, 5, 25));
    }

    // Apoyo para la demostración

    private static void probarValidaciones(CentroDistribucion c) {
        try {
            c.registrarPaquete(new Paquete("PQ731", "Armenia", 1.0, 3, 10));
        } catch (IllegalArgumentException e) {
            System.out.println("Código repetido rechazado: " + e.getMessage());
        }
        try {
            c.registrarPaquete(new Paquete("PQ999", "Armenia", -2.0, 3, 10));
        } catch (IllegalArgumentException e) {
            System.out.println("Peso inválido rechazado: " + e.getMessage());
        }
        try {
            c.registrarPaquete(new Paquete("PQ998", "Armenia", 2.0, 9, 10));
        } catch (IllegalArgumentException e) {
            System.out.println("Prioridad inválida rechazada: " + e.getMessage());
        }
        try {
            c.registrarRepartidor(new Repartidor("R001", "Otro", "Armenia", true));
        } catch (IllegalArgumentException e) {
            System.out.println("Repartidor repetido rechazado: " + e.getMessage());
        }
    }

    private static void mostrarDespacho(CentroDistribucion c, Paquete p) {
        if (p == null) {
            System.out.println("No se pudo despachar (sin pendientes o sin repartidor disponible en la zona).");
        } else {
            System.out.println("Despachado " + p + " -> " + c.getRepartidorAsignado(p.getCodigo()));
        }
    }

    private static String codigos(List<Paquete> lista) {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < lista.size(); i++) {
            sb.append(lista.get(i).getCodigo());
            if (i < lista.size() - 1) {
                sb.append(", ");
            }
        }
        return sb.append("]").toString();
    }

    private static void mostrarOrden(String titulo, List<Paquete> lista) {
        System.out.println(titulo + " (primeros 5): " + codigos(lista.subList(0, 5)));
    }

    // Casos límite de la lista enlazada propia, usando Strings (prueba de genericidad)
    private static void probarLista() {
        ListaEnlazada<String> lista = new ListaEnlazada<>();
        System.out.println("Vacía? " + lista.estaVacia() + ", eliminar en vacía: " + lista.eliminar("A"));

        lista.agregar("A");
        System.out.println("Un elemento, eliminar A: " + lista.eliminar("A") + ", vacía? " + lista.estaVacia());

        lista.agregar("A");
        lista.agregar("B");
        lista.agregar("C");
        lista.agregar("D");
        System.out.println("Eliminar primero (A): " + lista.eliminar("A"));
        System.out.println("Eliminar intermedio (C): " + lista.eliminar("C"));
        System.out.println("Eliminar último (D): " + lista.eliminar("D"));
        System.out.println("Eliminar inexistente (Z): " + lista.eliminar("Z"));

        lista.agregar("E"); // verifica que 'ultimo' quedó bien actualizado
        System.out.print("Contenido final: ");
        for (String s : lista) {
            System.out.print(s + " ");
        }
        System.out.println("| tamaño = " + lista.tamanio());

        try {
            lista.obtener(10);
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Posición inválida: " + e.getMessage());
        }
    }
}