import com.couchbase.client.java.query.N1qlQuery;
import com.couchbase.client.java.query.N1qlQueryResult;
import com.couchbase.client.java.query.N1qlQueryRow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.couchbase.core.CouchbaseTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class CustomUserRepositoryImpl implements CustomUserRepository {

    @Autowired
    private CouchbaseTemplate couchbaseTemplate;

    @Override
    public List<User> findUsersByFilters(List<UserFilterInput> filters) {
        StringBuilder queryBuilder = new StringBuilder("SELECT META().id, * FROM `mybucket` WHERE 1=1 ");

        // Dynamically build the query based on the filters
        filters.forEach(filter -> {
            if (filter.getOperation() == FilterOperation.EQUAL) {
                // Handle EQUAL filters
                queryBuilder.append("AND ").append(filter.getKey()).append(" = '").append(filter.getValue()).append("' ");
            } else if (filter.getOperation() == FilterOperation.BETWEEN) {
                // Handle date range filters
                queryBuilder.append("AND ").append(filter.getKey()).append(" BETWEEN '")
                            .append(filter.getStartDate()).append("' AND '").append(filter.getEndDate()).append("' ");
            }
        });

        N1qlQuery n1qlQuery = N1qlQuery.simple(queryBuilder.toString());
        N1qlQueryResult result = couchbaseTemplate.getCouchbaseBucket().query(n1qlQuery);

        return result.allRows().stream()
                .map(N1qlQueryRow::value)
                .map(row -> couchbaseTemplate.getConverter().read(User.class, row))
                .collect(Collectors.toList());
    }
}
