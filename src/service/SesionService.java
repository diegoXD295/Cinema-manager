package service;

import java.util.List;
import java.util.Optional;

import excepciones.PersistenciaException;
import modelo.Sesion;
import repository.Repository;

public class SesionService {

    private final Repository<Sesion, Integer> sesiones;

    public SesionService(Repository<Sesion, Integer> sesiones) {
        this.sesiones = sesiones;
    }

    public List<Sesion> listar() throws PersistenciaException {
        return sesiones.findAll();
    }

    public Optional<Sesion> buscar(int id) throws PersistenciaException {
        if (id <= 0) {
            throw new IllegalArgumentException("El identificador debe ser positivo");
        }
        return sesiones.findById(id);
    }

    public Sesion guardar(Sesion sesion) throws PersistenciaException {
        if (sesion == null || sesion.getFechaHora() == null || sesion.getPrecio() == null) {
            throw new IllegalArgumentException("Los datos de la sesión son obligatorios");
        }
        if (sesion.getPeliculaId() <= 0 || sesion.getSalaId() <= 0
                || sesion.getPrecio().signum() < 0 || sesion.getPlazasDisponibles() < 0) {
            throw new IllegalArgumentException("Los datos numéricos de la sesión no son válidos");
        }
        return sesiones.save(sesion);
    }

    public boolean eliminar(int id) throws PersistenciaException {
        if (id <= 0) {
            throw new IllegalArgumentException("El identificador debe ser positivo");
        }
        return sesiones.deleteById(id);
    }
}
