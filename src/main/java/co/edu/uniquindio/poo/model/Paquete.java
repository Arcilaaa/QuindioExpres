package co.edu.uniquindio.poo.model;

public class Paquete implements Comparable<Paquete> {

    public static final int PRIORIDAD_DEFECTO = 3;
    public static final int PRIORIDAD_MIN = 1;
    public static final int PRIORIDAD_MAX = 5;

    // Atributos finales: un paquete no cambia después de creado
    private final String codigo;
    private final String destino;
    private final double peso;
    private final int prioridad;
    private final int tiempoEstimado;

    // Constructor con prioridad por defecto (3)
    public Paquete(String codigo, String destino, double peso, int tiempoEstimado) {
        this(codigo, destino, peso, PRIORIDAD_DEFECTO, tiempoEstimado);
    }

    public Paquete(String codigo, String destino, double peso, int prioridad, int tiempoEstimado) {
        // Validaciones de las reglas del enunciado: si algo es inválido se lanza excepción
        if (codigo == null || codigo.trim().isEmpty()) {
            throw new IllegalArgumentException("El código no puede ser nulo ni vacío.");
        }
        if (destino == null || destino.trim().isEmpty()) {
            throw new IllegalArgumentException("El destino no puede ser nulo ni vacío.");
        }
        if (peso <= 0) {
            throw new IllegalArgumentException("El peso debe ser mayor que cero.");
        }
        if (prioridad < PRIORIDAD_MIN || prioridad > PRIORIDAD_MAX) {
            throw new IllegalArgumentException("La prioridad debe estar entre 1 y 5.");
        }
        if (tiempoEstimado <= 0) {
            throw new IllegalArgumentException("El tiempo estimado debe ser mayor que cero.");
        }
        // trim() quita espacios sobrantes al inicio y al final
        this.codigo = codigo.trim();
        this.destino = destino.trim();
        this.peso = peso;
        this.prioridad = prioridad;
        this.tiempoEstimado = tiempoEstimado;
    }

    // Getters: solo lectura de los atributos
    public String getCodigo() { return codigo; }
    public String getDestino() { return destino; }
    public double getPeso() { return peso; }
    public int getPrioridad() { return prioridad; }
    public int getTiempoEstimado() { return tiempoEstimado; }

    // Orden natural: por código ascendente
    @Override
    public int compareTo(Paquete otro) {
        return this.codigo.compareTo(otro.codigo);
    }

    // equals y hashCode van juntos: HashMap y remove() de las colas los usan
    // Dos paquetes son iguales si tienen el mismo código
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Paquete)) return false;
        Paquete otro = (Paquete) o;
        return codigo.equals(otro.codigo);
    }

    @Override
    public int hashCode() {
        return codigo.hashCode();
    }

    @Override
    public String toString() {
        return String.format("%s [%s, %.1f kg, prioridad %d, %d min]",
                codigo, destino, peso, prioridad, tiempoEstimado);
    }
}
