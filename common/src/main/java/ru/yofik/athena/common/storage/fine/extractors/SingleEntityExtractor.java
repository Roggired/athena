package ru.yofik.athena.common.storage.fine.extractors;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class SingleEntityExtractor<T> implements ResultSetExtractor<T> {
    @Override
    public final T extractData(ResultSet rs) throws SQLException, DataAccessException {
        if (!rs.next()) return null;
        return extract(rs);
    }

    /**
     * Names of columns are: 'TABLE_NAME'.'COLUMN_NAME' based on EntityDescriptor.
     * In the ResultSet all nested entities columns are presented. You should get values of
     * columns by it names only. For foreign keys TABLE_NAME is the name of owning table.
     */
    public abstract T extract(ResultSet rs) throws SQLException, DataAccessException ;
}
