package repository;

import excepciones.PersistenciaException;
import modelo.EstadisticaPelicula;

public interface EstadisticasRepository {

    EstadisticaPelicula obtenerPorPelicula(int peliculaId)
            throws PersistenciaException;
}
