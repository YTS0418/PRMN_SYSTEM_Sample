package grades;

import grades.application.GradeService;
import grades.domain.Course;
import grades.domain.CourseRepository;
import grades.domain.EnrollmentRepository;
import grades.domain.StudentRepository;
import grades.infrastructure.memory.InMemoryCourseRepository;
import grades.infrastructure.memory.InMemoryEnrollmentRepository;
import grades.infrastructure.memory.InMemoryStudentRepository;
import grades.presentation.ConsoleApp;

import java.util.List;

/**
 * 部品をつないで起動する（全部の層を知っている唯一のクラス）。
 * 保存先を変えたいときは、ここの new InMemory… を差し替えるだけ（step2 で実際にやる）。
 */
public class Main {

    public static void main(String[] args) {
        // 1. 保存先を決めて作る
        StudentRepository studentRepository = new InMemoryStudentRepository();
        CourseRepository courseRepository = new InMemoryCourseRepository(List.of(
                new Course(1, "プログラミング基礎", 2),
                new Course(2, "データベース", 2),
                new Course(3, "線形代数", 4),
                new Course(4, "電気回路", 2),
                new Course(5, "有機化学", 2)));
        EnrollmentRepository enrollmentRepository = new InMemoryEnrollmentRepository();

        // 2. Application 層に渡す
        GradeService service = new GradeService(studentRepository, courseRepository, enrollmentRepository);

        // 3. Presentation 層に渡して起動
        new ConsoleApp(service).run();
    }
}
