package config;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

import excepciones.PersistenciaException;

public class Configuracion {

    private final Properties propiedades = new Properties();

    public Configuracion(Path rutaArchivo) throws PersistenciaException {
        try (InputStream entrada = Files.newInputStream(rutaArchivo)) {
            propiedades.load(entrada);
        } catch (IOException e) {
            throw new PersistenciaException(
                    "No se pudo leer el archivo de configuración: " + rutaArchivo,
                    e);
        }
    }

    public String obtener(String clave) {
        String valor = propiedades.getProperty(clave);

        if (valor == null) {
            throw new IllegalArgumentException(
                    "Falta la propiedad de configuración: " + clave);
        }

        return valor.trim();
    }
    
}
