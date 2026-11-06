package repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import config.JdbcConnectionFactory;
import excepciones.PersistenciaException;
import mapper.SesionRowMapper;
import modelo.Sesion;

public class SesionJdbcRepository implements Repository<Sesion, Integer> {

    private final JdbcConnectionFactory conexiones;
    private final SesionRowMapper mapper = new SesionRowMapper();

    public SesionJdbcRepository(JdbcConnectionFactory conexiones) {
        this.conexiones = conexiones;
    }

    @Override
    public List<Sesion> findAll() throws PersistenciaException {
        String sql = "SELECT id, pelicula_id, sala_id, fecha_hora, precio, plazas_disponibles "
                + "FROM sesiones ORDER BY id";
        List<Sesion> sesiones = new ArrayList<>();

        try (Connection conexion = conexiones.crearConexion();
                PreparedStatement sentencia = conexion.prepareStatement(sql);
                ResultSet resultado = sentencia.executeQuery()) {
            while (resultado.next()) {
                sesiones.add(mapper.fromResultSet(resultado));
            }
            return sesiones;
        } catch (SQLException e) {
            throw new PersistenciaException("No se pudieron recuperar las sesiones", e);
        }
    }

    @Override
    public Optional<Sesion> findById(Integer id) throws PersistenciaException {
        String sql = "SELECT id, pelicula_id, sala_id, fecha_hora, precio, plazas_disponibles "
                + "FROM sesiones WHERE id = ?";

        try (Connection conexion = conexiones.crearConexion();
                PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            sentencia.setInt(1, id);
            try (ResultSet resultado = sentencia.executeQuery()) {
                return resultado.next()
                        ? Optional.of(mapper.fromResultSet(resultado))
                        : Optional.empty();
            }
        } catch (SQLException e) {
            throw new PersistenciaException("No se pudo recuperar la sesión " + id, e);
        }
    }

    @Override
    public Sesion save(Sesion sesion) throws PersistenciaException {
        if (sesion.getId() <= 0) {
            return insertar(sesion);
        }

        String sql = "UPDATE sesiones SET pelicula_id = ?, sala_id = ?, fecha_hora = ?, "
                + "precio = ?, plazas_disponibles = ? WHERE id = ?";

        try (Connection conexion = conexiones.crearConexion();
                PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            asignarValores(sentencia, sesion);
            sentencia.setInt(6, sesion.getId());

            if (sentencia.executeUpdate() == 0) {
                String consulta = "SELECT 1 FROM sesiones WHERE id = ?";
                try (PreparedStatement comprobar = conexion.prepareStatement(consulta)) {
                    comprobar.setInt(1, sesion.getId());
                    try (ResultSet resultado = comprobar.executeQuery()) {
                        if (!resultado.next()) {
                            throw new PersistenciaException(
                                    "No existe la sesión con identificador "
                                            + sesion.getId(), null);
                        }
                    }
                }
            }
            return sesion;
        } catch (SQLException e) {
            throw new PersistenciaException("No se pudo actualizar la sesión", e);
        }
    }

    private Sesion insertar(Sesion sesion) throws PersistenciaException {
        String sql = "INSERT INTO sesiones "
                + "(pelicula_id, sala_id, fecha_hora, precio, plazas_disponibles) "
                + "VALUES (?, ?, ?, ?, ?)";

        try (Connection conexion = conexiones.crearConexion();
                PreparedStatement sentencia = conexion.prepareStatement(
                        sql, Statement.RETURN_GENERATED_KEYS)) {
            asignarValores(sentencia, sesion);
            sentencia.executeUpdate();

            try (ResultSet claves = sentencia.getGeneratedKeys()) {
                if (!claves.next()) {
                    throw new SQLException("MySQL no devolvió el identificador generado");
                }
                sesion.setId(claves.getInt(1));
            }
            return sesion;
        } catch (SQLException e) {
            throw new PersistenciaException("No se pudo crear la sesión", e);
        }
    }

    private void asignarValores(PreparedStatement sentencia, Sesion sesion)
            throws SQLException {
        sentencia.setInt(1, sesion.getPeliculaId());
        sentencia.setInt(2, sesion.getSalaId());
        sentencia.setTimestamp(3, Timestamp.valueOf(sesion.getFechaHora()));
        sentencia.setBigDecimal(4, sesion.getPrecio());
        sentencia.setInt(5, sesion.getPlazasDisponibles());
    }

    @Override
    public boolean deleteById(Integer id) throws PersistenciaException {
        String sql = "DELETE FROM sesiones WHERE id = ?";

        try (Connection conexion = conexiones.crearConexion();
                PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            sentencia.setInt(1, id);
            return sentencia.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new PersistenciaException("No se pudo eliminar la sesión " + id, e);
        }
    }
}
