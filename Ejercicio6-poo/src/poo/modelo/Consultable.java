package poo.modelo;

import java.math.BigDecimal;

/** CC2008, Ejercicio 6. Consultas logísticas sin transacciones financieras. */
public interface Consultable {
    default Resultado consultar(String referencia) {
        return Resultado.ignorada("Esta integración no ofrece rastreo de paquetes.");
    }

    default Resultado cotizar(BigDecimal pesoKg) {
        return Resultado.ignorada("Esta integración no cotiza envíos.");
    }
}
