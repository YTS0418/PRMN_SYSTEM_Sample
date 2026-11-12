package grades.dataaccess.jdbc;

import grades.domain.保存失敗例外;
import grades.domain.学生;
import grades.domain.学生リポジトリ;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.simple.JdbcClient;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * 学生を PostgreSQL に保存する実装。JdbcClient や SQL を知っているのは、この層だけ。
 * テーブル名・列名は英語（students.student_id など）。日本語の名前との対応は、このクラスが受け持つ。
 */
public class Jdbc学生リポジトリ implements 学生リポジトリ {

    private final JdbcClient jdbcClient;

    // コンストラクタインジェクション：JdbcClient は外から受け取る（自分で作らない）
    public Jdbc学生リポジトリ(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    @Override
    public 学生 保存する(学生 学生) {
        String sql = """
                INSERT INTO students (name, department_id, enrolled_year)
                VALUES (:name, :departmentId, :enrolledYear)
                RETURNING student_id
                """;
        try {
            int 採番された学生ID = jdbcClient.sql(sql)
                    .param("name", 学生.氏名())
                    .param("departmentId", 学生.学科ID())
                    .param("enrolledYear", 学生.入学年度())
                    .query(Integer.class)                         // RETURNING があるので query
                    .single();
            return 学生.学生IDを付ける(採番された学生ID);          // 採番された ID を持つ学生を返す
        } catch (DataAccessException e) {
            // Spring の例外を上の層に出さない。利用者に見せる文は短くし、詳細は原因（cause）に残す
            throw new 保存失敗例外("学生の登録に失敗しました", e);
        }
    }

    @Override
    public Optional<学生> 探す(int 学生ID) {
        String sql = """
                SELECT student_id, name, department_id, enrolled_year
                FROM students
                WHERE student_id = :id
                """;
        try {
            return jdbcClient.sql(sql)
                    .param("id", 学生ID)                          // :id に値を渡す
                    .query((rs, rowNum) -> 学生にする(rs))
                    .optional();                                  // 0 件なら Optional.empty()
        } catch (DataAccessException e) {
            throw new 保存失敗例外("学生の取得に失敗しました", e);
        }
    }

    @Override
    public List<学生> すべて探す() {
        String sql = """
                SELECT student_id, name, department_id, enrolled_year
                FROM students
                ORDER BY student_id
                """;
        try {
            return jdbcClient.sql(sql)
                    .query((rs, rowNum) -> 学生にする(rs))
                    .list();
        } catch (DataAccessException e) {
            throw new 保存失敗例外("学生の取得に失敗しました", e);
        }
    }

    // 行（ResultSet）→ domain のオブジェクト。ResultSet は、この層から外に出さない
    private static 学生 学生にする(ResultSet rs) throws SQLException {
        return new 学生(rs.getInt("student_id"), rs.getString("name"),
                rs.getInt("department_id"), rs.getInt("enrolled_year"));
    }
}
