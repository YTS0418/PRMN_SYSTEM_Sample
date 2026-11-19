package grades.dataaccess.jdbc;

import grades.domain.学生;
import grades.domain.学生リポジトリ;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * 学生を PostgreSQL に保存する実装。
 * step2 との違いは、JdbcClient を Main で作らず、Spring Boot が用意して渡してくれること。
 * 失敗したときの例外は、Spring が実行時例外（DataAccessException の仲間）に変換してくれる。
 */
@Repository
class Jdbc学生リポジトリ implements 学生リポジトリ {

    private final JdbcClient jdbcClient;

    // コンストラクタインジェクション：JdbcClient は Spring Boot が用意して渡してくれる
    Jdbc学生リポジトリ(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    @Override
    public 学生 保存する(学生 学生) {
        String sql = """
                INSERT INTO students (name, department_id, enrolled_year)
                VALUES (:name, :departmentId, :enrolledYear)
                RETURNING student_id
                """;
        int 採番された学生ID = jdbcClient.sql(sql)
                .param("name", 学生.氏名())
                .param("departmentId", 学生.学科ID())
                .param("enrolledYear", 学生.入学年度())
                .query(Integer.class)                         // RETURNING があるので query
                .single();
        return 学生.学生IDを付ける(採番された学生ID);          // 採番された ID を持つ学生を返す
    }

    @Override
    public Optional<学生> 探す(int 学生ID) {
        String sql = """
                SELECT student_id, name, department_id, enrolled_year
                FROM students
                WHERE student_id = :id
                """;
        return jdbcClient.sql(sql)
                .param("id", 学生ID)                          // :id に値を渡す
                .query((rs, rowNum) -> 学生にする(rs))
                .optional();                                  // 0 件なら Optional.empty()
    }

    @Override
    public List<学生> すべて探す() {
        String sql = """
                SELECT student_id, name, department_id, enrolled_year
                FROM students
                ORDER BY student_id
                """;
        return jdbcClient.sql(sql)
                .query((rs, rowNum) -> 学生にする(rs))
                .list();
    }

    // 行（ResultSet）→ domain のオブジェクト。ResultSet は、この層から外に出さない
    private static 学生 学生にする(ResultSet rs) throws SQLException {
        return new 学生(rs.getInt("student_id"), rs.getString("name"),
                rs.getInt("department_id"), rs.getInt("enrolled_year"));
    }
}
