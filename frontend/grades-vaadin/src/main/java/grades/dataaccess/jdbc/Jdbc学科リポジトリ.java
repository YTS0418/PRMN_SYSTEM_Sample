package grades.dataaccess.jdbc;

import grades.domain.学科;
import grades.domain.学科リポジトリ;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.util.List;

/** 学科を PostgreSQL から読み取る実装（読み取りのみ）。 */
@Repository
class Jdbc学科リポジトリ implements 学科リポジトリ {

    private final JdbcClient jdbcClient;

    Jdbc学科リポジトリ(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    @Override
    public List<学科> すべて探す() {
        String sql = """
                SELECT department_id, name
                FROM departments
                ORDER BY department_id
                """;
        return jdbcClient.sql(sql)
                .query((rs, rowNum) -> new 学科(rs.getInt("department_id"), rs.getString("name")))
                .list();
    }
}
