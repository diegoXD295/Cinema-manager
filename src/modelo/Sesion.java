package modelo;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Sesion {
	private int id;
	private int peliculaId;
	private int salaId;
	private LocalDateTime fechaHora;
	private BigDecimal precio;
	private int plazasDisponibles;

	public Sesion() {
	}

	public Sesion(int id, int peliculaId, int salaId, LocalDateTime fechaHora, BigDecimal precio,
			int plazasDisponibles) {
		this.id = id;
		this.peliculaId = peliculaId;
		this.salaId = salaId;
		this.fechaHora = fechaHora;
		this.precio = precio;
		this.plazasDisponibles = plazasDisponibles;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getPeliculaId() {
		return peliculaId;
	}

	public void setPeliculaId(int v) {
		this.peliculaId = v;
	}

	public int getSalaId() {
		return salaId;
	}

	public void setSalaId(int v) {
		this.salaId = v;
	}

	public LocalDateTime getFechaHora() {
		return fechaHora;
	}

	public void setFechaHora(LocalDateTime v) {
		this.fechaHora = v;
	}

	public BigDecimal getPrecio() {
		return precio;
	}

	public void setPrecio(BigDecimal v) {
		this.precio = v;
	}

	public int getPlazasDisponibles() {
		return plazasDisponibles;
	}

	public void setPlazasDisponibles(int v) {
		this.plazasDisponibles = v;
	}
}
