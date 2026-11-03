package modelo;

public class Sala {
	private int id;
	private String nombre;
	private int filas;
	private int asientosPorFila;
	private String tipo;

	public Sala() {
	}

	public Sala(int id, String nombre, int filas, int asientosPorFila, String tipo) {
		this.id = id;
		this.nombre = nombre;
		this.filas = filas;
		this.asientosPorFila = asientosPorFila;
		this.tipo = tipo;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public int getFilas() {
		return filas;
	}

	public void setFilas(int filas) {
		this.filas = filas;
	}

	public int getAsientosPorFila() {
		return asientosPorFila;
	}

	public void setAsientosPorFila(int n) {
		this.asientosPorFila = n;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public int getCapacidad() {
		return filas * asientosPorFila;
	}

	public String toString() {
		return nombre + " (" + tipo + ")";
	}
}
