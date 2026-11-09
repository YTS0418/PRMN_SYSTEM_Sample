package grades.infrastructure.jdbc;

import grades.domain.Enrollment;
import grades.domain.EnrollmentRepository;
import grades.domain.RepositoryException;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.simple.JdbcClient;

import java.sql.Types;
import java.util.List;

/**
 * 履修を PostgreSQL に保存する実装。
 * enrollments.enrollment_id（代理キー）は、業務上の意味がないので、Enrollment には持たせない。
 */
public class JdbcEnrollmentRepository implements EnrollmentRepository {

    private final JdbcClient jdbcClient;

    public JdbcEnrollmentRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    @Override
    public void save(Enrollment enrollment) {
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
                    .param("studentId", enrollment.studentId())
                    .param("courseId", enrollment.courseId())
                    .param("academicYear", enrollment.academicYear())
                    .param("score", enrollment.score(), Types.SMALLINT)   // null（履修中）も渡せる
                    .update();
        } catch (DataAccessException e) {
            throw new RepositoryException("成績の登録に失敗しました", e);
        }
    }

    @Override
    public List<Enrollment> findByStudentId(int studentId) {
        String sql = """
                SELECT student_id, course_id, academic_year, score
                FROM enrollments
                WHERE student_id = :studentId
                ORDER BY course_id, academic_year
                """;
        try {
            return jdbcClient.sql(sql)
                    .param("studentId", studentId)
                    .query((rs, rowNum) -> new Enrollment(
                            rs.getInt("student_id"), rs.getInt("course_id"), rs.getInt("academic_year"),
                            rs.getObject("score", Integer.class)))   // getInt だと NULL（履修中）が 0 点になる
                    .list();
        } catch (DataAccessException e) {
            throw new RepositoryException("履修の取得に失敗しました", e);
        }
    }
}
