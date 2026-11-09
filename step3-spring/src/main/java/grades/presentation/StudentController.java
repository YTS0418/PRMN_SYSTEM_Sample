package grades.presentation;

import grades.application.GradeService;
import grades.application.Transcript;
import grades.domain.Student;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * HTTP の受け口（Presentation 層）。step1 の ConsoleApp にあたる。
 * 入力を受け取り、Service を呼び、結果を返すだけ。業務のルール（点数の範囲など）は書かない。
 */
@RestController
@RequestMapping("/students")
class StudentController {

    private final GradeService service;

    StudentController(GradeService service) {                  // コンストラクタ注入
        this.service = service;
    }

    @PostMapping                                                // POST /students
    Student register(@RequestBody RegisterRequest req) {
        return service.registerStudent(req.name(), req.departmentId(), req.enrolledYear());
    }

    @PutMapping("/{id}/enrollments")                            // PUT /students/1/enrollments
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void recordScore(@PathVariable int id, @RequestBody RecordScoreRequest req) {
        service.recordScore(id, req.courseId(), req.academicYear(), req.score());
    }

    @GetMapping("/{id}/transcript")                             // GET /students/1/transcript
    Transcript transcript(@PathVariable int id) {
        return service.transcript(id);
    }

    record RegisterRequest(String name, int departmentId, int enrolledYear) { }   // 入力用の DTO

    /** score を省く（null）と、履修中になる。 */
    record RecordScoreRequest(int courseId, int academicYear, Integer score) { }
}
