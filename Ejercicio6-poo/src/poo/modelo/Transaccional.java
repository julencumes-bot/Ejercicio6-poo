package poo.modelo;

import java.math.BigDecimal;

/** CC2008, Ejercicio 6. Contrato financiero con capacidad opcional explícita. */
public interface Transaccional {
    default Resultado cobrar(BigDecimal monto) {
        return Resultado.ignorada("Esta integración no admite cobros; el proceso continúa.");
    }

    default Resultado reintegrar(BigDecimal monto) {
        return Resultado.ignorada("Esta integración no admite reintegros; el proceso continúa.");
    }
}
