package service;

import java.util.List;
import java.util.Optional;

import excepciones.PersistenciaException;
import modelo.Sala;
import repository.Repository;

public class SalaService {

    private final Repository<Sala, Integer> salas;

    public SalaService(Repository<Sala, Integer> salas) {
        this.salas = salas;
    }

    public List<Sala> listar() throws PersistenciaException {
        return salas.findAll();
    }

    public Optional<Sala> buscar(int id) throws PersistenciaException {
        if (id <= 0) {
            throw new IllegalArgumentException("El identificador debe ser positivo");
        }

        return salas.findById(id);
    }

    public Sala guardar(Sala sala) throws PersistenciaException {
        if (sala == null) {
            throw new IllegalArgumentException("La sala no puede ser nula");
        }

        if (sala.getNombre() == null || sala.getNombre().isBlank()) {
            throw new IllegalArgumentException("El nombre de la sala es obligatorio");
        }

        if (sala.getFilas() <= 0 || sala.getAsientosPorFila() <= 0) {
            throw new IllegalArgumentException(
                    "La sala debe tener filas y asientos positivos");
        }

        return salas.save(sala);
    }

    public boolean eliminar(int id) throws PersistenciaException {
        if (id <= 0) {
            throw new IllegalArgumentException("El identificador debe ser positivo");
        }

        return salas.deleteById(id);
    }
}
