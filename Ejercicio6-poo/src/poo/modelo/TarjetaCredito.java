package poo.modelo;

/** CC2008, Ejercicio 6. Proveedor de cobros con tarjeta (simulado). */
public final class TarjetaCredito extends PasarelaPago {
    public TarjetaCredito(String id, String nombre, String endpoint, int latenciaMs, boolean conectada) {
        super(id, nombre, endpoint, latenciaMs, conectada);
    }

    @Override
    public String getTipo() { return "Tarjeta de crédito"; }
}
