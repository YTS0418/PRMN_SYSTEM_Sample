package grades.presentation;

import grades.application.GradeService;
import grades.application.Transcript;
import grades.domain.Course;
import grades.domain.Student;

import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * コンソールの入出力を担当するクラス（Presentation 層）。
 * 文字列を数値に変換して GradeService を呼び、結果を表示するだけ。
 * 業務のルール（点数の範囲など）の検査は書かない。違反は、例外のメッセージを表示する。
 */
public class ConsoleApp {

    private final GradeService gradeService;
    private final Scanner sc = new Scanner(System.in);

    public ConsoleApp(GradeService gradeService) {
        this.gradeService = gradeService;
    }

    /** 入力が終わった（Ctrl+D など）ことを、メニューのループに伝える。 */
    private static class InputEnded extends RuntimeException { }

    public void run() {
        System.out.println("=== 成績管理システム（step1：保存先はメモリ）===");
        try {
            while (true) {
                System.out.println();
                System.out.println("1) 学生を登録する");
                System.out.println("2) 成績を登録する");
                System.out.println("3) 成績一覧を表示する");
                System.out.println("0) 終了");
                String menu = readLine("選んでください > ");
                try {
                    switch (menu) {
                        case "1" -> registerStudent();
                        case "2" -> recordScore();
                        case "3" -> showTranscript();
                        case "0" -> {
                            System.out.println("終了します（データは消えます）");
                            return;
                        }
                        default -> System.out.println("0〜3 から選んでください");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("数字で入力してください");          // 入力形式のエラー：ここの仕事
                } catch (IllegalArgumentException | NoSuchElementException e) {
                    System.out.println("エラー: " + e.getMessage());       // Domain / Application が投げた例外
                }
            }
        } catch (InputEnded e) {
            System.out.println();
            System.out.println("入力が終わったので終了します");
        }
    }

    private void registerStudent() {
        String name = readLine("名前 > ");
        int departmentId = Integer.parseInt(readLine("学科 ID（1:情報システム 2:電子工学 3:応用化学）> "));
        int enrolledYear = Integer.parseInt(readLine("入学年度（例：2025）> "));
        Student student = gradeService.registerStudent(name, departmentId, enrolledYear);
        System.out.println("登録しました: 学生 ID = " + student.id() + "（" + student.name() + "）");
    }

    private void recordScore() {
        showStudents();
        int studentId = Integer.parseInt(readLine("学生 ID > "));
        showCourses();
        int courseId = Integer.parseInt(readLine("科目 ID > "));
        int academicYear = Integer.parseInt(readLine("履修した年度（例：2025）> "));
        String text = readLine("点数（履修中ならそのまま Enter）> ");
        Integer score = text.isBlank() ? null : Integer.parseInt(text.trim());   // 文字列 → 数値に変換
        gradeService.recordScore(studentId, courseId, academicYear, score);     // Application 層を呼ぶだけ
        System.out.println("登録しました");
    }

    private void showTranscript() {
        showStudents();
        int studentId = Integer.parseInt(readLine("学生 ID > "));
        Transcript t = gradeService.transcript(studentId);
        System.out.println();
        System.out.println("【" + t.studentName() + " さんの成績】");
        for (Transcript.Line line : t.lines()) {
            String score = (line.score() == null) ? "履修中" : line.score() + "点";
            String result = (line.score() == null) ? "" : (line.passed() ? "（合格）" : "（不合格）");
            System.out.printf("  %d年度  %s（%d単位）  %s%s%n",
                    line.academicYear(), line.courseTitle(), line.credits(), score, result);
        }
        System.out.printf("平均点（点数のある科目のみ）: %.1f%n", t.average());
    }

    private void showStudents() {
        System.out.println("-- 学生 --");
        for (Student s : gradeService.students()) {
            System.out.println("  " + s.id() + ": " + s.name());
        }
    }

    private void showCourses() {
        System.out.println("-- 科目 --");
        for (Course c : gradeService.courses()) {
            System.out.println("  " + c.id() + ": " + c.title());
        }
    }

    private String readLine(String prompt) {
        System.out.print(prompt);
        if (!sc.hasNextLine()) {
            throw new InputEnded();
        }
        return sc.nextLine().trim();
    }
}
