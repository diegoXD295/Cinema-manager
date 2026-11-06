package repository;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import excepciones.PersistenciaException;
import mapper.PeliculaCsvMapper;
import modelo.Pelicula;

public class PeliculaCsvRepository implements Repository<Pelicula, Integer> {

	private static final String CABECERA = "id;titulo;duracion_minutos;genero;edad_minima";

	private final Path ruta;
	private final PeliculaCsvMapper mapper;

	public PeliculaCsvRepository(Path ruta) {
		this.ruta = ruta;
		this.mapper = new PeliculaCsvMapper();
	}

	@Override
    public List<Pelicula> findAll() throws PersistenciaException {
        List<String> lineas = leerLineas();
        List<Pelicula> peliculas = new ArrayList<>();

        for (int i = 1; i < lineas.size(); i++) {
            String linea = lineas.get(i);

            if (linea.isBlank()) {
                continue;
            }

            try {
                peliculas.add(mapper.fromCsv(linea));
            } catch (IllegalArgumentException e) {
                System.err.printf("Error en línea %d: %s%n", i + 1, e.getMessage());
            }
        }

        return peliculas;
    }

    @Override
    public Optional<Pelicula> findById(Integer id) throws PersistenciaException {
        return findAll().stream()
                .filter(pelicula -> pelicula.getId() == id)
                .findFirst();
    }

    @Override
    public Pelicula save(Pelicula pelicula) throws PersistenciaException {
        List<String> lineas = leerLineas();
        boolean actualizado = false;

        for (int i = 1; i < lineas.size(); i++) {
            try {
                Pelicula existente = mapper.fromCsv(lineas.get(i));

                if (existente.getId() == pelicula.getId()) {
                    lineas.set(i, mapper.toCsv(pelicula));
                    actualizado = true;
                    break;
                }
            } catch (IllegalArgumentException e) {
            }
        }

        if (!actualizado) {
            lineas.add(mapper.toCsv(pelicula));
        }

        guardarLineas(lineas);
        return pelicula;
    }

    @Override
    public boolean deleteById(Integer id) throws PersistenciaException {
        List<String> lineas = leerLineas();

        for (int i = 1; i < lineas.size(); i++) {
            try {
                Pelicula pelicula = mapper.fromCsv(lineas.get(i));

                if (pelicula.getId() == id) {
                    lineas.remove(i);
                    guardarLineas(lineas);
                    return true;
                }
            } catch (IllegalArgumentException e) {
               
            }
        }

        return false;
    }

    private List<String> leerLineas() throws PersistenciaException {
        try {
            if (!Files.exists(ruta)) {
                return new ArrayList<>(List.of(CABECERA));
            }

            List<String> lineas =
                    new ArrayList<>(Files.readAllLines(ruta, StandardCharsets.UTF_8));

            if (lineas.isEmpty()) {
                lineas.add(CABECERA);
            }

            return lineas;

        } catch (IOException e) {
            throw new PersistenciaException(
                    "No se pudo leer el catálogo de películas", e);
        }
    }

    private void guardarLineas(List<String> lineas) throws PersistenciaException {
        try {
            Path carpeta = ruta.toAbsolutePath().getParent();

            if (carpeta != null) {
                Files.createDirectories(carpeta);
            }

            Files.write(ruta, lineas, StandardCharsets.UTF_8);

        } catch (IOException e) {
            throw new PersistenciaException(
                    "No se pudo guardar el catálogo de películas", e);
        }
    }
    
    
   
}
