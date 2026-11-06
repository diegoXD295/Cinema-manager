package service;

import excepciones.PersistenciaException;
import modelo.EstadisticaPelicula;
import repository.EstadisticasRepository;

public class EstadisticasService {

    private final EstadisticasRepository estadisticas;

    public EstadisticasService(EstadisticasRepository estadisticas) {
        this.estadisticas = estadisticas;
    }

    public EstadisticaPelicula obtenerPorPelicula(int peliculaId)
            throws PersistenciaException {
        if (peliculaId <= 0) {
            throw new IllegalArgumentException("El identificador debe ser positivo");
        }
        return estadisticas.obtenerPorPelicula(peliculaId);
    }
}
