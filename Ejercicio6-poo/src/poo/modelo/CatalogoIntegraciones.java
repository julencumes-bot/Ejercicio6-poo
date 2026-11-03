package poo.modelo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/** CC2008, Ejercicio 6. Única estructura polimórfica que almacena las integraciones. */
public final class CatalogoIntegraciones {
    private final List<Integracion> integraciones = new ArrayList<>();

    public Resultado agregar(Integracion integracion) {
        if (integracion == null) {
            return Resultado.error("No se puede registrar una integración nula.");
        }
        if (buscar(integracion.getId()).isPresent()) {
            return Resultado.error("Ya existe el identificador " + integracion.getId() + ".");
        }
        integraciones.add(integracion);
        return Resultado.exito("Integración registrada: " + integracion.getId());
    }

    public Optional<Integracion> buscar(String id) {
        if (id != null) {
            for (Integracion integracion : integraciones) {
                if (integracion.getId().equalsIgnoreCase(id.trim())) {
                    return Optional.of(integracion);
                }
            }
        }
        return Optional.empty();
    }

    public Integracion obtener(int indice) { return integraciones.get(indice); }
    public int cantidad() { return integraciones.size(); }

    public void ordenarPorLatencia() {
        // Ordena la lista existente usando Integracion.compareTo; no crea otra lista.
        Collections.sort(integraciones);
    }
}
