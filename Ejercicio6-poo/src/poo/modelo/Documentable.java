package poo.modelo;

/** CC2008, Ejercicio 6. Generación opcional de documentos XML de demostración. */
public interface Documentable {
    default Resultado generarDocumento(String referencia) {
        return Resultado.ignorada("Esta integración no genera documentos; el proceso continúa.");
    }
}
