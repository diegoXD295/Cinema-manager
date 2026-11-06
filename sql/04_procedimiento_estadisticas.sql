USE cinema_manager;

DROP PROCEDURE IF EXISTS sp_estadisticas_pelicula;
DELIMITER $$
CREATE PROCEDURE sp_estadisticas_pelicula(IN p_pelicula_id INT)
BEGIN
    SELECT
        COUNT(DISTINCT s.id) AS sesiones_realizadas,
        COUNT(e.id) AS entradas_vendidas,
        COALESCE(SUM(e.precio), 0) AS importe_total
    FROM sesiones s
    LEFT JOIN entradas e ON e.sesion_id = s.id
    WHERE s.pelicula_id = p_pelicula_id
      AND s.fecha_hora <= NOW();
END$$
DELIMITER ;
