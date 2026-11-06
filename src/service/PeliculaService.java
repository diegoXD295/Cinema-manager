package service;

import java.util.List;
import java.util.Optional;

import excepciones.PersistenciaException;
import modelo.Pelicula;
import repository.Repository;

public class PeliculaService {

    private final Repository<Pelicula, Integer> peliculas;

    public PeliculaService(Repository<Pelicula, Integer> peliculas) {
        this.peliculas = peliculas;
    }

    public List<Pelicula> listar() throws PersistenciaException {
        return peliculas.findAll();
    }

    public Optional<Pelicula> buscar(int id) throws PersistenciaException {
        if (id <= 0) {
            throw new IllegalArgumentException("El identificador debe ser positivo");
        }

        return peliculas.findById(id);
    }

    public Pelicula guardar(Pelicula pelicula) throws PersistenciaException {
        if (pelicula == null) {
            throw new IllegalArgumentException("La película no puede ser nula");
        }

        if (pelicula.getTitulo() == null || pelicula.getTitulo().isBlank()) {
            throw new IllegalArgumentException("El título es obligatorio");
        }

        return peliculas.save(pelicula);
    }

    public boolean eliminar(int id) throws PersistenciaException {
        if (id <= 0) {
            throw new IllegalArgumentException("El identificador debe ser positivo");
        }

        return peliculas.deleteById(id);
    }
}
