package repository;

import java.util.List;

import excepciones.PersistenciaException;

public interface VentaRepository {

    int vender(Integer clienteId, int sesionId, List<String> asientos)
            throws PersistenciaException;
}
