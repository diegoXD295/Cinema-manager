package service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import excepciones.PersistenciaException;
import mapper.ClienteCsvMapper;
import modelo.Cliente;
import repository.ClienteRepository;

public class ImportacionClientesService {

    private final Path rutaClientes;
    private final Path rutaErrores;
    private final ClienteRepository clientes;
    private final ClienteCsvMapper mapper = new ClienteCsvMapper();

    public ImportacionClientesService(Path rutaClientes, Path rutaErrores,
            ClienteRepository clientes) {
        this.rutaClientes = rutaClientes;
        this.rutaErrores = rutaErrores;
        this.clientes = clientes;
    }

    public ResultadoImportacion importar(EstrategiaImportacion estrategia)
            throws PersistenciaException {
        List<Cliente> validos = new ArrayList<>();
        List<ErrorImportacion> errores = new ArrayList<>();
        int procesadas = 0;
        int numeroLinea = 0;

        try (BufferedReader lector = Files.newBufferedReader(
                rutaClientes, StandardCharsets.UTF_8)) {
            String linea;
            while ((linea = lector.readLine()) != null) {
                numeroLinea++;
                if (numeroLinea == 1 && linea.toLowerCase().startsWith("nombre;email;")) {
                    continue;
                }

                procesadas++;
                try {
                    validos.add(mapper.fromCsv(linea));
                } catch (IllegalArgumentException e) {
                    errores.add(new ErrorImportacion(numeroLinea, linea, e.getMessage()));
                }
            }
        } catch (IOException e) {
            throw new PersistenciaException("No se pudo leer el CSV de clientes", e);
        }

        escribirErrores(errores);
        int insertados = estrategia.continuarConErrores(errores.size())
                ? clientes.guardarLote(validos)
                : 0;

        return new ResultadoImportacion(procesadas, insertados, errores.size());
    }

    private void escribirErrores(List<ErrorImportacion> errores)
            throws PersistenciaException {
        try {
            Path carpeta = rutaErrores.toAbsolutePath().getParent();
            if (carpeta != null) {
                Files.createDirectories(carpeta);
            }

            try (BufferedWriter escritor = Files.newBufferedWriter(
                    rutaErrores, StandardCharsets.UTF_8)) {
                escritor.write("numero_linea;contenido;motivo");
                escritor.newLine();
                for (ErrorImportacion error : errores) {
                    escritor.write(error.numeroLinea() + ";"
                            + csv(error.contenido()) + ";"
                            + csv(error.motivo()));
                    escritor.newLine();
                }
            }
        } catch (IOException e) {
            throw new PersistenciaException("No se pudo escribir el CSV de errores", e);
        }
    }

    private String csv(String valor) {
        return "\"" + valor.replace("\"", "\"\"") + "\"";
    }
}
