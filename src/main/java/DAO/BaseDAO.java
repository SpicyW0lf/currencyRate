package DAO;

import java.sql.SQLException;
import java.util.List;

public interface BaseDAO<T> {
    List<T> findAll() throws SQLException;
    T findByCode(String code) throws SQLException;
    void create(T object) throws SQLException;
}
