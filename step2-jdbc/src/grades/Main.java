package grades;

import grades.application.GradeService;
import grades.domain.CourseRepository;
import grades.domain.EnrollmentRepository;
import grades.domain.StudentRepository;
import grades.infrastructure.jdbc.JdbcCourseRepository;
import grades.infrastructure.jdbc.JdbcEnrollmentRepository;
import grades.infrastructure.jdbc.JdbcStudentRepository;
import grades.presentation.ConsoleApp;
import org.postgresql.ds.PGSimpleDataSource;
import org.springframework.jdbc.core.simple.JdbcClient;

/**
 * 部品をつないで起動する。step1 からの変更は、保存先を new InMemory… から new Jdbc… に差し替えた部分だけ。
 * GradeService も ConsoleApp も Domain 層も、1 行も変えていない。
 *
 * 接続するには、環境変数 DB_PASSWORD が必要（パスワードをコードに書かない）。
 * 接続先を変えたいときだけ、DB_URL と DB_USER も指定できる。
 */
public class Main {

    public static void main(String[] args) {
        // データベースへの接続（データベース編 12 章 2 節）
        PGSimpleDataSource dataSource = new PGSimpleDataSource();
        dataSource.setUrl(System.getenv().getOrDefault("DB_URL", "jdbc:postgresql://localhost:5432/grades_db"));
        dataSource.setUser(System.getenv().getOrDefault("DB_USER", "postgres"));
        dataSource.setPassword(System.getenv("DB_PASSWORD"));
        JdbcClient jdbcClient = JdbcClient.create(dataSource);

        // 1. 保存先を決めて作る（new InMemory… から差し替え）
        StudentRepository studentRepository = new JdbcStudentRepository(jdbcClient);
        CourseRepository courseRepository = new JdbcCourseRepository(jdbcClient);
        EnrollmentRepository enrollmentRepository = new JdbcEnrollmentRepository(jdbcClient);

        // 2. Application 層に渡す
        GradeService service = new GradeService(studentRepository, courseRepository, enrollmentRepository);

        // 3. Presentation 層に渡して起動
        new ConsoleApp(service).run();
    }
}
