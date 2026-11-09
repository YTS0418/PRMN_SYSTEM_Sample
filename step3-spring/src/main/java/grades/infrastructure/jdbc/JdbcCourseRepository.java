package grades.infrastructure.jdbc;

import grades.domain.Course;
import grades.domain.CourseRepository;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/** 科目を PostgreSQL から読み取る実装（読み取りのみ）。courses.teacher_id は使わないので SELECT しない。 */
@Repository
class JdbcCourseRepository implements CourseRepository {

    private final JdbcClient jdbcClient;

    JdbcCourseRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    @Override
    public Optional<Course> findById(int id) {
        String sql = """
                SELECT course_id, title, credits
                FROM courses
                WHERE course_id = :id
                """;
        return jdbcClient.sql(sql)
                .param("id", id)
                .query((rs, rowNum) -> new Course(rs.getInt("course_id"), rs.getString("title"),
                        rs.getInt("credits")))
                .optional();
    }

    @Override
    public List<Course> findAll() {
        String sql = """
                SELECT course_id, title, credits
                FROM courses
                ORDER BY course_id
                """;
        return jdbcClient.sql(sql)
                .query((rs, rowNum) -> new Course(rs.getInt("course_id"), rs.getString("title"),
                        rs.getInt("credits")))
                .list();
    }
}
