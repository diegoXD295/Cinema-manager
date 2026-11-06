package repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import config.JdbcConnectionFactory;
import excepciones.PersistenciaException;
import modelo.Cliente;

public class ClienteJdbcRepository implements ClienteRepository {

    private final JdbcConnectionFactory conexiones;

    public ClienteJdbcRepository(JdbcConnectionFactory conexiones) {
        this.conexiones = conexiones;
    }

    @Override
    public int guardarLote(List<Cliente> clientes) throws PersistenciaException {
        if (clientes.isEmpty()) {
            return 0;
        }

        String sql = "INSERT INTO clientes (nombre, email, saldo) VALUES (?, ?, ?)";

        try (Connection conexion = conexiones.crearConexion()) {
            conexion.setAutoCommit(false);

            try (PreparedStatement sentencia = conexion.prepareStatement(sql)) {
                for (Cliente cliente : clientes) {
                    sentencia.setString(1, cliente.getNombre());
                    sentencia.setString(2, cliente.getEmail());
                    sentencia.setBigDecimal(3, cliente.getSaldo());
                    sentencia.addBatch();
                }

                int cantidad = sentencia.executeBatch().length;
                conexion.commit();
                return cantidad;
            } catch (SQLException e) {
                try {
                    conexion.rollback();
                } catch (SQLException errorRollback) {
                    e.addSuppressed(errorRollback);
                }
                throw e;
            }
        } catch (SQLException e) {
            throw new PersistenciaException("No se pudo importar el lote de clientes", e);
        }
    }
}
