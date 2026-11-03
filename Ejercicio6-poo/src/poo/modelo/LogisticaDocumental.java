package poo.modelo;

import java.math.BigDecimal;

/** CC2008, Ejercicio 6. Logística que también emite guías XML de demostración. */
public final class LogisticaDocumental extends RastreoLogistico {
    public LogisticaDocumental(String id, String nombre, String endpoint, int latenciaMs,
                              boolean conectada, BigDecimal tarifaBase, BigDecimal precioKg) {
        super(id, nombre, endpoint, latenciaMs, conectada, tarifaBase, precioKg);
    }

    @Override
    public String getTipo() { return "Logística con guía XML"; }

    @Override
    public Resultado generarDocumento(String referencia) {
        if (!isConectada()) {
            return registrar(Resultado.error("Proveedor desconectado. No se generó la guía."));
        }
        if (!referenciaValida(referencia)) {
            return registrar(Resultado.error("Ingrese una referencia de 1 a 80 caracteres válidos."));
        }
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                + "<GuiaEnvio modo=\"SIMULACION\">\n  <Proveedor>" + escaparXml(getId())
                + "</Proveedor>\n  <Referencia>" + escaparXml(referencia.trim())
                + "</Referencia>\n  <Estado>PREPARADA</Estado>\n</GuiaEnvio>";
        return registrar(Resultado.exito("Guía XML de demostración generada.", xml));
    }
}
