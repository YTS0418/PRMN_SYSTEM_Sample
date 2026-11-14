package grades.domain;

/**
 * 履修（どの学生が、どの科目を、何年度に履修したか）。
 * 点数が null のときは「履修中」（まだ点数がない）。
 */
public record 履修(int 学生ID, int 科目ID, int 年度, Integer 点数) {

    /** 合格とする最低点。 */
    private static final int 合格点 = 60;

    public 履修 {
        if (点数 != null && (点数 < 0 || 点数 > 100)) {
            throw new IllegalArgumentException("点数は0〜100にしてください: " + 点数);
        }
    }

    /** 履修中（まだ点数がない）かどうか。 */
    public boolean 履修中である() {
        return 点数 == null;
    }

    /** 合格かどうか。履修中（点数なし）は合格ではない。 */
    public boolean 合格している() {
        if (履修中である()) {
            return false;
        }
        return 点数 >= 合格点;
    }
}
