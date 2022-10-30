package ru.yofik.athena.common.storage.fine.extractors;

import lombok.AllArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
public final class MultipleEntityExtractor<T> implements ResultSetExtractor<List<T>> {
    private final SingleEntityExtractor<T> singleEntityExtractor;

    @Override
    public List<T> extractData(ResultSet rs) throws SQLException, DataAccessException {
        var result = new ArrayList<T>();
        while (rs.next()) {
            result.add(singleEntityExtractor.extract(rs));
        }

        return result;
    }
}
