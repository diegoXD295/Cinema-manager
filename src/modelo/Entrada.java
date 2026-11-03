package modelo;

import java.math.BigDecimal;

public class Entrada {
	private int id;
	private int ventaId;
	private int sesionId;
	private String asiento;
	private BigDecimal precio;

	public Entrada() {
	}

	public Entrada(int id, int ventaId, int sesionId, String asiento, BigDecimal precio) {
		this.id = id;
		this.ventaId = ventaId;
		this.sesionId = sesionId;
		this.asiento = asiento;
		this.precio = precio;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getVentaId() {
		return ventaId;
	}

	public void setVentaId(int v) {
		this.ventaId = v;
	}

	public int getSesionId() {
		return sesionId;
	}

	public void setSesionId(int v) {
		this.sesionId = v;
	}

	public String getAsiento() {
		return asiento;
	}

	public void setAsiento(String v) {
		this.asiento = v;
	}

	public BigDecimal getPrecio() {
		return precio;
	}

	public void setPrecio(BigDecimal v) {
		this.precio = v;
	}
}
