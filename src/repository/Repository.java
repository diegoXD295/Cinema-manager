package repository;

import java.util.List;
import java.util.Optional;

import excepciones.PersistenciaException;

public interface Repository<T, ID> {

	List<T> findAll() throws PersistenciaException;

	Optional<T> findById(ID id) throws PersistenciaException;

	T save(T entity) throws PersistenciaException;

	boolean deleteById(ID id) throws PersistenciaException;
}
