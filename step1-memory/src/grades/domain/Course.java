package grades.domain;

/** 科目。 */
public record Course(int id, String title, int credits) {

    public Course {
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("科目名を入力してください");
        }
        if (credits < 1 || credits > 4) {
            throw new IllegalArgumentException("単位数は1〜4にしてください: " + credits);
        }
    }
}
