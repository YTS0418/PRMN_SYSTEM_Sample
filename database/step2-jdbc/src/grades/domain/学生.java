package grades.domain;

/**
 * 学生。
 * 学生ID は、まだ保存していない学生では null（保存したときに採番される）。
 */
public record 学生(Integer 学生ID, String 氏名, int 学科ID, int 入学年度) {

    public 学生 {
        if (氏名 == null || 氏名.isBlank()) {
            throw new IllegalArgumentException("名前を入力してください");
        }
        if (入学年度 < 2000) {
            throw new IllegalArgumentException("入学年度は2000以上にしてください: " + 入学年度);
        }
    }

    /** record は変更できないので、学生ID を付けた新しい学生を返す。 */
    public 学生 学生IDを付ける(int 採番された学生ID) {
        return new 学生(採番された学生ID, 氏名, 学科ID, 入学年度);
    }
}
