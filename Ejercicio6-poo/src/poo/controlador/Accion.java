package poo.controlador;

/** CC2008, Ejercicio 6. Acciones compartidas por las vistas de consola y Swing. */
public enum Accion {
    COBRAR("Cobrar (Q)"), REINTEGRAR("Reintegrar (Q)"),
    DOCUMENTAR("Generar XML"), RASTREAR("Rastrear paquete"),
    COTIZAR("Cotizar envío (kg)"), AUDITAR("Ver auditoría");

    private final String etiqueta;
    Accion(String etiqueta) { this.etiqueta = etiqueta; }
    @Override
    public String toString() { return etiqueta; }
}
