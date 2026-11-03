package poo.modelo;

import java.math.BigDecimal;
import java.math.RoundingMode;

/** CC2008, Ejercicio 6. API que únicamente rastrea y cotiza envíos. */
public class RastreoLogistico extends Integracion {
    private final BigDecimal tarifaBase;
    private final BigDecimal precioKg;

    public RastreoLogistico(String id, String nombre, String endpoint, int latenciaMs,
                           boolean conectada, BigDecimal tarifaBase, BigDecimal precioKg) {
        super(id, nombre, endpoint, latenciaMs, conectada);
        if (tarifaBase == null || precioKg == null
                || tarifaBase.signum() < 0 || precioKg.signum() < 0) {
            throw new IllegalArgumentException("Las tarifas deben ser no negativas.");
        }
        this.tarifaBase = tarifaBase;
        this.precioKg = precioKg;
    }

    public final BigDecimal getTarifaBase() { return tarifaBase; }
    public final BigDecimal getPrecioKg() { return precioKg; }

    @Override
    public String getTipo() { return "Rastreo y cotización"; }

    @Override
    public Resultado consultar(String referencia) {
        if (!isConectada()) {
            return registrar(Resultado.error("Proveedor desconectado. Rastreo no disponible."));
        }
        if (!referenciaValida(referencia)) {
            return registrar(Resultado.error("Ingrese una referencia de 1 a 80 caracteres válidos."));
        }
        return registrar(Resultado.exito("Rastreo simulado de " + referencia.trim() + ": EN TRÁNSITO."));
    }

    @Override
    public Resultado cotizar(BigDecimal pesoKg) {
        if (!isConectada()) {
            return registrar(Resultado.error("Proveedor desconectado. Cotización no disponible."));
        }
        if (pesoKg == null || pesoKg.signum() <= 0
                || pesoKg.compareTo(new BigDecimal("1000")) > 0
                || pesoKg.stripTrailingZeros().scale() > 2) {
            return registrar(Resultado.error("El peso debe estar entre 0.01 y 1000 kg, con hasta 2 decimales."));
        }
        BigDecimal total = tarifaBase.add(precioKg.multiply(pesoKg)).setScale(2, RoundingMode.HALF_UP);
        return registrar(Resultado.exito("Cotización simulada para " + pesoKg.toPlainString()
                + " kg: Q" + total + "."));
    }

    @Override
    public String getConfiguracion() {
        return super.getConfiguracion() + "\nMoneda: GTQ\nTarifa base: Q" + tarifaBase
                + "\nPrecio por kg: Q" + precioKg + "\nPeso máximo: 1000 kg";
    }
}
