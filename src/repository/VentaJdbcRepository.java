package repository;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.List;

import config.JdbcConnectionFactory;
import excepciones.PersistenciaException;

public class VentaJdbcRepository implements VentaRepository {

    private final JdbcConnectionFactory conexiones;

    public VentaJdbcRepository(JdbcConnectionFactory conexiones) {
        this.conexiones = conexiones;
    }

    @Override
    public int vender(Integer clienteId, int sesionId, List<String> asientos)
            throws PersistenciaException {
        try (Connection conexion = conexiones.crearConexion()) {
            conexion.setAutoCommit(false);

            try {
                BigDecimal precio = obtenerPrecioYComprobarPlazas(conexion, sesionId,
                        asientos.size());
                BigDecimal total = precio.multiply(BigDecimal.valueOf(asientos.size()));
                int ventaId = insertarVenta(conexion, clienteId, sesionId, total);
                insertarEntradas(conexion, ventaId, sesionId, precio, asientos);
                descontarPlazas(conexion, sesionId, asientos.size());
                conexion.commit();
                return ventaId;
            } catch (SQLException e) {
                rollback(conexion, e);
                throw new PersistenciaException("No se pudo completar la venta", e);
            } catch (PersistenciaException e) {
                rollback(conexion, e);
                throw e;
            }
        } catch (SQLException e) {
            throw new PersistenciaException("No se pudo abrir la transacción de venta", e);
        }
    }

    private BigDecimal obtenerPrecioYComprobarPlazas(Connection conexion,
            int sesionId, int cantidad) throws SQLException, PersistenciaException {
        String sql = "SELECT precio, plazas_disponibles FROM sesiones WHERE id = ? FOR UPDATE";

        try (PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            sentencia.setInt(1, sesionId);
            try (ResultSet resultado = sentencia.executeQuery()) {
                if (!resultado.next()) {
                    throw new PersistenciaException("No existe la sesión " + sesionId, null);
                }

                int disponibles = resultado.getInt("plazas_disponibles");
                if (cantidad > disponibles) {
                    throw new PersistenciaException("No hay plazas suficientes", null);
                }

                return resultado.getBigDecimal("precio");
            }
        }
    }

    private int insertarVenta(Connection conexion, Integer clienteId,
            int sesionId, BigDecimal total) throws SQLException {
        String sql = "INSERT INTO ventas (cliente_id, sesion_id, total) VALUES (?, ?, ?)";

        try (PreparedStatement sentencia = conexion.prepareStatement(
                sql, Statement.RETURN_GENERATED_KEYS)) {
            if (clienteId == null) {
                sentencia.setNull(1, Types.INTEGER);
            } else {
                sentencia.setInt(1, clienteId);
            }
            sentencia.setInt(2, sesionId);
            sentencia.setBigDecimal(3, total);
            sentencia.executeUpdate();

            try (ResultSet claves = sentencia.getGeneratedKeys()) {
                if (!claves.next()) {
                    throw new SQLException("MySQL no devolvió el identificador de venta");
                }
                return claves.getInt(1);
            }
        }
    }

    private void insertarEntradas(Connection conexion, int ventaId, int sesionId,
            BigDecimal precio, List<String> asientos) throws SQLException {
        String sql = "INSERT INTO entradas (venta_id, sesion_id, asiento, precio) "
                + "VALUES (?, ?, ?, ?)";

        try (PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            for (String asiento : asientos) {
                sentencia.setInt(1, ventaId);
                sentencia.setInt(2, sesionId);
                sentencia.setString(3, asiento);
                sentencia.setBigDecimal(4, precio);
                sentencia.addBatch();
            }
            sentencia.executeBatch();
        }
    }

    private void descontarPlazas(Connection conexion, int sesionId, int cantidad)
            throws SQLException, PersistenciaException {
        String sql = "UPDATE sesiones SET plazas_disponibles = plazas_disponibles - ? "
                + "WHERE id = ? AND plazas_disponibles >= ?";

        try (PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            sentencia.setInt(1, cantidad);
            sentencia.setInt(2, sesionId);
            sentencia.setInt(3, cantidad);
            if (sentencia.executeUpdate() != 1) {
                throw new PersistenciaException("No se pudieron descontar las plazas", null);
            }
        }
    }

    private void rollback(Connection conexion, Exception causa) {
        try {
            conexion.rollback();
        } catch (SQLException e) {
            causa.addSuppressed(e);
        }
    }
}
