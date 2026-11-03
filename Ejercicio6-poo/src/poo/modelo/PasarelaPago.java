package poo.modelo;

import java.math.BigDecimal;
import java.math.RoundingMode;

/** CC2008, Ejercicio 6. Reutiliza la lógica financiera de tres tipos de pago. */
public abstract class PasarelaPago extends Integracion {
    private BigDecimal saldoProcesado = new BigDecimal("0.00");

    protected PasarelaPago(String id, String nombre, String endpoint,
                           int latenciaMs, boolean conectada) {
        super(id, nombre, endpoint, latenciaMs, conectada);
    }

    public final BigDecimal getSaldoProcesado() { return saldoProcesado; }

    private boolean montoValido(BigDecimal monto) {
        return monto != null && monto.signum() > 0
                && monto.stripTrailingZeros().scale() <= 2
                && monto.compareTo(new BigDecimal("1000000.00")) <= 0;
    }

    @Override
    public Resultado cobrar(BigDecimal monto) {
        if (!isConectada()) {
            return registrar(Resultado.error("Proveedor desconectado. No se realizó el cobro."));
        }
        if (!montoValido(monto)) {
            return registrar(Resultado.error("El monto debe ser mayor que 0, hasta Q1,000,000 y tener como máximo 2 decimales."));
        }
        saldoProcesado = saldoProcesado.add(monto).setScale(2, RoundingMode.UNNECESSARY);
        return registrar(Resultado.exito("Cobro simulado: Q" + monto.setScale(2)
                + ". Saldo procesado: Q" + saldoProcesado + "."));
    }

    @Override
    public Resultado reintegrar(BigDecimal monto) {
        if (!isConectada()) {
            return registrar(Resultado.error("Proveedor desconectado. No se realizó el reintegro."));
        }
        if (!montoValido(monto)) {
            return registrar(Resultado.error("El monto debe ser mayor que 0, hasta Q1,000,000 y tener como máximo 2 decimales."));
        }
        if (monto.compareTo(saldoProcesado) > 0) {
            return registrar(Resultado.error("El reintegro supera el saldo cobrado de Q" + saldoProcesado + "."));
        }
        saldoProcesado = saldoProcesado.subtract(monto).setScale(2, RoundingMode.UNNECESSARY);
        return registrar(Resultado.exito("Reintegro simulado: Q" + monto.setScale(2)
                + ". Saldo procesado: Q" + saldoProcesado + "."));
    }

    @Override
    public String getConfiguracion() {
        return super.getConfiguracion() + "\nMoneda: GTQ\nSaldo procesado: Q" + saldoProcesado
                + "\nLímite por operación: Q1,000,000.00\nModalidad: " + getTipo();
    }
}
