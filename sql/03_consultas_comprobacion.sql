USE cinema_manager;
SELECT COUNT(*) AS sesiones FROM sesiones;
SELECT COUNT(*) AS clientes FROM clientes;
SELECT COUNT(*) AS ventas FROM ventas;
SELECT COUNT(*) AS entradas FROM entradas;
SELECT pelicula_id, COUNT(*) AS sesiones FROM sesiones GROUP BY pelicula_id ORDER BY sesiones DESC, pelicula_id;
SELECT sesion_id, COUNT(*) AS entradas_vendidas, SUM(precio) AS recaudacion
FROM entradas GROUP BY sesion_id ORDER BY recaudacion DESC LIMIT 15;
