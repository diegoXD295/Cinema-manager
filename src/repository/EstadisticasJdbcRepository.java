package repository;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import config.JdbcConnectionFactory;
import excepciones.PersistenciaException;
import mapper.EstadisticaRowMapper;
import modelo.EstadisticaPelicula;

public class EstadisticasJdbcRepository implements EstadisticasRepository {

    private final JdbcConnectionFactory conexiones;
    private final EstadisticaRowMapper mapper = new EstadisticaRowMapper();

    public EstadisticasJdbcRepository(JdbcConnectionFactory conexiones) {
        this.conexiones = conexiones;
    }

    @Override
    public EstadisticaPelicula obtenerPorPelicula(int peliculaId)
            throws PersistenciaException {
        try (Connection conexion = conexiones.crearConexion();
                CallableStatement llamada = conexion.prepareCall("{CALL sp_estadisticas_pelicula(?)}")) {
            llamada.setInt(1, peliculaId);

            try (ResultSet resultado = llamada.executeQuery()) {
                if (!resultado.next()) {
                    throw new PersistenciaException(
                            "El procedimiento no devolvió estadísticas", null);
                }
                return mapper.fromResultSet(resultado);
            }
        } catch (SQLException e) {
            throw new PersistenciaException("No se pudieron recuperar las estadísticas", e);
        }
    }
}
