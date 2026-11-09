package grades.infrastructure.jdbc;

import grades.domain.Course;
import grades.domain.CourseRepository;
import grades.domain.RepositoryException;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.simple.JdbcClient;

import java.util.List;
import java.util.Optional;

/**
 * 科目を PostgreSQL から読み取る実装（読み取りのみ）。
 * courses.teacher_id は、今回のユースケースでは使わないので、SELECT しない。
 */
public class JdbcCourseRepository implements CourseRepository {

    private final JdbcClient jdbcClient;

    public JdbcCourseRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    @Override
    public Optional<Course> findById(int id) {
        String sql = """
                SELECT course_id, title, credits
                FROM courses
                WHERE course_id = :id
                """;
        try {
            return jdbcClient.sql(sql)
                    .param("id", id)
                    .query((rs, rowNum) -> new Course(rs.getInt("course_id"), rs.getString("title"),
                            rs.getInt("credits")))
                    .optional();
        } catch (DataAccessException e) {
            throw new RepositoryException("科目の取得に失敗しました", e);
        }
    }

    @Override
    public List<Course> findAll() {
        String sql = """
                SELECT course_id, title, credits
                FROM courses
                ORDER BY course_id
                """;
        try {
            return jdbcClient.sql(sql)
                    .query((rs, rowNum) -> new Course(rs.getInt("course_id"), rs.getString("title"),
                            rs.getInt("credits")))
                    .list();
        } catch (DataAccessException e) {
            throw new RepositoryException("科目の取得に失敗しました", e);
        }
    }
}
