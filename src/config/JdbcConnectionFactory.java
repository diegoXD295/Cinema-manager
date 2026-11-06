package config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import excepciones.PersistenciaException;

public class JdbcConnectionFactory {

    private final Configuracion configuracion;

    public JdbcConnectionFactory(Configuracion configuracion) {
        this.configuracion = configuracion;
    }

    public Connection crearConexion() throws PersistenciaException {
        try {
            return DriverManager.getConnection(
                    configuracion.obtener("db.url"),
                    configuracion.obtener("db.user"),
                    configuracion.obtener("db.password"));
        } catch (SQLException e) {
            throw new PersistenciaException(
                    "No se pudo conectar con MySQL: " + e.getMessage(), e);
        }
    }
}
