package poo.controlador;

import java.math.BigDecimal;
import java.util.Optional;
import poo.modelo.CatalogoIntegraciones;
import poo.modelo.Integracion;
import poo.modelo.MotorCheckout;
import poo.modelo.Resultado;

/** CC2008, Ejercicio 6. Coordina los casos de uso sin realizar entrada/salida. */
public final class ControladorIntegraciones {
    private final CatalogoIntegraciones catalogo;
    private final MotorCheckout motor;

    public ControladorIntegraciones(CatalogoIntegraciones catalogo, MotorCheckout motor) {
        this.catalogo = catalogo;
        this.motor = motor;
    }

    public int cantidad() { return catalogo.cantidad(); }

    /** Proyección por celda: la tabla no guarda copias de objetos del catálogo. */
    public String celda(int fila, int columna) {
        Integracion integracion = catalogo.obtener(fila);
        return switch (columna) {
            case 0 -> integracion.getId();
            case 1 -> integracion.getNombre();
            case 2 -> integracion.getTipo();
            case 3 -> integracion.getEstadoConexion();
            case 4 -> Integer.toString(integracion.getLatenciaMs());
            default -> "";
        };
    }

    public Resultado buscar(String id) {
        Optional<Integracion> encontrada = catalogo.buscar(id);
        return encontrada.isPresent()
                ? Resultado.exito("Configuración de " + encontrada.get().getId(), encontrada.get().getConfiguracion())
                : Resultado.error("No existe una integración con ese identificador.");
    }

    public Resultado ordenar() {
        catalogo.ordenarPorLatencia();
        return Resultado.exito("Catálogo ordenado de menor a mayor latencia (ms); desempate por ID.");
    }

    public Resultado ejecutar(String id, Accion accion, String dato) {
        Optional<Integracion> encontrada = catalogo.buscar(id);
        if (encontrada.isEmpty()) {
            return Resultado.error("Ingrese o seleccione un identificador existente.");
        }
        if (accion == null) { return Resultado.error("Seleccione una operación."); }
        Integracion integracion = encontrada.get();
        if (accion == Accion.DOCUMENTAR) { return motor.procesar(integracion, dato); }
        if (accion == Accion.RASTREAR) { return motor.consultar(integracion, dato); }
        if (accion == Accion.AUDITAR) { return motor.auditar(integracion); }
        String numero = dato == null ? "" : dato.trim().replace(',', '.');
        if (!numero.matches("[0-9]{1,10}(\\.[0-9]{1,2})?")) {
            return Resultado.error("Ingrese un número sin signo ni separador de miles, con hasta 2 decimales.");
        }
        BigDecimal valor = new BigDecimal(numero);
        return switch (accion) {
            case COBRAR -> motor.procesar(integracion, valor);
            case REINTEGRAR -> motor.procesar(integracion, valor, true);
            case COTIZAR -> motor.cotizar(integracion, valor);
            default -> Resultado.error("Operación no disponible.");
        };
    }
}
