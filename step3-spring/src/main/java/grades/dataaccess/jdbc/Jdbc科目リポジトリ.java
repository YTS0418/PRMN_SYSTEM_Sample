package grades.dataaccess.jdbc;

import grades.domain.科目;
import grades.domain.科目リポジトリ;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * 科目を PostgreSQL から読み取る実装（読み取りのみ）。
 * courses.teacher_id は、今回のユースケースでは使わないので、SELECT しない。
 */
@Repository
class Jdbc科目リポジトリ implements 科目リポジトリ {

    private final JdbcClient jdbcClient;

    Jdbc科目リポジトリ(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    @Override
    public Optional<科目> 探す(int 科目ID) {
        String sql = """
                SELECT course_id, title, credits
                FROM courses
                WHERE course_id = :id
                """;
        return jdbcClient.sql(sql)
                .param("id", 科目ID)
                .query((rs, rowNum) -> 科目にする(rs))
                .optional();
    }

    @Override
    public List<科目> すべて探す() {
        String sql = """
                SELECT course_id, title, credits
                FROM courses
                ORDER BY course_id
                """;
        return jdbcClient.sql(sql)
                .query((rs, rowNum) -> 科目にする(rs))
                .list();
    }

    // 行（ResultSet）→ domain のオブジェクト。ResultSet は、この層から外に出さない
    private static 科目 科目にする(ResultSet rs) throws SQLException {
        return new 科目(rs.getInt("course_id"), rs.getString("title"), rs.getInt("credits"));
    }
}
