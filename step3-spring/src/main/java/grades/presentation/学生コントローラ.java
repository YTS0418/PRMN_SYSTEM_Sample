package grades.presentation;

import com.fasterxml.jackson.annotation.JsonProperty;
import grades.domain.学生;
import grades.usecase.成績一覧;
import grades.usecase.成績明細;
import grades.usecase.学生を登録する;
import grades.usecase.成績を登録する;
import grades.usecase.成績一覧を表示する;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * HTTP の受け口（プレゼンテーション層）。step1 の コンソール画面 にあたる。
 * 入力を受け取り、ユースケースを呼び、結果を返すだけ。業務のルール（点数の範囲など）は書かない。
 *
 * URL と JSON のキーは英語のまま（/students、"name" など）。
 * JSON のキーと日本語の項目名の対応は、このファイルの下にある入出力用の record が受け持つ。
 */
@RestController
@RequestMapping("/students")
class 学生コントローラ {

    private final 学生を登録する 学生を登録する;
    private final 成績を登録する 成績を登録する;
    private final 成績一覧を表示する 成績一覧を表示する;

    // コンストラクタ注入
    学生コントローラ(学生を登録する 学生を登録する, 成績を登録する 成績を登録する, 成績一覧を表示する 成績一覧を表示する) {
        this.学生を登録する = 学生を登録する;
        this.成績を登録する = 成績を登録する;
        this.成績一覧を表示する = 成績一覧を表示する;
    }

    @PostMapping                                                // POST /students
    学生の応答 学生を登録(@RequestBody 学生登録の入力 入力) {
        学生 登録した学生 = 学生を登録する.実行する(入力.氏名(), 入力.学科ID(), 入力.入学年度());
        return new 学生の応答(登録した学生.学生ID(), 登録した学生.氏名(), 登録した学生.学科ID(), 登録した学生.入学年度());
    }

    @PutMapping("/{id}/enrollments")                            // PUT /students/1/enrollments
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void 成績を登録(@PathVariable("id") int 学生ID, @RequestBody 成績登録の入力 入力) {
        成績を登録する.実行する(学生ID, 入力.科目ID(), 入力.年度(), 入力.点数());
    }

    @GetMapping("/{id}/transcript")                             // GET /students/1/transcript
    成績一覧の応答 成績一覧を表示(@PathVariable("id") int 学生ID) {
        成績一覧 成績一覧 = 成績一覧を表示する.実行する(学生ID);
        List<成績明細の応答> 明細リスト = new ArrayList<>();
        for (成績明細 明細 : 成績一覧.明細リスト()) {
            明細リスト.add(new 成績明細の応答(明細.科目名(), 明細.単位数(), 明細.年度(), 明細.点数(), 明細.合格()));
        }
        return new 成績一覧の応答(成績一覧.氏名(), 明細リスト, 成績一覧.平均点());
    }

    // ---- 入出力用の record（DTO）。@JsonProperty で、JSON のキーの名前を決める ----

    /** POST /students の入力。 */
    record 学生登録の入力(@JsonProperty("name") String 氏名,
                   @JsonProperty("departmentId") int 学科ID,
                   @JsonProperty("enrolledYear") int 入学年度) { }

    /** PUT /students/{id}/enrollments の入力。score を省く（null）と、履修中になる。 */
    record 成績登録の入力(@JsonProperty("courseId") int 科目ID,
                   @JsonProperty("academicYear") int 年度,
                   @JsonProperty("score") Integer 点数) { }

    /** POST /students の応答。 */
    record 学生の応答(@JsonProperty("id") Integer 学生ID,
                 @JsonProperty("name") String 氏名,
                 @JsonProperty("departmentId") int 学科ID,
                 @JsonProperty("enrolledYear") int 入学年度) { }

    /** GET /students/{id}/transcript の応答。 */
    record 成績一覧の応答(@JsonProperty("studentName") String 氏名,
                   @JsonProperty("lines") List<成績明細の応答> 明細リスト,
                   @JsonProperty("average") double 平均点) { }

    record 成績明細の応答(@JsonProperty("courseTitle") String 科目名,
                   @JsonProperty("credits") int 単位数,
                   @JsonProperty("academicYear") int 年度,
                   @JsonProperty("score") Integer 点数,
                   @JsonProperty("passed") boolean 合格) { }
}
