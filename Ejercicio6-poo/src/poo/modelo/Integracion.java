package poo.modelo;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Objects;

/** CC2008, Ejercicio 6. Identidad, configuración y auditoría comunes a las APIs. */
public abstract class Integracion implements Transaccional, Documentable,
        Consultable, Auditable, Comparable<Integracion> {
    private final String id;
    private final String nombre;
    private final String endpoint;
    private int latenciaMs;
    private int timeoutMs;
    private boolean conectada;
    // La bitácora contiene texto; no crea una segunda colección de integraciones.
    private final StringBuilder bitacora = new StringBuilder();

    protected Integracion(String id, String nombre, String endpoint,
                          int latenciaMs, boolean conectada) {
        if (!textoValido(id) || !textoValido(nombre) || !textoValido(endpoint)) {
            throw new IllegalArgumentException("La configuración requiere ID, nombre y endpoint.");
        }
        this.id = id.trim().toUpperCase(Locale.ROOT);
        this.nombre = nombre.trim();
        this.endpoint = endpoint.trim();
        setLatenciaMs(latenciaMs);
        setTimeoutMs(5000);
        this.conectada = conectada;
        registrar(Resultado.exito("Integración registrada en modo demostración."));
    }

    public abstract String getTipo();
    public final String getId() { return id; }
    public final String getNombre() { return nombre; }
    public final String getEndpoint() { return endpoint; }
    public final int getLatenciaMs() { return latenciaMs; }
    public final int getTimeoutMs() { return timeoutMs; }
    public final boolean isConectada() { return conectada; }

    public final void setLatenciaMs(int latenciaMs) {
        if (latenciaMs < 0) {
            throw new IllegalArgumentException("La latencia no puede ser negativa.");
        }
        this.latenciaMs = latenciaMs;
    }

    public final void setTimeoutMs(int timeoutMs) {
        if (timeoutMs <= 0) {
            throw new IllegalArgumentException("El timeout debe ser positivo.");
        }
        this.timeoutMs = timeoutMs;
    }

    public final void setConectada(boolean conectada) {
        this.conectada = conectada;
        registrar(Resultado.exito("Estado de conexión: " + getEstadoConexion()));
    }

    public final String getEstadoConexion() {
        return conectada ? "CONECTADA" : "DESCONECTADA";
    }

    public String getConfiguracion() {
        return "ID: " + id + "\nProveedor: " + nombre + "\nTipo: " + getTipo()
                + "\nEndpoint: " + endpoint + "\nConexión: " + getEstadoConexion()
                + "\nLatencia promedio simulada: " + latenciaMs + " ms"
                + "\nTimeout: " + timeoutMs + " ms\nEntorno: demostración local"
                + "\nAutenticación: ninguna (sin credenciales reales)";
    }

    protected final Resultado registrar(Resultado resultado) {
        bitacora.append(LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                .append(" | ").append(resultado.getEstado()).append(" | ")
                .append(resultado.getMensaje()).append('\n');
        return resultado;
    }

    @Override
    public final Resultado consultarAuditoria() {
        return Resultado.exito("Auditoría de " + id, bitacora.toString());
    }

    // Se conserva la política default, agregando auditoría común a las omisiones.
    @Override
    public Resultado cobrar(java.math.BigDecimal monto) {
        return registrar(Transaccional.super.cobrar(monto));
    }

    @Override
    public Resultado reintegrar(java.math.BigDecimal monto) {
        return registrar(Transaccional.super.reintegrar(monto));
    }

    @Override
    public Resultado generarDocumento(String referencia) {
        return registrar(Documentable.super.generarDocumento(referencia));
    }

    @Override
    public Resultado consultar(String referencia) {
        return registrar(Consultable.super.consultar(referencia));
    }

    @Override
    public Resultado cotizar(java.math.BigDecimal pesoKg) {
        return registrar(Consultable.super.cotizar(pesoKg));
    }

    protected static boolean textoValido(String valor) {
        return valor != null && !valor.trim().isEmpty();
    }

    /** Evita XML inválido y rechaza caracteres de control no permitidos por XML 1.0. */
    protected static boolean referenciaValida(String valor) {
        if (!textoValido(valor) || valor.trim().length() > 80) {
            return false;
        }
        return valor.codePoints().allMatch(c -> c == 9 || c == 10 || c == 13
                || (c >= 32 && c <= 0xD7FF) || (c >= 0xE000 && c <= 0xFFFD)
                || (c >= 0x10000 && c <= 0x10FFFF));
    }

    protected static String escaparXml(String texto) {
        return texto.replace("&", "&amp;").replace("<", "&lt;")
                .replace(">", "&gt;").replace("\"", "&quot;").replace("'", "&apos;");
    }

    @Override
    public final int compareTo(Integracion otra) {
        int orden = Integer.compare(latenciaMs, otra.latenciaMs);
        return orden == 0 ? id.compareTo(otra.id) : orden;
    }

    /** La identidad no cambia al modificar la conexión o la latencia. */
    @Override
    public final boolean equals(Object objeto) {
        return this == objeto || (objeto instanceof Integracion
                && id.equals(((Integracion) objeto).id));
    }

    @Override
    public final int hashCode() { return Objects.hash(id); }

    @Override
    public String toString() {
        return id + " | " + nombre + " | " + getTipo() + " | "
                + getEstadoConexion() + " | " + latenciaMs + " ms";
    }
}
