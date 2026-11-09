package grades.application;

import java.util.List;

/**
 * 成績一覧（画面に必要な形にまとめた、層をまたぐ入れ物＝DTO）。
 * average は、点数のある履修だけの平均点。点数のある履修が 1 件もなければ 0.0。
 */
public record Transcript(String studentName, List<Line> lines, double average) {

    /** 1 科目分の行。score が null のときは履修中。 */
    public record Line(String courseTitle, int credits, int academicYear,
                       Integer score, boolean passed) { }
}
