package ru.yofik.athena.common.storage.fine.extractors;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import ru.yofik.athena.common.storage.fine.entityDescriptor.EntityDescriptor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class NestedEntityIdsResultSetExtractor implements ResultSetExtractor<Map<String, Long>> {
    private final EntityDescriptor<?> entityDescriptor;

    public NestedEntityIdsResultSetExtractor(EntityDescriptor<?> entityDescriptor) {
        this.entityDescriptor = entityDescriptor;
    }

    @Override
    public Map<String, Long> extractData(ResultSet rs) throws SQLException, DataAccessException {
        if (!rs.next()) return Collections.emptyMap();

        var map = new HashMap<String, Long>();
        for (var foreignKeyColumn : entityDescriptor.nestedEntityDescriptors().keySet()) {
            map.put(foreignKeyColumn, rs.getLong(foreignKeyColumn));
        }

        return map;
    }
}
