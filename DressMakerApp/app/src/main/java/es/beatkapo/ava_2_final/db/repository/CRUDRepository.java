package es.beatkapo.ava_2_final.db.repository;

import java.util.List;

public interface CRUDRepository<T> {
    long insert(T entity);

    T getById(int id);

    void update(T entity);

    void delete(int id);

    List<T> getAll();
}
