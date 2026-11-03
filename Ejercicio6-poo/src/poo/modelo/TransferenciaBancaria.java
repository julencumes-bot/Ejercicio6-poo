package poo.modelo;

/** CC2008, Ejercicio 6. Proveedor de transferencias bancarias simuladas. */
public final class TransferenciaBancaria extends PasarelaPago {
    public TransferenciaBancaria(String id, String nombre, String endpoint, int latenciaMs, boolean conectada) {
        super(id, nombre, endpoint, latenciaMs, conectada);
    }

    @Override
    public String getTipo() { return "Transferencia bancaria"; }
}
