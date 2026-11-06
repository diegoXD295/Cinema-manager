package service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import excepciones.PersistenciaException;
import repository.VentaRepository;

public class VentaService {

    private final VentaRepository ventas;

    public VentaService(VentaRepository ventas) {
        this.ventas = ventas;
    }

    public int vender(Integer clienteId, int sesionId, List<String> asientos)
            throws PersistenciaException {
        if (sesionId <= 0 || asientos == null || asientos.isEmpty()) {
            throw new IllegalArgumentException("La sesión y los asientos son obligatorios");
        }

        Set<String> unicos = new HashSet<>();
        for (String asiento : asientos) {
            if (asiento == null || asiento.isBlank() || !unicos.add(asiento.trim())) {
                throw new IllegalArgumentException("Hay un asiento vacío o repetido");
            }
        }

        return ventas.vender(clienteId, sesionId,
                asientos.stream().map(String::trim).toList());
    }
}
