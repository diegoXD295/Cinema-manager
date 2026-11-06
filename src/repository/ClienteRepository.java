package repository;

import java.util.List;

import excepciones.PersistenciaException;
import modelo.Cliente;

public interface ClienteRepository {

    int guardarLote(List<Cliente> clientes) throws PersistenciaException;
}
