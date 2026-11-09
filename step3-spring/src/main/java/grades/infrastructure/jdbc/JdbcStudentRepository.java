package grades.infrastructure.jdbc;

import grades.domain.Student;
import grades.domain.StudentRepository;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 学生を PostgreSQL に保存する実装。
 * step2 との違いは、JdbcClient を Main で作らず、Spring Boot が用意して渡してくれること。
 * 失敗したときの例外は、Spring が実行時例外（DataAccessException の仲間）に変換してくれる。
 */
@Repository
class JdbcStudentRepository implements StudentRepository {

    private final JdbcClient jdbcClient;                 // Spring Boot が用意してくれる部品

    JdbcStudentRepository(JdbcClient jdbcClient) {       // コンストラクタインジェクション
        this.jdbcClient = jdbcClient;
    }

    @Override
    public Student save(Student student) {
        String sql = """
                INSERT INTO students (name, department_id, enrolled_year)
                VALUES (:name, :departmentId, :enrolledYear)
                RETURNING student_id
                """;
        int id = jdbcClient.sql(sql)
                .param("name", student.name())
                .param("departmentId", student.departmentId())
                .param("enrolledYear", student.enrolledYear())
                .query(Integer.class)                    // RETURNING があるので query
                .single();
        return student.withId(id);                       // 採番された ID を持つ Student を返す
    }

    @Override
    public Optional<Student> findById(int id) {
        String sql = """
                SELECT student_id, name, department_id, enrolled_year
                FROM students
                WHERE student_id = :id
                """;
        return jdbcClient.sql(sql)
                .param("id", id)                         // :id に値を渡す（SQL インジェクションの心配なし）
                .query((rs, rowNum) -> new Student(rs.getInt("student_id"), rs.getString("name"),
                        rs.getInt("department_id"), rs.getInt("enrolled_year")))
                .optional();                              // 0 件なら Optional.empty()
    }

    @Override
    public List<Student> findAll() {
        String sql = """
                SELECT student_id, name, department_id, enrolled_year
                FROM students
                ORDER BY student_id
                """;
        return jdbcClient.sql(sql)
                .query((rs, rowNum) -> new Student(rs.getInt("student_id"), rs.getString("name"),
                        rs.getInt("department_id"), rs.getInt("enrolled_year")))
                .list();
    }
}
