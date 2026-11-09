package grades.domain;

/**
 * 履修（どの学生が、どの科目を、何年度に履修したか）。
 * score が null のときは「履修中」（まだ点数がない）。
 */
public record Enrollment(int studentId, int courseId, int academicYear, Integer score) {

    /** 合格とする最低点。 */
    private static final int PASSING_SCORE = 60;

    public Enrollment {
        if (score != null && (score < 0 || score > 100)) {
            throw new IllegalArgumentException("点数は0〜100にしてください: " + score);
        }
    }

    /** 合格かどうか。履修中（点数なし）は合格ではない。 */
    public boolean isPassed() {
        return score != null && score >= PASSING_SCORE;
    }
}
