package mapper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import modelo.Pelicula;

public class PeliculaCsvMapper {

    public Pelicula fromCsv(String linea) {
    	List<String> campos = new ArrayList<>(Arrays.asList(linea.split(";", -1)));

        if (campos.size() != 5) {
            throw new IllegalArgumentException(
                    "Se esperaban 5 campos y se encontraron " + campos.size());
        }

        try {
            int id = Integer.parseInt(campos.get(0).trim());
            String titulo = campos.get(1).trim();
            int duracionMinutos = Integer.parseInt(campos.get(2).trim());
            String genero = campos.get(3).trim();
            int edadMinima = Integer.parseInt(campos.get(4).trim());

            if (titulo.isBlank()) {
                throw new IllegalArgumentException("El título está vacío");
            }

            return new Pelicula(id, titulo, duracionMinutos, genero, edadMinima);

        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                    "El identificador, la duración o la edad no son números", e);
        }
    }
    
    public String toCsv(Pelicula pelicula) {
        return pelicula.getId() + ";" +
                pelicula.getTitulo() + ";" +
                pelicula.getDuracionMinutos() + ";" +
                pelicula.getGenero() + ";" +
                pelicula.getEdadMinima();
    }
    
}
