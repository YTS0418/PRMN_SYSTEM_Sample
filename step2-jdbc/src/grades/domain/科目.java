package grades.domain;

/** 科目。 */
public record 科目(int 科目ID, String 科目名, int 単位数) {

    public 科目 {
        if (科目名 == null || 科目名.isBlank()) {
            throw new IllegalArgumentException("科目名を入力してください");
        }
        if (単位数 < 1 || 単位数 > 4) {
            throw new IllegalArgumentException("単位数は1〜4にしてください: " + 単位数);
        }
    }
}
