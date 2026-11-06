package config;

import java.nio.file.Path;
import java.util.Locale;

import modelo.Pelicula;
import modelo.Sesion;
import modelo.Sala;
import repository.ClienteJdbcRepository;
import repository.ClienteRepository;
import repository.EstadisticasJdbcRepository;
import repository.EstadisticasRepository;
import repository.PeliculaCsvRepository;
import repository.Repository;
import repository.SalaXmlRepository;
import repository.SesionJdbcRepository;
import repository.VentaJdbcRepository;
import repository.VentaRepository;

public class RepositoryFactory {

	 private final Configuracion configuracion;
	 private final JdbcConnectionFactory conexiones;

	    public RepositoryFactory(Configuracion configuracion) {
	        this.configuracion = configuracion;
	        this.conexiones = new JdbcConnectionFactory(configuracion);
	    }
	    
	    public Repository<Pelicula, Integer> crearRepositorioPeliculas() {
	        String tipo = configuracion.obtener("persistencia.peliculas");

	        return switch (tipo.toLowerCase(Locale.ROOT)) {
	            case "csv" -> {
	                String ruta = configuracion.obtener("ruta.peliculas");
	                yield new PeliculaCsvRepository(Path.of(ruta));
	            }
	            default -> throw new IllegalArgumentException(
	                    "Tipo de persistencia de películas no válido: " + tipo);
	        };
	    }

    public Repository<Sala, Integer> crearRepositorioSalas() {
        String tipo = configuracion.obtener("persistencia.salas");

        return switch (tipo.toLowerCase(Locale.ROOT)) {
            case "xml" -> {
                String ruta = configuracion.obtener("ruta.salas");
                yield new SalaXmlRepository(Path.of(ruta));
            }
            default -> throw new IllegalArgumentException(
                    "Tipo de persistencia de salas no válido: " + tipo);
        };
    }

    public Repository<Sesion, Integer> crearRepositorioSesiones() {
        String tipo = configuracion.obtener("persistencia.sesiones");
        if (!tipo.equalsIgnoreCase("jdbc")) {
            throw new IllegalArgumentException(
                    "Tipo de persistencia de sesiones no válido: " + tipo);
        }
        return new SesionJdbcRepository(conexiones);
    }

    public ClienteRepository crearRepositorioClientes() {
        String tipo = configuracion.obtener("persistencia.clientes");
        if (!tipo.equalsIgnoreCase("jdbc")) {
            throw new IllegalArgumentException(
                    "Tipo de persistencia de clientes no válido: " + tipo);
        }
        return new ClienteJdbcRepository(conexiones);
    }

    public VentaRepository crearRepositorioVentas() {
        return new VentaJdbcRepository(conexiones);
    }

    public EstadisticasRepository crearRepositorioEstadisticas() {
        return new EstadisticasJdbcRepository(conexiones);
    }
}
