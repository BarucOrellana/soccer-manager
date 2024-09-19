package org.app.persistence;

import java.util.List;

public interface Repository<T> {
    List<T> findAll();
    T getById(Integer id);
    void save(T t);
    void update (T t);
    boolean delete(Integer id);
    boolean exits(Integer id);
}
