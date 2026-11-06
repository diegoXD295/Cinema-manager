package modelo;

import java.math.BigDecimal;

public class Cliente {

    private int id;
    private String nombre;
    private String email;
    private BigDecimal saldo;

    public Cliente(int id, String nombre, String email, BigDecimal saldo) {
        this.id = id;
        this.nombre = nombre;
        this.email = email;
        this.saldo = saldo;
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

    public String getEmail() {
        return email;
    }

    public BigDecimal getSaldo() {
        return saldo;
    }
}
