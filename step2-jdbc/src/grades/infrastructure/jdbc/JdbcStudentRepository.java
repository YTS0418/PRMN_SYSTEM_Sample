package grades.infrastructure.jdbc;

import grades.domain.RepositoryException;
import grades.domain.Student;
import grades.domain.StudentRepository;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.simple.JdbcClient;

import java.util.List;
import java.util.Optional;

/** 学生を PostgreSQL に保存する実装。JdbcClient や SQL を知っているのは、この層だけ。 */
public class JdbcStudentRepository implements StudentRepository {

    private final JdbcClient jdbcClient;

    // コンストラクタインジェクション：JdbcClient は外から受け取る（自分で作らない）
    public JdbcStudentRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    @Override
    public Student save(Student student) {
        String sql = """
                INSERT INTO students (name, department_id, enrolled_year)
                VALUES (:name, :departmentId, :enrolledYear)
                RETURNING student_id
                """;
        try {
            int id = jdbcClient.sql(sql)
                    .param("name", student.name())
                    .param("departmentId", student.departmentId())
                    .param("enrolledYear", student.enrolledYear())
                    .query(Integer.class)                         // RETURNING があるので query
                    .single();
            return student.withId(id);                            // 採番された ID を持つ Student を返す
        } catch (DataAccessException e) {
            // Spring の例外を上の層に出さない。利用者に見せる文は短くし、詳細は cause に残す
            throw new RepositoryException("学生の登録に失敗しました", e);
        }
    }

    @Override
    public Optional<Student> findById(int id) {
        String sql = """
                SELECT student_id, name, department_id, enrolled_year
                FROM students
                WHERE student_id = :id
                """;
        try {
            return jdbcClient.sql(sql)
                    .param("id", id)                              // :id に値を渡す
                    .query((rs, rowNum) -> toStudent(rs))
                    .optional();                                  // 0 件なら Optional.empty()
        } catch (DataAccessException e) {
            throw new RepositoryException("学生の取得に失敗しました", e);
        }
    }

    @Override
    public List<Student> findAll() {
        String sql = """
                SELECT student_id, name, department_id, enrolled_year
                FROM students
                ORDER BY student_id
                """;
        try {
            return jdbcClient.sql(sql)
                    .query((rs, rowNum) -> toStudent(rs))
                    .list();
        } catch (DataAccessException e) {
            throw new RepositoryException("学生の取得に失敗しました", e);
        }
    }

    // 行（ResultSet）→ Domain のオブジェクト。ResultSet は、この層から外に出さない
    private static Student toStudent(java.sql.ResultSet rs) throws java.sql.SQLException {
        return new Student(rs.getInt("student_id"), rs.getString("name"),
                rs.getInt("department_id"), rs.getInt("enrolled_year"));
    }
}
