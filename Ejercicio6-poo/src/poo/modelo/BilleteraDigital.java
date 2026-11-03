package poo.modelo;

/** CC2008, Ejercicio 6. Proveedor de billetera digital simulada. */
public final class BilleteraDigital extends PasarelaPago {
    public BilleteraDigital(String id, String nombre, String endpoint, int latenciaMs, boolean conectada) {
        super(id, nombre, endpoint, latenciaMs, conectada);
    }

    @Override
    public String getTipo() { return "Billetera digital"; }
}
