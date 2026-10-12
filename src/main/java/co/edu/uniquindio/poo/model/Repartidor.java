package co.edu.uniquindio.poo.model;

public class Repartidor {

    private final String identificacion;
    private final String nombre;
    private final String zona;
    private Boolean disponible;

    public Repartidor(String identificacion, String nombre, String zona, boolean disponible) {
        if (identificacion == null || identificacion.trim().isEmpty()) {
            throw new IllegalArgumentException("El identificacion es obligatorio");
        }
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre es obligatorio");
        }
        if (zona == null || zona.trim().isEmpty()) {
            throw new IllegalArgumentException("El zona es obligatorio");
        }

        this.identificacion = identificacion.trim();
        this.nombre = nombre.trim();
        this.zona = zona.trim();
        this.disponible = disponible;
    }


    public String getIdentificacion() {
        return identificacion;
    }

    public String getNombre() {
        return nombre;
    }

    public String getZona() {
        return zona;
    }

    public Boolean getDisponible() {
        return disponible;
    }

    public void setDisponible(Boolean disponible) {
        this.disponible = disponible;
    }

    public boolean puedeAtender(Paquete paquete) {
        return disponible && zona.equalsIgnoreCase(paquete.getDestino());
    }

    @Override
    public String toString() {
        return "Repartidor{" +
                "identificacion='" + identificacion + '\'' +
                ", nombre='" + nombre + '\'' +
                ", zona='" + zona + '\'' +
                ", disponible=" + disponible +
                '}';
    }
}
