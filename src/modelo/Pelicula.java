package modelo;

public class Pelicula {
	private int id;
	private String titulo;
	private int duracionMinutos;
	private String genero;
	private int edadMinima;

	public Pelicula() {
	}

	public Pelicula(int id, String titulo, int duracionMinutos, String genero, int edadMinima) {
		this.id = id;
		this.titulo = titulo;
		this.duracionMinutos = duracionMinutos;
		this.genero = genero;
		this.edadMinima = edadMinima;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public int getDuracionMinutos() {
		return duracionMinutos;
	}

	public void setDuracionMinutos(int v) {
		this.duracionMinutos = v;
	}

	public String getGenero() {
		return genero;
	}

	public void setGenero(String genero) {
		this.genero = genero;
	}

	public int getEdadMinima() {
		return edadMinima;
	}

	public void setEdadMinima(int v) {
		this.edadMinima = v;
	}

	public String toString() {
		return id + " - " + titulo;
	}
}
