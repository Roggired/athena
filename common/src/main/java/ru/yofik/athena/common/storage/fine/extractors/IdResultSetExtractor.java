package ru.yofik.athena.common.storage.fine.extractors;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.sql.ResultSet;
import java.sql.SQLException;

public final class IdResultSetExtractor implements ResultSetExtractor<Long> {
    @Override
    public Long extractData(ResultSet rs) throws SQLException, DataAccessException {
        if (!rs.next()) return null;
        return rs.getLong(1);
    }
}
