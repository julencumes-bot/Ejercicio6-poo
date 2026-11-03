package poo.modelo;

/** CC2008, Ejercicio 6. Valor inmutable que comunica el resultado sin imprimir. */
public final class Resultado {
    public enum Estado { EXITO, IGNORADA, ERROR }
    private final Estado estado;
    private final String mensaje;
    private final String contenido;

    private Resultado(Estado estado, String mensaje, String contenido) {
        this.estado = estado;
        this.mensaje = mensaje;
        this.contenido = contenido;
    }

    public static Resultado exito(String mensaje) {
        return exito(mensaje, "");
    }

    /** Sobrecarga útil para devolver también una configuración o un XML. */
    public static Resultado exito(String mensaje, String contenido) {
        return new Resultado(Estado.EXITO, mensaje, contenido);
    }

    public static Resultado ignorada(String mensaje) {
        return new Resultado(Estado.IGNORADA, mensaje, "");
    }

    public static Resultado error(String mensaje) {
        return new Resultado(Estado.ERROR, mensaje, "");
    }

    public Estado getEstado() { return estado; }
    public String getMensaje() { return mensaje; }
    public String getContenido() { return contenido; }

    @Override
    public String toString() {
        return "[" + estado + "] " + mensaje
                + (contenido.isEmpty() ? "" : "\n" + contenido);
    }
}
