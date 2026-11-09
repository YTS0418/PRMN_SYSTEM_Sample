package grades.application;

import grades.domain.Course;
import grades.domain.CourseRepository;
import grades.domain.Enrollment;
import grades.domain.EnrollmentRepository;
import grades.domain.Student;
import grades.domain.StudentRepository;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.OptionalDouble;

/**
 * ユースケースの手順を並べるクラス（Application 層）。
 * 業務のルール（点数の範囲など）は Domain 層、表示は Presentation 層、保存は Infrastructure 層の仕事。
 * ここには new も println も書かない。
 */
public class GradeService {

    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;
    private final EnrollmentRepository enrollmentRepository;

    // 必要な部品は、コンストラクタの引数で受け取る（型はインタフェース）
    public GradeService(StudentRepository studentRepository,
                        CourseRepository courseRepository,
                        EnrollmentRepository enrollmentRepository) {
        this.studentRepository = studentRepository;
        this.courseRepository = courseRepository;
        this.enrollmentRepository = enrollmentRepository;
    }

    /** ユースケース 1：学生を登録する。 */
    public Student registerStudent(String name, int departmentId, int enrolledYear) {
        return studentRepository.save(new Student(null, name, departmentId, enrolledYear));
    }

    /** ユースケース 2：成績を登録する。score が null のときは履修中。 */
    public void recordScore(int studentId, int courseId, int academicYear, Integer score) {
        findStudent(studentId);        // 学生がいなければ NoSuchElementException
        findCourse(courseId);          // 科目がいなければ NoSuchElementException
        enrollmentRepository.save(new Enrollment(studentId, courseId, academicYear, score));
    }

    /** ユースケース 3：成績一覧を作る。 */
    public Transcript transcript(int studentId) {
        Student student = findStudent(studentId);
        List<Enrollment> enrollments = enrollmentRepository.findByStudentId(studentId);

        List<Transcript.Line> lines = enrollments.stream()
                .map(e -> {
                    Course course = findCourse(e.courseId());
                    return new Transcript.Line(course.title(), course.credits(), e.academicYear(),
                            e.score(), e.isPassed());
                })
                .toList();

        // 平均は、点数のある履修だけで求める（履修中は含めない）
        OptionalDouble average = enrollments.stream()
                .filter(e -> e.score() != null)
                .mapToInt(Enrollment::score)
                .average();
        return new Transcript(student.name(), lines, average.orElse(0.0));
    }

    /** 画面で選ぶときのための一覧（読み取りだけ）。 */
    public List<Student> students() {
        return studentRepository.findAll();
    }

    /** 画面で選ぶときのための一覧（読み取りだけ）。 */
    public List<Course> courses() {
        return courseRepository.findAll();
    }

    private Student findStudent(int studentId) {
        return studentRepository.findById(studentId)
                .orElseThrow(() -> new NoSuchElementException("学生が見つかりません: " + studentId));
    }

    private Course findCourse(int courseId) {
        return courseRepository.findById(courseId)
                .orElseThrow(() -> new NoSuchElementException("科目が見つかりません: " + courseId));
    }
}
