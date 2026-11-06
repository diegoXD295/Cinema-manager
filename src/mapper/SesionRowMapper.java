package mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import modelo.Sesion;

public class SesionRowMapper {

    public Sesion fromResultSet(ResultSet resultado) throws SQLException {
        Timestamp fechaHora = resultado.getTimestamp("fecha_hora");
        return new Sesion(
                resultado.getInt("id"),
                resultado.getInt("pelicula_id"),
                resultado.getInt("sala_id"),
                fechaHora.toLocalDateTime(),
                resultado.getBigDecimal("precio"),
                resultado.getInt("plazas_disponibles"));
    }
}
