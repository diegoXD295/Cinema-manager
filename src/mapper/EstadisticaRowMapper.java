package mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import modelo.EstadisticaPelicula;

public class EstadisticaRowMapper {

    public EstadisticaPelicula fromResultSet(ResultSet resultado) throws SQLException {
        return new EstadisticaPelicula(
                resultado.getInt("sesiones_realizadas"),
                resultado.getInt("entradas_vendidas"),
                resultado.getBigDecimal("importe_total"));
    }
}
