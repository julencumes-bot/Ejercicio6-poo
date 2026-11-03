package poo.modelo;

import java.math.BigDecimal;

/** CC2008, Ejercicio 6. Despacha por contratos, sin conocer proveedores concretos. */
public final class MotorCheckout {
    public Resultado procesar(Transaccional servicio, BigDecimal monto) {
        return procesar(servicio, monto, false);
    }

    public Resultado procesar(Transaccional servicio, BigDecimal monto, boolean reintegro) {
        if (servicio == null) { return Resultado.error("Servicio no disponible."); }
        return reintegro ? servicio.reintegrar(monto) : servicio.cobrar(monto);
    }

    /** Mismo nombre, contrato y parámetros distintos: overloading real. */
    public Resultado procesar(Documentable servicio, String referencia) {
        return servicio == null ? Resultado.error("Servicio no disponible.")
                : servicio.generarDocumento(referencia);
    }

    public Resultado consultar(Consultable servicio, String referencia) {
        return servicio == null ? Resultado.error("Servicio no disponible.")
                : servicio.consultar(referencia);
    }

    public Resultado cotizar(Consultable servicio, BigDecimal peso) {
        return servicio == null ? Resultado.error("Servicio no disponible.")
                : servicio.cotizar(peso);
    }

    public Resultado auditar(Auditable servicio) {
        return servicio == null ? Resultado.error("Servicio no disponible.")
                : servicio.consultarAuditoria();
    }
}
