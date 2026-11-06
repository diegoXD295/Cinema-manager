package mapper;

import java.math.BigDecimal;

import modelo.Cliente;

public class ClienteCsvMapper {

    public Cliente fromCsv(String linea) {
        String[] campos = linea.split(";", -1);
        if (campos.length != 3) {
            throw new IllegalArgumentException("Se esperaban 3 campos");
        }

        String nombre = campos[0].trim();
        String email = campos[1].trim();
        if (nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre está vacío");
        }
        if (email.isBlank()) {
            throw new IllegalArgumentException("El email está vacío");
        }

        try {
            BigDecimal saldo = new BigDecimal(campos[2].trim());
            if (saldo.signum() < 0) {
                throw new IllegalArgumentException("El saldo es negativo");
            }
            return new Cliente(0, nombre, email, saldo);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("El saldo no es numérico", e);
        }
    }
}
