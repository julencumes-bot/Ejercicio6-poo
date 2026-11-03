package poo.modelo;

/** CC2008, Ejercicio 6. Simula un documento FEL; no tiene validez fiscal. */
public final class CertificadorFel extends Integracion {
    public CertificadorFel(String id, String nombre, String endpoint, int latenciaMs, boolean conectada) {
        super(id, nombre, endpoint, latenciaMs, conectada);
    }

    @Override
    public String getTipo() { return "Certificador FEL"; }

    @Override
    public Resultado generarDocumento(String referencia) {
        if (!isConectada()) {
            return registrar(Resultado.error("Certificador desconectado. No se generó el XML."));
        }
        if (!referenciaValida(referencia)) {
            return registrar(Resultado.error("Ingrese una referencia de 1 a 80 caracteres válidos."));
        }
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                + "<FacturaDemo modo=\"SIMULACION\" validezFiscal=\"false\">\n"
                + "  <Certificador>" + escaparXml(getId()) + "</Certificador>\n"
                + "  <Referencia>" + escaparXml(referencia.trim()) + "</Referencia>\n"
                + "  <Moneda>GTQ</Moneda>\n</FacturaDemo>";
        return registrar(Resultado.exito("XML FEL de demostración generado (sin validez fiscal).", xml));
    }

    @Override
    public String getConfiguracion() {
        return super.getConfiguracion() + "\nFormato: XML de demostración\nValidez fiscal: no";
    }
}
