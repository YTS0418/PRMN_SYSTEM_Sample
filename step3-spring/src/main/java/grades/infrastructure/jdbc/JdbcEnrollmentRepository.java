package grades.infrastructure.jdbc;

import grades.domain.Enrollment;
import grades.domain.EnrollmentRepository;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.sql.Types;
import java.util.List;

/** 履修を PostgreSQL に保存する実装。 */
@Repository
class JdbcEnrollmentRepository implements EnrollmentRepository {

    private final JdbcClient jdbcClient;

    JdbcEnrollmentRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    @Override
    public void save(Enrollment enrollment) {
        // 同じ（学生, 科目, 年度）がすでにあれば、点数だけ更新する（UNIQUE 制約が支える）
        String sql = """
                INSERT INTO enrollments (student_id, course_id, academic_year, score)
                VALUES (:studentId, :courseId, :academicYear, :score)
                ON CONFLICT (student_id, course_id, academic_year)
                DO UPDATE SET score = EXCLUDED.score
                """;
        jdbcClient.sql(sql)
                .param("studentId", enrollment.studentId())
                .param("courseId", enrollment.courseId())
                .param("academicYear", enrollment.academicYear())
                .param("score", enrollment.score(), Types.SMALLINT)   // null（履修中）も渡せる
                .update();
    }

    @Override
    public List<Enrollment> findByStudentId(int studentId) {
        String sql = """
                SELECT student_id, course_id, academic_year, score
                FROM enrollments
                WHERE student_id = :studentId
                ORDER BY course_id, academic_year
                """;
        return jdbcClient.sql(sql)
                .param("studentId", studentId)
                .query((rs, rowNum) -> new Enrollment(
                        rs.getInt("student_id"), rs.getInt("course_id"), rs.getInt("academic_year"),
                        rs.getObject("score", Integer.class)))   // getInt だと NULL（履修中）が 0 点になる
                .list();
    }
}
