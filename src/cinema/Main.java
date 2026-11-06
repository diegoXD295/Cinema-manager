package cinema;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

import config.Configuracion;
import config.RepositoryFactory;
import excepciones.PersistenciaException;
import modelo.EstadisticaPelicula;
import modelo.Pelicula;
import modelo.Sala;
import modelo.Sesion;
import repository.Repository;
import service.EstadisticasService;
import service.ImportacionClientesService;
import service.ImportacionParcial;
import service.ImportacionTodoONada;
import service.PeliculaService;
import service.ResultadoImportacion;
import service.SalaService;
import service.SesionService;
import service.VentaService;

public class Main {

    public static void main(String[] args) {
        try {
            Configuracion configuracion = new Configuracion(
                    Path.of("config", "app.properties"));
            RepositoryFactory factory = new RepositoryFactory(configuracion);

            if (args.length == 0 || args[0].equalsIgnoreCase("catalogo")) {
                mostrarCatalogo(factory);
                return;
            }

            switch (args[0].toLowerCase()) {
                case "sesiones" -> mostrarSesiones(factory);
                case "importar-parcial" -> importarClientes(factory, configuracion,
                        new ImportacionParcial());
                case "importar-todo" -> importarClientes(factory, configuracion,
                        new ImportacionTodoONada());
                case "estadisticas" -> mostrarEstadisticas(factory, args);
                case "vender" -> vender(factory, args);
                default -> mostrarUso();
            }
        } catch (PersistenciaException e) {
            System.err.println("Error de persistencia: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.err.println("Error en la operación: " + e.getMessage());
        }
    }

    private static void mostrarCatalogo(RepositoryFactory factory)
            throws PersistenciaException {
        PeliculaService peliculas = new PeliculaService(
                factory.crearRepositorioPeliculas());
        SalaService salas = new SalaService(factory.crearRepositorioSalas());

        System.out.println("Películas:");
        peliculas.listar().forEach(System.out::println);
        System.out.println("\nSalas:");
        salas.listar().forEach(System.out::println);
    }

    private static void mostrarSesiones(RepositoryFactory factory)
            throws PersistenciaException {
        SesionService sesiones = new SesionService(
                factory.crearRepositorioSesiones());
        for (Sesion sesion : sesiones.listar()) {
            System.out.printf("%d | película %d | sala %d | %s | %s € | %d plazas%n",
                    sesion.getId(), sesion.getPeliculaId(), sesion.getSalaId(),
                    sesion.getFechaHora(), sesion.getPrecio(),
                    sesion.getPlazasDisponibles());
        }
    }

    private static void importarClientes(RepositoryFactory factory,
            Configuracion configuracion, service.EstrategiaImportacion estrategia)
            throws PersistenciaException {
        ImportacionClientesService servicio = new ImportacionClientesService(
                Path.of(configuracion.obtener("ruta.clientes")),
                Path.of(configuracion.obtener("ruta.errores.clientes")),
                factory.crearRepositorioClientes());
        ResultadoImportacion resultado = servicio.importar(estrategia);
        System.out.printf("Líneas procesadas: %d; insertados: %d; rechazados: %d%n",
                resultado.lineasProcesadas(), resultado.clientesInsertados(),
                resultado.clientesRechazados());
    }

    private static void mostrarEstadisticas(RepositoryFactory factory, String[] args)
            throws PersistenciaException {
        if (args.length < 2) {
            throw new IllegalArgumentException("Uso: estadisticas <id-pelicula>");
        }
        EstadisticasService servicio = new EstadisticasService(
                factory.crearRepositorioEstadisticas());
        EstadisticaPelicula resultado = servicio.obtenerPorPelicula(
                Integer.parseInt(args[1]));
        System.out.printf("Sesiones realizadas: %d; entradas: %d; importe: %s €%n",
                resultado.getSesionesRealizadas(), resultado.getEntradasVendidas(),
                resultado.getImporteTotal());
    }

    private static void vender(RepositoryFactory factory, String[] args)
            throws PersistenciaException {
        if (args.length < 4) {
            throw new IllegalArgumentException(
                    "Uso: vender <id-sesion> <id-cliente|-> <asiento1,asiento2>");
        }
        Integer clienteId = args[2].equals("-") ? null : Integer.valueOf(args[2]);
        List<String> asientos = Arrays.asList(args[3].split(","));
        int idVenta = new VentaService(factory.crearRepositorioVentas()).vender(
                clienteId, Integer.parseInt(args[1]), asientos);
        System.out.println("Venta registrada con identificador " + idVenta);
    }

    private static void mostrarUso() {
        System.out.println("Uso:");
        System.out.println("  catalogo");
        System.out.println("  sesiones");
        System.out.println("  importar-parcial");
        System.out.println("  importar-todo");
        System.out.println("  estadisticas <id-pelicula>");
        System.out.println("  vender <id-sesion> <id-cliente|-> <asiento1,asiento2>");
    }
}
