package grades.dataaccess.jdbc;

import grades.domain.保存失敗例外;
import grades.domain.履修;
import grades.domain.履修リポジトリ;
import grades.domain.履修一覧;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.simple.JdbcClient;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;

/**
 * 履修を PostgreSQL に保存する実装。
 * enrollments.enrollment_id（代理キー）は、業務上の意味がないので、履修 には持たせない。
 */
public class Jdbc履修リポジトリ implements 履修リポジトリ {

    private final JdbcClient jdbcClient;

    public Jdbc履修リポジトリ(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    @Override
    public void 保存する(履修 履修) {
        // 同じ（学生, 科目, 年度）がすでにあれば、点数だけ更新する。
        // メモリ版ではコードで守っていたルールを、データベースでは UNIQUE 制約が支える
        String sql = """
                INSERT INTO enrollments (student_id, course_id, academic_year, score)
                VALUES (:studentId, :courseId, :academicYear, :score)
                ON CONFLICT (student_id, course_id, academic_year)
                DO UPDATE SET score = EXCLUDED.score
                """;
        try {
            jdbcClient.sql(sql)
                    .param("studentId", 履修.学生ID())
                    .param("courseId", 履修.科目ID())
                    .param("academicYear", 履修.年度())
                    .param("score", 履修.点数(), Types.SMALLINT)   // null（履修中）も渡せる
                    .update();
        } catch (DataAccessException e) {
            throw new 保存失敗例外("成績の登録に失敗しました", e);
        }
    }

    @Override
    public 履修一覧 学生IDで探す(int 学生ID) {
        String sql = """
                SELECT student_id, course_id, academic_year, score
                FROM enrollments
                WHERE student_id = :studentId
                ORDER BY course_id, academic_year
                """;
        try {
            List<履修> 見つかった履修 = jdbcClient.sql(sql)
                    .param("studentId", 学生ID)
                    .query((rs, rowNum) -> 履修にする(rs))
                    .list();
            return new 履修一覧(見つかった履修);
        } catch (DataAccessException e) {
            throw new 保存失敗例外("履修の取得に失敗しました", e);
        }
    }

    // 行（ResultSet）→ domain のオブジェクト。ResultSet は、この層から外に出さない
    private static 履修 履修にする(ResultSet rs) throws SQLException {
        return new 履修(rs.getInt("student_id"), rs.getInt("course_id"), rs.getInt("academic_year"),
                rs.getObject("score", Integer.class));   // getInt だと NULL（履修中）が 0 点になる
    }
}
